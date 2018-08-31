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
         :class (style/card)
         :style {:display               "grid"
                 :grid-gap              "0.5rem"
                 :grid-template-columns "1fr 3rem"}}
   (s-general/input-section {:type      "textarea"
                             :width     "14rem"
                             :value     (or description "")
                             :on-change (fn [e]
                                          (trigger-event
                                            (event/set-report-issue-description (oops/oget e [:target :value]))))
                             :text      "Describe your issue in detail."})

   [:div {:on-click send-response
          :style    {:width              "3rem"
                     :height             "2.5rem"
                     :border-radius      "0.25rem"
                     :display            "grid"
                     :grid-template-rows "1.5rem 1rem"
                     :justify-content    "center"
                     :color              color/shaded-context-background
                     :background-color   color/shaded-context-secondary-text}}
    [:span {:style {:font-size  "1rem"
                    :text-align "center"
                    :align-self "end"}}
     [:i {:class "fas fa-check-circle"}]]
    [:span {:style {:font-size  "0.75rem"
                    :align-self "center"}} "Submit"]]

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
            :src   object-url}])])



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
       {:image          [:div {:style {:text-align   "center"
                                       :width        "6rem"
                                       :height       "6rem"
                                       :display      "grid"
                                       :borderRadius "0.5rem"}}
                         (cond (and (:image computer) (not= (:image computer) ""))
                               [:img {:src   (:image computer)
                                      :style {:width        "6rem"
                                              :height       "6rem"
                                              :borderRadius "inherit"
                                              :object-fit   "cover"}}]
                               :else
                               [:span {:style {:font-size  "5.5rem"
                                               :align-self "center"
                                               :color      color/dark-context-primary-text}}
                                (s-general/device-icon-set {:item computer})])]
        :heading        (str (:brand computer) " " (:model-name computer))
        :actions        [{:title     "Reassign Device"
                          :icon      "fas fa-exchange-alt"
                          :scroll-to "#assignee"
                          :on-click  (fn [] (trigger-event (rem/create-event
                                                             {:name :reassign-device-clicked})))}
                         {:title     "Report Issue with Device"
                          :icon      "fas fa-exclamation-triangle"
                          :scroll-to "#timeline-dev"
                          :on-click  (fn [] (trigger-event (event/report-issue-clicked (:id computer))))}
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
     [:div {:id    "assignee"
            :style {:margin-top  "0.5rem"
                    :margin-left (cond (< viewport-width style/viewport-mobile) 0
                                       :else "7.5rem")}}

      [:div {:style {:display        "flex"
                     :flex-direction "column"
                     :width          "100%"}}
       (s-general/section-title {:title    "Current Assignee"
                                 :viewport (cond (< viewport-width style/viewport-mobile) "mobile"
                                                 :else "desktop")
                                 :buttons  (cond edit-mode
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
     [:div {:id    "timeline-dev"
            :style {:margin-top  "0.5rem"
                    :margin-left (cond (< viewport-width style/viewport-mobile) 0
                                       :else "7.5rem")}}

      (s-general/section-title {:title    "Timeline"
                                :viewport (cond (< viewport-width style/viewport-mobile) "mobile"
                                                :else "desktop")
                                :buttons  (cond (core/should-show-report-issue-form? state)
                                                (s-general/section-title-button {:icon     "fas fa-exclamation-triangle" ;
                                                                                 :text     "Cancel Issue Reporting"
                                                                                 :color    color/light-context-secondary-negative
                                                                                 :on-click (fn [] (trigger-event (rem/create-event {:name :close-report-issue})))})
                                                :else
                                                (s-general/section-title-button {:text     "Report Issue with Device"
                                                                                 :icon     "fas fa-exclamation-triangle"
                                                                                 :on-click (fn [] (trigger-event (event/report-issue-clicked (:id computer))))}))})

      ;Report Issue Box
      (when (core/should-show-report-issue-form? state)
        (report-issue-form {:report-issue-form (:report-issue-form state)
                            :trigger-event     trigger-event}))

      (cond (not= (:history computer) [])
            (s-general/timeline
              {:viewport-width viewport-width
               :enable-comment false
               :timeline-items (for [history-item (reverse (sort-by (fn [history-item] (:instant history-item)) (:history computer)))]
                                 [:span {:key (:instant history-item)}
                                  (s-general/timeline-item {:icon     (s-general/circle-icon {:icon "fas fa-user"})
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
            [:div {:style {:color      color/light-context-primary-text
                           :font-style "italic"}}
             "No history available"])]]))














