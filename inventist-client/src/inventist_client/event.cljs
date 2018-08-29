(ns inventist-client.event
  (:require [remodular.core :as rem]
            [inventist-client.core :as core]))

(defn clicked-navigation-icon [target-path]
  (rem/create-event {:name :clicked-navigation-icon
                     :data {:target-path target-path}}))

(defn handle-event
  [_state event]
  (cond
    (rem/triggered-by-me? event)
    (case (:name event)
      :inventory-item-selected
      (let [inventory-item    (get-in event [:data :inventory-item])
            inventory-item-id (:id inventory-item)]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :show-inventory-item
                                  :fn-and-args [core/show-inventory-item inventory-item-id]}))
            (rem/append-action
              (rem/create-action {:name        :set-inventory-collection-selection
                                  :fn-and-args [core/set-inventory-collection-selected-item inventory-item]}))))

      :clicked-navigation-icon
      (let [{:keys [target-path]} (:data event)]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :navigate-to-path
                                  :fn-and-args [core/set-path target-path]}))))

      :show-person
      (let [person-id (get-in event [:data :person-id])]
        (-> event
            (rem/append-action
              (rem/create-action {:name        :show-person
                                  :fn-and-args [core/show-person person-id]})))))

    (rem/triggered-by-child? core/inventory-collections-state-path event)
    (case (:name event)
      :clicked-collection
      (let [active-page (get-in event [:data :collection-id])]
        (-> event
            (rem/append-action
              (rem/create-action
                {:name        :set-active-page
                 :fn-and-args [core/set-active-page active-page]})))))

    :else
    event))


