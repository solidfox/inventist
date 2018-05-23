(ns view-person-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]
            [ysera.test :as test]))


(defn create-state
  [{person-id :person-id}]
  {:person-id                          person-id
   :edit-mode                          false
   :new-inventory-item-input           {}
   :fetching-person-details            false
   :pending-inventory-item-assignments []
   :ongoing-inventory-item-assignment  nil
   :get-person-details-response        nil})

(defn get-person [state]
  (get-in state [:get-person-details-response :data :person]))


; User input state logic

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))

(defn set-new-device-serial-number
  [state new-serial-number]
  (assoc-in state [:new-inventory-item-input :serial-number] new-serial-number))

(defn get-new-device-serial-number
  [state]
  (get-in state [:new-inventory-item-input :serial-number]))

(defn reset-new-inventory-item-input
  [state]
  (assoc state :new-inventory-item-input {}))


; Service call support logic

(defn remove-pending-inventory-item-assignment
  [state {serial-number :serial-number}]
  (update state :pending-inventory-item-assignments
          (fn [pending-assignments]
            (remove pending-assignments
                    (fn [assignment]
                      (= (:serial-number assignment) serial-number))))))

(defn commit-new-pending-inventory-item-assignment
  {:test (fn [] (test/is= (commit-new-pending-inventory-item-assignment
                            {:new-inventory-item-input {:serial-number "test"}})
                          {:new-inventory-item-input           {}
                           :pending-inventory-item-assignments [{:serial-number "test"}]}))}
  [state]
  (-> state
      (update :pending-inventory-item-assignments conj {:serial-number
                                                        (get-new-device-serial-number state)})
      (reset-new-inventory-item-input)
      (set-edit-mode false)))

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

(defn get-next-pending-inventory-item-assignment-to-perform
  {:test (fn [] (test/is= (get-next-pending-inventory-item-assignment-to-perform
                            {:pending-inventory-item-assignments [{}]
                             :ongoing-inventory-item-assignment  {}})
                          nil)
           (test/is= (get-next-pending-inventory-item-assignment-to-perform
                       {:pending-inventory-item-assignments [{:serial-number 1} {:serial-number 2}]
                        :ongoing-inventory-item-assignment nil})
                     {:serial-number 1}))}

  [state]
  (when (not (:ongoing-inventory-item-assignment state))
    (first (:pending-inventory-item-assignments state))))

(defn started-reassign-inventory-item-service-call
  [state {serial-number :serial-number
          :as           assignment}]
  (assoc state :ongoing-inventory-item-assignment assignment)
  (remove-pending-inventory-item-assignment state assignment))

(defn receive-reassign-inventory-item-service-response
  [state response request]
  (-> state
      (assoc :ongoing-inventory-item-assignment nil)
      (assoc :get-person-details-response {:data {:person (get-in (util/->clojure-keys response) [:data :set-user-of-inventory-item :new-user])}})))
