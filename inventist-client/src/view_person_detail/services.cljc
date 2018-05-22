(ns view-person-detail.services
  (:require [view-person-detail.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]
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

(def person-details-graph-ql
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
   [:inventory [:id
                :brand
                :model-name
                :color
                :model-identifier
                :serial-number
                :class
                {:field/alias :photo
                 :field/data  [:image-url]}]]
   [:history [:fragment/person-history-reallocation]]])



(defn get-person-details
  [person-id]
  {:name   :get-person-details
   :before [core/started-get-person-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (util/graphql-string {:fragments reallocation-fragment
                                                  :queries   [[:person {:id person-id} person-details-graph-ql]]})}}
   :after  [core/receive-get-person-detail-service-response]})

;(defn reassignInventoryItem
;  [{inventory-item-serial-number :inventory-item-serial-number
;    new-user-id                  :new-user-id}]
;  {:name   :reassign-inventory-item
;   :before [core/started-reassign-inventory-item-service-call]
;   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
;            :params {:query (str/join
;                              "\n"
;                              ["mutation {"
;                               (str "  person(id:" person-id ") {")
;                               "    id"
;                               "    type: occupation"
;                               "    fname: first_name"
;                               "    lname: last_name"
;                               "    groups {"
;                               "      name"
;                               "      id"
;                               "    }"
;                               "    image: photo_url"
;                               "    email"
;                               "    phone"
;                               "    address"
;                               "    inventory {"
;                               "      id"
;                               "      brand"
;                               "      model_name"
;                               "      color"
;                               "      model_identifier"
;                               "      serial_number"
;                               "      class"
;                               "      photo: image_url"
;                               "    }"
;                               "    history {"
;                               "      ... on Reallocation {"
;                               "        inventory_item {"
;                               "          id"
;                               "          brand"
;                               "          model_name"
;                               "          model_identifier"
;                               "          serial_number"
;                               "        }"
;                               "        instant"
;                               "      }"
;                               "    }"
;                               "  }"
;                               "}"])}}
;   :after  [core/receive-get-person-detail-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-person-detail? state)
    [(get-person-details (:person-id state))]))
