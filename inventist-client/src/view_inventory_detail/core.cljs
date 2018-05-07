(ns view-inventory-detail.core
  (:require [view-inventory-detail.mock-data :as mock-data]))

(defn create-state
  [{inventory-id :inventory-id}]
  {:inventory-id                   inventory-id
   :fetching-inventory-details     false
   :get-inventory-details-response {:status 200
                                    :response (mock-data/create-inventory-detail)}})

