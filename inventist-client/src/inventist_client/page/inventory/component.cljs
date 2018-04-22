(ns inventist-client.page.inventory.component
  (:require [remodular.core :refer [modular-component]]
            [view-inventory-detail.component :refer [inventory-detail]]
            [inventist-client.page.inventory.core :as core]
            [rum.core :refer [defc]]))

(defc component < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div
   (inventory-detail (core/create-inventory-detail-args state))])
