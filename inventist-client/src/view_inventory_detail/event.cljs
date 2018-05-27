(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]))

(defn clicked-user [user-id]
  (rem/create-event {:name :clicked-user
                     :data {:user-id user-id}}))

(def cancel-device-reassignment
  (rem/create-event {:name :cancel-device-reassignment}))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
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
          (rem/create-event {:new-name :show-person
                             :new-data {:person-id (get-in event [:data :user-id])}})))))



