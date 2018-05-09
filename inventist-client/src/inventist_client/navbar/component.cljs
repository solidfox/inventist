(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [inventist-client.event :as event]))


(defc navigation-icon
  [{title    :title
    image    :image
    color    :color
    opaque   :opacity
    on-click :on-click}]
  (let [opaque (or opaque 0.4)]
    [:div {:style    {:text-align "center"
                      :opacity    opaque
                      :color      (or color "#000000")
                      :width      "4rem"
                      :margin     "0"
                      :cursor     "pointer"}
           :on-click on-click}
     [:div {:style {:height "1.75rem" :text-align "center"}}
      [:i {:class image :style {:font-size "1.5rem"}}]]

     [:div {:style {:font-size "0.7rem"
                    :margin    "0rem"}}
      title]]))

(def navbar-main-sections
  [{:title          "Dashboard"
    :image          "fas fa-tachometer-alt"
    :target-page-id :dashboard}
   {:title          "People"
    :image          "fas fa-users"
    :target-page-id :people}
   {:title          "Inventory"
    :image          "fas fa-sitemap"
    :target-page-id :inventory}
   {:title          "Contractors"
    :image          "fas fa-ribbon"
    :target-page-id :contractors}
   {:title          "Settings"
    :image          "fas fa-cog"
    :target-page-id :settings}])


(defc navigation-bar [{trigger-event    :trigger-event
                       current-path     :current-path
                       auth-status-item :auth-status-item}]
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
    (for [{title          :title
           image          :image
           target-page-id :target-page-id} navbar-main-sections]
      (navigation-icon (merge {:title    title
                               :image    image
                               :on-click (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id})))}
                              (when (= (first current-path) target-page-id)
                                {:selected true
                                 :opacity  0.9
                                 :color    "#e67e22"}))))]
   auth-status-item])
