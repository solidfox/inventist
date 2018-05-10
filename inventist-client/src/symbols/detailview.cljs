(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as c]))

;Toolbar contains breadcrumb and action-buttons
(defc toolbar [type item]
  [:div {:style {:height          "3rem"
                 :backgroundColor c/grey-dark}}

   [:div {:style {:margin "0.75rem" :cursor "pointer" :color c/white}}
    (cond (= type "dashboard")
          [:span {:style {:margin "0 0.5rem 0 0"}} "Welcome to Inventist's Dashboard"]
          :else
          [:span {:style {:margin "0 0.5rem 0 0"}}
           ;;0-Back Button (Only required in mobile-view)
           ;[:span {:style {:margin "0 1rem 0 0"}}
           ; [:i {:class "fas fa-arrow-circle-left"}]]
           ;1-Dashboard
           [:span {:style {:opacity "0.75"
                           :margin  "0 0.5rem 0 0"}} "Dashboard"]
           [:span {:style {:opacity "0.75"
                           :margin  "0 0.5rem 0 0"}} "/"]
           ;2-Main Navigation
           [:span {:style {:opacity "0.75"
                           :margin  "0 0.5rem 0 0"}}
            (cond (= type "people") "People"
                  (= type "inventory") "Inventory")]
           [:span {:style {:opacity "0.75"
                           :margin  "0 0.5rem 0 0"}} "/"]
           ;3-Current Item
           [:span
            (cond (= type "people") [:span (str (:fname item) " " (:lname item))]
                  (= type "inventory") [:span
                                        (s-general/device-icon-set item)
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
                        :object-fit "cover" :backgroundColor c/grey-light}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size      "2rem"
                    :color          c/black
                    :font-weight    "300"
                    :text-transform "capitalize"}}
     heading] [:br]

    [:span {:style {:font-weight    "400"
                    :color          c/grey-blue
                    :text-transform "capitalize"}}
     sub-heading-1 [:br] sub-heading-2]]])

;Title for sections
(defc section-title [title]
  [:div {:id    "header"
         :style {:font-size "1.5rem" :color c/grey-blue}} title])

;Divider after sections
(defc section-divider []
  [:div {:id    "divider"
         :style {:margin          "1rem 0"
                 :backgroundColor c/silver
                 :width           "100%"
                 :height          "1px"}}])

;Card to show devices assigned
(defc device-card
  [item]
  [:div {:key   (:id item)
         :style {:backgroundColor       c/grey-light
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
           :style {:width "3rem" :height "3rem" :object-fit "cover" :borderRadius "0.25rem" :backgroundColor c/white}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size "1rem" :color c/grey-dark :line-height "1rem" :text-transform "capitalize"}}
     (str (:brand item) " " (:model-name item))] [:br]
    [:span {:style {:font-size "0.8rem" :color c/grey-blue :line-height "1rem" :text-transform "capitalize"}}
     (str (:color item) " - " (:serial-number item))] [:br]
    [:span {:style {:font-size "0.8rem" :color c/grey-blue :line-height "1rem" :text-transform "capitalize"}}
     (str "Assigned on " (:date item))]]])