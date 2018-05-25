(ns view-person-detail.services
  (:require [view-person-detail.core :as core]
            [ysera.test :refer [is=]]
            [util.inventory.core :as util]))

(def reallocation-fragment
  [{:fragment/name   :fragment/person-history-reallocation
    :fragment/type   :Reallocation
    :fragment/fields [:instant
                      [:inventory_item [:id
                                        :brand
                                        :model-name
                                        :model-identifier
                                        :serial-number]]]}])

(def inventroy-details-graph-ql
  [:id
   :brand
   :model-name
   :color
   :model-identifier
   :serial-number
   :class
   {:field/alias :photo
    :field/data  [:image-url]}])



(def person-details-graphql
  [:id
   :occupation
   :first-name
   :last-name
   [:groups [:name
             :id]]
   {:field/alias :image
    :field/data  [:photo-url]}
   :email
   :phone
   :address
   [:inventory inventroy-details-graph-ql]
   [:history [:fragment/person-history-reallocation]]])


(defn get-person-details
  [person-id]
  {:name   :get-person-details
   :before [core/started-get-person-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:fragments reallocation-fragment
                                                  :queries   [[:person {:id person-id} person-details-graphql]]})}}
   :after  [core/receive-get-person-detail-service-response]})

(defn reassign-inventory-item
  [{serial-number :serial-number
    new-user-id   :new-user-id}]
  {:name   :reassign-inventory-item
   :before [core/started-reassign-inventory-item-service-call {:serial-number serial-number}]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:operation {:operation/type :mutation
                                                              :operation/name "ReassignDevice"}
                                                  :fragments reallocation-fragment
                                                  :queries   [[:set-user-of-inventory-item {:inventory-item-serial-number serial-number
                                                                                            :new-user-id                  new-user-id}
                                                               [[:new-user person-details-graphql]]]]})}}
   :after  [core/receive-reassign-inventory-item-service-response]})


(defn get-services
  [{{state :state} :input}]
  (remove nil?
    (concat (when (core/should-get-person-detail? state)
              [(get-person-details (:person-id state))])
            (when-let
              [pending-inventory-item-assignment (core/get-next-pending-inventory-item-assignment-to-perform state)]
              [(reassign-inventory-item {:new-user-id   (:person-id state)
                                         :serial-number (:serial-number pending-inventory-item-assignment)})]))))
