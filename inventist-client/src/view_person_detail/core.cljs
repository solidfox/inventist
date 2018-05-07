(ns view-person-detail.core
  (:require [view-person-detail.mock-data :as mock-data]))

(defn create-state
  [{person-id :person-id}]
  {:person-id                   person-id
   :fetching-person-details     false
   :get-person-details-response {:status 200
                                 :response (mock-data/create-person-detail {:id "mock-person-id"})}})

