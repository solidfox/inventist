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
  [{{state :state}  :input
    trigger-event   :trigger-event
    viewport-height :viewport-height
    viewport-width  :viewport-width}]
  (let [person (core/get-person state)
        {phone   :phone
         address :address} person
        should-show-item-assignment-box (core/should-show-item-assignment-input-box state)]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height          (str (- (js/parseInt viewport-height) 48) "px")
                   :overflow-x      "hidden"
                   :overflow-y      "scroll"
                   :padding         "1.5rem"
                   :z-index         style/z-index-details-section
                   :backgroundColor color/light-context-background}}


     ;Detailed Information
     (s-detailview/section-information
       {:image          [:div {:style {:text-align      "center"
                                       :width           "6rem"
                                       :height          "6rem"
                                       :display         "grid"
                                       :borderRadius    "0.5rem"
                                       :backgroundColor color/light-context-secondary-text}}
                         (cond (and (:image person) (not= (:image person) ""))
                               [:img {:src   (:image person)
                                      :style {:width        "6rem"
                                              :height       "6rem"
                                              :borderRadius "inherit"
                                              :object-fit   "cover"}}]
                               :else
                               [:span {:style {:font-size  "3rem"
                                               :align-self "center"
                                               :color      color/dark-context-primary-text}}
                                (str (subs (or (:first-name person) "") 0 1) (subs (or (:last-name person) "") 0 1))])]
        :heading        (str (:first-name person) " " (:last-name person))
        :actions        [{:icon      "fas fa-plus-circle"
                          :title     "Assign New Device"
                          :on-click  (fn [] (trigger-event (rem/create-event
                                                             {:name :assign-new-device-clicked})))
                          :scroll-to "#devices"}
                         {:icon     "fas fa-qrcode"
                          :title    "QR Code"
                          :on-click ""}
                         {:icon     "far fa-share-square"
                          :title    "Share"
                          :on-click ""}]
        :fields         [{:label    "Occupation"
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
        :edit-mode      false
        :enable-edit    false
        :viewport-width viewport-width})

     ;Assigned Devices
     [:div {:style {:margin-top "0.5rem"}
            :id    "devices"}
      [:div {:style {:margin-left    (cond (< viewport-width style/viewport-mobile) 0
                                           :else "7.5rem")
                     :display        "flex"
                     :flex-direction "column"
                     :width          "auto"}}
       (s-general/section-title {:title    "Assigned Devices"
                                 :viewport (cond (< viewport-width style/viewport-mobile) "mobile"
                                                 :else "desktop")
                                 :buttons  [(cond (not should-show-item-assignment-box)
                                                  (with-key (s-general/section-title-button
                                                              {:icon     "fas fa-plus-circle"
                                                               :text     "Assign New Device"
                                                               :on-click (fn [] (trigger-event (rem/create-event
                                                                                                 {:name :assign-new-device-clicked})))}) 42)
                                                  :else
                                                  (with-key (s-general/section-title-button
                                                              {:icon     "fas fa-times-circle"
                                                               :text     "Cancel Assignment"
                                                               :color    color/light-context-secondary-negative
                                                               :on-click (fn [] (trigger-event event/cancel-new-device-assignment))}) 43))]})


       [:div {:style {:display        "flex"
                      :flex-direction "row"
                      :flex-wrap      "wrap"
                      :align-items    "flex-start"}}

        (when should-show-item-assignment-box
          (let [trigger-commit-new-device-event (fn [] (trigger-event event/commit-new-device))]
            (s-detailview/input-card {:id          "Add Device"
                                      :placeholder "New device's serial number"
                                      :value       (or (core/get-new-device-serial-number state) "")
                                      :on-change   (fn [e] (trigger-event (event/new-device-serial-number-changed (oops/oget e [:target :value]))))
                                      :on-enter    trigger-commit-new-device-event
                                      :on-click    trigger-commit-new-device-event})))

        (when (not should-show-item-assignment-box)
          (cond (= (count (:inventory person)) 0)
                [:div {:style {:color      color/light-context-primary-text
                               :font-style "italic"
                               :margin-top "0.75rem"}}
                 "No Devices Assigned."]))

        (for [item (remove nil?
                           (concat [(:ongoing-inventory-item-assignment state)]
                                   (:inventory person)))]
          (-> (s-detailview/device-card {:item     item
                                         :on-click (fn [] (trigger-event (event/clicked-device (:id item))))})
              (with-key (:id item))))]


       (s-general/section-divider)]]

     ;Timeline
     [:div {:id    "assignee"
            :style {:margin-top  "0.5rem"
                    :margin-left (cond (< viewport-width style/viewport-mobile) 0
                                       :else "7.5rem")}}
      (s-general/section-title {:title "Timeline"})
      (cond (not= (:history person) [])
            (s-general/timeline
              {:viewport-width viewport-width
               :timeline-items (for [history-item (reverse (sort-by (fn [history-item] (:instant history-item)) (:history person)))]
                                 (-> (s-general/timeline-item {:icon     (s-general/circle-icon {:icon "fas fa-laptop"})
                                                               :title    (str "Registered " (get-in history-item [:inventory-item :model-name]))
                                                               :on-click (fn [] (trigger-event (event/clicked-device (get-in history-item [:inventory-item :id]))))
                                                               :content  [:div (str (s-general/time-format-string {:time   (:instant history-item)
                                                                                                                   :format "yyyy-MM-dd"}) " — "
                                                                                    (get-in history-item [:inventory-item :serial-number]))]})
                                     (with-key (:instant history-item))))})
            :else
            [:div {:style {:color      color/light-context-primary-text
                           :font-style "italic"}}
             "No history available"])]]))








