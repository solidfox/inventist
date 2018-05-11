(ns view-person-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]))

(def col-width "11rem")

(defc person-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (get-in state [:get-person-details-response :response])]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "people"
                                                                   :item person})
                            :items-right [(s-general/button {:color color/white
                                                             :icon  "fas fa-plus-square"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Assign New Device"
                                                             :icon  "fas fa-plus-square"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Assign New Device"})]})

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
        {:fields ["Email"
                  "Phone"
                  "Gender"
                  "Address"
                  "Assigned Devices"]
         :values [(:email person)
                  (:phone person)
                  (cond (= (:sex person) "f") "Female"
                        (= (:sex person) "m") "Male")
                  (:address person)
                  (s-general/length (:inventory person))]})

      ;Assigned Devices
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "devices"}
       (s-detailview/section-left)
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                      :width  "100%"}}
        (s-detailview/section-title {:title "Assigned Devices"})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (for [item (:inventory person)]
           (s-detailview/device-card {:item item}))]
        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type    "people"
                                      :history (:history person)})]]))
