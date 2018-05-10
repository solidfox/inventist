(ns view-inventory-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :as s-overview]))

(defc inventory-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [items (get-in state [:get-inventory-list-response :response :items])]
    (s-overview/overview-list "inventory" items)))
