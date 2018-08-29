(ns collections.event
  (:require [remodular.core :as rem]
            [collections.core :as core]
            [util.inventory.core :as util]))

(defn handle-event
  [_state event]
  (if (not (rem/triggered-by-me? event))
    (rem/create-anonymous-event event)
    (case (:name event)

      :clicked-collection
      (let [{collection-id :collection-id} (:data event)]
        (-> (rem/create-event event)
            (rem/append-action (rem/create-action {:name        :set-selected-collection
                                                   :fn-and-args [core/set-selected-collection-id collection-id]}))))

      (rem/create-anonymous-event event))))
