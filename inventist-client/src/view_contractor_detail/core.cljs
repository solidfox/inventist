(ns view-contractor-detail.core
  (:require [view-contractor-detail.mock-data :as mock-data]))

(defn create-state
  [{contractor-id :contractor-id}]
  {:contractor-id                   contractor-id
   :fetching-contractor-details     false
   :get-contractor-details-response {:status 200
                                     :response (mock-data/create-contractor-detail {:id "mock-contractor-id"})}})

