(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]))

(defc inventory-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "inventory"
                                                                   :item item})
                            :items-right [(s-general/button {:color color/grey-normal
                                                             :text  "Reassign"
                                                             :icon  "fas fa-share-square"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Report Issue"
                                                             :icon  "fas fa-exclamation-triangle"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Add Comment"
                                                             :icon  "fas fa-comment-dots"})]})

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:image   (:photo item)
         :heading (str (:brand item) " " (:model-name item))})

      ;Information
      (s-detailview/section-information
        {:fields ["Serial Number"
                  "Color"
                  "Identifier"
                  "Supplier"
                  "Insaurance expiry"
                  "Warranty expiry"]
         :values [(:serial-number item)
                  (:color item)
                  (:model-identifier item)
                  (:supplier (:purchase-details item))
                  (:insurance-expires (:purchase-details item))
                  (:warranty-expires (:purchase-details item))]})

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       (s-detailview/section-left)

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-detailview/section-title {:title "Current Assignee"})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (s-detailview/person-card {:person (first (:history item))})]
        ;(s-detailview/person-card {:person (last (:history item))})]
        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type     "inventory"
                                      :history  (:history item)
                                      :purchase (:purchase-details item)})]]))



;Report Issue
(defc report-issue []
  [:div {:id    "report-issue-form"
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
      "Reassign Device"]
     (s-detailview/section-divider)

     [:form {:id    "form"
             :style {:display         "flex"
                     :flex-wrap       "wrap"
                     :justify-content "space-between"}}
      (s-general/input-filed {:field    "Selected Device"
                              :value    "Apple MacBook Pro (13-Inch, 2016, Four Thunderbolt 3 Ports)"
                              :disabled true})
      (s-general/input-filed {:field    "Selected Device Serial Number"
                              :value    "C02SVXXXXF1R"
                              :disabled true})
      (s-general/input-filed {:type  "text"
                              :field "Select Assignee"
                              :text  "Enter the name of the person to whom the selected device is to be assigned."})
      (s-general/input-filed {:type     "textarea"
                              :required false
                              :field    "Comment"
                              :text     "Any associated comment"})]



     (s-detailview/section-divider)
     [:div {:style {:display         "flex"
                    :justify-content "center"}}
      (s-general/button {:color color/theme
                         :text  "Reassign Device"
                         :icon  "fas fa-check-circle"})
      (s-general/button {:color color/grey-normal
                         :text  "Cancel"
                         :icon  "fas fa-times-circle"})]]]])



