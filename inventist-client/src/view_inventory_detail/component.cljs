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
  [:div {:id    "detail-container"
         :style style/float-box}
   ;Toolbar
   (s-detailview/toolbar {:items-left  [:span {:style {:margin-left "1rem"}}
                                        [:i {:class "fas fa-exclamation-triangle"}] " Report Issue with Device"]
                          :items-right (s-general/button {:color    color/white
                                                          :icon     "far fa-times-circle"
                                                          :on-click (fn [] (trigger-event (rem/create-event {:name :close-report-issue})))})})
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
      (when object-url
        [:img {:class (style/card-image)
               :src   object-url}])
      (s-general/input-section {:field     "Photo"
                                :type      "upload"
                                :id        "report-image"
                                :color     color/transparent
                                :required  false
                                :text      "You may upload a photo showing the problem."
                                :on-change (fn [e] (trigger-event
                                                     (event/new-report-issue-file (oops/oget e [:target :files :0]))))
                                :style     {:margin 0}})]]]

   [:div {:style {:display         "flex"
                  :justify-content "space-between"}}
    (s-general/button {:color    color/link-active
                       :text     "Report this Issue"
                       :icon     "fas fa-paper-plane"
                       :style    {:margin "0.5rem 1rem"}
                       :on-click (fn [] (trigger-event (rem/create-event {:name :send-report-issue-form})))})]])

(defc inventory-detail < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  
  (let [computer  (get-in state [:get-inventory-details-response :data :computer])
        edit-mode (:edit-mode state)]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "inventory"
                                                                   :item computer})
                            :items-right (s-general/button {:color    color/grey-blue
                                                            :text     "Report Issue with Device"
                                                            :icon     "fas fa-exclamation-triangle"
                                                            :on-click (fn [] (trigger-event (event/report-issue-clicked (:id computer))))})})
     ;Report Issue Box
     (when (core/should-show-report-issue-form? state)
       (report-issue-form {:report-issue-form (:report-issue-form state)
                           :trigger-event     trigger-event}))

     ;------------

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
        {:title     (s-general/section-title {:title "Information"})
         ;:buttons [(s-general/section-title-button {:icon     "far fa-edit"
         ;                                           :text     "Edit"
         ;                                           :on-click ""})]})
         :fields    [{:label    "Serial Number"
                      :value    (:serial-number computer)
                      :editable false}
                     {:label    "Model Identifier"
                      :value    (:model-identifier computer)
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
         :edit-mode edit-mode})

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       (s-general/section-left)

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-general/section-title {:title   "Current Assignee"
                                  :buttons (s-general/section-title-button {:icon     "fas fa-exchange-alt"
                                                                            :text     "Reassign Device"
                                                                            :on-click (fn [] (trigger-event (rem/create-event
                                                                                                              {:name :reassign-device-clicked})))})})

        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}
         (when edit-mode
           (s-detailview/card {:id      "reassign-device"
                               :style   {:display "block !important"
                                         :padding "1rem"}
                               :content [:div {:style {:display         "flex"
                                                       :flex-wrap       "wrap"
                                                       :justify-content "space-between"}}
                                         (s-general/input-field {:placeholder "Search new Assignee's name..."})
                                         ;(s-general/text-area {:required    false
                                         ;                      :maxWidth    "100%"
                                         ;                      :placeholder "Enter comment (optional)."})
                                         (s-general/button {:color    color/theme
                                                            :icon     "fas fa-check-circle"
                                                            :text     "Assign Device"
                                                            :on-click (fn [] (trigger-event (rem/create-event {:name :cancel-device-reassignment})))
                                                            :style    {:margin "0.5rem 0 0 0"}})
                                         (s-general/button {:color    color/grey-normal
                                                            :icon     "fas fa-times-circle"
                                                            :text     "Cancel"
                                                            :on-click (fn [] (trigger-event (rem/create-event {:name :cancel-device-reassignment})))
                                                            :style    {:margin "0.5rem 0 0 0"}})]}))


         (if-let [user (:user computer)]
           (s-detailview/person-card {:user     user
                                      :on-click (fn [] (trigger-event (event/clicked-user (:id user))))})
           [:div {:style {:color      color/grey-normal
                          :font-style "italic"}}
            "This device is unassigned."])]

        (s-general/section-divider)]]

      ;Timeline
      (cond (not= (:history computer) [])
            (s-general/timeline
              {:enable-comment false
               :timeline-items (for [history-item (reverse (sort-by (fn [history-item] (:instant history-item)) (:history computer)))]
                                 [:span {:key (:instant history-item)}
                                  (s-general/timeline-item {:icon     (s-general/circle-icon {:icon "fas fa-user" :color color/link-active})
                                                            :title    (str "Registered to " (get-in history-item [:new-user :first-name])
                                                                           " " (get-in history-item [:new-user :last-name]))
                                                            :on-click (fn [] (trigger-event (event/clicked-user (get-in history-item [:new-user :id]))))
                                                            :content  [:div (str (s-general/time-format-string {:time   (:instant history-item)
                                                                                                                :format "yyyy-MM-dd"}) " — "
                                                                                 (get-in history-item [:new-user :occupation]) " "
                                                                                 (for [group (get-in history-item [:new-user :groups])]
                                                                                   (:name group)))]})])})
            :else
            (s-general/timeline
              {:enable-comment false
               :timeline-items [:div {:style {:color      color/grey-normal
                                              :font-style "italic"
                                              :margin     "-1rem 0 0 1.5rem"}}
                                "No history available"]}))]]))











