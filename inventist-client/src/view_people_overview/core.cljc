(ns view-people-overview.core
  (:require [view-people-overview.mock-data :as mock-data]))

(defn create-state
  []
  {:selected-person-id       nil
   :filter                   nil
   :fetching-people-list     false
   :get-people-list-response nil})

(defn started-get-people-list-service-call [state]
  (assoc state :fetching-people-list true))

(defn should-get-people-list? [state]
  (and (not (:fetching-people-list state))
       (not (get-in state [:get-people-list-response :data]))))

(defn receive-get-people-list-service-response [state response request]
  (assoc state :get-people-list-response response))
