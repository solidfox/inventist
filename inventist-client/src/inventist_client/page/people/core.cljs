(ns inventist-client.page.people.core
  (:require [view-person-detail.core :as view-person-detail]
            [view-people-overview.core :as view-people-overview]
            [clojure.string :refer [lower-case]]))

(defn person-detail-state-path [person-id] [:view-modules :view-person-detail person-id])
(defn people-overview-state-path [] [:view-modules :view-people-overview])

(defn create-state
  []
  (-> {}
      (assoc-in (people-overview-state-path) (view-people-overview/create-state))
      (assoc-in (person-detail-state-path "mock-person-id") (view-person-detail/create-state "mock-person-id"))))

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
