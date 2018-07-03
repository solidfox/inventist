(ns view-inventory-detail.core
  (:require [clojure.string :as str]
            [util.inventory.core :as util]
            [oops.core :refer [ocall oget]]))

(defn create-state
  [{inventory-id :inventory-item-id}]
  {:inventory-item-id              inventory-id
   :edit-mode                      false
   :report-issue-form              nil
   :fetching-inventory-details     false
   :get-inventory-details-response nil})


;;;;;;;;;;;;;;; Inventory detail fetching

(defn should-get-inventory-detail? [state]
  (and (not (:fetching-inventory-details state))
       (or (:should-refetch-get-inventory-detail state)
           (not (get-in state [:get-inventory-details-response :body :data])))))

(defn started-get-inventory-detail-service-call [state]
  (assoc state :fetching-inventory-details true))

(defn receive-get-inventory-detail-service-response [state response request]
  (-> state
      (assoc :fetching-inventory-details false)
      (assoc :get-inventory-details-response (util/->clojure-keys response))))


;;;;;;;;;;;;;;;; Report issue form

(defn should-show-report-issue-form? [state]
  (get-in state [:report-issue-form :should-show]))

(defn set-show-report-issue-form
  [state new-report-issue-mode]
  (assoc-in state [:report-issue-form :should-show] new-report-issue-mode))

(defn set-report-issue-description
  [state description]
  (assoc-in state [:report-issue-form :user-input :description] description))

(defn set-report-issue-file
  [state file-object]
  (let [file-object-url (ocall js/window [:URL :createObjectURL] file-object)]
    (assoc-in state [:report-issue-form :user-input :file] {:object-url file-object-url
                                                            :name       (oget file-object :name)
                                                            :type       (oget file-object :type)
                                                            :size       (oget file-object :size)})))

(defn should-send-report-issue-form?
  [{report-issue-form :report-issue-form}]
  (and
    (not (:is-sending report-issue-form))
    (:should-send report-issue-form)))

(defn set-should-send-report-issue-form
  [state]
  (assoc-in state [:report-issue-form :should-send] true))

(defn started-send-report-issue-form-service-call
  [state]
  (-> state
      (assoc-in [:report-issue-form :is-sending] true)
      (assoc-in [:report-issue-form :should-send] false)))

(defn receive-send-report-issue-form-service-response
  [state response request]
  (update state :report-issue-form
          (fn [report-issue-form]
            (-> report-issue-form
                (assoc :send-response response
                       :should-send false
                       :is-sending false)))))


;;;;;;;;;;;;;;;;; Edit mode

(defn set-edit-mode
  [state new-edit-mode]
  (assoc state :edit-mode new-edit-mode))
