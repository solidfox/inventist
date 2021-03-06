(ns view-inventory-overview.component
  (:require [rum.core :as rum :refer [defc defcs with-key]]
            [symbols.overview :refer [search-toolbar second-column-header list-card]]
            [symbols.style :as style]
            [view-inventory-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-inventory-overview.event :as event]
            [cljs-react-material-ui.icons :as icon]
            [symbols.general :as s-general]
            [util.inventory.core :as util]
            [symbols.mixin :as mixin]
            [symbols.inventory :as s-inventory]))



(defc inventory-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [inventory         (core/filtered-inventory state)
        limited-inventory (take 75 inventory)
        n-results         (count limited-inventory)
        search-terms      (:search-terms state)]
    (s-general/scrollable
      {:floating-header
       [:div
        (second-column-header "Inventory")
        (search-toolbar
          {:search-field-value (core/get-free-text-search state)
           :shown-results      (count limited-inventory)
           :total-results      (count inventory)
           :on-change          (fn [e]
                                 (trigger-event
                                   (rem/create-event {:name :search-string-changed
                                                      :data {:new-value (o/oget e [:target :value])}})))})]
       :content
       (cond
         (:fetching-inventory-list state)
         (s-general/centered-loading-indicator "inventory")
         (core/get-inventory-list-failed? state)
         (s-general/centered-message {:icon       (icon/alert-error-outline {:color color/shaded-context-primary-text
                                                                             :style {:height "3rem"
                                                                                     :width  "3rem"}})
                                      :message    "Error while fetching inventory-list from server."
                                      :text-color color/shaded-context-primary-text
                                      :actions    (s-general/button {:text     "Retry"
                                                                     :bg-color color/shaded-context-highlight-bg
                                                                     :on-click (fn [] (trigger-event (rem/create-event {:name :retry-fetching-data})))})})
         :else
         [:div {:style {:height           "auto"
                        :background-color color/transparent
                        :padding          "0.25rem"}}
          (let [item-selected-event (fn [item] (trigger-event (rem/create-event {:name :selected-inventory-item
                                                                                 :data {:item item}})))]
            (->> limited-inventory
                 (map
                   (fn [item]
                     [:div {:key      (:id item)
                            :on-click (fn [] (item-selected-event item))}
                      (s-inventory/inventory-list-card {:item      item
                                                        :drop-zone [{:drag-data-type "inventist/person"
                                                                     :drop-zone-text (str "Assign " (:class item) " to the dragged person.")
                                                                     :drop-effect    "link"}]
                                                        :on-drop   (fn [drag-data] (trigger-event
                                                                                     (rem/create-event {:name :reassign-inventory-item
                                                                                                        :data {:inventory-item-id (:id item)
                                                                                                               :new-assignee-id   (:id drag-data)}})))
                                                        :selected  (= (:selected-inventory-id state) (:id item))})]))))
          ;:hidden    (not (core/inventory-matches item search-terms))})]))))

          (when (= n-results 0)
            [:div {:style {:width            "100%"
                           :height           "100%"
                           :color            color/shaded-context-primary-text
                           :background-color color/transparent
                           :text-align       "center"
                           :margin-top       "2rem"}}
             "No matches found!"])])})))

