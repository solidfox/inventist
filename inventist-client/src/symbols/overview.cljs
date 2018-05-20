(ns symbols.overview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

;Person-list card
(defc person-list-card [{person   :person
                         on-click :on-click}]
  [:div {:key   (:id person)
         :class (style/list-item)}
   [:div {:style {:width "3rem"}}
    [:img {:style style/card-image
           :src   (cond (and (:image person) (not= (:image person) "")) (:image person)
                        :else "image/person-m-placeholder.png")}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/card-title}
     (str (:fname person) " " (:lname person))] [:br]
    [:span {:style style/card-subtitle}
     (str (:type person) " - " (str/join ", " (for [group (:groups person)] (:name group))))] [:br]
    [:span {:style style/card-title}
     (for [item (:inventory person)]
       [:span {:style {:margin "0 1rem 0 0"}}
        (s-general/device-icon-set {:item item})])]]])

;Contractor-list card
(defc contractor-list-card [{contractor :contractor
                             on-click   :on-click}]
  [:div {:key   (:id contractor)
         :class (style/list-item)}
   [:div {:style {:width "3rem"}}
    [:img {:style style/card-image
           :src   (cond (and (:image contractor) (not= (:image contractor) "")) (:image contractor)
                        :else "image/contractor-placeholder.png")}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/card-title}
     (str (:name contractor))] [:br]
    [:span {:style style/card-subtitle}
     (str (:type contractor) " - " (count (:inventory contractor)) " Products")]]])


;Inventory-list card
(defc inventory-list-card [{item     :item
                            on-click :on-click}]
  [:div {:key   (:id item)
         :class (style/list-item)}
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
                  :backgroundColor       color/white
                  :display               "grid"
                  :grid-template-columns "auto 1fr auto"
                  :align-items           "center"
                  :box-shadow            "0px 0px 5px rgba(0,0,0,0.25) inset"}}
    [:div {:style {:margin "0.75rem" :color color/grey-normal}}
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
                           :backgroundColor color/tp
                           :border          0}}]]
   [:div {:style {:height          "auto" :padding "0.25rem 1rem"
                  :font-size       "0.9rem"
                  :backgroundColor color/silver
                  :display         "flex" :justify-content "space-between"}}
    [:div (str "Total " (count list-items) " results")]
    [:div {:style {:color color/link-active :cursor "pointer"}} (str "View Table")]]])

;Sidebar-Footer
(defc overview-footer []
  [:div {:style {:width                 "100%"
                 :height                "3rem"
                 :backgroundColor       color/silver
                 :display               "grid"
                 :grid-template-columns "1fr auto"
                 :box-shadow            "0 0 5px rgba(0,0,0,0.25)"}}
   (s-general/button {:icon "fas fa-list-alt" :color color/tp :text "Powered by Inventist"})
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
   [:div {:style {:overflow-x                 "hidden"
                  :overflow-y                 "scroll"
                  :-webkit-overflow-scrolling "touch"
                  :backgroundColor            color/grey-light}}
    (for [list-item list-items]
      (cond (= type "people") (person-list-card {:person list-item})
            (= type "inventory") (inventory-list-card {:item list-item})
            (= type "contractors") (contractor-list-card {:contractor list-item})))]

   ;Footer
   (overview-footer)])


