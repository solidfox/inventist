(ns inventist-client.event
  (:require [remodular.core :as rem]
            [inventist-client.core :as core]))

(defn clicked-navigation-icon
  [{target-page-id :target-page-id}]
  {:name :clicked-navigation-icon
   :data {:target-page-id target-page-id}})

(defn clicked-navigation-icon-action
  [event]
  (rem/create-action {:name        "Navigate to page"
                      :fn-and-args [core/set-active-page (get-in event [:data :target-page-id])]}))

(defn handle-event
  [_state event]
  (cond
    (= (:name event) :show-inventory-item)
    (let [inventory-item-id (get-in event [:data :inventory-item-id])]
      (-> event
          (rem/append-action
            (rem/create-action {:name        :show-inventory-item
                                :fn-and-args [core/show-inventory-item inventory-item-id]}))))

    (= (:name event) :show-person)
    (let [person-id (get-in event [:data :person-id])]
      (-> event
          (rem/append-action
            (rem/create-action {:name        :show-person
                                :fn-and-args [core/show-person person-id]}))))

    (rem/triggered-by-me? event)
    (condp = (:name event)
      :clicked-navigation-icon
      (rem/append-action event (clicked-navigation-icon-action event)))

    :else
    event))


