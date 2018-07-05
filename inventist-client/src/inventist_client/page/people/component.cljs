(ns inventist-client.page.people.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.people.core :as core]
            [view-person-detail.component :refer [person-detail]]
            [view-people-overview.component :refer [people-list]]
            [rum.core :refer [defc]]
            [inventist-client.page.people.event :as event]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc component < (modular-component event/handle-event)
  [{{state :state}  :input
    trigger-event   :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]
  [:div {:id    (str ::component-id)
         :style {:height                viewport-height
                 :display               "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows    "100%"}}
   (people-list (assoc (core/create-people-overview-args state)
                  :trigger-parent-event trigger-event))
   [:div {:style (merge
                   {:background-color color/transparent}
                   style/z-index-details-section
                   style/box-shadow)}
    (if-let [selected-person-id (:selected-person-id state)]
      (person-detail (assoc (core/create-person-detail-args state selected-person-id)
                       :trigger-parent-event trigger-event))
      [:div {:id    "detail-container"
             :style (merge style/watermark
                           {:height             viewport-height
                            :display            "grid"
                            :grid-template-rows "1fr"})}


       ;Error text
       [:div {:style {:width      viewport-height
                      :height     "100%"
                      :color      color/light-context-secondary-text
                      :text-align "left"
                      :margin     "3.5rem 1.5rem"}}
        "No person selected. Use the side-bar to" [:br]
        "search and select a person to view details."]])]])


