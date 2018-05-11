(ns view-contractor-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]))

(def col-width "11rem")

(defc contractor-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [contractor (get-in state [:get-contractor-details-response :response])]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "contractors"
                                                                   :item contractor})
                            :items-right [(s-general/button {:color color/grey-normal
                                                             :text  "Add New Device"
                                                             :icon  "fas fa-plus-square"})]})


     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:image         (:image contractor)
         :heading       (:name contractor)
         :sub-heading-1 (:type contractor)})

      ;Information
      (s-detailview/section-information
        {:fields ["Email"
                  "Phone"
                  "Address"
                  "Linked Devices"]
         :values [(:email contractor)
                  (:phone contractor)
                  (:address contractor)
                  (s-general/length (:inventory contractor))]})

      ;Assigned Devices
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "devices"}
       (s-detailview/section-left)
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                      :width  "100%"}}
        (s-detailview/section-title {:title "Linked Devices"})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (for [item (:inventory contractor)]
           (s-detailview/device-card {:item item}))]
        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type    "contractors"
                                      :history (:history contractor)})]]))
