(ns collections.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  [{:keys [heading]}]
  {:heading                                heading

   :selected-collection-id                 nil

   :all-inventory-collection-selected-item nil
   :all-people-collection-selected-item    nil

   :get-collections-service-fetching       false
   :get-collections-service-response       nil})

(defn should-get-collections? [state]
  (not= 200 (get-in state [:get-collections-service-response :status])))

(defn started-get-collections-service-call [state]
  (assoc state :get-collections-service-fetching true))

(defn receive-get-collections-service-call [state response _request]
  (assoc state :get-collections-service-response response))

(defn set-all-inventory-collection-selected-item [state inventory-item]
  (assoc state :all-inventory-collection-selected-item inventory-item))

(defn set-all-people-collection-selected-person [state person]
  (assoc state :all-people-collection-selected-person person))

(defn set-selected-collection-id [state collection-id]
  (assoc state :selected-collection-id collection-id))
