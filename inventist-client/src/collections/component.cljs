(ns collections.component
  (:require [rum.core :refer [defc with-key]]
            [rum.core :as rum]
            [remodular.core :as rem]
            [collections.core :as core]
            [collections.event :as event]
            [symbols.color :as color]
            [clojure.string :refer [lower-case upper-case]]
            [cljs-react-material-ui.rum :as ui]
            [symbols.style :as style]
            [symbols.general :as s-general]
            [symbols.mixin :refer [hovered-mixin toggle-mixin]]
            [inventist-client.event :as client-event]
            [util.inventory.core :as util]
            [cognitect.transit :as transit]))

(def writer (transit/writer :json))

(def collection-list-item-height "2rem")

(defc collection-list-item
  [{:keys [title
           icon
           widget
           color
           on-click]}]
  [:div {:key       title
         ;:class    "collection-item"
         :draggable false
         :style     {:min-height            collection-list-item-height
                     :max-height            collection-list-item-height
                     :color                 color/dark-context-primary-text
                     :width                 "auto"
                     :padding               "0.5rem 1rem"
                     :cursor                "pointer"
                     :display               "grid"
                     :grid-template-columns "1.5rem auto 2rem"
                     :background-color      (or color color/transparent)}
         :on-click  on-click}
   [:i {:class icon
        :style {:font-size  "1.25rem"
                :margin-top "0.325rem"
                :align-self "start"}}]

   [:div {:draggable false
          :style     {:font-size   "1rem"
                      :font-weight "500"
                      :margin      "0 1rem"
                      :align-self  "center"}}
    title]

   ;;On-Mouse-Over "style.css"
   ;[:span {:class    "collection-item-edit"
   ;        :on-click (fn [] (println "Edit " title))
   ;        :style    {:font-weight "400"
   ;                   :font-size   "0.75rem"}}
   ; "Edit"]]

   ;Selected Item
   widget])

(def collections-list
  [{:title "Dashboard"
    :icon  "fas fa-tachometer-alt"
    :id    :dashboard}
   {:title "Inventory"
    :icon  "fas fa-sitemap"
    :id    :inventory}
   {:title "People"
    :icon  "fas fa-users"
    :id    :people}])

(rum/defcs widget-selected-collection-item < (hovered-mixin :hovered)
  [{:keys [hovered]}
   {:keys [image-url
           text-icon
           title
           subtitle
           drag-data]}]
  [:div {:on-drag-start (fn [event]
                          (.setData (.-dataTransfer event) (:type drag-data) (transit/write writer drag-data)))
         :style   {:position         "relative"
                   :background-color color/dark-context-secondary-text
                   :width            collection-list-item-height
                   :height           collection-list-item-height
                   :border-radius    "0.25rem"
                   :cursor           "grab"
                   :align-self       "start"}}
   ;:overflow         "hidden"}}
   (cond image-url [:img {:draggable true
                          :src       image-url
                          :style     {:height        "100%"
                                      :border-radius "inherit"
                                      :width         "100%"
                                      :object-fit    :cover}}]
         :else [:span {:draggable true
                       :style     {:width            "inherit"
                                   :height           "inherit"
                                   :border-radius    "inherit"
                                   :display          "grid"
                                   :font-size        "1.1rem"
                                   :align-items      "center"
                                   :text-align       "center"
                                   :background-color "inherit"
                                   :color            color/dark-context-primary-text}}
                text-icon])
   (when hovered
     (s-general/tooltip {:tooltip-text (str title "\n" subtitle "\nDrag to reassign")
                         :position     "bottom"
                         :alignment    "end"}))])

(rum/defcs collections-view < (rem/modular-component event/handle-event)
                              (toggle-mixin {:toggle-state-key :expanded
                                             :initial-state    true
                                             :on-fn-key        :trigger-expand
                                             :off-fn-key       :trigger-collapse})
  [{:keys [expanded trigger-expand trigger-collapse]}
   {{:keys [state
            selected-item-metadata-map]} :input
    :keys                                [trigger-event]}]
  (println selected-item-metadata-map)
  (let [heading                (:heading state)
        collection-list        collections-list
        selected-collection-id (:selected-collection-id state)]
    [:div {:style {:height         "auto"
                   :text-align     "left"
                   :display        "flex"
                   :flex-direction "column"}}
     ;Heading
     [:div {:id    heading
            :style {:height                collection-list-item-height
                    :min-height            collection-list-item-height
                    :color                 color/dark-context-title-text
                    :width                 "auto"
                    :margin-top            "1.5rem"
                    :padding               "0 1rem"
                    :display               "grid"
                    :grid-template-columns "auto 1rem 1.5rem"
                    :grid-gap              "1rem"
                    :align-items           "center"
                    :font-size             "1rem"
                    :cursor                "pointer"
                    :background-color      color/transparent}}

      [:div {:style    {:font-weight    "500"
                        :letter-spacing "2px"}
             :on-click (if expanded trigger-collapse trigger-expand)}
       (upper-case heading)]

      [:i {:class "fas fa-plus-circle"
           :title "Add New"}]
      [:div {:style    {:text-align "center"}
             :on-click (if expanded trigger-collapse trigger-expand)}
       [:img {:src   "/image/arrow.svg"
              :style {:height    "0.8rem"
                      :opacity   "0.9"
                      :transform (if expanded "" "rotate(90deg)")}}]]]

     ;List
     (when expanded
       (for [{title         :title
              collection-id :id
              icon          :icon} collection-list]
         (-> (collection-list-item
               (merge {:title    title
                       :icon     icon
                       :widget   (let [{:keys [peek-data
                                               drag-data]} (get selected-item-metadata-map collection-id)]
                                   (when peek-data
                                     (widget-selected-collection-item (merge peek-data
                                                                             {:drag-data drag-data}))))
                       :on-click (fn []
                                   (trigger-event (rem/create-event
                                                    {:name :clicked-collection
                                                     :data {:collection-id collection-id}})))}
                      (when (= selected-collection-id collection-id)
                        {:selected true
                         :color    color/dark-context-highlight-bg})))
             (with-key collection-id))))]))
