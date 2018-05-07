(ns view-people-overview.core
  (:require [view-people-overview.mock-data :as mock-data]))

(defn create-state
  []
  {:selected-person-id          nil
   :filter                      nil
   :fetching-people-list     false
   :get-people-list-response {:status   200
                              :response (mock-data/create-get-people-list-response)}})
