(ns view-person-detail.event
  (:require [remodular.core :as rem]
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

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :new-device-serial-number-changed
      (-> event
          (rem/append-action
            (rem/create-action {:name :new-device-serial-number-changed
                                :fn-and-args [core/set-new-device-serial-number (get-in event [:data :new-value])]}))
          (rem/create-anonymous-event))
      :commit-new-device
      (-> event
          (rem/append-action
            (rem/create-action {:name :commit-new-device
                                :fn-and-args [core/commit-new-pending-inventory-item-assignment]}))
          (rem/create-anonymous-event))
      :cancel-new-device-assignment
      (-> event
          (rem/append-action
            (rem/create-action {:name :cancel-new-device-assignment
                                :fn-and-args [core/set-edit-mode false]}))
          (rem/create-anonymous-event))
      :assign-new-device-clicked
      (-> event
          (rem/append-action
            (rem/create-action {:name :assign-new-device-clicked
                                :fn-and-args [core/set-edit-mode true]}))
          (rem/create-anonymous-event))
      :clicked-device
      (rem/create-event event
                        {:new-name :show-inventory-item
                         :new-data {:inventory-item-id (get-in event [:data :device-id])}}))))


