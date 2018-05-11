(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as c]))

(def field-col-width "11rem")

;Breadcrumb
(defc breadcrumb [{type :type
                   item :item}]
  [:div {:style {:margin "0.75rem 2.5rem" :cursor "pointer"}}
   (cond (= type "dashboard")
         [:span {:style {:margin "0 0.5rem 0 0"}} "Welcome to Inventist's Dashboard"]
         :else
         [:span {:style {:margin "0 0.5rem 0 0"}}
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
                                       (s-general/device-icon-set {:item item})
                                       (str " - " (:fname (first (:history item))) " " (:lname (first (:history item))))])]])])

;Toolbar contains breadcrumb and action-buttons
(defc toolbar [{type :type
                item :item}]
  [:div {:style {:height          "3rem"
                 :backgroundColor c/grey-dark
                 :display         "flex"
                 :justify-content "space-between"
                 :color           c/white}}
   [:div {:style {:display "flex"}}
    ;(s-general/button-light {:icon "fas fa-arrow-circle-left"}) ;back button for mobile view
    (breadcrumb {:type type
                 :item item})]                              ;breadcrumb for desktop view

   [:div {:style {:display        "flex"
                  :flex-direction "row"
                  :margin         "0 1rem"}}
    (cond (= type "inventory") [(s-general/button {:color c/white
                                                   :icon "fas fa-share-square"})
                                (s-general/button {:color c/grey-normal
                                                   :text "Transfer Device"
                                                   :icon "fas fa-share-square"})
                                (s-general/button {:color c/grey-normal
                                                   :text "Transfer Device"})]
          (= type "people") [(s-general/button {:color c/white
                                                :icon "fas fa-plus-square"})
                             (s-general/button {:color c/grey-normal
                                                :text "Assign New Device"
                                                :icon "fas fa-plus-square"})
                             (s-general/button {:color c/grey-normal
                                                :text "Assign New Device"})])]])


;Page Header - Image and Heading
(defc detail-header [{image         :image
                      heading       :heading
                      sub-heading-1 :sub-heading-1
                      sub-heading-2 :sub-heading-2}]
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
(defc section-title [{title :title}]
  [:div {:id    "header"
         :style {:font-size "1.5rem" :color c/grey-blue}} title])

;Empty div on left of section
(defc section-left []
  [:div {:style {:minWidth   "6rem"
                 :text-align "right"
                 :margin     "3rem 0 0"}}])

;Divider after sections
(defc section-divider []
  [:div {:id    "divider"
         :style {:margin          "1rem 0"
                 :backgroundColor c/silver
                 :width           "100%"
                 :height          "1px"}}])

;Information Section
(defc section-information [{fields :fields
                            values :values}]
  [:div {:style {:margin         "1rem 2.5rem 1rem"
                 :display        "flex"
                 :flex-direction "row"}
         :id    "information"}
   (section-left)
   [:div {:style {:margin         "0 0 0 1rem"
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    (section-title {:title "Information"})
    [:div {:style {:display        "flex"
                   :flex-direction "row"}}
     [:div {:style {:width      field-col-width
                    :color      c/grey-blue
                    :text-align "left"}}
      (for [field fields]
        [:div {:key field :style {:margin "0.5rem 0"}} field])]
     [:div {:style {:color      c/grey-dark
                    :margin     "0 0 0 1rem"
                    :text-align "left"}}
      (for [value values]
        [:div {:key value :style {:margin "0.5rem 0"}} value])]]
    (section-divider)]])

;Timeline Section
(defc section-timeline [{type     :type
                         history  :history
                         purchase :purchase}]
  [:div {:style {:margin         "1rem 2.5rem 1rem"
                 :display        "flex"
                 :flex-direction "row"}
         :id    "timeline"}
   (section-left)
   [:div {:style {:margin         "0 0 0 1rem"
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    (section-title {:title "Timeline"})

    [:div {:style {:display        "flex"
                   :flex-direction "row"}}
     [:div {:style {:text-transform "capitalize"}}
      (cond (= type "people")
            (for [item history]
              [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}
                     :key   (:inventory-id item)}
               [:div {:style {:color c/grey-blue :width field-col-width}} (:date item)]
               [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
                [:span (:brand item) " " (:model-name item)
                 " (" (s-general/device-icon-set {:item item}) ")"]
                [:br]
                [:span {:style {:font-weight "500"}} "Assigned "]
                [:span {:class "italic"} (:comment item)]]])

            (= type "inventory")
            [(for [{id      :person-id
                    date    :date
                    comment :comment
                    fname   :fname
                    lname   :lname
                    type    :type
                    group   :group} history]
               [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key id}
                [:div {:style {:color c/grey-blue :width field-col-width}} date]
                [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
                 [:span "Allotted to "]
                 [:span {:style {:font-weight "500"}} fname " " lname]
                 [:span " (" type " - " group ")"]
                 [:br]
                 [:span {:class "italic"} comment]]])
             [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
              [:div {:style {:color c/grey-blue :width field-col-width}} (:delivery-date purchase)]
              [:div {:style {:color c/grey-dark :margin "0 0 0 1rem"}}
               [:span "Purchased from "]
               [:span {:style {:font-weight "500"}} (:supplier purchase)]
               [:span {:style {:color c/link-active :cursor "pointer"}} " (PDF)"]]]])]]

    (section-divider)]])

;Card to show devices assigned
(defc device-card [{item :item}]
  [:div {:key   (:id item)
         :style {:backgroundColor       c/grey-light
                 :minHeight             "4rem"
                 :width                 "20rem"
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
     (str (:color item) " - " (:serial-number item)) [:br] (str "Assigned on " (:date item))]]])

;Card to show Person
(defc person-card [{person :person}]
  [:div {:key   (:id person)
         :style {:backgroundColor       c/grey-light
                 :minHeight             "4rem"
                 :width                 "20rem"
                 :padding               "1rem"
                 :borderRadius          "0.5rem"
                 :margin                "0.5rem 1rem 0.5rem 0"
                 :display               "grid"
                 :grid-template-columns "auto 1fr"
                 :cursor                "pointer"}}
   [:div {:style {:width "3rem"}}
    [:img {:src   (:image person)
           :style {:width "3rem" :height "3rem" :object-fit "cover" :borderRadius "0.25rem" :backgroundColor c/white}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style {:font-size "1rem" :color c/grey-dark :line-height "1rem" :text-transform "capitalize"}}
     (str (:fname person) " " (:lname person))] [:br]
    [:span {:style {:font-size "0.9rem" :color c/grey-blue :line-height "1rem" :text-transform "capitalize"}}
     (str (:type person) " - " (:group person)) [:br] (str "Assigned on " (:date person))]]])

