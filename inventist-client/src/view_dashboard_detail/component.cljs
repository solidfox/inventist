(ns view-dashboard-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [symbols.overview :as s-overview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]))

(def col-width "11rem")

(defc dashboard-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (get-in state [:get-dashboard-details-response :response])]

    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "dashboard"
                                                                   :item person})
                            :items-right [(s-general/button {:color color/grey-normal
                                                             :text  "New Comment"
                                                             :icon  "fas fa-comment-dots"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Register Device"
                                                             :icon  "fas fa-pen-square"})]})
     [:div {:id    (str ::component-id)
            :style {:display               "grid"
                    :overflow              "scroll"
                    :grid-template-columns "22rem 1fr"}}


      ;Dashboard Stats
      [:div {:style {:overflow-x      "hidden"
                     :overflow-y      "scroll"
                     :backgroundColor color/grey-light}}

       ;Division Heading
       (s-general/division-title
         {:title "Inventist Stats"})

       [:div {:style {:margin    "2.5rem"
                      :display   "flex"
                      :flex-wrap "wrap"}}

        (s-general/stat-card {:value 999
                              :text  "Total Inventory in School"})

        (s-general/stat-card {:value   999
                              :text    "Inventory per Type"
                              :subtext [:select
                                        [:option {:value "All"}]
                                        [:option {:value "All"}]]})

        (s-general/stat-card {:value 9999
                              :text  "Total Computers on repair"})

        (s-general/stat-card {:value   999
                              :text    "Computer on repair per Model"
                              :subtext [:select
                                        [:option {:value "All"}]
                                        [:option {:value "All"}]]})

        (s-general/stat-card {:value 999
                              :text  "Total Reserve computers"})

        (s-general/stat-card {:value 0
                              :text  "Unhandled issue reports"})]]




      ;User Details
      [:div {:style {:overflow-x      "hidden"
                     :overflow-y      "scroll"
                     :backgroundColor color/white}}

       ;Division Heading
       (s-general/division-title
         {:title "Your Details"})

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
                                       :history (:history person)})]]]))

