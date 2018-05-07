(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]
            [view-inventory-overview.core :as view-inventory-overview]))

(defn inventory-detail-state-path [item-id] [:view-modules :view-inventory-detail item-id])
(defn inventory-overview-state-path [{filter :filter}] [:view-modules :view-inventory-overview filter])

(defn create-state
  []
  (-> {}
      (assoc-in (inventory-detail-state-path "mock-item-id") (view-inventory-detail/create-state "mock-item-id"))
      (assoc-in (inventory-overview-state-path {}) (view-inventory-overview/create-state))))

(defn create-inventory-detail-args
  [state item-id]
  (let [state-path (inventory-detail-state-path item-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-inventory-overview-args
  [state state-path-deps]
  (let [state-path (inventory-overview-state-path state-path-deps)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

