(ns inventist-client.page.people.services
  (:require [view-people-overview.services :as people-overview]
            [remodular.core :as a]
            [inventist-client.page.people.core :as core]))

(defn get-services [{{state :state} :input}]
  (concat
    (a/prepend-state-path-to-services
      (people-overview/get-services (core/create-people-overview-args state))
      (core/people-overview-state-path))))
