(ns view-inventory-overview.services
  (:require [view-inventory-overview.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]
            [util.inventory.core :as util]))

(def inventory-list-graph-ql
  [:id
   :brand
   :model-name
   :class
   :color
   :serial-number
   [:user [[:groups [:name]]]]
   {:field/alias :image
    :field/data  [:image-url]}])

(defn get-inventory-list
  []
  {:name   :get-inventory-list
   :before [core/started-get-inventory-list-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:queries [[:computers inventory-list-graph-ql]]})}}
   :after  [core/receive-get-inventory-list-service-response]})

(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-inventory-list? state)
    [(get-inventory-list)]))

