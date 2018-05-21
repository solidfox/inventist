(ns view-person-detail.event
  (:require [remodular.core :as rem]
            [view-person-detail.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :assign-new-device-clicked
      (rem/append-action
        event
        (rem/create-action {:name :assign-new-device-clicked
                            :fn-and-args
                                  [core/set-edit-mode true]})))
    (rem/create-anonymous-event event)))


