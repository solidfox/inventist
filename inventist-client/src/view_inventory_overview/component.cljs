(ns view-inventory-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :refer [scrollable search-toolbar footer inventory-list-card]]
            [view-inventory-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-inventory-overview.event :as event]))

(defc inventory-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [inventory (core/filtered-inventory state)
        limited-inventory (take 75 inventory)]
    (scrollable
      {:floating-header
       (search-toolbar
         {:search-field-value (core/get-free-text-search state)
          :shown-results      (count limited-inventory)
          :total-results      (count inventory)
          :on-change          (fn [e]
                                (trigger-event
                                  (rem/create-event {:name :search-string-changed
                                                     :data {:new-value (o/oget e [:target :value])}})))})
       :content
       [:div {:style {:background-color color/grey-light}}
        (for [item limited-inventory]
          (inventory-list-card {:item      item
                                :on-select (fn [] (trigger-event
                                                    (rem/create-event
                                                      {:name :inventory-selected
                                                       :data {:inventory item}})))}))]
       :floating-footer
       (footer)})))
