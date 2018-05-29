(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]
            [oops.core :refer [ocall]]))

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

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :new-report-issue-file
      (let [file-object-url (ocall js/window [:URL :createObjectURL] (get-in event [:data :file]))]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :new-report-issue-file
                                  :fn-and-args [core/set-report-issue-file-url file-object-url]}))
            (rem/create-anonymous-event)))

      :set-report-issue-description
      (-> event
          (rem/append-action
            (rem/create-action {:name        :set-report-issue-description
                                :fn-and-args [core/set-report-issue-description (get-in event [:data :description])]}))
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

      :report-issue-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name        :report-issue-clicked
                                :fn-and-args [core/set-show-report-issue-form true]}))
          (rem/create-anonymous-event))

      :close-report-issue
      (-> event
          (rem/append-action
            (rem/create-action {:name        :close-report-issue
                                :fn-and-args [core/set-show-report-issue-form false]}))
          (rem/create-anonymous-event))

      :clicked-user
      (-> event
          (rem/create-event {:new-name :show-person
                             :new-data {:person-id (get-in event [:data :user-id])}})))))



