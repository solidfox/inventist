(ns view-dashboard.component
  (:require [rum.core :refer [defc]]
            [authentication.component :as auth]
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
                  :color      color/light-context-secondary-text
                  :text-align "left"
                  :margin     "2.5rem 0"}}
    "This is the Inventist Dashboard." [:br]
    "Students & Staff with a \"gripsholmsskolan.se\" email address will be able to see their infromation and registered devices." [:br] [:br]
    "You are logged-in with a external mail-id." [:br]
    "For non-gripsholmsskolan.se visitors, click on other sections like \"People\" and \"Inventory\" to view other's details." [:br]]])



(defc dashboard-stats []
  ;Dashboard Stats
  [:div {:style style/shaded-bar}

   ;Division Heading
   (s-general/division-title
     {:title "Inventist Stats"})

   [:div {:style {:margin    "2.5rem"
                  :display   "flex"
                  :flex-wrap "wrap"}}

    (s-general/stat-card {:value 467
                          :text  "Total Inventory in School"})

    ;(s-general/stat-card {:value   467
    ;                      :text    "Inventory per Type"
    ;                      :subtext [:select
    ;                                [:option "All"]]})
    (s-general/stat-card {:value 508
                          :text  "Total People in School"})]])





