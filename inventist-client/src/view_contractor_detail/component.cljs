(ns view-contractor-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]))

(def edit-mode false)

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
                                                             :icon  "fas fa-plus-square"})]})

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
       (s-detailview/section-left)
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                      :width  "100%"}}
        (s-detailview/section-title {:title "Linked Devices"})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}
         (for [item (:inventory contractor)]
           (s-detailview/device-card {:item item}))]
        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type           "contractors"
                                      :history        (:history contractor)
                                      :enable-comment false})]]))

;NEW CONTRACTOR
(defc contractor-add []
  [:div {:id    "detail-container"
         :style {:height             "auto"
                 :maxHeight          "50rem"
                 :width              "22rem"
                 :position           "absolute"
                 :top                "7rem"
                 :right              "0.5rem"
                 :display            "grid"
                 :backgroundColor    color/silver
                 :grid-template-rows "auto 1fr"
                 :box-shadow         "0 0 0.25rem 0 rgba(0,0,0,0.5)"}}

   ;Toolbar
   (s-detailview/toolbar {:items-left  [:span {:style {:margin-left "1rem"}}
                                        [:i {:class "fas fa-plus-square"}] " Add Order"]
                          :items-right [(s-general/button {:color color/white
                                                           :icon  "far fa-times-circle"})]})
   ;;Form Page
   [:div {:style {:overflow-x "hidden"
                  :overflow-y "scroll"}}
    [:div {:style style/form-box}
     [:div {:id    "form"
            :style {:display         "flex"
                    :flex-wrap       "wrap"
                    :justify-content "space-between"}}

      (s-general/input-section {:field "Order Description"
                                :type  "textarea"
                                :text  "Quantity, type, etc."})
      (s-general/input-section {:field "Select File to Upload"
                                :type  "button"
                                :color color/link-active
                                :icon  "fas fa-cloud-upload-alt"
                                :text  "Upload a \".tsv\" file to import the data."
                                :value "Click here"
                                :style {:margin 0}})]]]

   [:div {:style {:display         "flex"
                  :justify-content "space-between"}}
    (s-general/button {:color color/theme
                       :text  "Add Contractor"
                       :icon  "fas fa-check-circle"
                       :style {:margin "0.5rem 1rem"}})]])




