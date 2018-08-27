(ns inventist-client.navigation.component
  (:require [rum.core :refer [defc with-key]]
            [rum.core :as rum]
            [remodular.core :as rem]
            [clojure.string :refer [lower-case upper-case]]
            [inventist-client.event :as client-event]
            [symbols.mixin :refer [hovered-mixin toggle-mixin]]
            [symbols.color :as color]
            [symbols.style :as style]
            [util.inventory.core :as util]
            [symbols.general :as s-general]))

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



;---------------------------Side-nav-----------------------------------
(defc sidebar-content [sections]
  [:div {:style {:height                     "100%"
                 :background-color           color/dark-context-background
                 :text-align                 "left"
                 :display                    "flex"
                 :overflow-x                 "hidden"
                 :overflow-y                 "scroll"
                 :-webkit-overflow-scrolling "touch"
                 :flex-direction             "column"}}
   sections])

(defc sidebar [{sections        :sections
                trigger-event   :trigger-event
                current-path    :current-path
                user-bar        :user-bar
                viewport-height :viewport-height
                viewport-width  :viewport-width}]
  (s-general/scrollable
    {:floating-header (header-bar {:trigger-event   trigger-event
                                   :viewport-height viewport-height
                                   :viewport-width  viewport-width})
     :floating-footer user-bar
     :content         (sidebar-content sections)}))
