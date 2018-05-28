(ns view-inventory-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]))

(defn create-state
  [{inventory-id      :inventory-item-id}]
  {:inventory-item-id                   inventory-id
   :edit-mode                           false
   :report-issue-file                   {}
   :report-issue-mode                   false
   :fetching-inventory-details          false
   :should-refetch-get-inventory-detail false
   :get-inventory-details-response      nil})

(defn set-report-issue-file
  [state file]
  (assoc-in state [:report-issue-file :name] (:name file))
  (assoc-in state [:report-issue-file :size] (:size file))
  (assoc-in state [:report-issue-file :type] (:type file))
  (assoc-in state [:report-issue-file :date] (:date file)))

(defn started-get-inventory-detail-service-call [state]
  (assoc state :fetching-inventory-details true))

(defn should-get-inventory-detail? [state]
  (and (not (:fetching-inventory-details state))
       (or (:should-refetch-get-inventory-detail state)
           (not (get-in state [:get-inventory-details-response :data])))))

(defn receive-get-inventory-detail-service-response [state response request]
  (-> state
      (assoc :should-refetch-get-inventory-detail false)
      (assoc :fetching-inventory-details false)
      (assoc :get-inventory-details-response (util/->clojure-keys response))))

(defn set-report-issue-mode
  [state new-report-issue-mode]
  (assoc state :report-issue-mode new-report-issue-mode))

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))