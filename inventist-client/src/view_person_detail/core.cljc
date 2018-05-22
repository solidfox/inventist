(ns view-person-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]))


(defn create-state
  [{person-id :person-id}]
  {:person-id                        person-id
   :edit-mode                        false
   :fetching-person-details          false
   :should-refetch-get-person-detail false
   :get-person-details-response      nil})

(defn started-get-person-detail-service-call [state]
  (assoc state :fetching-person-details true))

(defn should-get-person-detail? [state]
  (and (not (:fetching-person-details state))
       (or (:should-refetch-get-person-detail state)
           (not (get-in state [:get-person-details-response :data])))))

(defn receive-get-person-detail-service-response [state response request]
  (-> state
      (assoc :should-refetch-get-person-detail false)
      (assoc :fetching-person-details false)
      (assoc :get-person-details-response (util/->clojure-keys response))))

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))
