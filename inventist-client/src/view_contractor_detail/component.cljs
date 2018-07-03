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
    trigger-event  :trigger-event}]
  (let [contractor (get-in state [:get-contractor-details-response :response])]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:edit-mode     edit-mode
         :on-change     ""
         :image         (:image contractor)
         :heading       (:name contractor)
         :sub-heading-1 (:type contractor)})

      ;Information
      (s-detailview/section-information
        {:fields      [{:label     "Email"
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
         :enable-edit true})

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
        (s-general/section-divider)]]]]))

;Timeline
;TODO
