(ns view-inventory-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]))

(defn create-state
  [{inventory-id :inventory-item-id}]
  {:inventory-item-id                   inventory-id
   :edit-mode                           false
   :should-show-report-issue-form       false
   :report-issue-form-data              nil
   :fetching-inventory-details          false
   :get-inventory-details-response      nil})

(defn should-show-report-issue-form? [state]
  (:should-show-report-issue-form state))

(defn set-report-issue-description
  [state description]
  (assoc-in state [:report-issue-form-data :description] description))

(defn set-report-issue-file
  [state file]
  (assoc-in state [:report-issue-form-data :file] file))

(defn started-get-inventory-detail-service-call [state]
  (assoc state :fetching-inventory-details true))

(defn should-get-inventory-detail? [state]
  (and (not (:fetching-inventory-details state))
       (or (:should-refetch-get-inventory-detail state)
           (not (get-in state [:get-inventory-details-response :data])))))

(defn receive-get-inventory-detail-service-response [state response request]
  (-> state
      (assoc :fetching-inventory-details false)
      (assoc :get-inventory-details-response (util/->clojure-keys response))))

(defn set-show-report-issue-form
  [state new-report-issue-mode]
  (assoc state :should-show-report-issue-form new-report-issue-mode))

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))
