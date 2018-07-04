(ns symbols.overview
  (:require [rum.core :refer [defc] :as rum]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]
            [oops.core :as o]
            [util.inventory.core :as util]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

;Person-list card
(defc person-list-card < rum/static
  [{person :person
    hidden :hidden}]
  [:div {:style (merge {}
                       (when hidden {:display "none"}))
         :key   (:id person)
         :class (style/list-item)}
   [:div {:class (style/list-item-left-column)}
    [:img {:class (style/card-image)
           :src   (cond (and (:image person) (not= (:image person) "")) (:image person)
                        :else "/image/person-m-placeholder.png")}]]
   [:div
    [:span {:style style/card-title}
     (str (:first-name person) " " (:last-name person))] [:br]
    [:span {:style style/card-subtitle}
     (str (:occupation person) " - " (str/join ", " (for [group (:groups person)] (:name group))))] [:br]
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
   [:div {:class (style/list-item-left-column)}
    [:img {:class (style/card-image)
           :src   (cond (and (:image contractor) (not= (:image contractor) "")) (:image contractor)
                        :else "/image/contractor-placeholder.png")}]]
   [:div
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
          [:img {:class (style/card-image)
                 :src   (:photo item)}]]
         :else
         [:div {:style {:width "3rem" :font-size "1.25rem"}}
          (s-general/device-icon-set {:item item})])

   [:div {:style {:margin "0 0 0 1rem"
                  :width  "16rem"}}
    [:span {:style style/card-title}
     (str (:brand item) " " (:model-name item))] [:br]
    [:span {:style style/card-subtitle}
     (str (:serial-number item) " - " (:color item))]]])

;Search component
(defc search-toolbar [{search-field-value :search-field-value
                       shown-results      :shown-results
                       total-results      :total-results
                       on-change          :on-change}]
  [:div
   [:div {:style {:width           "100%"
                  :height          "2.5rem"
                  :backgroundColor color/white
                  :box-shadow      (str "inset 0 0 0.25rem " color/theme-500)
                  :display         "flex"}}
    [:div {:style {:width           "3rem"
                   :height          "2.5rem"
                   :color           color/theme-500
                   :display         "flex"
                   :align-items     "center"
                   :justify-content "center"}}
     [:i {:class "fas fa-search"}]]
    [:input {:type        "input"
             :value       (or search-field-value "")
             :id          "search"
             :name        "search"
             :autoFocus   false
             :placeholder "Search"
             :on-change   on-change
             :style       {:width           "calc(100% - 0.2rem)"
                           :margin          ".1rem .1rem .1rem -2.9rem"
                           :padding-left    "2.9rem"
                           :height          "2.3rem"
                           :color           color/theme-500
                           :backgroundColor color/transparent
                           :border          0}}]]
   [:div {:style {:height          "1rem"
                  :padding         "0.25rem 1rem"
                  :font-size       "0.75rem"
                  :color           color/theme-700
                  :backgroundColor color/theme-300
                  :display         "flex"
                  :justify-content "space-between"
                  :align-items     "center"}}
    [:div (str/join " "
                    (concat (when (and shown-results (not= total-results
                                                           shown-results))
                              ["Showing " shown-results])
                            (when (and shown-results total-results (not= total-results
                                                                         shown-results))
                              ["of"])
                            [total-results]
                            [" results"]))]
    [:div {:style {:color color/theme-700 :cursor "pointer"}}
     (str "Sort Results")]]])

(defc second-column-header [header-text]
  [:div {:style {:width       "auto"
                 :height      "3.5rem"
                 :padding     "0 1rem"
                 :color       color/theme-900
                 :font-size   "1.5rem"
                 :font-weight "400"
                 :display     "grid"
                 :align-items "center"}}
   [:div (str header-text)]])

(defc scrollable
  [{floating-header :floating-header
    floating-footer :floating-footer
    content         :content}]
  [:div {:style {:height             "100vh"
                 :width              "auto"
                 :z-index            5
                 :box-shadow         (str "0 0 0.25rem " color/shadow)
                 :background-color   color/theme-100
                 :display            "grid"
                 :grid-template-rows "auto auto 1fr"}}
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
    {:floating-header [:div (second-column-header type)
                            (search-toolbar {:list-items list-items})]
     :content         [:div {:style {:background-color color/transparent}}
                       (for [list-item list-items]
                         (cond (= type "inventory") (inventory-list-card {:item list-item})
                               (= type "contractors") (contractor-list-card {:contractor list-item})))]}))





