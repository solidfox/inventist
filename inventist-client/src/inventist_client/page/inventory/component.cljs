(ns inventist-client.page.inventory.component
  (:require [remodular.core :refer [modular-component]]
            [view-inventory-detail.component :refer [inventory-detail]]
            [view-inventory-overview.component :refer [inventory-list]]
            [inventist-client.page.inventory.core :as core]
            [rum.core :refer [defc]]
            [inventist-client.page.inventory.event :as event]
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
                                        ;(cond (< viewport-width style/viewport-mobile) "20rem 20rem"
                                        ;      :else "20rem 1fr") ;320x3
                 :grid-template-rows    "100%"}}
   (inventory-list (assoc (core/create-inventory-overview-args state)
                     :trigger-parent-event trigger-event))
   [:div {:style (merge style/z-index-details-section
                        style/box-shadow)}
    (if-let [selected-inventory-id (:selected-inventory-id state)]
      (inventory-detail (assoc (core/create-inventory-detail-args state selected-inventory-id)
                          :trigger-parent-event trigger-event
                          :viewport-height viewport-height
                          :viewport-width viewport-width))
      [:div {:id    "detail-container"
             :style (merge style/watermark
                           {:height             viewport-height
                            :display            "grid"
                            :grid-template-rows "1fr"})}

       ;Error text
       [:div {:style {:width      "100%"
                      :height     viewport-height
                      :color      color/light-context-secondary-text
                      :text-align "left"
                      :margin     "3.5rem 1.5rem"}}
        "No item selected. Use the side-bar to" [:br]
        "search and select an item to view details."]])]])



