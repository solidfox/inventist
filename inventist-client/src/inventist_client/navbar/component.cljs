(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [inventist-client.event :as event]
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
                    :width      "4rem"
                    :margin     "0"
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
;(let [opaque (or opaque 0.4)]))


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
    :target-page-id :people}])


(defc navigation-bar [{trigger-event    :trigger-event
                       current-path     :current-path
                       auth-status-item :auth-status-item
                       viewport-height  :viewport-height
                       viewport-width   :viewport-width}]

  (cond (> viewport-width 800) ;Desktop View
        [:div {:style
               (merge {
                       ;:position        "absolute"
                       ;:bottom          0
                       ;:left            "50%"
                       :padding         "0.5rem"
                       ;:maxWidth        "500px"
                       :height          "2.5rem"
                       :display         "flex"
                       :flex-direction  "row"
                       :align-items     "center"
                       :backgroundColor color/silver
                       :justify-content "center"}
                      style/z-index-top-toolbar
                      style/box-shadow)}

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

         auth-status-item]

        (< viewport-width 800) ;Mobile View
        [:div {:style
               (merge {
                       ;:position        "absolute"
                       ;:bottom          0
                       ;:left            "50%"
                       :padding         "0.5rem"
                       ;:maxWidth        "500px"
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

         auth-status-item]))

