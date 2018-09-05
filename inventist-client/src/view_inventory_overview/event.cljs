(ns view-inventory-overview.event
  (:require [remodular.core :as rem]
            [service-reassign-inventory-item.core :as reassign]
            [view-inventory-overview.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :dropped-person-on-inventory-item
      (let [{:keys [inventory-item-id
                    new-assignee-id]} (:data event)]
        (-> event
            (rem/create-anonymous-event)
            (rem/append-action
              (rem/create-action {:name        :add-pending-inventory-item-reassignment
                                  :state-path  core/service-reassign-inventory-item-state-path
                                  :fn-and-args [reassign/add-pending-item-reassignment
                                                {:inventory-item-id inventory-item-id
                                                 :new-assignee-id   new-assignee-id}]}))))

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

      :selected-inventory-item
      (let [inventory-item (get-in event [:data :item])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :set-selected-inventory-id
                                  :fn-and-args [core/set-selected-inventory-id (:id inventory-item)]}))
            (rem/create-event {:new-name :selected-inventory-item
                               :new-data {:inventory-item inventory-item}})))
      (rem/create-anonymous-event event))

    event))


