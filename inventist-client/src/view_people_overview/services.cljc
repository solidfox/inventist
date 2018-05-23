(ns view-people-overview.services
  (:require [view-people-overview.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]
            [util.inventory.core :as util]))


(def people-overview-graph-ql
  [:id
   :occupation
   :first-name
   :last-name
   [:groups [:name]]
   {:field/alias :image
    :field/data  [:photo-url]}
   [:inventory [:id
                :brand
                :model-name
                :color
                :model-identifier
                :class]]])

(defn get-people-list
  []
  {:name   :get-people-list
   :before [core/started-get-people-list-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:queries [[:people people-overview-graph-ql]]})}}
   :after  [core/receive-get-people-list-service-response]})

(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-people-list? state)
    [(get-people-list)]))


