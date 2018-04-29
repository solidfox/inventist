(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [inventory.core :as inventory]
            [remodular.core :refer [modular-component]]))

(def col-width "9rem")

(defc inventory-detail < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     [:div {:style {:height          "3rem"
                    :backgroundColor "#BDC3C7"
                    :cursor          "pointer"}}
      [:div {:style {:margin "0.7rem"}}
       [:span {:style {:color  "rgba(255,255,255,1)"
                       :margin "0 1rem 0 0"}}
        [:i {:class "fas fa-arrow-circle-left"}]]
       [:span {:style {:color  "rgba(255,255,255,0.75)"
                       :margin "0 0.5rem 0 0"}} "Dashboard / Inventory /"]
       [:span {:style {:color "rgba(255,255,255,1)" :font-size "1rem"}}
        [:i {:class (:brand (inventory/inventory-icon item))}] " "
        [:i {:class (:model (inventory/inventory-icon item))}]
        (str " - " (:fname (first (:history item))) " " (:lname (first (:history item))))]]]

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor "#ffffff"}}
      ;Header
      [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
             :id    "header"}
       [:div [:img {:src (:photo item) :style {:width "6rem"}}]]
       [:div {:style {:font-size   "2rem"
                      :font-weight "300"
                      :margin      "0 0 0 1rem"}} (str " " (:brand item) " " (:model-name item))]]

      ;Information
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "information"}
       [:div {:style {:width      "6rem"
                      :text-align "right"
                      :margin     "0"}}]
       ;[:img {:src (:image (first (:history item))) :style {:width "3rem" :borderRadius "2rem"}}]]
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Information"]
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
          [:div {:style {:margin "0.5rem 0"}} (:insurance-expires (:purchase-details item))]
          [:div {:style {:margin "0.5rem 0"}} (:warranty-expires (:purchase-details item))]]]]]

      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       [:div {:style {:width      "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}
        [:img {:src (:image (first (:history item))) :style {:width "3rem" :borderRadius "2rem"}}]]

       [:div {:style {:margin "0 0 0 1rem"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Assignee"]
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Name"]
          [:div {:style {:margin "0.5rem 0"}} "Type-Group"]
          [:div {:style {:margin "0.5rem 0"}} "Assigned on"]]
         [:div {:style {:color "#4A4A4A" :text-transform "capitalize" :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (str (:fname (first (:history item))) " " (:lname (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (str (:type (first (:history item))) " - " (:group (first (:history item))))]
          [:div {:style {:margin "0.5rem 0"}} (:date (first (:history item)))]]]]]

      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]

      ;Timeline
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "timeline"}
       [:div {:style {:width      "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}]
       ;[:img {:src (:image (first (:history item))) :style {:width "3rem" :borderRadius "2rem"}}]]

       [:div {:style {:margin "0 0 0 1rem"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Timeline"]
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
             [:div {:style {:margin "0 0 0 1rem"}}
              (str "Allotted to " fname " " lname " (" type " - " group ")")
              [:br]
              [:span {:class "italic"} comment]]])

          [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
           [:div {:style {:color "#95A5A6" :width col-width}} (:delivery-date (:purchase-details item))]
           [:div {:style {:margin "0 0 0 1rem"}}
            (str "Purchased from " (:supplier (:purchase-details item)))
            [:span {:style {:color "#4A90E2" :cursor "pointer"}} " (PDF)"]]]]]]]

      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]]]))


;(defc person-card
;  [{fname :fname
;    lname :lname
;    image :image
;    type  :type
;    group :group
;    date  :date}]
;  (ant/card {:bordered true :class "card-photo"}
;            (ant/col {:span 12}
;                     [:div [:img {:src image :style {:width "80%"}}]])
;
;            (ant/col {:span 12} [:dl
;                                 [:dt "Name"]
;                                 [:dd fname " " lname]
;                                 [:dt "Type"]
;                                 [:dd type " - " group]
;                                 [:dt "Assigned on"]
;                                 [:dd date]]))))