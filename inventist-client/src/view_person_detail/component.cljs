(ns view-person-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]))

(defc person-detail < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (get-in state [:get-person-details-response :data :person])]

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
                        :value    (str (:email person))
                        :editable false}
                       {:label    "Phone"
                        :value    (:phone person)
                        :editable false}
                       {:label    "Gender"
                        :value    (cond (= (:sex person) "f") "Female"
                                        (= (:sex person) "m") "Male")
                        :editable false}
                       {:label    "Address"
                        :value    (:address person)
                        :editable false}
                       {:label    "Assigned Devices"
                        :value    (count (:inventory person))
                        :editable false}]
         :edit-mode   false
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
                                     :buttons [(s-detailview/section-title-button {:icon     "fas fa-plus-circle"
                                                                                   :text     "Assign new devices"
                                                                                   :on-click ""})]})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"
                       :align-items    "flex-start"}}
         (s-detailview/card {:id      "add-device"
                             :content [:form {:style {:display         "flex"
                                                      :flex-wrap       "wrap"
                                                      :justify-content "space-between"}}
                                       (s-general/input-field {:placeholder "Search Device's name..."
                                                               :value ""})
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
                                                          :style {:margin "0.5rem 0 0 0"}})]})
         (for [item (:inventory person)]
           (s-detailview/device-card {:item item}))]

        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type           "people"
                                      :enable-comment false
                                      :history        (:history person)})]]))
