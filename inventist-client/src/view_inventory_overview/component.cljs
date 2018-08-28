(ns view-inventory-overview.component
  (:require [rum.core :refer [defc with-key]]
            [symbols.overview :refer [search-toolbar second-column-header inventory-list-card]]
            [view-inventory-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-inventory-overview.event :as event]
            [symbols.general :as s-general]
            [util.inventory.core :as util]))

(defc inventory-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [inventory (core/filtered-inventory state)
        limited-inventory (take 75 inventory)]
    (s-general/scrollable
      {:floating-header
       [(second-column-header "Inventory")
        (-> (search-toolbar
              {:search-field-value (core/get-free-text-search state)
               :shown-results      (count limited-inventory)
               :total-results      (count inventory)
               :on-change          (fn [e]
                                     (trigger-event
                                       (rem/create-event {:name :search-string-changed
                                                          :data {:new-value (o/oget e [:target :value])}})))})
            (with-key 2))]
       :content
       [:div {:style {:height           "auto"
                      :background-color color/transparent
                      :padding          "0.25rem"}}
        (for [item limited-inventory]
          (inventory-list-card {:item      item
                                :selected  (= (:id item) (:selected-inventory-id state))
                                :on-select (fn [] (trigger-event
                                                    (rem/create-event
                                                      {:name :inventory-item-selected
                                                       :data {:inventory item}})))}))
        (cond (not= (count inventory) 0)
              nil
              (:fetching-inventory-list state)
              (s-general/centered-loading-indicator "inventory")
              :else
              [:div {:style {:width            "100%"
                             :height           "100%"
                             :color            color/shaded-context-primary-text
                             :background-color color/transparent
                             :text-align       "center"
                             :margin-top       "2rem"}}
               "No matches found!"])]})))

