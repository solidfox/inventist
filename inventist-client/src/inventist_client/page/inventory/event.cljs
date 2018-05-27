(ns inventist-client.page.inventory.event
  (:require [remodular.core :as rem]
            [inventist-client.page.inventory.core :as core]))


(defn handle-event
  [_state event]
  (cond
    (rem/triggered-by-child? (core/inventory-overview-state-path) event)
    (case (:name event)
      :inventory-item-selected
      (let [inventory-id (get-in event [:data :inventory-item-id])]
        (-> event
            (rem/create-event {:new-name :show-inventory-item
                               :new-data {:inventory-item-id inventory-id}})
            (rem/append-action
              (rem/create-action {:name        :set-selected-inventory-id
                                  :fn-and-args [core/set-selected-inventory-id inventory-id]}))))
      (rem/create-anonymous-event event))

    (rem/triggered-by-descendant-of-child? core/any-inventory-detail-state-path event)
    (case (:name event)
      :show-person
      (-> event
          (rem/create-event {:new-name (:name event)        ;TODO Tomas: is this really the best naming?
                             :new-data (:data event)}))
      (rem/create-anonymous-event event))

    :else
    (rem/create-anonymous-event event)))
