(ns inventist-client.page.people.services
  (:require [view-people-overview.services :as people-overview]
            [view-person-detail.services :as person-detail]
            [remodular.core :as a]
            [inventist-client.page.people.core :as core]))

(defn get-services [{{state :state} :input}]
  (concat
    (a/prepend-state-path-to-services
      (people-overview/get-services (core/create-people-overview-args state))
      (core/people-overview-state-path))
    (when (:selected-person-id state)
      (a/prepend-state-path-to-services
        (person-detail/get-services (core/create-person-detail-args state (:selected-person-id state)))
        (core/person-detail-state-path (:selected-person-id state))))))
