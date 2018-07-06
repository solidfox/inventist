(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [remodular.core :as rem]
            [clojure.string :refer [lower-case upper-case]]
            [inventist-client.event :as client-event]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc collection-heading
  [{heading :heading
    id      :id}]
  [:div {:style {:height                "2rem"
                 :color                 color/dark-context-title-text
                 :width                 "auto"
                 :margin-top            "1.5rem"
                 :padding               "0 1rem"
                 :display               "grid"
                 :grid-template-columns "auto 2rem 0.5rem"
                 :align-items           "center"
                 :font-size             "1rem"
                 :cursor                "pointer"
                 :background-color      color/transparent}}


   [:div {:style {:font-weight    "500"
                  :letter-spacing "2px"}}
    (upper-case heading)]

   [:i {:class "fas fa-plus-circle"
        :style {:cursor "copy"}}]
   [:i {:class "fas fa-caret-down"
        :style {:cursor "pointer"}}]])


(defc collection-item
  [{title    :title
    icon     :icon
    image    :image
    color    :color
    on-click :on-click}]
  [:div {:class    "collection-item"
         :style    {:height                "2rem"
                    :color                 color/dark-context-primary-text
                    :width                 "auto"
                    :padding               "0.5rem 1rem"
                    :cursor                "pointer"
                    :display               "grid"
                    :grid-template-columns "1.5rem auto 2rem"
                    :background-color      (or color color/transparent)}
         :on-click on-click}
   [:i {:class icon
        :style {:font-size  "1.25rem"
                :align-self "center"}}]

   [:div {:style {:font-size   "1rem"
                  :align-self  "center"
                  :font-weight "500"
                  :margin      "0 1rem"}}
    title

    ;On-Mouse-Over "style.css"
    [:span {:class "collection-item-edit"
            :on-click (fn [] (println "Edit " title))
            :style {:font-weight "400"
                    :font-size   "0.75rem"}}
     "Edit"]]

   ;Selected Item
   [:div {:draggable true
          :style     {:background-color color/dark-context-secondary-text
                      :width            "2rem" :height "2rem"
                      :border-radius    "0.25rem"
                      :cursor           "grab"
                      :border           (str "1px solid " color/dark-context-title-text)}}]])


(def collections-list
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


;---------------------------Side-nav-----------------------------------

(defc collection-sidebar [{trigger-event    :trigger-event
                           current-path     :current-path
                           auth-status-item :auth-status-item}]

  [:div {:style {:width              "100%"
                 :height             "100%"
                 :display            "grid"
                 :grid-template-rows "3.5rem auto 3.5rem"
                 :background-color   color/dark-context-background
                 :box-shadow         (str "0 0 0.5rem " color/shadow)
                 :z-index            0}}
   ;Header-Logo
   [:div {:style {:background-color      color/light-context-background
                  :padding               "0.5rem"
                  :display               "grid"
                  :grid-template-columns "calc(100% - 5rem) 5rem"}}
    [:img {:src      "/image/GHS-logotype-horizontal.svg"
           :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id :dashboard})))
           :style    {:height   "100%"
                      :maxWidth "10rem"
                      :cursor   "pointer"}
           :title    "GripsHolmsSkolan"}]
    [:img {:src      "/image/inventist-logo.svg"
           :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id :dashboard})))
           :style    {:width      "100%"
                      :align-self "end"
                      :cursor     "pointer"}
           :title    "Inventist"}]]

   [:div {:style {:height         "100%"
                  :text-align     "left"
                  :display        "flex"
                  :flex-direction "column"}}
    (collection-heading {:heading "Collections"
                         :id      "collection1"})
    (for [{title          :title
           icon           :icon
           image          :image
           on-click       :on-click
           target-page-id :target-page-id} collections-list]
      (-> (collection-item (merge {:title    title
                                   :icon     icon
                                   :image    image
                                   :on-click (or on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id}))))}
                                  (when (= (first current-path) target-page-id)
                                    {:selected true
                                     :color    color/dark-context-highlight-bg})))
          (with-key title)))]

   auth-status-item])

;------------------------ Old Nav-bar -------------------------

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
           target-page-id :target-page-id} collections-list]
      (-> (cond (not= (:name selected-item) nil)
                (navigation-badge (merge {:title         title
                                          :icon          icon
                                          :selected-item selected-item
                                          :on-click      (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                         (when (= (first current-path) target-page-id)
                                           {:selected true
                                            :color    color/theme})))
                :else
                (collection-item (merge {:title    title
                                         :icon     icon
                                         :image    image
                                         :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id target-page-id})))}
                                        (when (= (first current-path) target-page-id)
                                          {:selected true
                                           :color    color/theme}))))
          (with-key title)))]
   auth-status-item])




