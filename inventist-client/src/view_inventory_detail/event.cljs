(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]))

(defn clicked-user [user-id]
  (rem/create-event {:name :clicked-user
                     :data {:user-id user-id}}))

(defn report-issue-clicked [device-id]
  (rem/create-event {:name :report-issue-clicked
                     :data {:device-id device-id}}))

;(defn new-report-issue-file [file]
;  (rem/create-event {:name new-report-issue-file
;                     :data {:file file}}))

(defn new-report-issue-file [{name   :name
                              type   :type
                              size   :size
                              date   :date
                              object :object}]
  (rem/create-event {:name :new-report-issue-file
                     :data {:file {:name name
                                   :type type
                                   :size size
                                   :date date}}}))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :new-report-issue-file
      (-> event
          (rem/append-action
            (rem/create-action {:name        :new-report-issue-file
                                :fn-and-args [core/set-report-issue-file (get-in event [:data :file])]}))
          (js/console.log "FILE:"
                          (get-in event [:data :file :type])
                          (get-in event [:data :file :size])
                          (get-in event [:data :file :date])
                          (get-in event [:data :file :name]))
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
                                :fn-and-args [core/set-report-issue-mode true]}))
          (rem/create-anonymous-event))

      :close-report-issue
      (-> event
          (rem/append-action
            (rem/create-action {:name        :close-report-issue
                                :fn-and-args [core/set-report-issue-mode false]}))
          (rem/create-anonymous-event))

      :clicked-user
      (-> event
          (rem/create-event {:new-name :show-person
                             :new-data {:person-id (get-in event [:data :user-id])}})))))



