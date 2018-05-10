(ns view-people-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.sidebar :as sidebar]))

(defc people-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (get-in state [:get-people-list-response :response :items])]
    (sidebar/sidebar-list "people" people)))
