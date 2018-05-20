(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.rum :as ui]))

(defc inventory-detail < (modular-component)
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
                                                             :text  "Report Issue"
                                                             :icon  "fas fa-exclamation-triangle"})
                                          (s-general/button {:color color/grey-normal})]})


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
        {:fields      [{:label    "Serial Number"
                        :value    (:serial-number item)
                        :editable false}
                       {:label    "Phone"
                        :value    (:model-identifier item)
                        :editable false}
                       {:label    "Supplier"
                        :value    (:supplier (:purchase-details item))
                        :editable false}
                       {:label      "Insurance expiry"
                        :value      (s-general/time-format-string {:time (:insurance-expires (:purchase-details item))})
                        :side-value (str " (" (s-general/days-to-expiry (:insurance-expires (:purchase-details item))) " days left)")
                        :editable   false}
                       {:label      "Warranty expiry"
                        :value      (s-general/time-format-string {:time (:warranty-expires (:purchase-details item))})
                        :side-value (str " (" (s-general/days-to-expiry (:warranty-expires (:purchase-details item))) " days left)")
                        :editable   false}]
         :edit-mode   false
         :enable-edit false})

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       (s-detailview/section-left)

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-detailview/section-title {:title   "Current Assignee"
                                     :buttons [(s-detailview/section-title-button {:icon     "fas fa-exchange-alt"
                                                                                   :text     "Reassign Device"
                                                                                   :on-click ""})]})

        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}
         (s-detailview/card {:id      "reassign-device"
                             :style   {:display "block !important"
                                       :padding "1rem"}
                             :content [:form {:style {:display         "flex"
                                                      :flex-wrap       "wrap"
                                                      :justify-content "space-between"}}
                                       (s-general/input-field {:placeholder "Search new Assignee's name..."})
                                       (s-general/text-area {:required    false
                                                             :maxWidth    "100%"
                                                             :placeholder "Enter comment (optional)."})
                                       (s-general/button {:color color/theme
                                                          :icon  "fas fa-check-circle"
                                                          :text  "Assign Device"
                                                          :style {:margin "0.5rem 0 0 0"}})
                                       (s-general/button {:color color/grey-normal
                                                          :icon  "fas fa-times-circle"
                                                          :text  "Cancel"
                                                          :style {:margin "0.5rem 0 0 0"}})]})

         (s-detailview/person-card {:person (first (:history item))})]





        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type           "inventory"
                                      :enable-comment true
                                      :history        (:history item)
                                      :purchase       (:purchase-details item)})]]))



;
;;Report Issue
;(defc report-issue []
;  [:div {:id    "report-issue-form"
;         :style {:height             "100%"
;                 :width              "100%"
;                 ;:position           "absolute"
;                 ;:top                "3.5rem"
;                 ;:left               0
;                 :display            "grid"
;                 :backgroundColor    color/silver
;                 :grid-template-rows "auto 1fr"}}
;
;
;   ;Toolbar
;   (s-detailview/toolbar {
;                          ;:items-left  (s-detailview/breadcrumb {:type ""})
;                          :items-right [(s-general/button {:color color/grey-normal
;                                                           :text  "Help"
;                                                           :icon  "fas fa-help"})]})
;   ;;Form Page
;   [:div {:style {:overflow-x "hidden"
;                  :overflow-y "scroll"}}
;    [:div {:style style/form-box}
;     [:div {:style (merge {:display         "flex"
;                           :justify-content "center"}
;                          style/header-title)}
;      "Reassign Device"]
;     (s-detailview/section-divider)
;
;     [:form {:id    "form"
;             :style {:display         "flex"
;                     :flex-wrap       "wrap"
;                     :justify-content "space-between"}}
;      (s-general/input-section {:field    "Selected Device"
;                                :value    "Apple MacBook Pro (13-Inch, 2016, Four Thunderbolt 3 Ports)"
;                                :disabled true})
;      (s-general/input-section {:field    "Selected Device Serial Number"
;                                :value    "C02SVXXXXF1R"
;                                :disabled true})
;      (s-general/input-section {:type  "text"
;                                :field "Select Assignee"
;                                :text  "Enter the name of the person to whom the selected device is to be assigned."})
;      (s-general/input-section {:type     "textarea"
;                                :required false
;                                :field    "Comment"
;                                :text     "Any associated comment"})]
;
;
;
;     (s-detailview/section-divider)
;     [:div {:style {:display         "flex"
;                    :justify-content "center"}}
;      (s-general/button {:color color/theme
;                         :text  "Reassign Device"
;                         :icon  "fas fa-check-circle"})
;      (s-general/button {:color color/grey-normal
;                         :text  "Cancel"
;                         :icon  "fas fa-times-circle"})]]]])



