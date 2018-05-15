(ns symbols.general
  (:require [rum.core :refer [defc]]
            [symbols.color :as color]
            [symbols.style :as style]
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


;Division Title - Title
(defc division-title [{title :title}]

  [:div {:style {:margin        "2.5rem 0 0 2.5rem"
                 :border-bottom (str "1px solid " color/silver)}
         :id    "header"}
   [:span {:style {:font-size      "1.5rem"
                   :color          color/grey-normal
                   :font-weight    "300"
                   :text-transform "capitalize"}}
    title] [:br]])

(defc stat-card [{value   :value
                  text    :text
                  subtext :subtext}]
  [:div {:style style/item-stats}
   [:div {:style (merge style/card-subtitle
                        {:text-align    "left"
                         :margin-top    "auto"
                         :margin-bottom "auto"})}
    text [:br]
    subtext]
   [:div {:style (merge style/header-title
                        {:text-align    "right"
                         :margin-top    "auto"
                         :margin-bottom "auto"
                         :margin-left   "1rem"})}
    value]])

;button general
(defc button [{text     :text
               icon     :icon
               color    :color
               title    :title
               on-click :on-click}]
  (cond (not text)
        [:div {:style {:margin    "0.5rem"
                       :color     (or color color/grey-dark)
                       :font-size "1.5rem"
                       :cursor    "pointer"}}
         [:i {:class icon :title title}]]
        :else
        [:div {:style {:margin          "0.5rem"
                       :padding         "0.25rem 0.5rem"
                       :backgroundColor (or color color/grey-dark)
                       :color           (cond (= color color/white) color/grey-dark
                                              (= color "white") color/grey-dark
                                              (= color "#ffffff") color/grey-dark
                                              (= color color/tp) color/grey-dark
                                              :else color/white)
                       :cursor          "pointer"
                       :borderRadius    "0.25rem"}}
         ;:box-shadow      "0 0 0.25rem #fff"}}
         (cond (not= icon nil)
               [:span {:style {:margin "0 0.5rem 0 0"}} [:i {:class icon}]])
         [:span text]]))
