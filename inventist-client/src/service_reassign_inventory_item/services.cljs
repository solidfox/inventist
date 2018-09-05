(ns service-reassign-inventory-item.services
  (:require
    [service-reassign-inventory-item.core :as core]
    [util.inventory.services :as inventory-services]
    [util.inventory.core :as util]))

(defn reassign-inventory-item
  [{inventory-item-id :inventory-item-id
    new-user-id       :new-assignee-id}]
  (inventory-services/reassign-inventory-item {:inventory-item-id  inventory-item-id
                                               :new-user-id        new-user-id
                                               :new-user-graphql   [:id]
                                               :before-fn-and-args [core/started-fetching-reassign-inventory-item inventory-item-id]
                                               :after-fn-and-args  [core/receive-reassign-inventory-item-response {:inventory-item-id inventory-item-id}]}))

(defn get-services [{{:keys [state]} :input}]
  (->> (core/get-unstarted-reassign-inventory-item-requests state)
       (map reassign-inventory-item)))
