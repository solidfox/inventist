(ns view-person-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]
            [ysera.test :as test]))


(defn create-state
  [{person-id :person-id}]
  {:person-id                          person-id
   :edit-mode                          false
   :fetching-person-details            false
   :pending-inventory-item-assignments []
   :ongoing-inventory-item-assignment  nil
   :get-person-details-response        nil})

(defn remove-pending-inventory-item-assignment
  [state {inventory-item-serial-number :inventory-item-serial-number}]
  (update state :pending-inventory-item-assignments
          (fn [pending-assignments]
            (remove pending-assignments
                    (fn [assignment]
                      (= (:inventory-item-serial-number assignment) inventory-item-serial-number))))))

(defn add-pending-inventory-item-assignment
  {:test (fn [] (test/is= (add-pending-inventory-item-assignment {} {:inventory-item-serial-number "test"})
                          {:pending-inventory-item-assignments [{:inventory-item-serial-number "test"}]}))}
  [state {inventory-item-serial-number :inventory-item-serial-number
          :as                          assignment}]
  (update state :pending-inventory-item-assignments conj assignment))

(defn should-get-person-detail? [state]
  (and (not (:fetching-person-details state))
       (not (get-in state [:get-person-details-response :data]))))

(defn started-get-person-detail-service-call [state]
  (assoc state :fetching-person-details true))

(defn receive-get-person-detail-service-response
  [state response request]
  (-> state
      (assoc :fetching-person-details false)
      (assoc :get-person-details-response (util/->clojure-keys response))))

(defn get-next-pending-inventory-item-assignment
  [state]
  (first (:pending-inventory-item-assignments state)))

(defn started-reassign-inventory-item-service-call
  [state {inventory-item-serial-number :inventory-item-serial-number
          :as                          assignment}]
  (assoc state :ongoing-inventory-item-assignment assignment)
  (remove-pending-inventory-item-assignment state assignment))

(defn receive-reassign-inventory-item-service-response
  [state response request]
  (-> state
      (assoc :ongoing-inventory-item-assignment nil)
      (assoc :get-person-details-response {:data (get-in (util/->clojure-keys response) [:data :set-user-of-inventory-item :new-user])})))

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))
