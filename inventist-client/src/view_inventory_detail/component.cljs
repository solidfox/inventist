(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as symbol-general]
            [symbols.detailview :as symbol-detailview]
            [remodular.core :refer [modular-component]]))

(def col-width "11rem")

(defn length
  [list]
  (if (empty? list) 0
                    (+ 1 (length (rest list)))))


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
     (symbol-detailview/toolbar "inventory" item)

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor "#ffffff"}}

      ;Page Header
      (symbol-detailview/detail-header (:photo item)
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
        (symbol-detailview/section-title "Information")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Serial Number"]
          [:div {:style {:margin "0.5rem 0"}} "Color"]
          [:div {:style {:margin "0.5rem 0"}} "Identifier"]
          [:div {:style {:margin "0.5rem 0"}} "Insaurance expiry"]
          [:div {:style {:margin "0.5rem 0"}} "Warranty expiry"]]
         [:div {:style {:color "#4A4A4A" :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (:serial-number item)]
          [:div {:style {:margin "0.5rem 0"}} (:color item)]
          [:div {:style {:margin "0.5rem 0"}} (:model-identifier item)]
          [:div {:style {:margin "0.5rem 0"}}
           (:insurance-expires (:purchase-details item))]
          [:div {:style {:margin "0.5rem 0"}} (:warranty-expires (:purchase-details item))]]]
        (symbol-detailview/section-divider)]]


      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}
        [:img {:src (:image (first (:history item))) :style {:width "3rem" :height "3rem" :object-fit "cover" :backgroundColor "#f6f6f6" :borderRadius "2rem"}}]]

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (symbol-detailview/section-title "Assignee")
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Name"]
          [:div {:style {:margin "0.5rem 0"}} "Type - Group"]
          [:div {:style {:margin "0.5rem 0"}} "Assigned on"]]
         [:div {:style {:color "#4A4A4A" :text-transform "capitalize" :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (str (:fname (first (:history item))) " " (:lname (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (str (:type (first (:history item))) " - " (:group (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (:date (first (:history item)))]]]
        (symbol-detailview/section-divider)]]

      ;Timeline
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "timeline"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}]
       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (symbol-detailview/section-title "Timeline")
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
             [:div {:style {:color "#95A5A6" :width col-width}} date]
             [:div {:style {:color "#4A4A4A" :margin "0 0 0 1rem"}}
              [:span "Allotted to "]
              [:span {:style {:font-weight "500"}} fname " " lname]
              [:span " (" type " - " group ")"]
              [:br]
              [:span {:class "italic"} comment]]])

          [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
           [:div {:style {:color "#95A5A6" :width col-width}} (:delivery-date (:purchase-details item))]
           [:div {:style {:color "#4A4A4A" :margin "0 0 0 1rem"}}
            [:span "Purchased from "]
            [:span {:style {:font-weight "500"}} (:supplier (:purchase-details item))]
            [:span {:style {:color "#4A90E2" :cursor "pointer"}} " (PDF)"]]]]]
        (symbol-detailview/section-divider)]]]]))


