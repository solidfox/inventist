(ns view-contractor-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [util.inventory.component :as inventory]
            [symbols.color :as color]
            [symbols.style :as style]))

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
                                                             :text  "Add Order"
                                                             :icon  "fas fa-plus-square"})

                                          (s-general/button {:color color/grey-normal
                                                             :text  "New Contractor"
                                                             :icon  "fas fa-cart-plus"})]})


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

;NEW CONTRACTOR
(defc contractor-add []
  [:div {:id    "detail-container"
         :style {:height             "100%"
                 :width              "100%"
                 ;:position           "absolute"
                 ;:top                "3.5rem"
                 ;:left               0
                 :display            "grid"
                 :backgroundColor    color/silver
                 :grid-template-rows "auto 1fr"}}


   ;Toolbar
   (s-detailview/toolbar {
                          ;:items-left  (s-detailview/breadcrumb {:type ""})
                          :items-right [(s-general/button {:color color/grey-normal
                                                           :text  "Help"
                                                           :icon  "fas fa-help"})]})
   ;;Form Page
   [:div {:style {:overflow-x "hidden"
                  :overflow-y "scroll"}}
    [:div {:style style/form-box}
     [:div {:style (merge {:display         "flex"
                           :justify-content "center"}
                          style/header-title)}
      "New Contractor"]
     (s-detailview/section-divider)

     [:div {:id    "form"
            :style {:display         "flex"
                    :flex-wrap       "wrap"
                    :justify-content "space-between"}}

      (s-general/input-filed {:field "Contractor Name"
                              :text  "Name of supplier, repairer, etc."
                              :value ""})
      (s-general/input-filed {:field "Device Name"
                              :text  "Enter full Device Name"
                              :value "MacBook Pro"})
      (s-general/input-filed {:field "Serial Nr"})
      (s-general/input-filed {:field "Model Identifier/Class"
                              :text  "Eg. laptop, smartphone"})
      (s-general/input-filed {:field "Supplier"
                              :text  "From where the device is purchased"
                              :value "MediaMarkt"})]


     (s-detailview/section-divider)
     [:div {:style {:display         "flex"
                    :justify-content "center"}}
      (s-general/button {:color color/theme
                         :text  "Add Contractor"
                         :icon  "fas fa-check-circle"})
      (s-general/button {:color color/grey-normal
                         :text  "Cancel"
                         :icon  "fas fa-times-circle"})]]]])



