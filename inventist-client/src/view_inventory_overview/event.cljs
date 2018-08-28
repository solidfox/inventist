(ns view-inventory-overview.event
  (:require [remodular.core :as rem]
            [view-inventory-overview.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :retry-fetching-data
      (-> event
          (rem/create-anonymous-event)
          (rem/append-action
            (rem/create-action {:name :retry-fetching-data
                                :fn-and-args
                                      [core/set-should-retry-on-fetch-error true]})))
      :search-string-changed
      (rem/append-action
        event
        (rem/create-action {:name :search-string-changed
                            :fn-and-args
                                  [core/set-free-text-search (get-in event [:data :new-value])]}))
      :inventory-item-selected
      (let [inventory (get-in event [:data :item])]
        (-> event
            (rem/append-action
              (rem/create-action {:name :set-selected-inventory-id
                                  :fn-and-args [core/set-selected-inventory-id (:id inventory)]}))
            (rem/create-event {:new-name :inventory-item-selected
                               :new-data {:inventory-item-id (:id inventory)}})))
      (rem/create-anonymous-event event))

    event))


