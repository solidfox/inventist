(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]
            [view-inventory-list.core :as view-inventory-list]))

(defn inventory-detail-state-path [item-id] [:view-modules :view-inventory-detail item-id])
(defn inventory-list-state-path [{filter :filter}] [:view-modules :view-inventory-list filter])

(defn create-state
  []
  (-> {}
      (assoc-in (inventory-detail-state-path "mock-id") (view-inventory-detail/create-state "mock-id"))
      (assoc-in (inventory-list-state-path {}) (view-inventory-list.core/create-state))))

(defn create-inventory-detail-args
  [state item-id]
  (let [state-path (inventory-detail-state-path item-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-inventory-list-args
  [state state-path-deps]
  (let [state-path (inventory-list-state-path state-path-deps)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))
