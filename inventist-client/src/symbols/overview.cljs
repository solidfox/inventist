(ns symbols.overview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]
            [oops.core :as o]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

;Person-list card
(defc person-list-card [{person    :person
                         on-select :on-select}]
  [:div {:key      (:id person)
         :class    (style/list-item)
         :on-click on-select}
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
       [:span {:key   (:id item)
               :style {:margin "0 1rem 0 0"}}
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
(defc inventory-list-card [{item      :item
                            on-select :on-select}]
  [:div {:key      (:id item)
         :class    (style/list-item)
         :on-click on-select}
   (cond (and (:photo item) (not= (:photo item) ""))
         [:div {:style {:width "3rem"}}
          [:img {:style style/card-image
                 :src   (:photo item)}]]
         :else
         [:div {:style {:width "3rem" :font-size "1.25rem"}}
          (s-general/device-icon-set {:item item})])

   [:div {:style {:margin "0 0 0 1rem"
                  :width  "16rem"}}
    [:span {:style style/card-title}
     (str (:brand item) " " (:model_name item))] [:br]
    [:span {:style style/card-subtitle}
     (str (:serial_number item) " - " (:color item))]]])

;Search component
(defc search-toolbar [{search-field-value :search-field-value
                       shown-results      :shown-results
                       total-results      :total-results
                       on-change          :on-change}]
  [:div
   [:div {:style {:width                 "100%"
                  :height                "3rem"
                  :backgroundColor       color/white
                  :display               "grid"
                  :grid-template-columns "auto 1fr auto"
                  :align-items           "center"}}
    [:div {:style {:margin "0.75rem" :color color/grey-normal}}
     [:i {:class "fas fa-search"}]]
    [:input {:type        "input"
             :value       (or search-field-value "")
             :id          "search"
             :name        "search"
             :autoFocus   true
             :placeholder "Search"
             :on-change   on-change
             :style       {:width           "100%"
                           :margin          "0"
                           :font-size       "1rem"
                           :height          "100%"
                           :backgroundColor color/transparent
                           :border          0}}]]
   [:div {:style {:height          "auto" :padding "0.25rem 1rem"
                  :font-size       "0.9rem"
                  :color           color/de-emphasize
                  :backgroundColor color/silver
                  :display         "flex" :justify-content "space-between"}}
    [:div (str/join " "
                    (concat (when (and total-results (not= total-results
                                                           shown-results))
                              ["Showing"])
                            [shown-results]
                            (when (and total-results (not= total-results
                                                           shown-results))
                              ["of" total-results])
                            [" results"]))]]])
;[:div {:style {:color color/link-active :cursor "pointer"}} (str "View Table")]]])

;Sidebar-Footer
(defc footer []
  [:div {:style {:width                 "calc(100% - 2rem)"
                 :padding               "1rem"
                 :backgroundColor       color/silver
                 :display               "grid"
                 :grid-template-columns "1fr auto"}}
   ;:box-shadow            "0 0 5px rgba(0,0,0,0.25)"}}
   [:div {:style {:color color/de-emphasize}} "Powered by inventist"]])
;(s-general/button {:icon "fas fa-list-alt" :color color/tp :text "Powered by Inventist"})
;[:div {:style {:margin "0.75rem" :font-size "1.2rem" :opacity "0.75" :cursor "pointer"}}
; [:i {:class "fas fa-caret-left"}]]])

(defc scrollable
  [{floating-header :floating-header
    floating-footer :floating-footer
    content         :content}]
  [:div {:style {:height             "100%"
                 :width              "22rem"
                 :display            "grid"
                 :grid-template-rows "auto 1fr auto"}}
   floating-header
   [:div {:style {:overflow-x                 "hidden"
                  :overflow-y                 "scroll"
                  :-webkit-overflow-scrolling "touch"}}
    content]
   floating-footer])


;Footer



;overview with search and listing
(defc overview-list
  [{type       :type
    list-items :list-items}]
  (scrollable
    {:floating-header (search-toolbar {:list-items list-items})
     :floating-footer (footer)
     :content
                      [:div {:style {:background-color color/grey-light}}
                       (for [list-item list-items]
                         (cond (= type "inventory") (inventory-list-card {:item list-item})
                               (= type "contractors") (contractor-list-card {:contractor list-item})))]}))





