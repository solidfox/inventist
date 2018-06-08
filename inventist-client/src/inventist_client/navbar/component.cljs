(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [inventist-client.event :as event]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc navigation-badge
  [{title         :title
    icon          :icon
    image         :image
    color         :color
    selected-item :selected-item                            ;name, photo, custom-text, subtitle, on-drag, on-click
    on-click      :on-click}]
  [:div {:class (style/navbar-card)}
   [:div {:style    {:width              "auto"
                     :color              (or color color/grey-normal)
                     :text-align         "center"
                     :display            "grid"
                     :grid-template-rows "1fr 30%"}
          :on-click on-click}
    [:div {:style {:height "100%" :text-align "center"}}
     (cond
       (not= (:photo selected-item) nil) [:img {:src   (:photo selected-item)
                                                :style {:height       "1.5rem"
                                                        :width        "1.5rem"
                                                        :object-fit   "cover"
                                                        :borderRadius "1rem"}}]
       (not= image nil) [:img {:src   image
                               :style {:height "1.5rem"}}]
       :else [:i {:class icon :style {:font-size "1.5rem"}}])]
    [:div {:style {:font-size "0.7rem"
                   :margin    "0rem"}}
     title]]

   [:div {:style {:margin      "0rem 0.5rem"
                  :border-left (str "0.1rem solid " color/grey-light)}}]

   [:div {:style         {:display        "flex"
                          :flex-direction "column"
                          :margin-top     "0rem"
                          :color          color/grey-dark
                          :border-radius  "0.5rem"}
          :draggable     true
          :on-click      (:on-click selected-item)
          :on-drag-enter (:on-drag-enter selected-item)}
    [:div {:style {:font-size   "0.5rem"
                   :font-weight "600"
                   :color       color/theme}}
     (or (:custom-text selected-item) "Selected")]
    [:div {:style {:font-size   "0.9rem"
                   :font-weight "600"}}
     (:name selected-item)]
    [:div {:style {:font-size "0.7rem"
                   :color     color/grey-normal}}
     (:subtitle selected-item)]]])



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
    :image          "/image/ghs-icon.svg"
    :icon           "fas fa-tachometer-alt"
    :target-page-id :dashboard}
   {:title          "Inventory"
    :icon           "fas fa-sitemap"
    :selected-item  {:name          "MacBook Pro (2011)"
                     :subtitle      "Serial Number"
                     :on-drag-enter ""}
    :target-page-id :inventory}
   {:title          "People"
    :icon           "fas fa-users"
    :selected-item  {:photo         "/image/person-m-placeholder.png"
                     :name          "Daniel Schlaug"
                     :subtitle      "Admin"
                     :on-drag-enter ""}
    :target-page-id :people}])
;{:title          "Profile"
; :icon           "fas fa-user"
; :on-click       (fn [] (auth/log-out))
; :target-page-id :profile}])



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
    [:span [:img {:src   "/image/GHS-logotype-horizontal.svg"
                  :style {:height       "100%"
                          :borderRadius "0rem"}}]]]

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
                                          :image         image
                                          :on-click      (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                         (when (= (first current-path) target-page-id)
                                           {:selected true
                                            :color    color/theme})))
                :else
                (navigation-icon (merge {:title    title
                                         :icon     icon
                                         :image    image
                                         :on-click (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                        (when (= (first current-path) target-page-id)
                                          {:selected true
                                           :color    color/theme}))))
          (with-key title)))
    auth-status-item]])





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
                                   :on-click (or on-click (fn [] (trigger-event (event/clicked-navigation-icon {:target-page-id target-page-id}))))}
                                  (when (= (first current-path) target-page-id)
                                    {:selected true
                                     :color    color/theme})))
          (with-key title)))]

   auth-status-item])
