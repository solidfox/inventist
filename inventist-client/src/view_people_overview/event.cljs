(ns view-people-overview.event
  (:require [remodular.core :as rem]
            [view-people-overview.core :as core]
            [service-reassign-inventory-item.core :as reassign]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :reassign-inventory-item
      (let [{:keys [inventory-item-id
                    person-id]} (:data event)]
        (-> event
            (rem/create-anonymous-event)
            (rem/append-action
              (rem/create-action {:name        :add-pending-inventory-item-reassignment
                                  :state-path  core/service-reassign-inventory-item-state-path
                                  :fn-and-args [reassign/add-pending-item-reassignment
                                                {:inventory-item-id inventory-item-id
                                                 :new-assignee-id   person-id}]}))))

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
      :selected-person
      (let [person (get-in event [:data :person])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :set-selected-person-id
                                  :fn-and-args [core/set-selected-person-id (:id person)]}))
            (rem/create-event {:new-name :selected-person
                               :new-data {:person-id (:id person)
                                          :person    person}})))
      (rem/create-anonymous-event event))

    event))


