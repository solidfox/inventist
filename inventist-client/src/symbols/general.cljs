(ns symbols.general
  (:require [rum.core :refer [defc]]
            [symbols.color :as c]
            [util.inventory.core :as inventory]))

;Returns brand and model icon together
(defc device-icon-set [{item :item}]
  [:span
   [:i {:class (:brand (inventory/inventory-icon item))}] " "
   [:i {:class (:model (inventory/inventory-icon item))}]])

;to calculate length of an array
(defn length
  [list]
  (if (empty? list) 0
                    (+ 1 (length (rest list)))))

;button general
(defc button [{text     :text
               icon     :icon
               color    :color
               title    :title
               on-click :on-click}]
  (cond (not text)
        [:div {:style {:margin    "0.5rem"
                       :color     (or color c/grey-dark)
                       :font-size "1.5rem"
                       :cursor    "pointer"}}
         [:i {:class icon :title title}]]
        :else
        [:div {:style {:margin          "0.5rem"
                       :padding         "0.25rem 0.5rem"
                       :backgroundColor (or color c/grey-dark)
                       :color           (cond (= color c/white) c/grey-dark
                                              (= color "white") c/grey-dark
                                              (= color "#ffffff") c/grey-dark
                                              (= color c/tp) c/grey-dark
                                              :else c/white)
                       :cursor          "pointer"
                       :borderRadius    "0.25rem"}}
         ;:box-shadow      "0 0 0.25rem #fff"}}
         (cond (not= icon nil)
               [:span {:style {:margin "0 0.5rem 0 0"}} [:i {:class icon}]])
         [:span text]]))
