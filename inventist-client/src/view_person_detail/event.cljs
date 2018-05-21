(ns view-person-detail.event
  (:require [remodular.core :as rem]
            [view-person-detail.core :as core]
            [util.inventory.core :as util]))

(defn clicked-device [device-id]
  (rem/create-event {:name :clicked-device
                     :data {:device-id device-id}}))

(defn handle-event
  [_state event]
  (util/spy event)
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)
      :assign-new-device-clicked
      (rem/append-action
        event
        (rem/create-action {:name :assign-new-device-clicked
                            :fn-and-args [core/set-edit-mode true]}))
      :clicked-device
      (util/spy (rem/create-event event
                                  {:new-name :show-inventory-item
                                   :new-data {:inventory-item-id (get-in event [:data :device-id])}})))))


