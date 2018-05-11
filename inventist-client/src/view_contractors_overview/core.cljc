(ns view-contractors-overview.core
  (:require [view-contractors-overview.mock-data :as mock-data]))

(defn create-state
  []
  {:selected-person-id          nil
   :filter                      nil
   :fetching-contractors-list     false
   :get-contractors-list-response {:status   200
                                   :response (mock-data/create-get-contractors-list-response)}})
