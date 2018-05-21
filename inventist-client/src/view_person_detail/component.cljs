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
            [view-person-detail.event :as event]))

(defc person-detail < (modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (get-in state [:get-person-details-response :data :person])
        {phone   :phone
         address :address} person
        edit-mode (:edit-mode state)]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left (s-detailview/breadcrumb {:type "people"
                                                                  :item person})})

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:image         (:image person)
         :heading       (str (:fname person) " " (:lname person))
         :sub-heading-1 (:type person)
         :sub-heading-2 (:group person)})

      ;Information
      (s-detailview/section-information
        {:fields      [{:label    "Email"
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
         :edit-mode   edit-mode
         :enable-edit false})

      ;Assigned Devices
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "devices"}
       (s-detailview/section-left)
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                      :width  "100%"}}
        (s-detailview/section-title {:title   "Assigned Devices"
                                     :buttons [(when (not edit-mode)
                                                 (with-key (s-detailview/section-title-button
                                                             {:icon     "fas fa-plus-circle"
                                                              :text     "Assign new device"
                                                              :on-click (fn [] (trigger-event (rem/create-event
                                                                                                {:name :assign-new-device-clicked})))}) 42))]})

        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}

         (when edit-mode
           (s-detailview/card {:id      "add-device"
                               :content [:form {:style {:display         "flex"
                                                        :flex-wrap       "wrap"
                                                        :justify-content "space-between"}}
                                         (s-general/input-field {:placeholder "Search Device's name..."
                                                                 :value       ""})
                                         (s-general/text-area {:required    false
                                                               :maxWidth    "100%"
                                                               :placeholder "Enter comment (optional)."})
                                         (s-general/button {:color color/theme
                                                            :icon  "fas fa-check-circle"
                                                            :text  "Add Device"
                                                            :style {:margin "0.5rem 0 0 0"}})
                                         (s-general/button {:color color/grey-normal
                                                            :icon  "fas fa-times-circle"
                                                            :text  "Cancel"
                                                            :style {:margin "0.5rem 0 0 0"}})]}))

         (cond (= (count (:inventory person)) 0)
               [:div {:style {:color      color/grey-normal
                              :font-style "italic"}}
                "No Devices Assigned."])

         (for [item (:inventory person)]
           (s-detailview/device-card {:item item}))]


        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type           "people"
                                      :enable-comment false
                                      :history        (:history person)})]]))


