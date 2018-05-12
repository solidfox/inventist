(ns symbols.overview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as c]
            [symbols.style :as style]))

;To change listing bg color on hover.
(def list-bg-color c/highlight)

;Person-list card
(defc person-list-card [{person   :person
                         on-click :on-click}]
  [:div {:key   (:id person)
         :style style/list-item}
   [:div {:style {:width "3rem"}}
    [:img {:style style/card-image
           :src   (cond (and (:image person) (not= (:image person) "")) (:image person)
                        :else (cond (= (:sex person) "f") "image/person-f-placeholder.png"
                                    (= (:sex person) "m") "image/person-m-placeholder.png"))}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/card-title}
     (str (:fname person) " " (:lname person))] [:br]
    [:span {:style style/card-subtitle}
     (str (:type person) " - " (:group person))] [:br]
    [:span {:style style/card-title}
     (for [item (:inventory person)]
       [:span {:style {:margin "0 1rem 0 0"}}
        (s-general/device-icon-set {:item item})])]]])

;Contractor-list card
(defc contractor-list-card [{contractor :contractor
                             on-click   :on-click}]
  [:div {:key   (:id contractor)
         :style style/list-item}
   [:div {:style {:width "3rem"}}
    [:img {:style style/card-image
           :src   (cond (and (:image contractor) (not= (:image contractor) "")) (:image contractor)
                        :else "image/contractor-placeholder.png")}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/card-title}
     (str (:name contractor))] [:br]
    [:span {:style style/card-subtitle}
     (str (:type contractor) " - " (s-general/length (:inventory contractor)) " Products")]]])


;Inventory-list card
(defc inventory-list-card [{item     :item
                            on-click :on-click}]
  [:div {:key   (:id item)
         :style style/list-item}
   (cond (and (:photo item) (not= (:photo item) ""))
         [:div {:style {:width "3rem"}}
          [:img {:style style/card-image
                 :src   (:photo item)}]]
         :else
         [:div {:style {:width "3rem" :font-size "1.25rem"}}
          (s-general/device-icon-set {:item item})])

   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/card-title}
     (str (:model-name item) " - " (:color item))] [:br]
    [:span {:style style/card-subtitle}
     (str (:assignee item))]]])

;Search component
(defc overview-search [{list-items :list-items}]
  [:div
   [:div {:style {:width                 "100%"
                  :height                "3rem"
                  :backgroundColor       c/white
                  :display               "grid"
                  :grid-template-columns "auto 1fr auto"
                  :align-items           "center"
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
                           :font-size       "1rem"
                           :height          "100%"
                           :backgroundColor c/tp
                           :border          0}}]]
   [:div {:style {:height          "auto" :padding "0.25rem 1rem"
                  :font-size       "0.9rem"
                  :backgroundColor c/silver
                  :display         "flex" :justify-content "space-between"}}
    [:div (str "Total " (s-general/length list-items) " results")]
    [:div {:style {:color c/link-active :cursor "pointer"}} (str "View Table")]]])

;Sidebar-Footer
(defc overview-footer []
  [:div {:style {:width                 "100%"
                 :height                "3rem"
                 :backgroundColor       c/silver
                 :display               "grid"
                 :grid-template-columns "1fr auto"
                 :box-shadow            "0 0 5px rgba(0,0,0,0.25)"}}
   (s-general/button {:icon "fas fa-list-alt" :color c/tp :text "Powered by Inventist"})
   [:div {:style {:margin "0.75rem" :font-size "1.2rem" :opacity "0.75" :cursor "pointer"}}
    [:i {:class "fas fa-caret-left"}]]])


;overview with search and listing
(defc overview-list
  [{type       :type
    list-items :list-items}]
  [:div {:style {:height             "100%"
                 :display            "grid"
                 :grid-template-rows "auto 1fr auto"}}
   ;Normal Search
   (overview-search {:list-items list-items})

   ;listing
   [:div {:style {:overflow-x      "hidden"
                  :overflow-y      "scroll"
                  :backgroundColor c/grey-light}}
    (for [list-item list-items]
      (cond (= type "people") (person-list-card {:person list-item})
            (= type "inventory") (inventory-list-card {:item list-item})
            (= type "contractors") (contractor-list-card {:contractor list-item})))]

   ;Footer
   (overview-footer)])


