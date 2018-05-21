(ns view-inventory-detail.services
  (:require [view-inventory-detail.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]))

(defn get-inventory-details
  [inventory-id]
  {:name   :get-inventory-details
   :before [core/started-get-inventory-detail-service-call]
   :data   {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (str/join
                              "\n"
                              ["query {"
                               (str "  computer(id:" inventory-id ") {")
                               "    id"
                               "    date: release_date"
                               "    brand"
                               "    class"
                               "    model_name"
                               "    photo: image_url"
                               "    model_identifier"
                               "    serial_number"
                               "    user {"
                               "      id"
                               "      first_name"
                               "      last_name"
                               "      image: photo_url"
                               "      occupation"
                               "      groups {"
                               "        id"
                               "        name"
                               "      }"
                               "    }"
                               "    purchase_details {"
                               "      id: purchase_id"
                               "      delivery_date"
                               "      insurance_expires"
                               "      warranty_expires"
                               "      supplier {"
                               "        name"
                               "      }"
                               "      documents {"
                               "        url"
                               "      }"
                               "    }"
                               "  }"
                               "}"])}}



   :after  [core/receive-get-inventory-detail-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-inventory-detail? state)
    [(get-inventory-details (:inventory-id state))]))
