(ns inventist-client.page.people.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.people.core :as core]
            [view-person-detail.component :refer [person-detail]]
            [view-people-overview.component :refer [people-list]]
            [rum.core :refer [defc]]
            [inventist-client.page.people.event :as event]))

(defc component < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id (str ::component-id)
         :style {:height "100%"
                 :display "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows "100%"}}
   (people-list (assoc (core/create-people-overview-args state)
                  :trigger-parent-event trigger-event))
   (if-let [selected-person-id (:selected-person-id state)]
     (person-detail (assoc (core/create-person-detail-args state selected-person-id)
                      :trigger-parent-event trigger-event))
     [:div])])
