(ns view-people-overview.services
  (:require [view-people-overview.core :as core]
            [ysera.test :refer [is=]]))

(defn get-people-list
  []
  {:name   :get-people-list
   :before [core/started-get-people-list-service-call]
   :data   {:url     "serviceEndpoints.getAccountTransactions"
            :request {:groups            groups
                      :searchCriterion   {:fromDate   "",
                                          :toDate     "",
                                          :fromAmount "",
                                          :toAmount   ""}}}
   :after  [core/receive-get-people-list-service-response]})


(defn get-services
  [{{state :state} :input}]
  (when (core/should-get-people-list? state)
    [(get-people-list)]))


