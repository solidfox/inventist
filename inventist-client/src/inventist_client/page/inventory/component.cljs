(ns inventist-client.page.inventory.component
  (:require [remodular.core :refer [modular-component]]
            [view-inventory-detail.component :refer [inventory-detail report-issue]]
            [view-inventory-overview.component :refer [inventory-list]]
            [inventist-client.page.inventory.core :as core]
            [rum.core :refer [defc]]))

(defc component < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:height "100%"
                 :display "grid"
                 :grid-template-columns "22rem 1fr 22rem"
                 :grid-template-rows "100%"}}
   (inventory-list (core/create-inventory-overview-args state {}))
   (inventory-detail (core/create-inventory-detail-args state "mock-item-id"))
   (report-issue)])
