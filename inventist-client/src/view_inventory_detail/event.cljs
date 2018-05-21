(ns view-inventory-detail.event
  (:require [remodular.core :as rem]
            [view-inventory-detail.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :reassign-device-clicked
      (rem/append-action
        event
        (rem/create-action {:name :reassign-device-clicked
                            :fn-and-args
                                  [core/set-edit-mode true]})))
    (rem/create-anonymous-event event)))


