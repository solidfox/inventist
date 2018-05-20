(ns view-inventory-overview.services
  (:require [view-inventory-overview.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]))

(defn get-inventory-list
  []
  {:name   :get-inventory-list
   :before [core/started-get-inventory-list-service-call]
   :data   {:url   "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (str/join
                              "\n"
                              ["query {"
                               "  computer {"
                               "    id"
                               "    brand"
                               "    model_name"
                               "    class"
                               "    color"
                               "    photo: image_url"
                               "  }"
                               "}"])}}

   :after  [core/receive-get-inventory-list-service-response]})

(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-inventory-list? state)
    [(get-inventory-list)]))

