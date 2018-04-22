(ns inventist-client.page.inventory.component
  (:require [remodular.core :refer [modular-component]]
            [view-inventory-detail.component :refer [inventory-detail]]
            [view-inventory-list.component :refer [inventory-list]]
            [inventist-client.page.inventory.core :as core]
            [rum.core :refer [defc]]))

(defc component < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:display "grid"
                 :grid-template-columns "20rem 1fr"}}
   (inventory-list (core/create-inventory-list-args state {}))
   (inventory-detail (core/create-inventory-detail-args state "mock-id"))])
