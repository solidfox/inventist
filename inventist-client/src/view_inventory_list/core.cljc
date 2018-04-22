(ns view-inventory-list.core
 (:require [view-inventory-list.mock-data :as mock-data]))

(defn create-state
  []
  {:selected-item-id            nil
   :filter                      nil
   :fetching-inventory-list     false
   :get-inventory-list-response {:status 200
                                 :response (mock-data/create-get-inventory-list-response)}})
