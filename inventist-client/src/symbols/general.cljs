(ns symbols.general
  (:require [rum.core :refer [defc]]
            [util.inventory.core :as inventory]))

;Returns brand and model icon together
(defc device-icon-set [item]
  [:span
   [:i {:class (:brand (inventory/inventory-icon item))}] " "
   [:i {:class (:model (inventory/inventory-icon item))}]])

;to calculate length of an array
(defn length
  [list]
  (if (empty? list) 0
                    (+ 1 (length (rest list)))))