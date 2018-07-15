(ns inventist-client.notifications.component
  (:require [rum.core :refer [defc with-key]]
            [remodular.core :as rem]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc connection-bar []
  [:div {:style
         (merge {:position        "absolute"
                 :right           "1rem"
                 :bottom          "1rem"
                 :padding         "0.5rem 1rem"
                 :borderRadius    "0.5rem"
                 :minHeight       "1rem"
                 :minWidth        "5rem"
                 :display         "flex"
                 :flex-direction  "row"
                 :align-items     "center"
                 :backgroundColor color/danger
                 :color           color/grey-light
                 :justify-content "space-between"
                 :z-index         100}
                style/box-shadow)}

   [:span "No Internet Connection"]
   [:i {:class "fas fa-times-circle"
        :title "Close Alert"
        :style {:margin-left "1rem"
                :cursor      "pointer"}}]])