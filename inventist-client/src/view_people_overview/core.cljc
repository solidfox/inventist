(ns view-people-overview.core
  (:require [view-people-overview.mock-data :as mock-data]))

(defn create-state
  []
  {:selected-person-id          nil
   :filter                      nil
   :fetching-people-list     false
   :get-people-list-response {:status   200
                              :response (mock-data/create-get-people-list-response)}})

(defn started-get-people-list-service-call [state]
  (assoc state :fetching-people-list true))

(defn should-get-people-list? [state]
  (and (not (:fetching-people-list state))
       (not (= 200 (get-in state [:get-people-list-response :status])))))

(defn receive-get-people-list-service-response [state response request]
  (assoc state :get-people-list-response response))
