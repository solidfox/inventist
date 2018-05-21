(ns inventist-client.page.inventory.component
  (:require [remodular.core :refer [modular-component]]
            [view-inventory-detail.component :refer [inventory-detail]]
            [view-inventory-overview.component :refer [inventory-list]]
            [inventist-client.page.inventory.core :as core]
            [rum.core :refer [defc]]
            [inventist-client.page.inventory.event :as event]
            [symbols.style :as style]))

(defc component < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id    (str ::component-id)
         :style {:height                "100%"
                 :display               "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows    "100%"}}
   (inventory-list (assoc (core/create-inventory-overview-args state)
                     :trigger-parent-event trigger-event))
   [:div {:style (merge style/z-index-details-section
                        style/box-shadow)}
    (when-let [selected-inventory-id (:selected-inventory-id state)]
      (inventory-detail (assoc (core/create-inventory-detail-args state selected-inventory-id)
                          :trigger-parent-event trigger-event)))]])
