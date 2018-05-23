(ns view-inventory-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]))

(defn create-state
  [{inventory-id :inventory-id}]
  {:inventory-id                        inventory-id
   :edit-mode                           false
   :fetching-inventory-details          false
   :should-refetch-get-inventory-detail false
   :get-inventory-details-response      nil})

(defn started-get-inventory-detail-service-call [state]
  (assoc state :fetching-inventory-details true))

(defn should-get-inventory-detail? [state]
  (and (not (:fetching-inventory-details state))
       (or (:should-refetch-get-inventory-detail state)
           (not (get-in state [:get-inventory-details-response :data])))))

(defn receive-get-inventory-detail-service-response [state response request]
  (-> state
      (assoc :should-refetch-get-inventory-detail false)
      (assoc :fetching-inventory-details false)
      (assoc :get-inventory-details-response (util/->clojure-keys response))))

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))
