(ns symbols.general
  (:require [rum.core :refer [defc]]
            [util.inventory.core :as inventory]))

(defc device-icon-set [item]
  [:span
   [:i {:class (:brand (inventory/inventory-icon item))}] " "
   [:i {:class (:model (inventory/inventory-icon item))}]])