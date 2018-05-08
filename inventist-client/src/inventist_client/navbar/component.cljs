(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]))


(defc navigation-icon
  [{title  :title
    image  :image
    color  :color
    opaque :opacity}]
  [:div {:style {:text-align "center"
                 :opacity    opaque
                 :color      (or color "#000000")
                 :width      "4rem"
                 :margin     "0"
                 :cursor     "pointer"}}
   [:div {:style {:height "1.75rem" :text-align "center"}}
    [:i {:class image :style {:font-size "1.5rem"}}]]

   [:div {:style {:font-size "0.7rem"
                  :margin    "0rem"}}
    title]])

(defc navigation-bar [{auth-status-item :auth-status-item}]
  [:div {:style
         {:padding         "0.5rem"
          :height          "2.5rem"
          :display         "flex"
          :flex-direction  "row"
          :align-items     "center"
          :backgroundColor "#ECF0F1"
          :justify-content "space-between"}}
   [:div {:style {:height "100%" :text-align "left"}}
    [:span [:img {:src   "image/GHS-logotype-horizontal.svg"
                  :style {:height       "100%"
                          :borderRadius "0rem"}}]]]
   ;[:span {:style {:font-size   "24px"
   ;                :font-weight "bold"
   ;                :color       "black"}} " Inventist"]]

   [:div {:style {:height "100%" :text-align "center" :display "flex"}}
    (navigation-icon {:title   "Dashboard"
                      :image   "fas fa-tachometer-alt"
                      :opacity "0.4"})
    (navigation-icon {:title   "People"
                      :image   "fas fa-users"
                      :opacity "0.9"
                      :color   "#e67e22"})
    (navigation-icon {:title   "Inventory"
                      :image   "fas fa-sitemap"
                      :opacity "0.4"})
    (navigation-icon {:title   "Contractor"
                      :image   "fas fa-ribbon"
                      :opacity "0.4"})
    (navigation-icon {:title   "Settings"
                      :image   "fas fa-cog"
                      :opacity "0.4"})]
   auth-status-item])
