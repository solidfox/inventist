(ns inventist-client.page.inventory.event
  (:require [remodular.core :as rem]
            [inventist-client.page.inventory.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-child? (core/inventory-overview-state-path) event)
    (case (:name event)
      :inventory-selected
      (let [inventory-id (get-in event [:data :inventory-id])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :set-selected-inventory-id
                                  :fn-and-args [core/set-selected-inventory-id inventory-id]}))
            (rem/create-anonymous-event)))
      (rem/create-anonymous-event event))
    (rem/create-anonymous-event event)))


