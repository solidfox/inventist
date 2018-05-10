(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as symbol-general]))

;Toolbar contains breadcrumb and action-buttons
(defc toolbar [type item]
  [:div {:style {:height          "3rem"
                 :backgroundColor "#4a4a4a"}}

   [:div {:style {:margin "0.75rem" :cursor "pointer"}}
    (cond (= type "dashboard")
          [:span {:style {:color  "rgba(255,255,255,1)"
                          :margin "0 0.5rem 0 0"}} "Welcome to Inventist's Dashboard"]
          :else
          [:span
           ;;0-Back Button (Only required in mobile-view)
           ;[:span {:style {:color  "rgba(255,255,255,1)"
           ;                :margin "0 1rem 0 0"}}
           ; [:i {:class "fas fa-arrow-circle-left"}]]
           ;1-Dashboard
           [:span {:style {:color  "rgba(255,255,255,0.75)"
                           :margin "0 0.5rem 0 0"}} "Dashboard"]
           [:span {:style {:color  "rgba(255,255,255,0.75)"
                           :margin "0 0.5rem 0 0"}} "/"]
           ;2-Main Navigation
           [:span {:style {:color  "rgba(255,255,255,0.75)"
                           :margin "0 0.5rem 0 0"}}
            (cond (= type "people") "People"
                  (= type "inventory") "Inventory")]
           [:span {:style {:color  "rgba(255,255,255,0.75)"
                           :margin "0 0.5rem 0 0"}} "/"]
           ;3-Current Item
           [:span {:style {:color "rgba(255,255,255,1)"}}
            (cond (= type "people") [:span (str (:fname item) " " (:lname item))]
                  (= type "inventory") [:span
                                        (symbol-general/device-icon-set item)
                                        (str " - " (:fname (first (:history item))) " " (:lname (first (:history item))))])]])]])

;Page Header - Image and Heading
(defc detail-header [image
                     heading
                     sub-heading-1
                     sub-heading-2]
  [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
         :id    "header"}
   [:div [:img {:src   image
                :style {:width      "6rem" :height "6rem"
                        :object-fit "cover" :backgroundColor "#f6f6f6"}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size      "2rem"
                    :color          "#000000"
                    :font-weight    "300"
                    :text-transform "capitalize"}}
     heading] [:br]

    [:span {:style {:font-weight    "400"
                    :color          "#7F8C8D"
                    :text-transform "capitalize"}}
     sub-heading-1 [:br] sub-heading-2]]])

;Title for sections
(defc section-title [title]
  [:div {:id    "header"
         :style {:font-size "1.5rem" :color "#7F8C8D"}} title])

;Divider after sections
(defc section-divider []
  [:div {:id    "divider"
         :style {:margin        "1rem 0"
                 :border-bottom "1px solid #ddd"
                 :width         "100%"}}])

;Card to show devices assigned
(defc device-card
  [item]
  [:div {:key   (:id item)
         :style {:backgroundColor       "#f2f2f2"
                 :minHeight             "5rem"
                 :minWidth              "22rem"
                 :maxWidth              "22rem"
                 :padding               "1rem"
                 :borderRadius          "0.5rem"
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
     (str (:color item) " - " (:serial-number item))] [:br]
    [:span {:style {:font-size "0.8rem" :color "#7F8C8D" :line-height "1rem" :text-transform "capitalize"}}
     (str "Assigned on " (:date item))]]])