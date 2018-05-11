(ns view-contractors-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :as s-overview]))

(defc contractors-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [contractors (get-in state [:get-contractors-list-response :response :items])]
    (s-overview/overview-list {:type "contractors"
                               :list-items contractors})))
