(ns view-contractor-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]))

(def edit-mode false)

(defc contractor-detail < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]
  (let [contractor (get-in state [:get-contractor-details-response :response])]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height          (str (- (js/parseInt viewport-height) 48) "px")
                   :overflow-x      "hidden"
                   :overflow-y      "scroll"
                   :padding         "1.5rem"
                   :z-index         style/z-index-details-section
                   :backgroundColor color/light-context-background}}

     ;Information
     (s-detailview/section-information
       {:image       [:div {:style {:text-align      "center"
                                    :width           "6rem"
                                    :height          "6rem"
                                    :display         "grid"
                                    :borderRadius    "0.5rem"
                                    :backgroundColor color/light-context-secondary-text}}
                      (cond (and (:image contractor) (not= (:image contractor) ""))
                            [:img {:src   (:image contractor)
                                   :style {:width        "6rem"
                                           :height       "6rem"
                                           :borderRadius "inherit"
                                           :object-fit   "cover"}}]
                            :else
                            [:span {:style {:font-size  "3rem"
                                            :align-self "center"
                                            :color      color/dark-context-primary-text}}
                             (str (subs (or (:name contractor) "") 0 1))])]
        :heading     (:name contractor)
        :fields      [{:label     "Type"
                       :value     (:type contractor)
                       :on-change ""
                       :editable  false}
                      {:label     "Email"
                       :value     (:email contractor)
                       :on-change ""
                       :editable  true}
                      {:label     "Phone"
                       :value     (:phone contractor)
                       :on-change ""
                       :editable  true}
                      {:label     "Address"
                       :value     (:address contractor)
                       :on-change ""
                       :editable  true}
                      {:label     "Linked Devices"
                       :value     (count (:inventory contractor))
                       :on-change ""
                       :editable  false}]
        :edit-mode   edit-mode
        :enable-edit true
        :viewport-width viewport-width})

     ;Assigned Devices
     [:div {:style {:margin         "1rem 2.5rem 1rem"
                    :display        "flex"
                    :flex-direction "row"}
            :id    "devices"}
      (s-general/section-left)
      [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                     :width  "100%"}}
       (s-general/section-title {:title "Linked Devices"})
       [:div {:style {:display        "flex"
                      :flex-direction "row"
                      :flex-wrap      "wrap"
                      :align-items    "flex-start"}}
        (for [item (:inventory contractor)]
          (s-detailview/device-card {:item item}))]
       (s-general/section-divider)]]]))

;Timeline
;TODO
