(ns inventist-client.page.contractors.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.contractors.core :as core]
            [view-contractor-detail.component :refer [contractor-detail]]
            [view-contractors-overview.component :refer [contractors-list]]
            [rum.core :refer [defc]]))

(defc component < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:height "100%"
                 :display "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows "100%"}}
   (contractors-list (core/create-contractors-overview-args state))
   (contractor-detail (core/create-contractor-detail-args state "mock-contractor-id"))])
