(ns inventist-client.event
  (:require [remodular.core :as arch]
            [inventist-client.core :as core]))

(defn clicked-navigation-icon
  [{target-page-id :target-page-id}]
  {:name :clicked-navigation-icon
   :data {:target-page-id target-page-id}})

(defn clicked-navigation-icon-action
  [event]
  (arch/create-action {:name        "Navigate to page"
                       :fn-and-args [core/set-active-page (get-in event [:data :target-page-id])]}))

(defn handle-event
  [_state event]
  (if (arch/triggered-by-me? event)
      (condp = (:name event)
        :clicked-navigation-icon
        (arch/append-action event (clicked-navigation-icon-action event)))
      event))


