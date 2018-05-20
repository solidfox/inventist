(ns inventist-client.page.people.core
  (:require [view-person-detail.core :as view-person-detail]
            [view-people-overview.core :as view-people-overview]
            [clojure.string :refer [lower-case]]))

(defn person-detail-state-path [person-id] [:view-modules :view-person-detail person-id])
(defn people-overview-state-path [] [:view-modules :view-people-overview])

(defn create-state
  []
  (-> {:selected-person-id nil}
      (assoc-in (people-overview-state-path) (view-people-overview/create-state))))

(defn create-person-detail-args
  [state person-id]
  (let [state-path (person-detail-state-path person-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-people-overview-args
  [state]
  (let [state-path (people-overview-state-path)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn set-selected-person-id
  [state person-id]
  (-> state
      (assoc :selected-person-id person-id)
      (update-in (person-detail-state-path person-id)
                 (fn [person-detail-state]
                   (if (nil? person-detail-state)
                     (view-person-detail/create-state {:person-id person-id})
                     person-detail-state)))))
