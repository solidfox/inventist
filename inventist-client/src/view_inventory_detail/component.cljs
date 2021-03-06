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
            [util.inventory.core :as util]
            [symbols.people :as s-people]))

(def report-issue-icon "fa-ambulance")

(defc report-issue-form
  [{{{object-url :object-url} :file
     description              :description} :user-input
    is-sending                              :is-sending
    send-response                           :send-response
    trigger-event                           :trigger-event}]
  (let [successfully-sent send-response                     ;(= (:status send-response) 200)
        can-send          (and (not is-sending)
                               (not successfully-sent))]
    [:div {:id    "report-issue"
           :class (style/card {:actionable false})
           :style {:animation             style/bounce-animation
                   :display               "grid"
                   :grid-gap              "0.5rem"
                   :grid-template-columns "1fr 3rem"}}
     (s-general/input-section {:type      "textarea"
                               :disabled  (not can-send)
                               :value     (or description "")
                               :on-change (fn [e]
                                            (trigger-event
                                              (event/set-report-issue-description (oops/oget e [:target :value]))))
                               :text      "Describe your issue in detail."})

     [:div {:on-click (when (not is-sending)
                        (fn [] (trigger-event
                                 (event/send-report-issue-form))))
            :style    {:width              "3rem"
                       :height             "2.5rem"
                       :border-radius      "0.25rem"
                       :opacity            (if can-send 1 0.3)
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
                      :align-self "center"}}
       "Send"]]

     (when successfully-sent
       [:div {:style {:text-align "center"}}
        "Successfully sent!"])]))



(defc inventory-detail < (modular-component event/handle-event)
  [{{state :state}  :input
    trigger-event   :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]
  (let [computer  (get-in state [:get-inventory-details-response :body :data :computer])
        edit-mode (:edit-mode state)]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height     (str (- (js/parseInt viewport-height) 48) "px")
                   :overflow-x "hidden"
                   :overflow-y "scroll"
                   :padding    "1.5rem"}}

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
                          :icon      (str "fas " report-issue-icon)
                          :scroll-to "#timeline-dev"
                          :on-click  (fn [] (trigger-event (event/report-issue-clicked (:id computer))))}]
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
          (s-people/person-list-card {:person    user
                                      :on-click  (fn [] (trigger-event (event/clicked-user (:id user))))
                                      :drop-zone [{:drag-data-type "inventist/inventory-item"
                                                   :drop-zone-text (str "Assign "
                                                                        (:first-name user) " " (:last-name user)
                                                                        " to the dragged inventory-item.")
                                                   :drop-effect    "link"}]
                                      :on-drop   (fn [drag-data] (trigger-event
                                                                   (event/dropped-inventory-item-on-person
                                                                     {:inventory-item-id (:id drag-data)
                                                                      :person-id         (:id user)})))})
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
                                :buttons  (s-general/section-title-button {:text     "Report Problem"
                                                                           :icon     (str "fas " report-issue-icon)
                                                                           :active   (not (core/should-show-report-issue-form? state))
                                                                           :on-click (fn [] (trigger-event (event/report-issue-clicked (:id computer))))})})

      ;Report Issue Box
      (when (core/should-show-report-issue-form? state)
        (report-issue-form (merge (:report-issue-form state)
                                  {:trigger-event trigger-event})))

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














