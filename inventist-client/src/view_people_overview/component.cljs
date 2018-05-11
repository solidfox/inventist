(ns view-people-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :as s-overview]))

(defc people-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (get-in state [:get-people-list-response :response :items])]
    (s-overview/overview-list {:type "people"
                               :list-items people})))
