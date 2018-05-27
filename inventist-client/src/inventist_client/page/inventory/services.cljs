(ns inventist-client.page.inventory.services
  (:require [view-inventory-overview.services :as inventory-overview]
            [view-inventory-detail.services :as inventory-detail]
            [remodular.core :as a]
            [inventist-client.page.inventory.core :as core]))

(defn get-services [{{state :state} :input}]
  (concat
    (a/prepend-state-path-to-services
      (inventory-overview/get-services (core/create-inventory-overview-args state))
      (core/inventory-overview-state-path))
    (when (:selected-inventory-id state)
      (a/prepend-state-path-to-services
        (inventory-detail/get-services (core/create-inventory-detail-args state (:selected-inventory-id state)))
        (core/inventory-detail-state-path (:selected-inventory-id state))))))
