(ns view-inventory-overview.core
 (:require [view-inventory-overview.mock-data :as mock-data]
           [people.core :as mock-people]))

(defn create-state
  []
  {:selected-item-id            nil
   :filter                      nil
   :fetching-inventory-list     false
   :get-inventory-list-response {:status 200
                                 :response (mock-data/create-get-inventory-list-response)}})
