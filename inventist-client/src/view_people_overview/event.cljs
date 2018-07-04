(ns view-people-overview.event
  (:require [remodular.core :as rem]
            [view-people-overview.core :as core]))

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
      :person-selected
      (let [person (get-in event [:data :person])]
        (-> event
            (rem/append-action
              (rem/create-action {:name :set-selected-person-id
                                  :fn-and-args [core/set-selected-person-id (:id person)]}))
            (rem/create-event {:new-name :person-selected
                               :new-data {:person-id (:id person)}})))
      (rem/create-anonymous-event event))

    event))


