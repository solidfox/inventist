(ns inventist-client.page.people.component
  (:require [remodular.core :refer [modular-component]]
            [inventist-client.page.people.core :as core]
            [view-person-detail.component :refer [person-detail]]
            [view-people-overview.component :refer [people-list]]
            [rum.core :refer [defc]]
            [inventist-client.page.people.event :as event]
            [symbols.detailview :as s-detailview]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc component < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id    (str ::component-id)
         :style {:height                "100%"
                 :display               "grid"
                 :grid-template-columns "22rem 1fr"
                 :grid-template-rows    "100%"}}
   (people-list (assoc (core/create-people-overview-args state)
                  :trigger-parent-event trigger-event))
   [:div {:style (merge style/z-index-details-section
                        style/box-shadow)}
    (if-let [selected-person-id (:selected-person-id state)]
      (person-detail (assoc (core/create-person-detail-args state selected-person-id)
                       :trigger-parent-event trigger-event))
      [:div {:id    "detail-container"
             :style {:height             "100%"
                     :display            "grid"
                     :grid-template-rows "auto 1fr"}}
       (s-detailview/toolbar {:items-left
                              [:span {:style {:margin-left "1rem"}}
                               "Select an person from the list."]})
       ;Error text
       [:div {:style {:width      "100%"
                      :height     "100%"
                      :color      color/grey-blue
                      :text-align "left"
                      :top        "10rem"
                      :margin     "2rem"
                      :left       "2rem"}}
        "No person selected. Use the side-bar to" [:br]
        "search and select a person to view details."]

       ;background image
       [:div {:style {:width               "100%"
                      :height              "100%"
                      :position            "absolute"
                      :background-image    "url(\"image/GHS-logotype-horizontal.svg\")"
                      :background-position "35%"
                      :background-size     "25%"
                      :background-repeat   "no-repeat"
                      :opacity             0.1}}]])]])

