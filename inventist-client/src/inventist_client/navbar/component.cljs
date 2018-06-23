(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [remodular.core :as rem]
            [inventist-client.event :as client-event]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc navigation-badge
  [{title         :title
    icon          :icon
    color         :color
    selected-item :selected-item                            ;name, photo, custom-text, subtitle, on-drag, on-click
    on-click      :on-click}]
  [:div {:class (style/navbar-card)}
   [:div {:style    {:width              "auto"
                     :color              (or color color/grey-normal)
                     :text-align         "center"
                     :margin             "0.5rem"
                     :display            "grid"
                     :grid-template-rows "1fr 30%"}
          :on-click on-click}
    [:div {:style {:height "100%" :text-align "center"}}
     [:i {:class icon :style {:font-size "1.5rem"}}]]
    [:div {:style {:font-size "0.7rem"
                   :margin    "0rem"}}
     title]]
   [:div {:class         "navbar-badge-selection"
          :style         {:display        "flex"
                          :flex-direction "row"
                          :margin-top     "0rem"
                          :position       "relative"
                          :color          color/grey-dark
                          :border-radius  "0.5rem"}
          :draggable     true
          :on-click      (:on-click selected-item)
          :on-drag-enter (:on-drag-enter selected-item)}

    [:div {:class "navbar-badge-selection-data"
           :style {:display          "flex"
                   :draggable        false
                   :position         "absolute"
                   :top              "3.5rem"
                   :left             "-2rem"
                   :minWidth         "6rem"
                   :width            "auto"
                   :padding          "0.25rem 0.5rem 0rem"
                   :background-color color/white
                   :box-shadow       "0 0 0.2rem rgba(0,0,0,0.25)"
                   :border-radius    "0.5rem"
                   :border-left      (str "0.1rem solid " color/grey-light)
                   :flex-direction   "column"}}
     ;[:div {:style {:font-size   "0.5rem"
     ;               :font-weight "600"
     ;               :color       color/theme}}
     ; (or (:custom-text selected-item) "Selected")]
     [:div {:style {:font-size   "0.9rem"
                    :font-weight "600"}}
      (:name selected-item)]
     [:div {:style {:font-size "0.7rem"
                    :color     color/grey-normal}}
      (:subtitle selected-item)]]

    [:img {:src       (:photo selected-item)
           :draggable false
           :style     {:height           "100%"
                       :width            "3rem"
                       :object-fit       "cover"
                       :background-color color/grey-light
                       :borderRadius     "0.5rem"}}]]])     ;"0 0.5rem 0.5rem 0"}}]]])

(defc navigation-icon
  [{title    :title
    icon     :icon
    image    :image
    color    :color
    on-click :on-click}]
  [:div {:style    {:text-align "center"
                    :color      (or color color/grey-normal)
                    :width      "auto"
                    :margin     "0.25rem 0.5rem"
                    :cursor     "pointer"}
         :on-click on-click}
   [:div {:style {:height "1.75rem" :text-align "center"}}
    (cond (not= image nil) [:img {:src   image
                                  :style {:height       "100%"
                                          :borderRadius "0rem"
                                          :object-fit   "cover"}}]
          :else [:i {:class icon :style {:font-size "1.5rem"}}])]
   [:div {:style {:font-size "0.7rem"
                  :margin    "0rem"}}
    title]])


(def navbar-main-sections
  [{:title          "Dashboard"
    ;:image          "/image/ghs-icon.svg"
    :icon           "fas fa-tachometer-alt"
    :target-page-id :dashboard}
   {:title          "Inventory"
    :icon           "fas fa-sitemap"
    :selected-item  {:name          "MacBook Pro (2011)"
                     :subtitle      "Serial Number"
                     :photo         "https://static.bhphoto.com/images/images500x500/apple_z0rf_mjlq23_b_h_15_4_macbook_pro_notebook_1432050588000_1151716.jpg"
                     :on-drag-enter ""}
    :target-page-id :inventory}
   {:title          "People"
    :icon           "fas fa-users"
    :selected-item  {:photo         "/image/person-m-placeholder.png"
                     :name          "Daniel Schlaug"
                     :subtitle      "Admin"
                     :on-drag-enter ""}
    :target-page-id :people}])

(defc navigation-bar-desktop [{trigger-event    :trigger-event
                               current-path     :current-path
                               auth-status-item :auth-status-item}]
  [:div {:style
         (merge {:padding         "0.25rem"
                 :width           "100%"
                 :height          "3rem"
                 :display         "flex"
                 :flex-direction  "row"
                 :align-items     "center"
                 :backgroundColor color/silver
                 :justify-content "space-between"}
                style/z-index-top-toolbar
                style/box-shadow)}
   [:div {:style {:height "100%" :text-align "left"}}
    [:span [:img {:src      "/image/GHS-logotype-horizontal.svg"
                  :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id :dashboard})))
                  :style    {:height       "100%"
                             :borderRadius "0rem"
                             :cursor       "pointer"}
                  :title    "Inventist Dashboard"}]]]

   [:div {:style {:height "100%" :text-align "center" :display "flex"}}
    (for [{title          :title
           icon           :icon
           image          :image
           selected-item  :selected-item
           target-page-id :target-page-id} navbar-main-sections]
      (-> (cond (not= (:name selected-item) nil)
                (navigation-badge (merge {:title         title
                                          :icon          icon
                                          :selected-item selected-item
                                          :on-click      (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                         (when (= (first current-path) target-page-id)
                                           {:selected true
                                            :color    color/theme})))
                :else
                (navigation-icon (merge {:title    title
                                         :icon     icon
                                         :image    image
                                         :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                        (when (= (first current-path) target-page-id)
                                          {:selected true
                                           :color    color/theme}))))
          (with-key title)))]
   auth-status-item])





;---------------------------Mobile Nav-----------------------------------

(defc navigation-bar-mobile [{trigger-event    :trigger-event
                              current-path     :current-path
                              auth-status-item :auth-status-item}]

  [:div {:style {
                 ;:position         "absolute"
                 ;:bottom           "0rem"
                 ;:left             "calc(50% - 15rem)"
                 :padding          "0.5rem"
                 :width            "100%"
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
                                   :on-click (or on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id}))))}
                                  (when (= (first current-path) target-page-id)
                                    {:selected true
                                     :color    color/theme})))
          (with-key title)))]

   auth-status-item])
