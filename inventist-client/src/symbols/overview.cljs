(ns symbols.overview
  (:require [rum.core :refer [defc] :as rum]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]
            [oops.core :as o]
            [util.inventory.core :as util]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

;Person-list card
(defc list-card [{:keys [selected
                         hidden]
                  :as   props}
                 & children]
  [:div (merge {:class     (style/list-item-class)
                :draggable true
                :style     (merge (when selected style/list-item-selected)
                                  (when hidden {:display "none"}))}
               (dissoc props :selected))
   children])

(defc person-list-card < rum/static
  [{:keys [person
           selected
           hidden]}]
  (list-card {:selected selected
              :hidden   hidden}
             [:div {:class (style/list-item-left-column)}
              (cond (and (:image person) (not= (:image person) ""))
                    [:img {:src   (:image person)
                           :class (style/card-image)}]
                    :else
                    [:span {:style {:width            "3rem"
                                    :height           "3rem"
                                    :background-color color/shaded-context-secondary-text
                                    :border-radius    "0.25rem"
                                    :display          "grid"
                                    :font-size        "1.8rem"
                                    :align-items      "center"
                                    :text-align       "center"
                                    :color            color/shaded-context-highlight-bg}}
                     (str (subs (or (:first-name person) "") 0 1) (subs (or (:last-name person) "") 0 1))])]
             [:div
              [:span {:style style/card-title}
               (str (:first-name person) " " (:last-name person))] [:br]
              [:span {:style style/card-subtitle}
               (str (:occupation person) " - " (str/join ", " (for [group (:groups person)] (:name group))))] [:br]
              [:span {:style (merge style/card-title
                                    {:display   "flex"
                                     :flex-wrap "wrap"})}
               (for [item (:inventory person)]
                 [:div {:key   (:id item)
                        :style {:margin    "0.1rem 0.5rem 0rem 0"
                                :font-size "0.75rem"}}
                  (s-general/device-icon-set {:item item})])]]))

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

;Inventory-list card
(defc inventory-list-card [{:keys [item
                                   selected
                                   hidden
                                   on-select]}]
  (list-card {:selected selected
              :on-click on-select}
             [:div {:class (style/list-item-left-column)
                    :style {:font-size "3rem"}}
              (cond (and (:photo item) (not= (:photo item) ""))
                    [:img {:class (style/card-image)
                           :src   (:photo item)}]
                    :else
                    (s-general/device-icon-set {:item item}))]

             [:div {:style {:margin "0 0 0 1rem"
                            :width  "auto"}}
              [:span {:style style/card-title}
               (str (:brand item) " " (:model-name item))] [:br]
              [:span {:style style/card-subtitle}
               (str (:serial-number item) " - " (:color item))]]))


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

(defc second-column-header [header-text]
  [:div {:style {:width       "auto"
                 :height      "3.5rem"
                 :padding     "0 1rem"
                 :color       color/shaded-context-title-text
                 :font-size   "1.5rem"
                 :font-weight "400"
                 :display     "grid"
                 :align-items "center"}}
   [:div (str header-text)]])



;overview with search and listing
(defc overview-list
  [{type       :type
    list-items :list-items}]
  (s-general/scrollable
    {:floating-header [:div (second-column-header type)
                       (search-toolbar {:list-items list-items})]
     :content         [:div {:style {:background-color color/transparent}}
                       (for [list-item list-items]
                         (cond (= type "inventory") (inventory-list-card {:item list-item})
                               (= type "contractors") (contractor-list-card {:contractor list-item})))]}))





