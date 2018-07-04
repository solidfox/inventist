(ns view-person-detail.component
  (:require [rum.core :refer [defc with-key]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [symbols.style :as style]
            [cljs-react-material-ui.rum :as ui]
            [clojure.string :as str]
            [remodular.core :as rem]
            [view-person-detail.event :as event]
            [oops.core :as oops]
            [view-person-detail.core :as core]
            [util.inventory.core :as util]))

(defc person-detail < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (core/get-person state)
        {phone   :phone
         address :address} person
        should-show-item-assignment-box (core/should-show-item-assignment-input-box state)]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height          "100vh"
                   :overflow-x      "hidden"
                   :overflow-y      "scroll"
                   :padding         "1.5rem"
                   :backgroundColor color/light-context-background}}

     ;Detailed Information
     (s-detailview/section-information
       {:image       (:image person)
        :heading     (str (:first-name person) " " (:last-name person))
        :fields      [{:label    "Occupation"
                       :value    (:occupation person)
                       :editable false}
                      {:label    "Group"
                       :value    (:name (first (:groups person)))
                                 ;(str (for [group (:groups person)]
                                 ;       (:name group)) ", ")
                       :editable false}
                      {:label    "Email"
                       :value    (->> (:email person)
                                      (remove empty?)
                                      (map (fn [email]
                                             [:a {:key  email
                                                  :href (str "mailto:" email)} [:div email]])))
                       :editable false}
                      (when (not-empty phone) {:label    "Phone"
                                               :value    phone
                                               :editable false})
                      (when (not-empty address) {:label    "Address"
                                                 :value    address
                                                 :editable false})
                      {:label    "Assigned Devices"
                       :value    (count (:inventory person))
                       :editable false}]
        :edit-mode   false
        :enable-edit false})

     ;Assigned Devices
     [:div {:style {:display        "flex"
                    :flex-direction "row"}
            :id    "devices"}
      (s-general/section-left)
      [:div {:style {:margin-left    "1.5rem"
                     :display        "flex"
                     :flex-direction "column"
                     :width          "100%"}}
       (s-general/section-title {:title   "Assigned Devices"
                                 :buttons [(when (not should-show-item-assignment-box)
                                             (with-key (s-general/section-title-button
                                                         {:icon     "fas fa-plus-circle"
                                                          :text     "Assign new device"
                                                          :on-click (fn [] (trigger-event (rem/create-event
                                                                                            {:name :assign-new-device-clicked})))}) 42))]})

       [:div {:style {:display        "flex"
                      :flex-direction "row"
                      :flex-wrap      "wrap"
                      :align-items    "flex-start"}}

        (when should-show-item-assignment-box
          (let [trigger-commit-new-device-event (fn [] (trigger-event event/commit-new-device))]
            (s-detailview/card {:id      "add-device"
                                :content [:div {:style {:display         "flex"
                                                        :flex-wrap       "wrap"
                                                        :justify-content "space-between"}}
                                          ;(ui/auto-complete {:hint-text "New device's serial number"
                                          ;                   :dataSource ["test" "testa"]})
                                          (s-general/input-field {:placeholder "New device's serial number"
                                                                  :value       (or (core/get-new-device-serial-number state) "")
                                                                  :on-change   (fn [e] (trigger-event (event/new-device-serial-number-changed (oops/oget e [:target :value]))))
                                                                  :on-enter    trigger-commit-new-device-event})
                                          (s-general/button {:color    color/theme
                                                             :icon     "fas fa-check-circle"
                                                             :text     "Add Device"
                                                             :style    {:margin "0.5rem 0 0 0"}
                                                             :on-click trigger-commit-new-device-event})
                                          (s-general/button {:color    color/grey-normal
                                                             :icon     "fas fa-times-circle"
                                                             :text     "Cancel"
                                                             :on-click (fn [] (trigger-event event/cancel-new-device-assignment))
                                                             :style    {:margin "0.5rem 0 0 0"}})]})))

        (cond (= (count (:inventory person)) 0)
              [:div {:style {:color      color/grey-normal
                             :font-style "italic"}}
               "No Devices Assigned."])

        (for [item (remove nil?
                           (concat [(:ongoing-inventory-item-assignment state)]
                                   (:inventory person)))]
          (-> (s-detailview/device-card {:item     item
                                         :on-click (fn [] (trigger-event (event/clicked-device (:id item))))})
              (with-key (:id item))))]


       (s-general/section-divider)]]

     ;Timeline
     (cond (not= (:history person) [])
           (s-general/timeline
             {:enable-comment false
              :timeline-items (for [history-item (reverse (sort-by (fn [history-item] (:instant history-item)) (:history person)))]
                                (-> (s-general/timeline-item {:icon     (s-general/circle-icon {:icon "fas fa-laptop" :color color/link-active})
                                                              :title    (str "Registered " (get-in history-item [:inventory-item :model-name]))
                                                              :on-click (fn [] (trigger-event (event/clicked-device (get-in history-item [:inventory-item :id]))))
                                                              :content  [:div (str (s-general/time-format-string {:time   (:instant history-item)
                                                                                                                  :format "yyyy-MM-dd"}) " — "
                                                                                   (get-in history-item [:inventory-item :serial-number]))]})
                                    (with-key (:instant history-item))))})
           :else
           (s-general/timeline
             {:enable-comment false
              :timeline-items [:div {:style {:color      color/grey-normal
                                             :font-style "italic"
                                             :margin     "-1rem 0 0 1.5rem"}}
                               "No history available"]}))]))






