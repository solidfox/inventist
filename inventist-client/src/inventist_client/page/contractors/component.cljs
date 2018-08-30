(ns inventist-client.page.contractors.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.contractors.core :as core]
            [view-contractor-detail.component :refer [contractor-detail]]
            [view-contractors-overview.component :refer [contractors-list]]
            [rum.core :refer [defc]]))

(defc component < (modular-component)
  [{{state :state}  :input
    trigger-event   :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]
  [:div {:id    (str ::component-id)
         :style {:height                viewport-height
                 :display               "grid"
                 :grid-template-columns "20rem 1fr"
                 :grid-template-rows    "100%"}}
   (contractors-list (core/create-contractors-overview-args state))
   (contractor-detail (assoc (core/create-contractor-detail-args state "mock-contractor-id")
                        :trigger-parent-event trigger-event
                        :viewport-height viewport-height
                        :viewport-width viewport-width))])

