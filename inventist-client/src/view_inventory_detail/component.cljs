(ns view-inventory-detail.component
  (:require [rum.core :refer [defc with-key]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]
            [cljs-react-material-ui.rum :as ui]
            [clojure.string :as str]
            [remodular.core :as rem]
            [view-inventory-detail.event :as event]
            [view-inventory-detail.core :as core]
            [oops.core :as oops]
            [util.inventory.core :as util]))

(defn report-issue-form
  [{{{{object-url :object-url} :file
      description              :description} :user-input
     is-sending                              :is-sending
     send-response                           :send-response} :report-issue-form
    trigger-event                                            :trigger-event}]
  [:div {:id    "report-issue"
         :style style/float-box}
   ;Toolbar
   [:div {:style {:padding         "0.75rem"
                  :font-size       "1rem"
                  :display         "flex"
                  :justify-content "space-between"
                  :align-items     "center"}}
    [:span {:style {:margin-left "0rem"}}
     [:i {:class "fas fa-exclamation-triangle"}] " Report Issue with Device"]
    [:span {:style    {:font-size "1.25rem"
                       :color     color/dark-context-secondary-negative
                       :cursor    "pointer"}
            :on-click (fn [] (trigger-event (rem/create-event {:name :close-report-issue})))}
     [:i {:class "far fa-times-circle"}]]]

   ;;Form
   [:div {:style {:overflow-x "hidden"
                  :overflow-y "scroll"}}
    [:div {:style style/form-box}
     [:div {:id    "form"
            :style {:display         "flex"
                    :flex-wrap       "wrap"
                    :justify-content "space-between"}}
      (s-general/input-section {:field     "Issue"
                                :type      "textarea"
                                :value     (or description "")
                                :on-change (fn [e]
                                             (trigger-event
                                               (event/set-report-issue-description (oops/oget e [:target :value]))))
                                :text      "Describe your issue in detail."})

      (s-general/input-section {:field     "Photo"
                                :type      "upload"
                                :id        "report-image"
                                :color     color/transparent
                                :required  false
                                :text      "You may upload a photo showing the problem."
                                :on-change (fn [e] (trigger-event
                                                     (event/new-report-issue-file (oops/oget e [:target :files :0]))))
                                :style     {:margin 0}})
      (when object-url
        [:img {:class (style/card-image)
               :src   object-url}])]]]

   [:div {:style {:display         "flex"
                  :justify-content "space-between"}}
    (s-general/button {:color    color/shaded-context-primary-text
                       :bg-color color/shaded-context-background
                       :text     "Report this Issue"
                       :icon     "fas fa-paper-plane"
                       :style    {:margin "0.75rem"}
                       :on-click (fn [] (trigger-event (rem/create-event {:name :send-report-issue-form})))})]])

(defc inventory-detail < (modular-component event/handle-event)
  [{{state :state}  :input
    trigger-event   :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]

  (let [computer (get-in state [:get-inventory-details-response :body :data :computer])
        edit-mode (:edit-mode state)]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height          (str (- (js/parseInt viewport-height) 48) "px")
                   :overflow-x      "hidden"
                   :overflow-y      "scroll"
                   :padding         "1.5rem"
                   :z-index         style/z-index-details-section
                   :backgroundColor color/light-context-background}}

     ;Information
     (s-detailview/section-information
       {:image          (:photo computer)
        :heading        (str (:brand computer) " " (:model-name computer))
        :actions        [{:title    "Report Issue with Device"
                          :icon     "fas fa-exclamation-triangle"
                          :on-click (fn [] (trigger-event (event/report-issue-clicked (:id computer))))}
                         {:icon     "fas fa-qrcode"
                          :title    "QR Code"
                          :on-click ""}
                         {:icon     "far fa-share-square"
                          :title    "Share"
                          :on-click ""}]
        :fields         [{:label    "Serial Number"
                          :value    (:serial-number computer)
                          :editable false}
                         {:label    "Brand"
                          :value    (:brand computer)
                          :editable false}
                         {:label    "Model Identifier"
                          :value    (:model-identifier computer)
                          :editable false}
                         {:label    "Color"
                          :value    (:color computer)
                          :editable false}
                         (when (not-empty (:purchase_details computer)) {:label    "Supplier"
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
        :edit-mode      edit-mode
        :enable-edit    false
        :viewport-width viewport-width})

     ;Assignee
     [:div {:style {:margin-top  "0.5rem"
                    :margin-left (cond (< viewport-width style/viewport-mobile) 0
                                       :else "7.5rem")}}

      [:div {:style {:display        "flex"
                     :flex-direction "column"
                     :width          "100%"}}
       (s-general/section-title {:title   "Current Assignee"
                                 :viewport (cond (< viewport-width style/viewport-mobile) "mobile"
                                                 :else "desktop")
                                 :buttons (cond edit-mode
                                                (s-general/section-title-button {:icon     "far fa-times-circle" ;
                                                                                 :text     "Cancel Reassignment"
                                                                                 :color    color/light-context-secondary-negative
                                                                                 :on-click (fn [] (trigger-event (rem/create-event
                                                                                                                   {:name :cancel-device-reassignment})))})
                                                :else
                                                (s-general/section-title-button {:icon     "fas fa-exchange-alt"
                                                                                 :text     "Reassign Device"
                                                                                 :on-click (fn [] (trigger-event (rem/create-event
                                                                                                                   {:name :reassign-device-clicked})))}))})


       [:div {:style {:display        "flex"
                      :flex-direction "row"
                      :flex-wrap      "wrap"
                      :align-items    "flex-start"}}
        (when edit-mode
          (s-detailview/input-card {:id          "Reassign Device"
                                    :placeholder "Enter Person's Name"
                                    :value       ""
                                    :on-change   ""
                                    :on-enter    (fn [] (trigger-event (rem/create-event {:name :cancel-device-reassignment})))
                                    :on-click    (fn [] (trigger-event (rem/create-event {:name :cancel-device-reassignment})))}))


        (if-let [user (:user computer)]
          (s-detailview/person-card {:user     user
                                     :on-click (fn [] (trigger-event (event/clicked-user (:id user))))})
          (when (not edit-mode)
            [:div {:style {:color      color/light-context-primary-text
                           :font-style "italic"
                           :margin-top "0.75rem"}}
             "This device is unassigned."]))]

       (s-general/section-divider)]]

     ;Timeline
     (cond (not= (:history computer) [])
           (s-general/timeline
             {:viewport-width viewport-width
              :enable-comment false
              :timeline-items (for [history-item (reverse (sort-by (fn [history-item] (:instant history-item)) (:history computer)))]
                                [:span {:key (:instant history-item)}
                                 (s-general/timeline-item {:icon     (s-general/circle-icon {:icon "fas fa-user" :color color/link-active})
                                                           :title    (str "Registered to " (get-in history-item [:new-user :first-name])
                                                                          " " (get-in history-item [:new-user :last-name]))
                                                           :on-click (fn [] (trigger-event (event/clicked-user (get-in history-item [:new-user :id]))))
                                                           :content  [:div (str (s-general/time-format-string {:time   (:instant history-item)
                                                                                                               :format "yyyy-MM-dd"}) " — "
                                                                                (get-in history-item [:new-user :occupation]) " - "
                                                                                (:name (first (:groups (:new-user history-item)))))]})])})
           ;(for [group (get-in history-item [:new-user :groups])]
           ;  (:name group)))]})])})
           :else
           (s-general/timeline
             {:viewport-width viewport-width
              :enable-comment false
              :timeline-items [:div {:style {:color      color/light-context-primary-text
                                             :font-style "italic"
                                             :margin     "-1.5rem 0 0 1.5rem"}}
                               "No history available"]}))

     ;Report Issue Box
     (when (core/should-show-report-issue-form? state)
       (report-issue-form {:report-issue-form (:report-issue-form state)
                           :trigger-event     trigger-event}))]))











