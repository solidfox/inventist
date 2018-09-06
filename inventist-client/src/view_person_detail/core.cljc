(ns view-person-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]
            [ysera.test :as test]
            [service-reassign-inventory-item.core :as reassign]
            [#?(:cljs cljs-time.core :clj clj-time.core) :as time]
            [#?(:cljs cljs-time.coerce :clj clj-time.coerce) :as coerce]))

(defn create-empty-inventory-item-assignment []
  {:shown         false
   :serial-number ""})

(defn create-long-timestamp []
  (coerce/to-long (time/now)))

(def service-reassign-inventory-item-state-path [:module :service-reassign-inventory-item])

(defn create-state
  [{person-id :person-id}]
  (-> {:person-id                          person-id
       :latest-acceptable-cache-fetch-time (create-long-timestamp)
       :user-input                         {:inventory-item-assignment (create-empty-inventory-item-assignment)}

       :fetching-person-details            false
       :get-person-details-response        nil}
      (assoc-in service-reassign-inventory-item-state-path (reassign/create-state))))

(defn create-test-state []
  (create-state {:person-id :test-person-id}))

(defn get-person [state]
  (get-in state [:get-person-details-response :body :data :person]))


; User input state logic

(defn set-should-show-item-assignment-input-box
  [state should-show?]
  (assoc-in state [:user-input :inventory-item-assignment :shown] should-show?))

(defn should-show-item-assignment-input-box [state]
  (get-in state [:user-input :inventory-item-assignment :shown]))


; Service call support logic

(defn cache-is-dirty?
  [state]
  (> (:latest-acceptable-cache-fetch-time state)
     (get-in state [:get-person-details-response ::reception-timestamp])))

(defn has-good-person-details-response
  [state]
  (and (get-in state [:get-person-details-response :body :data])
       (not (cache-is-dirty? state))))

(defn should-get-person-detail? [state]
  (and (not (:fetching-person-details state))
       (not (has-good-person-details-response state))))

(defn started-get-person-detail-service-call [state]
  (assoc state :fetching-person-details true))

(defn receive-get-person-detail-service-response
  [state response _request]
  (assoc state :fetching-person-details false
               :get-person-details-response
               (-> response
                   (util/->clojure-keys)
                   (assoc ::reception-timestamp (create-long-timestamp)))))

(defn on-remote-state-mutation
  [state _]
  (assoc state :latest-acceptable-cache-fetch-time (create-long-timestamp)))
