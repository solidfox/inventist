(ns symbols.overview
  (:require [rum.core :refer [defc defcs] :as rum]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]
            [oops.core :as o]
            [util.inventory.core :as util]
            [cognitect.transit :as transit]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

(defc list-card-drag-over [drop-text]
  [:div {:style {:position         "absolute"
                 :width            "calc(100% - 4.5rem - 4px)"
                 :height           "calc(100% - 4.5rem - 4px)"
                 :padding          "1.5rem"
                 :display          "flex"
                 :text-align       "center"
                 :align-items      "center"
                 :justify-content  "center"
                 :opacity          "0.85"
                 :color            color/shaded-context-primary-text
                 :background-color color/shaded-context-background
                 :border-radius    style/inner-border-radius
                 :border           (str "2px dashed " color/shaded-context-secondary-text)}}
   ;[:div [:span {:style {:font-size "2.8rem"}} [:i {:class "fas fa-box-open"}]]]
   [:div drop-text]])

(def reader (transit/reader :json))

(defn get-drag-data [event type]
  (transit/read reader (.getData (.-dataTransfer event) type)))

(defn drop-zone-data-for-event [drop-zone event]
  (filter (fn [{:keys [drag-data-type]}] (-> event .-dataTransfer .-types (.includes drag-data-type)))
          drop-zone))


(defcs list-card < (rum/local nil :current-drag-metadata-atom)
                   "drop-zone expects a map like this
                    [{{:drag-data-type \"text/plain\"
                       :drop-zone-text \"Drop plain text here.\"
                       :drop-effect    \"copy\"}}
                   The list-card will only react to dragged content of the specified drag-data-type
                   and the drop-zone will then display the provided text and set the given drop-effect (if provided)."
  [{:keys [current-drag-metadata-atom]}
   {:keys [selected
           on-drop
           drop-zone
           hidden
           is-drag-over]
    :as   props}
   & children]
  (let [drop-zone-data (deref current-drag-metadata-atom)]
    [:div (merge (when drop-zone
                   {:on-drag-over  (fn [event] (if-let [drop-zone-data (first (drop-zone-data-for-event drop-zone event))]
                                                 (do (.preventDefault event)
                                                     (aset event "dropEffect" "link")
                                                     (reset! current-drag-metadata-atom drop-zone-data))))
                    :on-drag-leave (fn [_event] (reset! current-drag-metadata-atom nil))
                    :on-drop       (fn [event]
                                     (on-drop (get-drag-data event (:drag-data-type @current-drag-metadata-atom)))
                                     (reset! current-drag-metadata-atom nil))})
                 {:class     [(style/list-item-class)
                              (when is-drag-over "drag")]
                  :hidden    hidden
                  :draggable true
                  :style     (merge
                               {:position "relative"}
                               (when (or selected drop-zone-data) style/list-item-selected)
                               (when hidden {:display "none"}))})
     (when drop-zone-data
       (list-card-drag-over (:drop-zone-text drop-zone-data)))
     [:div {:style {:display               "grid"
                    :grid-template-columns "auto 1fr"
                    :grid-gap              "1rem"}}
      children]]))

;Search component
(defc search-toolbar [{search-field-value :search-field-value
                       shown-results      :shown-results
                       total-results      :total-results
                       on-change          :on-change}]
  [:div
   [:div {:style {:width           "100%"
                  :height          "2.5rem"
                  :backgroundColor color/light-context-background
                  ;:box-shadow      (str "inset 0 0 0.25rem " color/light-context-secondary-text)
                  :display         "flex"}}
    [:div {:style {:width           "3rem"
                   :height          "100%"
                   :color           color/light-context-secondary-text
                   :display         "flex"
                   :align-items     "center"
                   :justify-content "center"}}
     [:i {:class "fas fa-search"}]]
    [:input {:type        "input"
             :value       (or search-field-value "")
             :id          "search"
             :name        "search"
             :autoFocus   false
             :placeholder "Search"
             :on-change   on-change
             :style       {:width           "100%"
                           :padding         "0 0 0 3rem"
                           :margin-left     "-3rem"
                           :height          "100%"
                           :color           color/light-context-secondary-text
                           :backgroundColor color/transparent
                           :border          0}}]]
   [:div {:style {:height          "1rem"
                  :padding         "0.25rem 1rem"
                  :font-size       "0.75rem"
                  :color           color/shaded-context-primary-text
                  :backgroundColor color/shaded-context-highlight-bg
                  :display         "flex"
                  :justify-content "space-between"
                  :align-items     "center"}}
    [:div (str/join " "
                    (concat (when (and shown-results (not= total-results
                                                           shown-results))
                              ["Showing " shown-results])
                            (when (and shown-results total-results (not= total-results
                                                                         shown-results))
                              ["of"])
                            [total-results]
                            [" results"]))]
    [:div {:style {:cursor "pointer"}}
     (str "Sort Results")]]])

(defc second-column-header < {:key-fn (fn [header-text] (str header-text "-header"))}
  [header-text]
  [:div {:style {:width       "auto"
                 :height      "3.5rem"
                 :padding     "0 1rem"
                 :color       color/shaded-context-title-text
                 :font-size   "1.5rem"
                 :font-weight "400"
                 :display     "grid"
                 :align-items "center"}}
   [:div (str header-text)]])


;Contractor-list card
(defc contractor-list-card [{contractor :contractor
                             on-click   :on-click}]
  [:div {:key   (:id contractor)
         :class (style/list-item-class)}
   [:div {:class (style/list-item-left-column)}
    [:img {:class (style/card-image)
           :src   (cond (and (:image contractor) (not= (:image contractor) "")) (:image contractor)
                        :else "/image/contractor-placeholder.png")}]]
   [:div
    [:span {:style style/card-title}
     (str (:name contractor))] [:br]
    [:span {:style style/card-subtitle}
     (str (:type contractor) " - " (count (:inventory contractor)) " Products")]]])


;overview with search and listing For Contractors
(defc overview-list
  [{type       :type
    list-items :list-items}]
  (s-general/scrollable
    {:floating-header [:div (second-column-header type)
                       (search-toolbar {:list-items list-items})]
     :content         [:div {:style {:background-color color/transparent}}
                       (for [list-item list-items]
                         (cond (= type "contractors") (contractor-list-card {:contractor list-item})))]}))





