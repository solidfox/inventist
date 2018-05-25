(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]
            [view-inventory-overview.core :as view-inventory-overview]
            [clojure.string :refer [lower-case]]))

(def any-inventory-detail-state-path [:view-modules :view-inventory-detail])
(defn inventory-detail-state-path [inventory-id] (concat any-inventory-detail-state-path [inventory-id]))
(defn inventory-overview-state-path [] [:view-modules :view-inventory-overview])

(defn create-state
  []
  (-> {:selected-inventory-id nil}
      (assoc-in (inventory-overview-state-path) (view-inventory-overview/create-state))))

(defn create-inventory-detail-args
  [state inventory-id]
  (let [state-path (inventory-detail-state-path inventory-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-inventory-overview-args
  [state]
  (let [state-path (inventory-overview-state-path)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn set-selected-inventory-id
  [state inventory-id]
  (-> state
      (assoc :selected-inventory-id inventory-id)
      (update-in (inventory-detail-state-path inventory-id)
                 (fn [inventory-detail-state]
                   (if (nil? inventory-detail-state)
                     (view-inventory-detail/create-state {:inventory-id inventory-id})
                     inventory-detail-state)))))
