(ns inventist-client.services
  (:require [inventist-client.page.people.services :as people-page]
            [inventist-client.page.inventory.services :as inventory-page]
            [remodular.core :as a]
            [inventist-client.core :as core]
            [ajax.core :refer [GET POST]]))

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

(defn perform-services
  [services handle-event]
  {:pre [(not-empty services)]}
  (doseq [{{url    :url
            params :params
            :as    data} :data
           on-response   :after
           state-path    :state-path} services]
    (POST url
          {:body            (:query params)
           :response-format :json
           :keywords?       true
           :handler         (fn [response]
                              (handle-event {:actions
                                             [{:fn-and-args (concat on-response [response data])
                                               :state-path  state-path}]}))}))
  (handle-event {:actions (map (fn [service]
                                 {:fn-and-args (:before service)
                                  :state-path  (or (:state-path service) [])})
                               services)}))
