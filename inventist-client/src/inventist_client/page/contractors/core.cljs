(ns inventist-client.page.contractors.core
  (:require [view-contractor-detail.core :as view-contractor-detail]
            [view-contractors-overview.core :as view-contractors-overview]
            [clojure.string :refer [lower-case]]))

(defn contractor-detail-state-path [contractor-id] [:view-modules :view-contractor-detail contractor-id])
(defn contractors-overview-state-path [] [:view-modules :view-contractors-overview])

(defn create-state
  []
  (-> {}
      (assoc-in (contractors-overview-state-path) (view-contractors-overview/create-state))
      (assoc-in (contractor-detail-state-path "mock-contractor-id") (view-contractor-detail/create-state "mock-contractor-id"))))

(defn create-contractor-detail-args
  [state contractor-id]
  (let [state-path (contractor-detail-state-path contractor-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-contractors-overview-args
  [state]
  (let [state-path (contractors-overview-state-path)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))
