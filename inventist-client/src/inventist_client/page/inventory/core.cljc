(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]))

(defn inventory-detail-state-path [item-id] [:view-modules :view-inventory-detail item-id])

(defn create-state
  []
  (-> {}
      (assoc-in (inventory-detail-state-path "mock-id") (view-inventory-detail/create-state "mock-id"))))

(defn create-inventory-detail-args
  [state]
  {:input      {:state (get-in state (inventory-detail-state-path "mock-id"))}
   :state-path (inventory-detail-state-path "mock-id")})

