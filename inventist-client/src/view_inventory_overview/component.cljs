(ns view-inventory-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :refer [scrollable search-toolbar footer inventory-list-card]]
            [view-inventory-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-inventory-overview.event :as event]
            [symbols.general :as s-general]))

(defc inventory-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [inventory         (core/filtered-inventory state)
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
       [:div {:style {:height           "100%"
                      :background-color color/grey-light}}
        (for [item limited-inventory]
          (inventory-list-card {:item      item
                                :on-select (fn [] (trigger-event
                                                    (rem/create-event
                                                      {:name :inventory-item-selected
                                                       :data {:inventory item}})))}))
        (cond (not= (count inventory) 0)
              nil
              (:fetching-inventory-list state)
              (s-general/full-view-loading "inventory")
              :else
              [:div {:style {:width            "100%"
                             :height           "100%"
                             :color            color/grey-blue
                             :background-color color/transparent
                             :text-align       "left"
                             :margin           "2rem"}}
               "No matches found!"])]
       :floating-footer
       (footer)})))
