(ns view-dashboard.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [symbols.overview :as s-overview]
            [remodular.core :refer [modular-component]]
            [view-person-detail.component :refer [person-detail]]
            [inventist-client.page.people.core :as core]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc dashboard-detail < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id    "detail-container"
         :style {:height             "100%"
                 :display            "grid"
                 :grid-template-rows "auto 1fr"}}
   ;Error text
   [:div {:style {:width      "100%"
                  :height     "100%"
                  :color      color/grey-blue
                  :text-align "left"
                  :top        "10rem"
                  :margin     "2rem"
                  :left       "2rem"}}
    "This is the Inventist Dashboard." [:br]
    "Students & Staff with a \"gripsholmsskolan.se\" email address will be able to see their infromation and registered devices." [:br] [:br]
    "For non-gripsholmsskolan.se visitors, click on other sections like \"People\" and \"Inventory\" to view other's details."]

   ;background image
   [:div {:style {:width               "100%"
                  :height              "100%"
                  :position            "absolute"
                  :background-image    "url(\"image/GHS-logotype-horizontal.svg\")"
                  :background-position "35%"
                  :background-size     "25%"
                  :background-repeat   "no-repeat"
                  :opacity             0.1}}]])





(defc dashboard-stats []
  ;Dashboard Stats
  [:div {:style {:overflow-x      "hidden"
                 :overflow-y      "scroll"
                 :backgroundColor color/grey-light}}

   ;Division Heading
   (s-general/division-title
     {:title "Inventist Stats"})

   [:div {:style {:margin    "2.5rem"
                  :display   "flex"
                  :flex-wrap "wrap"}}

    (s-general/stat-card {:value 999
                          :text  "Total Inventory in School"})

    (s-general/stat-card {:value   999
                          :text    "Inventory per Type"
                          :subtext [:select
                                    [:option "All"]
                                    [:option "All"]]})

    (s-general/stat-card {:value 9999
                          :text  "Total Computers on repair"})

    (s-general/stat-card {:value   999
                          :text    "Computer on repair per Model"
                          :subtext [:select
                                    [:option "All"]
                                    [:option "All"]]})

    (s-general/stat-card {:value 999
                          :text  "Total Reserve computers"})

    (s-general/stat-card {:value 0
                          :text  "Unhandled issue reports"})]])
