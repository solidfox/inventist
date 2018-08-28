(ns collections.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  []
  {:selected-collection nil

   :get-collections-service-fetching false
   :get-collections-service-response nil})

(defn should-get-collections? [state]
  (not= 200 (get-in state [:get-collections-service-response :status])))

(defn started-get-collections-service-call [state]
  (assoc state :get-collections-service-fetching true))

(defn receive-get-collections-service-call [state response _request]
  (assoc state :get-collections-service-response response))
