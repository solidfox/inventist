(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [inventist-client.event :as event]
            [authentication.component :as auth]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc navigation-icon
  [{title    :title
    icon     :icon
    image    :image
    color    :color
    on-click :on-click}]
  [:div {:style    {:text-align "center"
                    :color      (or color color/grey-normal)
                    :width      "auto"
                    :margin     "0 0.5rem"
                    :cursor     "pointer"}
         :on-click on-click}
   [:div {:style {:height "1.75rem" :text-align "center"}}
    (cond (not= image nil) [:img {:src   image
                                  :style {:height       "100%"
                                          :borderRadius "0rem"}}]
          :else [:i {:class icon :style {:font-size "1.5rem"}}])]
   [:div {:style {:font-size "0.7rem"
                  :margin    "0rem"}}
    title]])


(def navbar-main-sections
  [{:title          "Dashboard"
    :image          "/image/ghs-icon.svg"
    :icon           "fas fa-tachometer-alt"
    :target-page-id :dashboard}
   {:title          "Inventory"
    :icon           "fas fa-sitemap"
    :target-page-id :inventory}
   {:title          "People"
    :icon           "fas fa-users"
    :target-page-id :people}
   {:title          "Logout"
    :icon           "fas fa-user"
    :on-click       (fn [] (auth/log-out))
    :target-page-id :profile}])


(defc navigation-bar-desktop [{trigger-event    :trigger-event
                               current-path     :current-path
                               auth-status-item :auth-status-item}]

  [:div {:style {:position         "absolute"
                 :bottom           "0rem"
                 :left             "calc(50% - 15rem)"
                 :padding          "0.5rem"
                 :width            "30rem"
                 :height           "2.5rem"
                 :display          "flex"
                 :flex-direction   "row"
                 :align-items      "center"
                 :background-color color/white
                 :border-radius    "1rem 1rem 0 0"
                 :justify-content  "center"
                 :box-shadow       (str "0 0 1rem " color/shadow)
                 :z-index          10}}

   [:div {:style {:height "100%" :text-align "center" :display "flex"}}
    (for [{title          :title
           icon           :icon
           image          :image
           on-click       :on-click
           target-page-id :target-page-id} navbar-main-sections]
      (-> (navigation-icon (merge {:title    title
                                   :icon     icon
                                   :image    image
                                   :on-click (or on-click (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id}))))}
                                  (when (= (first current-path) target-page-id)
                                    {:selected true
                                     :color    color/theme})))
          (with-key title)))]

   auth-status-item])

(defc navigation-bar-mobile [{trigger-event    :trigger-event
                              current-path     :current-path
                              auth-status-item :auth-status-item}]
  [:div {:style
         (merge {:padding         "0.5rem"
                 :width           "100%"
                 :height          "2.5rem"
                 :display         "flex"
                 :flex-direction  "row"
                 :align-items     "center"
                 :backgroundColor color/silver
                 :justify-content "space-between"}
                style/z-index-top-toolbar
                style/box-shadow)}
   [:div {:style {:height "100%" :text-align "left"}}
    [:span [:img {:src   "/image/GHS-logotype-horizontal.svg"
                  :style {:height       "100%"
                          :borderRadius "0rem"}}]]]

   [:div {:style {:height "100%" :text-align "center" :display "flex"}}
    (for [{title          :title
           icon           :icon
           image          :image
           target-page-id :target-page-id} navbar-main-sections]
      (-> (navigation-icon (merge {:title    title
                                   :icon     icon
                                   :image    image
                                   :on-click (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                  (when (= (first current-path) target-page-id)
                                    {:selected true
                                     :color    color/theme})))
          (with-key title)))]

   auth-status-item])

