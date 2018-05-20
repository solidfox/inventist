(ns inventist-client.page.people.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.people.core :as core]
            [view-person-detail.component :refer [person-detail]]
            [view-people-overview.component :refer [people-list]]
            [rum.core :refer [defc]]))

(defc component < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:height "100%"
                 :display "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows "100%"}}
   (people-list (assoc (core/create-people-overview-args state)
                  :trigger-parent-event trigger-event))
   (person-detail (assoc (core/create-person-detail-args state "mock-person-id")
                    :trigger-parent-event trigger-event))])
