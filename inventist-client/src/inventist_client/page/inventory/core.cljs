(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]
            [view-inventory-overview.core :as view-inventory-overview]
            [clojure.string :refer [lower-case]]
            [util.inventory.core :as util]))

(def any-inventory-detail-state-path [:view-modules :view-inventory-detail])
(defn inventory-detail-state-path [inventory-id] (concat any-inventory-detail-state-path [inventory-id]))
(def inventory-overview-state-path [:view-modules :view-inventory-overview])

(defn set-selected-inventory-id
  [state inventory-id]
  (-> state
      (assoc :selected-inventory-id inventory-id)
      (update-in (inventory-detail-state-path inventory-id)
                 (fn [inventory-detail-state]
                   (or inventory-detail-state
                       (view-inventory-detail/create-state {:inventory-item-id inventory-id}))))))

(defn create-state
  [{selected-inventory-id :selected-inventory-id}]
  (-> {:selected-inventory-id nil
       :view-modules          {:view-inventory-detail {}}}
      (assoc-in inventory-overview-state-path (view-inventory-overview/create-state))
      (set-selected-inventory-id selected-inventory-id)))

(defn create-inventory-detail-args
  [state inventory-id]
  (let [state-path (inventory-detail-state-path inventory-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-inventory-overview-args
  [state]
  (let [state-path inventory-overview-state-path]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn on-remote-state-mutation [state remote-state-uri]
  (-> state
      (update-in inventory-overview-state-path view-inventory-overview/on-remote-state-mutation)
      (update-in any-inventory-detail-state-path
                 (fn [inventory-details-map]
                   (->> inventory-details-map
                        (map (fn [[inventory-id details-state]]
                               [inventory-id
                                (view-inventory-detail/on-remote-state-mutation details-state remote-state-uri)]))
                        (into {}))))))
