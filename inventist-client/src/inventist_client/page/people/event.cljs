(ns inventist-client.page.people.event
  (:require [remodular.core :as rem]
            [inventist-client.page.people.core :as core]
            [util.inventory.core :as util]))

(defn handle-event
  [_state event] ;TODO State not working here?
  (cond
    (rem/triggered-by-child? (core/people-overview-state-path) event)
    (case (:name event)
      :person-selected
      (let [person-id (get-in event [:data :person-id])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :set-selected-person-id
                                  :fn-and-args [core/set-selected-person-id person-id]}))
            (rem/create-event {:new-name :show-person
                               :new-data {:person-id person-id}})))
      (rem/create-anonymous-event event))

    (rem/triggered-by-descendant-of-child? core/any-person-detail-state-path event)
    (case (:name event)
      :show-inventory-item
      (rem/create-event event {:new-name (:name event) ;TODO Tomas: is this really the best naming?
                               :new-data (:data event)})
      (rem/create-anonymous-event event))

    :else
    (rem/create-anonymous-event event)))
