(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.rum :as ui]
            [clojure.string :as str]
            [remodular.core :as rem]
            [view-inventory-detail.event :as event]))

(defc inventory-detail < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [computer (get-in state [:get-inventory-details-response :data :computer])
        {purchase_details :purchase-details
         user             :user} computer
        edit-mode (:edit-mode state)]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "inventory"
                                                                   :item computer})
                            :items-right [(s-general/button {:color color/grey-normal
                                                             :text  "Report Issue"
                                                             :icon  "fas fa-exclamation-triangle"})]})

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:image   (:photo computer)
         :heading (str (:brand computer) " " (:model-name computer))})

      ;Information
      (s-detailview/section-information
        {:fields      [{:label    "Serial Number"
                        :value    (:serial-number computer)
                        :editable false}
                       {:label    "Model Identifier"
                        :value    (:model-identifier computer)
                        :editable false}
                       (when (not-empty purchase_details) {:label    "Supplier"
                                                           :value    (:name (:supplier (:purchase_details computer)))
                                                           :editable false}
                                                          {:label      "Insurance expiry"
                                                           :value      (s-general/time-format-string {:time (:insurance_expires (:purchase_details computer))})
                                                           :side-value (str " (" (s-general/days-to-expiry (:insurance_expires (:purchase_details computer))) " days left)")
                                                           :editable   false}
                                                          {:label      "Warranty expiry"
                                                           :value      (s-general/time-format-string {:time (:warranty_expires (:purchase_details computer))})
                                                           :side-value (str " (" (s-general/days-to-expiry (:warranty_expires (:purchase_details computer))) " days left)")
                                                           :editable   false})]
         :edit-mode   edit-mode
         :enable-edit false})

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       (s-general/section-left)

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-general/section-title {:title   "Current Assignee"
                                  :buttons [(s-general/section-title-button {:icon     "fas fa-exchange-alt"
                                                                             :text     "Reassign Device"
                                                                             :on-click (fn [] (trigger-event (rem/create-event
                                                                                                               {:name :reassign-device-clicked})))})]})

        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}
         (when edit-mode (s-detailview/card {:id      "reassign-device"
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
                                                                          :style {:margin "0.5rem 0 0 0"}})]}))


         (if user (s-detailview/person-card {:user user})
                  [:div {:style {:color      color/grey-normal
                                 :font-style "italic"}}
                   "This device is unassigned."])]

        (s-general/section-divider)]]]]))
;
;;Timeline
;(if (:history computer)
;  (s-detailview/section-timeline {:type           "inventory"
;                                  :enable-comment true
;                                  :history        (:history computer)
;                                  :purchase       (:purchase-details computer)}))]]))



