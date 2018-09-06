(ns view-person-detail.event
  (:require [remodular.core :as rem]
            [service-reassign-inventory-item.core :as reassign]
            [view-person-detail.core :as core]
            [util.inventory.core :as util]))

(defn clicked-device [device-id]
  (rem/create-event {:name :clicked-device
                     :data {:device-id device-id}}))

(def cancel-new-device-assignment
  (rem/create-event {:name :cancel-new-device-assignment}))

(defn new-device-serial-number-changed [new-value]
  (rem/create-event {:name :new-device-serial-number-changed
                     :data {:new-value new-value}}))

(def commit-new-device
  (rem/create-event {:name :commit-new-device}))

(defn reassign-inventory-item [{:keys [inventory-item-id
                                       person-id]
                                :as   data}]
  (rem/create-event {:name :reassign-inventory-item
                     :data data}))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :reassign-inventory-item
      (let [{:keys [inventory-item-id
                    person-id]} (:data event)]
        (-> event
            (rem/create-anonymous-event)
            (rem/append-action
              (rem/create-action {:name        :reassign-inventory-item
                                  :state-path  core/service-reassign-inventory-item-state-path
                                  :fn-and-args [reassign/add-pending-item-reassignment
                                                {:inventory-item-id inventory-item-id
                                                 :new-assignee-id   person-id}]}))))
      :cancel-new-device-assignment
      (-> event
          (rem/append-action
            (rem/create-action {:name        :cancel-new-device-assignment
                                :fn-and-args [core/set-should-show-item-assignment-input-box false]}))
          (rem/create-anonymous-event))
      :assign-new-device-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name        :assign-new-device-clicked
                                :fn-and-args [core/set-should-show-item-assignment-input-box true]}))
          (rem/create-anonymous-event))
      :clicked-device
      (rem/create-event event
                        {:new-name :selected-inventory-item
                         :new-data {:inventory-item-id (get-in event [:data :device-id])}}))))


