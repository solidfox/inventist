(ns view-person-detail.services
  (:require [view-person-detail.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]))

(defn get-person-details
  [person-id]
  {:name   :get-person-details
   :before [core/started-get-person-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (str/join
                              "\n"
                              ["query {"
                               (str "  person(id:" person-id ") {")
                               "    id"
                               "    type: occupation"
                               "    fname: first_name"
                               "    lname: last_name"
                               "    groups {"
                               "      name"
                               "      id"
                               "    }"
                               "    image: photo_url"
                               "    email"
                               "    phone"
                               "    address"
                               "    inventory {"
                               "      id"
                               "      brand"
                               "      model_name"
                               "      color"
                               "      model_identifier"
                               "      serial_number"
                               "      class"
                               "      photo: image_url"
                               "    }"
                               "    history {"
                               "      ... on Reallocation {"
                               "        inventory_item {"
                               "          id"
                               "          brand"
                               "          model_name"
                               "          model_identifier"
                               "          serial_number"
                               "        }"
                               "        instant"
                               "      }"
                               "    }"
                               "  }"
                               "}"])}}



   :after  [core/receive-get-person-detail-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-person-detail? state)
    [(get-person-details (:person-id state))]))
