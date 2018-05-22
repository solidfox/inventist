(ns view-inventory-detail.services
  (:require [view-inventory-detail.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]
            [util.inventory.core :as util]))

(def person-details-graph-ql
  [:id
   :first-name
   :last-name
   :occupation
   [:groups [:name
             :id]]
   {:field/alias :image
    :field/data  [:photo-url]}])

(def inventory-details-graph-ql [:id
                                 {:field/alias :date
                                  :field/data  [:release-date]}
                                 :brand
                                 :class
                                 :model-name
                                 {:field/alias :photo
                                  :field/data  [:image-url]}
                                 :model-identifier
                                 :color
                                 :serial-number
                                 [:user person-details-graph-ql]])

(defn get-inventory-details
  [inventory-id]
  {:name   :get-inventory-details
   :before [core/started-get-inventory-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:queries [[:computer {:id inventory-id}
                                                             inventory-details-graph-ql]]})}}
   :after  [core/receive-get-inventory-detail-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-inventory-detail? state)
    [(get-inventory-details (:inventory-id state))]))
