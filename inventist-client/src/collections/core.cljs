(ns collections.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  [{:keys [heading]}]
  {:heading                          heading

   :selected-collection-id           nil

   :selected-collection-items        {}

   :get-collections-service-fetching false
   :get-collections-service-response nil})

(defn set-selected-collection-item [state {:keys [collection-id selected-item]}]
  (assoc-in state [:selected-collection-items collection-id] selected-item))

(defn get-selected-collection-item [state {:keys [collection-id]}]
  (get-in state [:selected-collection-items collection-id]))

(defn should-get-collections? [state]
  (not= 200 (get-in state [:get-collections-service-response :status])))

(defn started-get-collections-service-call [state]
  (assoc state :get-collections-service-fetching true))

(defn receive-get-collections-service-call [state response _request]
  (assoc state :get-collections-service-response response))

(defn set-selected-collection-id [state collection-id]
  (assoc state :selected-collection-id collection-id))
