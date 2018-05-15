(ns inventist-client.page.dashboard.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.dashboard.core :as core]
            [view-dashboard-detail.component :refer [dashboard-detail]]
            [rum.core :refer [defc]]))

(defc component < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:height "100%"
                 :display "grid"
                 :grid-template-columns "1fr"
                 :grid-template-rows "100%"}}
   (dashboard-detail (core/create-dashboard-detail-args state "mock-dashboard-id"))])
