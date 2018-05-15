(ns view-dashboard-detail.core
  (:require [view-dashboard-detail.mock-data :as mock-data]))

(defn create-state
  [{dashboard-id :dashboard-id}]
  {:dashboard-id                   dashboard-id
   :fetching-dashboard-details     false
   :get-dashboard-details-response {:status   200
                                    :response (mock-data/create-person-detail {:id "mock-dashboard-id"})}})

