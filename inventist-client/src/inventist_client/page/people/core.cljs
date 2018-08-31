(ns inventist-client.page.people.core
  (:require [view-person-detail.core :as view-person-detail]
            [view-people-overview.core :as view-people-overview]
            [clojure.string :refer [lower-case]]
            [view-people-overview.core]
            [view-person-detail.core]))

(def any-person-detail-state-path [:view-modules :view-person-detail])
(defn person-detail-state-path [person-id] (concat any-person-detail-state-path [person-id]))
(defn people-overview-state-path [] [:view-modules :view-people-overview])

(defn set-selected-person-id
  [state person-id]
  (-> state
      (assoc :selected-person-id person-id)
      (update-in (person-detail-state-path person-id)
                 (fn [person-detail-state]
                   (if (nil? person-detail-state)
                     (view-person-detail/create-state {:person-id person-id})
                     person-detail-state)))))

(defn create-state
  [{selected-person-id :selected-person-id}]
  (-> {}
      (assoc-in (people-overview-state-path) (view-people-overview/create-state))
      (set-selected-person-id selected-person-id)))

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

(defn all-cached-person-detail-state-paths [state]
  (->> (keys (get-in state any-person-detail-state-path))
       (map person-detail-state-path)))

(defn on-remote-state-mutation
  [state remote-state-uri]
  (as-> state $
        (update-in $ (people-overview-state-path) view-people-overview.core/on-remote-state-mutation remote-state-uri)
        (reduce (fn [state person-detail-state-path]
                  (update-in state person-detail-state-path view-person-detail.core/on-remote-state-mutation remote-state-uri))
                $ (all-cached-person-detail-state-paths state))))

(defn get-selected-item-peek-data [state]
  (view-people-overview/get-selected-item-peek-data
    (get-in state (people-overview-state-path))))
