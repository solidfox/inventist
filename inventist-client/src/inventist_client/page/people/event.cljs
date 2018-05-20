(ns inventist-client.page.people.event
  (:require [remodular.core :as rem]
            [inventist-client.page.people.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-child? (core/people-overview-state-path) event)
    (case (:name event)
      :person-selected
      (let [person-id (get-in event [:data :person-id])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :set-selected-person-id
                                  :fn-and-args [core/set-selected-person-id person-id]}))
            (rem/create-anonymous-event)))
      (rem/create-anonymous-event event))
    (rem/create-anonymous-event event)))


