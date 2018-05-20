(ns view-people-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :as s-overview]
            [view-people-overview.core :as core]))

(defc people-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (core/filtered-people state)]
    (s-overview/overview-list {:type "people"
                               :list-items people})))
