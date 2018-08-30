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
    trigger-event  :trigger-event}])




(defc dashboard-stats []
  ;Dashboard Stats
  [:div {:style style/shaded-bar}

   ;Division Heading
   (s-general/division-title
     {:title "Inventist Stats"})

   [:div {:style {:margin    "1.5rem"
                  :display   "flex"
                  :flex-wrap "wrap"}}

    (s-general/stat-card {:value 469
                          :text  "Total Inventory in School"})

    ;(s-general/stat-card {:value   467
    ;                      :text    "Inventory per Type"
    ;                      :subtext [:select
    ;                                [:option "All"]]})
    (s-general/stat-card {:value 709
                          :text  "Total People in School"})]])





