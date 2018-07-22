(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc with-key]]
            [rum.core :as rum]
            [remodular.core :as rem]
            [clojure.string :refer [lower-case upper-case]]
            [inventist-client.event :as client-event]
            [symbols.mixin :refer [hovered-mixin toggle-mixin]]
            [symbols.color :as color]
            [symbols.style :as style]
            [util.inventory.core :as util]))

(defc header-bar [{trigger-event   :trigger-event
                   viewport-width  :viewport-width
                   viewport-height :viewport-height}]
  [:div {:style {:background-color      color/light-context-background
                 :padding               "0.5rem"
                 :display               "grid"
                 :grid-template-columns "calc(100% - 5rem) 5rem"
                 :grid-template-rows    "1rem 1.5rem"}}

   ;School Logo
   [:img {:src      "/image/GHS-logotype-horizontal.svg"
          :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id :dashboard})))
          :style    {:height         "100%"
                     :maxWidth       "10rem"
                     :grid-row-start 1
                     :grid-row-end   3
                     :cursor         "pointer"}
          :title    "Gripsholmsskolan"}]

   ;TBR - Remove in final version.
   [:span {:style {:align-self "start"
                   :text-align "right"
                   :font-size  "0.75rem"
                   :color      color/light-context-title-text}}
    (str "w." viewport-width "  h." viewport-height)]

   ;Inventist Logo
   [:img {:src      "/image/inventist-logo.svg"
          :on-click (fn [] (trigger-event (client-event/clicked-navigation-icon {:target-page-id :dashboard})))
          :style    {:width          "100%"
                     :align-self     "end"
                     :grid-row-start 2
                     :grid-row-end   3
                     :cursor         "pointer"}
          :title    "Inventist"}]])

(def collection-item-height "2rem")

(defc collection-heading
  [{heading  :heading
    id       :id
    on-click :on-click}])



(defc collection-item
  [{title    :title
    icon     :icon
    image    :image
    color    :color
    on-click :on-click}]
  [:div {:key      title
         :class    "collection-item"
         :style    {:height                collection-item-height
                    :min-height            collection-item-height
                    :color                 color/dark-context-primary-text
                    :width                 "auto"
                    :padding               "0.5rem 1rem"
                    :cursor                "pointer"
                    :display               "grid"
                    :grid-template-columns "1.5rem auto 2rem"
                    :align-items           "start"
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
    [:span {:class    "collection-item-edit"
            :on-click (fn [] (println "Edit " title))
            :style    {:font-weight "400"
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

(rum/defcs collections-view < (toggle-mixin {:toggle-state-key :expanded
                                             :on-fn-key        :trigger-expand
                                             :off-fn-key       :trigger-collapse})
  [{:keys [expanded trigger-expand trigger-collapse]}
   {trigger-event   :trigger-event
    current-path    :current-path
    collection-list :collection-list
    heading         :collection-heading}]
  [:div {:style {:height         "auto"
                 :text-align     "left"
                 :display        "flex"
                 :flex-direction "column"}}
   ;Heading
   [:div {:id    heading
          :style {:height                collection-item-height
                  :min-height            collection-item-height
                  :color                 color/dark-context-title-text
                  :width                 "auto"
                  :margin-top            "1.5rem"
                  :padding               "0 1rem"
                  :display               "grid"
                  :grid-template-columns "1.5rem auto 1rem"
                  :grid-gap              "1rem"
                  :align-items           "center"
                  :font-size             "1rem"
                  :cursor                "pointer"
                  :background-color      color/transparent}}

    [:div {:style    {:text-align "center"}
           :on-click (if expanded trigger-collapse trigger-expand)}
     [:img {:src   "/image/arrow.svg"
            :style {:height    "0.75rem"
                    :opacity   "0.9"
                    :transform (if expanded "" "rotate(-90deg)")}}]]

    [:div {:style    {:font-weight    "500"
                      :letter-spacing "2px"}
           :on-click (if expanded trigger-collapse trigger-expand)}
     (upper-case heading)]

    [:i {:class "fas fa-plus-circle"
         :title "Add New"}]]

   ;List
   (if expanded (for [{title          :title
                       icon           :icon
                       image          :image
                       on-click       :on-click
                       target-page-id :target-page-id} collection-list]
                  (-> (collection-item (merge {:title    title
                                               :icon     icon
                                               :image    image
                                               :on-click (or on-click (fn []
                                                                        (trigger-event (client-event/clicked-navigation-icon
                                                                                         {:target-page-id target-page-id}))))}
                                              (when (= (first current-path) target-page-id)
                                                {:selected true
                                                 :color    color/dark-context-highlight-bg})))
                      (with-key title))))])


;---------------------------Side-nav-----------------------------------
(defc collection-sidebar [{trigger-event   :trigger-event
                           current-path    :current-path
                           user-bar        :user-bar
                           viewport-height :viewport-height
                           viewport-width  :viewport-width}]

  [:div {:style {:width              "100%"
                 :height             viewport-height
                 :display            "grid"
                 :grid-template-rows "3.5rem 1fr fit-content(13rem)"
                 :background-color   color/dark-context-background
                 :z-index            style/z-index-top-toolbar}}

   ;Header-Logo
   (header-bar {:trigger-event   trigger-event
                :viewport-height viewport-height
                :viewport-width  viewport-width})

   ;Collections
   [:div {:style {:height                     "auto"
                  :text-align                 "left"
                  :display                    "flex"
                  :overflow-x                 "hidden"
                  :overflow-y                 "scroll"
                  :-webkit-overflow-scrolling "touch"
                  :flex-direction             "column"}}
    (collections-view {:trigger-event trigger-event
                       :current-path         current-path
                       :collection-list      collections-list
                       :collection-heading   "Collections"})]

   ;Footer - User-bar
   user-bar])
