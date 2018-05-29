(ns inventist-client.services
  (:require [inventist-client.page.people.services :as people-page]
            [inventist-client.page.inventory.services :as inventory-page]
            [remodular.core :as a]
            [inventist-client.core :as core]))

(defn get-services [{{state :state} :input}]
  (case (first (:path state))
    :people
    (a/prepend-state-path-to-services
      (people-page/get-services (core/create-people-page-args state))
      core/people-page-state-path)
    :inventory
    (a/prepend-state-path-to-services
      (inventory-page/get-services (core/create-inventory-page-args state))
      core/inventory-page-state-path)
    []))


