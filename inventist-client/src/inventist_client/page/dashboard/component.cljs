(ns inventist-client.page.dashboard.component
  (:require [remodular.core :refer [modular-component]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [inventist-client.page.dashboard.core :as core]
            [view-dashboard.component :refer [dashboard-detail dashboard-stats]]
            [symbols.color :as color]
            [rum.core :refer [defc]]
            [inventist-client.no_connection.component :as connection]))

(defc component < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id    (str ::component-id)
         :style {:height                "100%"
                 :display               "grid"
                 :grid-template-columns "22rem auto"
                 :grid-template-rows    "100%"}}
   (dashboard-stats)

   [:div {:id    (str ::component-id)
          :style {:display            "grid"
                  :overflow           "scroll"
                  :grid-template-rows "auto 1fr"}}

    ;Toolbar
    (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "dashboard"})
                           :items-right [(s-general/button {:color color/grey-normal
                                                            :text  "Register Device"
                                                            :icon  "fas fa-pen-square"})]})
    (dashboard-detail (core/create-dashboard-detail-args state "mock-dashboard-id"))
    (connection/connection-bar)]])



