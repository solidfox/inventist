(ns symbols.overview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as c]))

;To change listing bg color on hover.
(def listBGcolor c/grey-list)

;Person-list card
(defc person-list-card [person]
  [:div {:key   (:id person)
         :style {:width                 "100%"
                 :backgroundColor       listBGcolor
                 :minHeight             "3rem"
                 :padding               "1rem"
                 :margin                "0 0 2px 0"
                 :display               "grid"
                 :grid-template-columns "auto 1fr"
                 :cursor                "pointer"}}
   [:div {:style {:width "3rem"}}
    [:img {:src   (:image person)
           :style {:width "3rem" :height "3rem" :object-fit "cover" :borderRadius "2rem" :backgroundColor c/white}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size "1rem" :color c/grey-dark :line-height "1rem" :text-transform "capitalize"}}
     (str (:fname person) " " (:lname person))] [:br]
    [:span {:style {:font-size "0.8rem" :color c/grey-blue :line-height "1rem" :text-transform "capitalize"}}
     (str (:type person) " - " (:group person))] [:br]
    [:span {:style {:font-size "1rem" :color c/grey-dark :line-height "1.5rem" :text-transform "capitalize"}}
     (for [item (:inventory person)]
       [:span {:style {:margin "0 1rem 0 0"}}
        (s-general/device-icon-set item)])]]])


;Inventory-list card
(defc inventory-list-card [item]
  [:div {:key   (:id item)
         :style {:width                 "100%"
                 :backgroundColor       listBGcolor
                 :minHeight             "3rem"
                 :padding               "1rem"
                 :margin                "0 0 2px 0"
                 :display               "grid"
                 :grid-template-columns "auto 1fr"
                 :cursor                "pointer"}}
   [:div {:style {:width "2.5rem" :font-size "1.1rem"}}
    (s-general/device-icon-set item)]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size "1rem" :color c/grey-dark :line-height "1rem" :text-transform "capitalize"}}
     (str (:model-name item) " - " (:color item))] [:br]
    [:span {:style {:font-size "1rem" :color c/grey-blue :line-height "1rem" :text-transform "capitalize"}}
     (str (:assignee item))]]])

;Search component
(defc overview-search [list-items]
  [:div
   [:div {:style {:width                 "100%"
                  :height                "3rem"
                  :backgroundColor       c/white
                  :display               "grid"
                  :grid-template-columns "auto 1fr auto"
                  :box-shadow            "0px 0px 5px rgba(0,0,0,0.25) inset"}}
    [:div {:style {:margin "0.75rem" :color c/grey-normal}}
     [:i {:class "fas fa-search"}]]
    [:input {:type        "input"
             :id          "search"
             :name        "search"
             :autoFocus   true
             :placeholder "Search"
             :style       {:width           "100%"
                           :margin          "0"
                           :height          "100%"
                           :backgroundColor c/tp
                           :border          0}}]]
   [:div {:style {:height          "auto" :padding "0.25rem 1rem"
                  :font-size       "0.9rem"
                  :backgroundColor c/grey-light
                  :display         "flex" :justify-content "space-between"}}
    [:div (str "Total " (s-general/length list-items) " results")]
    [:div {:style {:color c/link-active :cursor "pointer"}} (str "View Table")]]])

;Sidebar-Footer
(defc overview-footer []
  [:div {:style {:width                 "100%"
                 :height                "3rem"
                 :backgroundColor       c/silver
                 :display               "grid"
                 :grid-template-columns "auto 1fr auto"
                 :box-shadow            "0 0 5px rgba(0,0,0,0.25)"}}
   [:div {:style {:margin "0.75rem" :opacity "0.75"}}
    [:img {:src "/image/inventist_icon.svg" :style {:width "1rem"}}]]
   [:div {:style {:margin "0.75rem 0" :font-size "1rem" :opacity "0.75"}}
    [:span "Powered by Inventist"]]
   [:div {:style {:margin "0.75rem" :font-size "1.2rem" :opacity "0.75" :cursor "pointer"}}
    [:i {:class "fas fa-caret-left"}]]])


;overview with search and listing
(defc overview-list
  [type list-items]
  [:div {:style {:height             "100%"
                 :display            "grid"
                 :grid-template-rows "auto 1fr auto"}}
   ;Normal Search
   (overview-search list-items)

   ;listing
   [:div {:style {:overflow-x      "hidden"
                  :overflow-y      "scroll"
                  :backgroundColor c/grey-light}}
    (for [list-item list-items]
      (cond (= type "people") (person-list-card list-item)
            (= type "inventory") (inventory-list-card list-item)))]

   ;Footer
   (overview-footer)])

