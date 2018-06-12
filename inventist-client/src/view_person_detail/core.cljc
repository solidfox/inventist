(ns view-person-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]
            [ysera.test :as test]
            [#?(:cljs cljs-time.core :clj clj-time.core) :as time]
            [#?(:cljs cljs-time.coerce :clj clj-time.coerce) :as coerce]))

(defn create-empty-inventory-item-assignment []
  {:shown         false
   :serial-number ""})

(defn create-long-timestamp []
  (coerce/to-long (time/today)))

(defn create-state
  [{person-id :person-id}]
  {:person-id                          person-id
   :new-inventory-item-input           {}
   :latest-acceptable-cache-fetch-time (create-long-timestamp)
   :fetching-person-details            false
   :user-input                         {:inventory-item-assignment (create-empty-inventory-item-assignment)}
   :pending-inventory-item-assignments []
   :ongoing-inventory-item-assignment  nil
   :get-person-details-response        nil})

(defn create-test-state []
  (create-state {:person-id :test-person-id}))

(defn get-person [state]
  (get-in state [:get-person-details-response :data :person]))


; User input state logic

(defn set-should-show-item-assignment-input-box
  [state should-show]
  (assoc-in state [:user-input :inventory-item-assignment :shown] should-show))

(defn should-show-item-assignment-input-box [state]
  (get-in state [:user-input :inventory-item-assignment :shown]))

(defn set-new-device-serial-number
  [state new-serial-number]
  (assoc-in state [:user-input :inventory-item-assignment :serial-number] new-serial-number))

(defn get-new-device-serial-number
  [state]
  (get-in state [:user-input :inventory-item-assignment :serial-number]))

(defn reset-new-inventory-item-input
  [state]
  (assoc-in state [:user-input :inventory-item-assignment :serial-number] (create-empty-inventory-item-assignment)))


; Service call support logic

(defn get-next-pending-inventory-item-assignment-to-perform
  {:test (fn [] (test/is= (get-next-pending-inventory-item-assignment-to-perform
                            {:pending-inventory-item-assignments [{}]
                             :ongoing-inventory-item-assignment  {}})
                          nil)
           (test/is= (get-next-pending-inventory-item-assignment-to-perform
                       {:pending-inventory-item-assignments [{:serial-number 1} {:serial-number 2}]
                        :ongoing-inventory-item-assignment  nil})
                     {:serial-number 1}))}

  [state]
  (when (not (:ongoing-inventory-item-assignment state))
    (first (:pending-inventory-item-assignments state))))

(defn commit-new-pending-inventory-item-assignment
  {:test (fn [] (let [post-commit-state (-> (create-test-state)
                                            (set-new-device-serial-number :test-serial-number)
                                            (commit-new-pending-inventory-item-assignment))]
                  (test/is (empty? (get-new-device-serial-number post-commit-state)))
                  (test/is-not (should-show-item-assignment-input-box post-commit-state))
                  (test/is= (count (get-next-pending-inventory-item-assignment-to-perform post-commit-state))
                            1)))}
  [state]
  (-> state
      (update :pending-inventory-item-assignments conj {:serial-number
                                                        (get-new-device-serial-number state)})
      (reset-new-inventory-item-input)
      (set-should-show-item-assignment-input-box false)))

(defn remove-pending-inventory-item-assignment
  {:test (fn [] (test/is= (-> (create-state {:person-id "test"})
                              (set-new-device-serial-number "test-sn")
                              (commit-new-pending-inventory-item-assignment)
                              (set-new-device-serial-number "test-sn 2")
                              (commit-new-pending-inventory-item-assignment)
                              (remove-pending-inventory-item-assignment {:serial-number "test-sn"})
                              (get-next-pending-inventory-item-assignment-to-perform)
                              (:serial-number))
                          "test-sn 2"))}

  [state {serial-number :serial-number}]
  (update state :pending-inventory-item-assignments
          (fn [pending-assignments]
            (remove (fn [assignment]
                      (= (:serial-number assignment) serial-number))
                    pending-assignments))))

(defn cache-is-dirty?
  [state]
  (> (:latest-acceptable-cache-fetch-time state)
     (get-in state [:get-person-details-response ::reception-timestamp])))

(defn has-good-person-details-response
  [state]
  (and (get-in state [:get-person-details-response :data])
       (not (cache-is-dirty? state))))

(defn should-get-person-detail? [state]
  (and (not (:fetching-person-details state))
       (not (has-good-person-details-response state))))

(defn started-get-person-detail-service-call [state]
  (assoc state :fetching-person-details true))

(defn receive-get-person-detail-service-response
  [state response request]
  (assoc state :fetching-person-details false
               :get-person-details-response
               (-> response
                   (util/->clojure-keys)
                   (assoc ::reception-timestamp (create-long-timestamp)))))

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

(defn on-remote-state-mutation
  [state _]
  (assoc state :latest-acceptable-cache-fetch-time (create-long-timestamp)))
