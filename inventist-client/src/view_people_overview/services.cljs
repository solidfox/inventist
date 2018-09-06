(ns view-people-overview.services
  (:require [view-people-overview.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]
            [service-reassign-inventory-item.services :as reassign]
            [util.inventory.core :as util]
            [remodular.core :as rem]))


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
  (concat (when (core/should-get-people-list? state)
            [(get-people-list)])
          (rem/prepend-state-path-to-services
            (reassign/get-services
              {:input {:state (util/spy (get-in state core/service-reassign-inventory-item-state-path))}})
            core/service-reassign-inventory-item-state-path)))


