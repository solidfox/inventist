(ns inventist-client.services
  (:require [inventist-client.page.people.services :as people-page]
            [remodular.core :as a]
            [inventist-client.core :as core]))

(defn get-services [{{state :state} :input}]
  (concat
    (a/prepend-state-path-to-services
      (people-page/get-services (core/create-people-page-args state))
      core/people-page-state-path)))
