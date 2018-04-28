(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]))

(defc navigation-icon
  [{title  :title
    image  :image
    opaque :opacity}]
  [:div {:style {:height     "100%"
                 :text-align "center"
                 :opacity    opaque
                 :margin     "0 1rem"
                 :cursor     "pointer"}}
   [:span {:style {:height "100%" :text-align "center"}}
    [:img {:src image :style {:height       "100%"
                              :borderRadius "2rem"}}]]

   [:span {:style {:font-size "0.8rem"
                   :margin    "0 0.5rem"
                   :color     "#000000"}} title]])

(defc navigation-bar [{auth-status-item :auth-status-item}]
  [:div {:style
         {:padding         "0.5rem"
          :height          "3rem"
          :display         "flex"
          :flex-direction  "row"
          :align-items     "center"
          :backgroundColor "#FFEA80"
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
                      :image   "image/dashboard.svg"
                      :opacity "0.4"})
    (navigation-icon {:title   "People"
                      :image   "image/people.svg"
                      :opacity "0.4"})
    (navigation-icon {:title   "Inventory"
                      :image   "image/inventory.svg"
                      :opacity "0.9"})
    (navigation-icon {:title   "Contractor"
                      :image   "image/contractor.svg"
                      :opacity "0.4"})]
   auth-status-item])
