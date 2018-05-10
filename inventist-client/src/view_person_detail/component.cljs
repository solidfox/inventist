(ns view-person-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [remodular.core :refer [modular-component]]
            [symbols.color :as c]))

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
     (s-detailview/toolbar "people" person)

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor c/white}}

      ;Page Header
      (s-detailview/detail-header (:image person)
                                  (str (:fname person) " " (:lname person))
                                  (:type person)
                                  (:group person))

      ;Information
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "information"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "0"}}]
       [:div {:style {:margin         "0 0 0 1rem"
                      :display        "flex"
                      :flex-direction "column"
                      :width          "100%"}}
        (s-detailview/section-title "Information")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color c/grey-blue :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Email"]
          [:div {:style {:margin "0.5rem 0"}} "Phone"]
          [:div {:style {:margin "0.5rem 0"}} "Gender"]
          [:div {:style {:margin "0.5rem 0"}} "Address"]
          [:div {:style {:margin "0.5rem 0"}} "Assigned Devices"]]
         [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (:email person)]
          [:div {:style {:margin "0.5rem 0"}} (:phone person)]
          [:div {:style {:margin "0.5rem 0"}} (cond (= (:sex person) "f") "Female"
                                                    (= (:sex person) "m") "Male")]
          [:div {:style {:margin "0.5rem 0"}} (:address person)]
          [:div {:style {:margin "0.5rem 0"}} (s-general/length (:inventory person))]]]
        (s-detailview/section-divider)]]



      ;Assigned Devices
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "devices"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "0"}}]

       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"
                      :width  "100%"}}
        (s-detailview/section-title "Assigned Devices")
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (for [item (:inventory person)]
           (s-detailview/device-card item))]
        (s-detailview/section-divider)]]


      ;Timeline
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "timeline"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}]
       [:div {:style {:margin "0 0 0 1rem"
                      :width  "100%"}}
        (s-detailview/section-title "Timeline")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:text-transform "capitalize"}}
          (for [item (:history person)]
            [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key (:inventory-id item)}
             [:div {:style {:color c/grey-blue :width col-width}} (:date item)]
             [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
              [:span (:brand item) " " (:model-name item)
               " (" (s-general/device-icon-set item) ")"]
              [:br]
              [:span {:style {:font-weight "500"}} "Assigned "]
              [:span {:class "italic"} (:comment item)]]])]]
        (s-detailview/section-divider)]]]]))


