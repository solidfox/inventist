(ns view-inventory-list.core
 (:require [view-inventory-list.mock-data :as mock-data]
           [people.core :as mock-people]))

(defn create-state
  []
  {:selected-item-id            nil
   :filter                      nil
   :fetching-inventory-list     false
   :get-inventory-list-response {:status 200
                                 :response (mock-people/create-get-people-list-response)}})
                                 ;:response (mock-data/create-get-inventory-list-response)}})
