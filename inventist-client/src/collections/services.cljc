(ns collections.services
  (:require [collections.core :as core]
            [ysera.test :refer [is=]]
            [util.inventory.core :as util]))

(defn get-collections
  []
  {:name   :get-collections
   :before [core/started-get-collections-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:queries [[:collections [:id :name]]]})}}
   :after  [core/receive-get-collections-service-call]})

(defn get-services
  [{{state :state} :input}]
  (remove nil?
          [(when (core/should-get-collections? state) (get-collections))]))
