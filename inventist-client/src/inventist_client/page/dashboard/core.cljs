(ns inventist-client.page.dashboard.core
  (:require [view-dashboard.core :as view-dashboard-detail]
            [clojure.string :refer [lower-case]]))

(defn dashboard-detail-state-path [dashboard-id] [:view-modules :view-dashboard-detail dashboard-id])

(defn create-state
  []
  (-> {}
      (assoc-in (dashboard-detail-state-path "mock-dashboard-id") (view-dashboard-detail/create-state "mock-dashboard-id"))))

(defn create-dashboard-detail-args
  [state dashboard-id]
  (let [state-path (dashboard-detail-state-path dashboard-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))
