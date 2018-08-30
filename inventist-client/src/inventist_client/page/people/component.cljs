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
                 :grid-template-columns "20rem 1fr"
                 ;(cond (< viewport-width style/viewport-mobile)
                 ;      (str "20rem " (- style/viewport-mobile 640) "px")
                 ;      :else "20rem 1fr") ;320x3
                 :grid-template-rows    "100%"}}
   (people-list (assoc (core/create-people-overview-args state)
                  :trigger-parent-event trigger-event))
   [:div {:style (merge
                   {:background-color color/light-context-background}
                   style/z-index-details-section
                   style/box-shadow)}
    (if-let [selected-person-id (:selected-person-id state)]
      (person-detail (assoc (core/create-person-detail-args state selected-person-id)
                       :trigger-parent-event trigger-event
                       :viewport-height viewport-height
                       :viewport-width viewport-width))

      (s-detailview/no-selection-view {:viewport-height viewport-height
                                       :viewport-width  viewport-width
                                       :image-url       "/image/ghs-logotype.svg"
                                       :heading         "No person selected."
                                       :sub-heading     "Use the side-bar to search and select a person to view details."}))]])


