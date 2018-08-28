(ns collections.component
  (:require [rum.core :refer [defc with-key]]
            [rum.core :as rum]
            [remodular.core :as rem]
            [collections.core :as core]
            [symbols.color :as color]
            [clojure.string :refer [lower-case upper-case]]
            [cljs-react-material-ui.rum :as ui]
            [symbols.style :as style]
            [symbols.general :as s-general]
            [symbols.mixin :refer [hovered-mixin toggle-mixin]]
            [inventist-client.event :as client-event]))


(def collection-item-height "2rem")

(defc collection-item
  [{title    :title
    icon     :icon
    image    :image
    color    :color
    on-click :on-click}]
  [:div {:key      title
         :class    "collection-item"
         :style    {:min-height            collection-item-height
                    :max-height            collection-item-height
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
                :margin-top "0.325rem"
                :align-self "start"}}]

   [:div {:style {:font-size   "1rem"
                  :font-weight "500"
                  :margin      "0 1rem"
                  :align-self  "center"}}
    title

    ;On-Mouse-Over "style.css"
    [:span {:class    "collection-item-edit"
            :on-click (fn [] (println "Edit " title))
            :style    {:font-weight "400"
                       :font-size   "0.75rem"}}
     "Edit"]]

   ;Selected Item
   [:div {:draggable true
          :style     {:background-color   color/dark-context-secondary-text
                      :width              "2rem" :height "2rem"
                      :border-radius      "0.25rem"
                      :cursor             "grab"
                      :box-sizing         "border-box"
                      :-moz-box-sizing    "border-box"
                      :-webkit-box-sizing "border-box"
                      :align-self         "start"
                      :border             (str "1px solid " color/dark-context-title-text)}}]])

(def collections-list
  [{:title          "Dashboard"
    :id             1
    :icon           "fas fa-tachometer-alt"
    :target-page-id :dashboard}
   {:title          "Inventory"
    :id             2
    :icon           "fas fa-sitemap"
    :selected-item  {:name          "MacBook Pro (2011)"
                     :subtitle      "Serial Number"
                     :photo         "https://static.bhphoto.com/images/images500x500/apple_z0rf_mjlq23_b_h_15_4_macbook_pro_notebook_1432050588000_1151716.jpg"
                     :on-drag-enter ""}
    :target-page-id :inventory}
   {:title          "People"
    :id             3
    :icon           "fas fa-users"
    :selected-item  {:photo         "/image/person-m-placeholder.png"
                     :name          "Daniel Schlaug"
                     :subtitle      "Admin"
                     :on-drag-enter ""}
    :target-page-id :people}])


(rum/defcs collections-view < (toggle-mixin {:toggle-state-key :expanded
                                             :initial-state    true
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
                  :grid-template-columns "auto 1rem 1.5rem"
                  :grid-gap              "1rem"
                  :align-items           "center"
                  :font-size             "1rem"
                  :cursor                "pointer"
                  :background-color      color/transparent}}

    [:div {:style    {:font-weight    "500"
                      :letter-spacing "2px"}
           :on-click (if expanded trigger-collapse trigger-expand)}
     (upper-case heading)]

    [:i {:class "fas fa-plus-circle"
         :title "Add New"}]
    [:div {:style    {:text-align "center"}
           :on-click (if expanded trigger-collapse trigger-expand)}
     [:img {:src   "/image/arrow.svg"
            :style {:height    "0.8rem"
                    :opacity   "0.9"
                    :transform (if expanded "" "rotate(90deg)")}}]]]

   ;List
   (if expanded (for [{title          :title
                       id             :id
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
                      (with-key id))))])
