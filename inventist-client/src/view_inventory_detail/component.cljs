(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as c]))

(def col-width "11rem")

(defc inventory-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar "inventory" item)

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor c/white}}

      ;Page Header
      (s-detailview/detail-header (:photo item)
                                  (str (:brand item) " " (:model-name item))
                                  "" "")

      ;Information
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "information"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "0"}}]
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column" :width "100%"}}
        (s-detailview/section-title "Information")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color c/grey-blue :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Serial Number"]
          [:div {:style {:margin "0.5rem 0"}} "Color"]
          [:div {:style {:margin "0.5rem 0"}} "Identifier"]
          [:div {:style {:margin "0.5rem 0"}} "Insaurance expiry"]
          [:div {:style {:margin "0.5rem 0"}} "Warranty expiry"]]
         [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (:serial-number item)]
          [:div {:style {:margin "0.5rem 0"}} (:color item)]
          [:div {:style {:margin "0.5rem 0"}} (:model-identifier item)]
          [:div {:style {:margin "0.5rem 0"}}
           (:insurance-expires (:purchase-details item))]
          [:div {:style {:margin "0.5rem 0"}} (:warranty-expires (:purchase-details item))]]]
        (s-detailview/section-divider)]]


      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}
        [:img {:src (:image (first (:history item))) :style {:width "3rem" :height "3rem" :object-fit "cover" :backgroundColor c/grey-light :borderRadius "2rem"}}]]

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-detailview/section-title "Assignee")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color c/grey-blue :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Name"]
          [:div {:style {:margin "0.5rem 0"}} "Type - Group"]
          [:div {:style {:margin "0.5rem 0"}} "Assigned on"]]
         [:div {:style {:color c/grey-dark :text-transform "capitalize" :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (str (:fname (first (:history item))) " " (:lname (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (str (:type (first (:history item))) " - " (:group (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (:date (first (:history item)))]]]
        (s-detailview/section-divider)]]

      ;Timeline
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "timeline"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}]
       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-detailview/section-title "Timeline")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:text-transform "capitalize"}}
          (for [{id      :person-id
                 date    :date
                 comment :comment
                 fname   :fname
                 lname   :lname
                 type    :type
                 group   :group} (:history item)]
            [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key id}
             [:div {:style {:color c/grey-blue :width col-width}} date]
             [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
              [:span "Allotted to "]
              [:span {:style {:font-weight "500"}} fname " " lname]
              [:span " (" type " - " group ")"]
              [:br]
              [:span {:class "italic"} comment]]])

          [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
           [:div {:style {:color c/grey-blue :width col-width}} (:delivery-date (:purchase-details item))]
           [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
            [:span "Purchased from "]
            [:span {:style {:font-weight "500"}} (:supplier (:purchase-details item))]
            [:span {:style {:color c/link-active :cursor "pointer"}} " (PDF)"]]]]]
        (s-detailview/section-divider)]]]]))


