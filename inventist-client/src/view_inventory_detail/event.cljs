(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]
            [oops.core :refer [ocall]]
            [util.inventory.core :as util]
            [service-reassign-inventory-item.core :as reassign]))

(defn dropped-inventory-item-on-person [{:keys [inventory-item-id
                                                person-id]
                                         :as   data}]
  (rem/create-event {:name :dropped-inventory-item-on-person
                     :data data}))

(defn clicked-user [user-id]
  (rem/create-event {:name :clicked-user
                     :data {:user-id user-id}}))

(defn report-issue-clicked [device-id]
  (rem/create-event {:name :report-issue-clicked
                     :data {:device-id device-id}}))

(defn new-report-issue-file [file]
  (rem/create-event {:name :new-report-issue-file
                     :data {:file file}}))

(defn set-report-issue-description [description]
  (rem/create-event {:name :set-report-issue-description
                     :data {:description description}}))

(defn send-report-issue-form []
  (rem/create-event {:name :send-report-issue-form}))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)

      :report-issue-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name        :report-issue-clicked
                                :fn-and-args [core/set-show-report-issue-form true]}))
          (rem/create-anonymous-event))

      :new-report-issue-file
      (-> event
          (rem/append-action
            (rem/create-action {:name        :new-report-issue-file
                                :fn-and-args [core/set-report-issue-file (get-in event [:data :file])]}))
          (rem/create-anonymous-event))

      :set-report-issue-description
      (-> event
          (rem/append-action
            (rem/create-action {:name        :set-report-issue-description
                                :fn-and-args [core/set-report-issue-description (get-in event [:data :description])]}))
          (rem/create-anonymous-event))

      :send-report-issue-form
      (-> event
          (rem/append-action
            (rem/create-action {:name        :send-report-issue-form
                                :fn-and-args [core/set-should-send-report-issue-form true]}))
          (rem/create-anonymous-event))

      :close-report-issue
      (-> event
          (rem/append-action
            (rem/create-action {:name        :close-report-issue
                                :fn-and-args [core/set-show-report-issue-form false]}))
          (rem/create-anonymous-event))

      :reassign-device-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name        :reassign-device-clicked
                                :fn-and-args [core/set-edit-mode true]}))
          (rem/create-anonymous-event))

      :cancel-device-reassignment
      (-> event
          (rem/append-action
            (rem/create-action {:name        :cancel-device-reassignment
                                :fn-and-args [core/set-edit-mode false]}))
          (rem/create-anonymous-event))

      :clicked-user
      (-> event
          (rem/create-event {:new-name :selected-person
                             :new-data {:person-id (get-in event [:data :user-id])}}))

      :dropped-inventory-item-on-person
      (let [{:keys [inventory-item-id
                    person-id]} (:data event)]
        (-> event
            (rem/create-anonymous-event)
            (rem/append-action
              (rem/create-action {:name        :add-pending-inventory-item-reassignment
                                  :state-path  core/service-reassign-inventory-item-state-path
                                  :fn-and-args [reassign/add-pending-item-reassignment
                                                {:inventory-item-id inventory-item-id
                                                 :new-assignee-id   person-id}]})))))))



