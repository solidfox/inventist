(ns view-inventory-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.sidebar :as sidebar]))

(defc inventory-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [items (get-in state [:get-inventory-list-response :response :items])]
    (sidebar/sidebar-list "inventory" items)))
