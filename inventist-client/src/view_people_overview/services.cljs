(ns view-people-overview.services
  (:require [view-people-overview.core :as core]
            [ysera.test :refer [is=]]
            [clojure.string :as str]))

(defn get-people-list
  []
  {:name   :get-people-list
   :before [core/started-get-people-list-service-call]
   :data   {:url   "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
            :params {:query (str/join
                              "\n"
                              ["query {"
                               "  people {"
                               "    id"
                               "    type: occupation"
                               "    fname: first_name"
                               "    lname: last_name"
                               "    groups {"
                               "      name"
                               "    }"
                               "    image: photo_url"
                               "    inventory {"
                               "      id"
                               "      brand"
                               "      model_name"
                               "      color"
                               "      class"
                               "      model_identifier"
                               "    }"
                               "  }"
                               "}"])}}



   :after  [core/receive-get-people-list-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-people-list? state)
    [(get-people-list)]))


