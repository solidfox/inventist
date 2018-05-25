(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]))


(defn clicked-user [user-id]
  (rem/create-event {:name :clicked-user
                     :data {:user-id user-id}}))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :reassign-device-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name :reassign-device-clicked
                                :fn-and-args [core/set-edit-mode true]})))

      :clicked-user
      (rem/create-event event
                        {:new-name :show-person
                         :new-data {:person-id (get-in event [:data :user-id])}}))))



