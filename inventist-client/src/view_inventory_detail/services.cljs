(ns view-inventory-detail.services
  (:require [view-inventory-detail.core :as core]
            [ysera.test :refer [is=]]
            [util.inventory.core :as util]))

(def reallocation-fragment
  [{:fragment/name   :fragment/inventory-history-reallocation
    :fragment/type   :Reallocation
    :fragment/fields [:instant
                      [:new_user [:id
                                  :first_name
                                  :last_name
                                  :occupation
                                  [:groups [:id
                                            :name]]]]]}])

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
                                 {:field/alias :image
                                  :field/data  [:image-url]}
                                 :model-identifier
                                 :color
                                 :serial-number
                                 [:user person-details-graph-ql]
                                 [:history [:fragment/inventory-history-reallocation]]])


(defn get-inventory-details
  [inventory-id]
  {:name   :get-inventory-details
   :before [core/started-get-inventory-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:fragments reallocation-fragment
                                                  :queries   [[:computer {:id inventory-id}
                                                               inventory-details-graph-ql]]})}}
   :after  [core/receive-get-inventory-detail-service-response]})

(defn send-report-issue-form
  [{item-id     :item-id
    description :description
    photos      :photos}]
  {:name   :send-report-issue-form
   :before [core/started-send-report-issue-form-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:operation {:operation/type :mutation
                                                              :operation/name "ReassignDevice"}
                                                  :fragments reallocation-fragment
                                                  :queries   [[:report-issue-with-inventory-item {:item_id     item-id
                                                                                                  :description description}
                                                               [[:inventory-item inventory-details-graph-ql]]]]})}}
   :after  [core/receive-send-report-issue-form-service-response]})


(defn get-services
  [{{state :state} :input}]
  (as-> [] $
        (conj $ (when (core/should-get-inventory-detail? state)
                  (get-inventory-details (:inventory-item-id state))))
        (conj $ (when (core/should-send-report-issue-form? state)
                  (send-report-issue-form {:item-id     (:inventory-item-id state)
                                           :description (get-in state [:report-issue-form :user-input :description])
                                           :photos      [(get-in state [:report-issue-form :user-input :file])]})))
        (remove nil? $)))
