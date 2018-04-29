(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [inventist-client.page.inventory.core :as inventory]
            [remodular.core :refer [modular-component]]))

(def col-width "9rem")

(defn length
  [list]
  (if (empty? list) 0
                    (+ 1 (length (rest list)))))

(defc device-card
  [item]
  [:div {:key   (:id item)
         :style {:backgroundColor       "#f2f2f2"
                 :minHeight             "5rem"
                 :minWidth              "22rem"
                 :maxWidth              "22rem"
                 :padding               "1rem"
                 :borderRadius "0.5rem"
                 :margin                "0.5rem 1rem 0.5rem 0"
                 :display               "grid"
                 :grid-template-columns "auto 1fr"
                 :cursor                "pointer"}}
   [:div {:style {:width "3rem"}}
    [:img {:src   (:photo item)
           :style {:width "3rem" :height "3rem" :object-fit "cover" :borderRadius "0.25rem" :backgroundColor "#ffffff"}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size "1rem" :color "#4a4a4a" :line-height "1rem" :text-transform "capitalize"}}
     (str (:brand item) " " (:model-name item))] [:br]
    [:span {:style {:font-size "0.8rem" :color "#7F8C8D" :line-height "1rem" :text-transform "capitalize"}}
     (str (:color item) " - " (:serial-number item))]]])
    ; [:br]
    ;[:span {:style {:font-size "1rem" :color "#4a4a4a" :line-height "1.5rem" :text-transform "capitalize"}}
    ; [:i {:class (:brand (inventory/inventory-icon item))}] " "
    ; [:i {:class (:model (inventory/inventory-icon item))}]]]])


(defc inventory-detail < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [person (get-in state [:get-inventory-details-response :response])]


    ;PEOPLE DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     [:div {:style {:height          "3rem"
                    :backgroundColor "#4a4a4a"
                    :cursor          "pointer"}}
      [:div {:style {:margin "0.75rem"}}
       [:span {:style {:color  "rgba(255,255,255,1)"
                       :margin "0 1rem 0 0"}}
        [:i {:class "fas fa-arrow-circle-left"}]]
       [:span {:style {:color  "rgba(255,255,255,0.75)"
                       :margin "0 0.5rem 0 0"}} "Dashboard / People /"]
       [:span {:style {:color "rgba(255,255,255,1)" :font-size "0.9rem"}}
        (str (:fname person) " " (:lname person))]]]

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor "#ffffff"}}
      ;Header
      [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
             :id    "header"}
       [:div [:img {:src   (:image person)
                    :style {:width "6rem" :height "6rem" :object-fit "cover" :backgroundColor "#f6f6f6"}}]]
       [:div {:style {:margin "0 0 0 1rem"}}
        [:span {:style {:font-size      "2rem"
                        :font-weight    "300"
                        :text-transform "capitalize"}}
         (str (:fname person) " " (:lname person))] [:br]
        [:span {:style {:font-size      "1rem"
                        :font-weight    "400"
                        :color          "#7F8C8D"
                        :text-transform "capitalize"}}
         (:type person) [:br] (:group person)]]]

      ;Information
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "information"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "0"}}]
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Information"]
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
          [:div {:style {:margin "0.5rem 0"}} "Email"]
          [:div {:style {:margin "0.5rem 0"}} "Phone"]
          [:div {:style {:margin "0.5rem 0"}} "Gender"]
          [:div {:style {:margin "0.5rem 0"}} "Address"]
          [:div {:style {:margin "0.5rem 0"}} "Assigned Devices"]]
         [:div {:style {:color "#4A4A4A" :margin "0 0 0 1rem"}}
          [:div {:style {:margin "0.5rem 0"}} (:email person)]
          [:div {:style {:margin "0.5rem 0"}} (:phone person)]
          [:div {:style {:margin "0.5rem 0"}} (cond (= (:sex person) "f") "Female"
                                                    (= (:sex person) "m") "Male")]
          [:div {:style {:margin "0.5rem 0"}} (:address person)]
          [:div {:style {:margin "0.5rem 0"}} (length (:inventory person))]]]]]

      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]

      ;Assigned Devices
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "devices"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "0"}}]
       [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Assigned Devices"]
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (for [item (:inventory person)]
           (device-card item))]]]



      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]


      ;Timeline
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "timeline"}
       [:div {:style {:minWidth   "6rem"
                      :text-align "right"
                      :margin     "3rem 0 0"}}]
       [:div {:style {:margin "0 0 0 1rem"}}
        [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Timeline"]
        [:div {:style {:display        "flex"
                       :flex-direction "row"}}
         [:div {:style {:text-transform "capitalize"}}
          (for [item (:history person)]
            [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key (:inventory-id item)}
             [:div {:style {:color "#95A5A6" :width col-width}} (:date item)]
             [:div {:style {:margin "0 0 0 1rem"}}
              [:span (:brand item) " " (:model-name item) " ("
               [:i {:class (:brand (inventory/inventory-icon item))}] " "
               [:i {:class (:model (inventory/inventory-icon item))}] ")"]
              [:br]
              [:span {:style {:font-weight "500"}} "Assigned "]
              [:span {:class "italic"} (:comment item)]]])]]]]

      [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
                                   :border-bottom "1px solid #ddd"
                                   :width         "100%"}}]]]))




;;INVENTORY DETAILS
;[:div {:id    "detail-container"
;       :style {:height             "100%"
;               :display            "grid"
;               :grid-template-rows "auto 1fr"}}
; ;Toolbar
; [:div {:style {:height          "3rem"
;                :backgroundColor "#4a4a4a"
;                :cursor          "pointer"}}
;  [:div {:style {:margin "0.75rem"}}
;   [:span {:style {:color  "rgba(255,255,255,1)"
;                   :margin "0 1rem 0 0"}}
;    [:i {:class "fas fa-arrow-circle-left"}]]
;   [:span {:style {:color  "rgba(255,255,255,0.75)"
;                   :margin "0 0.5rem 0 0"}} "Dashboard / Inventory /"]
;   [:span {:style {:color "rgba(255,255,255,1)" :font-size "0.9rem"}}
;    [:i {:class (:brand (inventory/inventory-icon item))}] " "
;    [:i {:class (:model (inventory/inventory-icon item))}]
;    (str " - " (:fname (first (:history item))) " " (:lname (first (:history item))))]]]
;
; ;Main Details Container
; [:div {:style {:overflow-x      "hidden"
;                :overflow-y      "scroll"
;                :backgroundColor "#ffffff"}}
;  ;Header
;  [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
;         :id    "header"}
;   [:div [:img {:src (:photo item) :style {:width "6rem" :height "6rem" :object-fit "cover" :backgroundColor "#f6f6f6"}}]]
;   [:div {:style {:font-size   "2rem"
;                  :font-weight "300"
;                  :margin      "0 0 0 1rem"}} (str " " (:brand item) " " (:model-name item))]]
;
;  ;Information
;  [:div {:style {:margin         "1rem 2.5rem 1rem"
;                 :display        "flex"
;                 :flex-direction "row"}
;         :id    "information"}
;   [:div {:style {:minWidth      "6rem"
;                  :text-align "right"
;                  :margin     "0"}}]
;   [:div {:style {:margin "0 0 0 1rem" :display "flex" :flex-direction "column"}}
;    [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Information"]
;    [:div {:style {:display        "flex"
;                   :flex-direction "row"}}
;     [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
;      [:div {:style {:margin "0.5rem 0"}} "Serial Number"]
;      [:div {:style {:margin "0.5rem 0"}} "Color"]
;      [:div {:style {:margin "0.5rem 0"}} "Identifier"]
;      [:div {:style {:margin "0.5rem 0"}} "Insaurance expiry"]
;      [:div {:style {:margin "0.5rem 0"}} "Warranty expiry"]]
;     [:div {:style {:color "#4A4A4A" :margin "0 0 0 1rem"}}
;      [:div {:style {:margin "0.5rem 0"}} (:serial-number item)]
;      [:div {:style {:margin "0.5rem 0"}} (:color item)]
;      [:div {:style {:margin "0.5rem 0"}} (:model-identifier item)]
;      [:div {:style {:margin "0.5rem 0"}}
;       (:insurance-expires (:purchase-details item))]
;      [:div {:style {:margin "0.5rem 0"}} (:warranty-expires (:purchase-details item))]]]]]
;
;  [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
;                               :border-bottom "1px solid #ddd"
;                               :width         "100%"}}]
;
;  ;Assignee
;  [:div {:style {:margin         "1rem 2.5rem 1rem"
;                 :display        "flex"
;                 :flex-direction "row"}
;         :id    "assignee"}
;   [:div {:style {:minWidth      "6rem"
;                  :text-align "right"
;                  :margin     "3rem 0 0"}}
;    [:img {:src (:image (first (:history item))) :style {:width "3rem" :height "3rem" :object-fit "cover" :backgroundColor "#f6f6f6" :borderRadius "2rem"}}]]
;
;   [:div {:style {:margin "0 0 0 1rem"}}
;    [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Assignee"]
;    [:div {:style {:display        "flex"
;                   :flex-direction "row"}}
;     [:div {:style {:width col-width :color "#95A5A6" :text-align "left"}}
;      [:div {:style {:margin "0.5rem 0"}} "Name"]
;      [:div {:style {:margin "0.5rem 0"}} "Type - Group"]
;      [:div {:style {:margin "0.5rem 0"}} "Assigned on"]]
;     [:div {:style {:color "#4A4A4A" :text-transform "capitalize" :margin "0 0 0 1rem"}}
;      [:div {:style {:margin "0.5rem 0"}} (str (:fname (first (:history item))) " " (:lname (first (:history item))))]
;      [:div {:style {:margin "0.5rem 0"}} (str (:type (first (:history item))) " - " (:group (first (:history item))))]
;      [:div {:style {:margin "0.5rem 0"}} (:date (first (:history item)))]]]]]
;
;  [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
;                               :border-bottom "1px solid #ddd"
;                               :width         "100%"}}]
;
;  ;Timeline
;  [:div {:style {:margin         "1rem 2.5rem 1rem"
;                 :display        "flex"
;                 :flex-direction "row"}
;         :id    "timeline"}
;   [:div {:style {:minWidth      "6rem"
;                  :text-align "right"
;                  :margin     "3rem 0 0"}}]
;   [:div {:style {:margin "0 0 0 1rem"}}
;    [:div {:style {:font-size "1.5rem" :color "#7F8C8D"}} "Timeline"]
;    [:div {:style {:display        "flex"
;                   :flex-direction "row"}}
;     [:div {:style {:text-transform "capitalize"}}
;      (for [{id      :person-id
;             date    :date
;             comment :comment
;             fname   :fname
;             lname   :lname
;             type    :type
;             group   :group} (:history item)]
;        [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key id}
;         [:div {:style {:color "#95A5A6" :width col-width}} date]
;         [:div {:style {:margin "0 0 0 1rem"}}
;          [:span "Allotted to "]
;          [:span {:style {:font-weight "500"}} fname " " lname]
;          [:span " (" type " - " group ")"]
;          [:br]
;          [:span {:class "italic"} comment]]])
;
;      [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
;       [:div {:style {:color "#95A5A6" :width col-width}} (:delivery-date (:purchase-details item))]
;       [:div {:style {:margin "0 0 0 1rem"}}
;        [:span "Purchased from "]
;        [:span {:style {:font-weight "500"}} (:supplier (:purchase-details item))]
;        [:span {:style {:color "#4A90E2" :cursor "pointer"}} " (PDF)"]]]]]]]
;
;  [:div {:id "divider" :style {:margin        "1rem 0 1rem 9.5rem"
;                               :border-bottom "1px solid #ddd"
;                               :width         "100%"}}]]]))
;