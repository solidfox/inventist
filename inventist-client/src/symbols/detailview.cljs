(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]))

(def field-col-width "11rem")

;Breadcrumb
(defc breadcrumb [{type :type
                   item :item}]
  [:div {:style {:margin "0.75rem 2.5rem" :cursor "pointer"}}
   (cond
     (= type "back")
     [:span (s-general/button {:color color/white
                               :icon "fas fa-arrow-circle-left"
                               :title "Go Back"})]
     (= type "dashboard")
     [:span "Hi " (:fname item) " " (:lname item) ", welcome to Inventist."]
     :else
     [:span
      ;1-Dashboard
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}} "Dashboard"]
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}} "/"]
      ;2-Main Navigation
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}}
       (cond (= type "people") "People"
             (= type "inventory") "Inventory"
             (= type "contractors") "Contractors")]
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}} "/"]
      ;3-Current Item
      [:span
       (cond (= type "people") [:span (str (:fname item) " " (:lname item))]
             (= type "inventory") [:span
                                   (s-general/device-icon-set {:item item})
                                   (str " - " (:fname (first (:history item))) " " (:lname (first (:history item))))]
             (= type "contractors") [:span (str (:name item))])]])])

;Toolbar contains breadcrumb and action-buttons
(defc toolbar [{items-left  :items-left
                items-right :items-right}]
  [:div {:style {:height          "3rem"
                 :backgroundColor color/grey-dark
                 :display         "flex"
                 :justify-content "space-between"
                 :align-items     "center"
                 :color           color/white}}
   [:div {:style {:display "flex"}}
    ;(s-general/button-light {:icon "fas fa-arrow-circle-left"}) ;back button for mobile view
    items-left]                                             ;breadcrumb for desktop view
   [:div {:style {:display        "flex"
                  :flex-direction "row"
                  :margin         "0 1rem"}}
    items-right]])

;Page Header - Image and Heading
(defc detail-header [{image         :image
                      heading       :heading
                      sub-heading-1 :sub-heading-1
                      sub-heading-2 :sub-heading-2}]
  [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
         :id    "header"}
   [:div [:img {:src   (cond (and image (not= image "")) image
                             :else "image/no-image.png")
                :style {:width      "6rem" :height "6rem"
                        :object-fit "cover" :backgroundColor color/grey-light}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/header-title}
     heading] [:br]

    [:span {:style {:font-weight    "400"
                    :color          color/grey-blue
                    :text-transform "capitalize"}}
     sub-heading-1 [:br] sub-heading-2]]])


;Title for sections
(defc section-title [{title :title}]
  [:div {:id    "header"
         :style {:font-size "1.5rem" :color color/grey-blue}} title])

;Empty div on left of section
(defc section-left []
  [:div {:style {:minWidth   "6rem"
                 :text-align "right"
                 :margin     "3rem 0 0"}}])

;Divider after sections
(defc section-divider []
  [:div {:id    "divider"
         :style {:margin          "1rem 0"
                 :backgroundColor color/silver
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
                    :color      color/grey-blue
                    :text-align "left"}}
      (for [field fields]
        [:div {:key field :style {:margin "0.5rem 0"}} field])]
     [:div {:style {:color      color/grey-dark
                    :margin     "0 0 0 1rem"
                    :text-align "left"}}
      (for [value values]
        [:div {:key value :style {:margin "0.5rem 0"}} value [:br]])]]
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
               [:div {:style {:color color/grey-blue :width field-col-width}} (:date item)]
               [:div {:style {:color color/grey-dark :margin "0 0 0 1rem"}}
                [:span (:brand item) " " (:model-name item)
                 " (" (s-general/device-icon-set {:item item}) ")"]
                [:br]
                [:span {:style {:font-weight "500"}} "Assigned "]
                [:span {:class "italic"} (:comment item)]]])

            (= type "contractors")
            (for [item history]
              [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}
                     :key   (:inventory-id item)}
               [:div {:style {:color color/grey-blue :width field-col-width}} (:date item)]
               [:div {:style {:color color/grey-dark :margin "0 0 0 1rem"}}
                [:span (:brand item) " " (:model-name item)
                 " (" (s-general/device-icon-set {:item item}) ")"]
                [:br]
                [:span {:style {:font-weight "500"}} "Purchased "]
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
                [:div {:style {:color color/grey-blue :width field-col-width}} date]
                [:div {:style {:color color/grey-dark :margin "0 0 0 1rem"}}
                 [:span "Allotted to "]
                 [:span {:style {:font-weight "500"}} fname " " lname]
                 [:span " (" type " - " group ")"]
                 [:br]
                 [:span {:class "italic"} comment]]])
             [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
              [:div {:style {:color color/grey-blue :width field-col-width}} (:delivery-date purchase)]
              [:div {:style {:color color/grey-dark :margin "0 0 0 1rem"}}
               [:span "Purchased from "]
               [:span {:style {:font-weight "500"}} (:supplier purchase)]
               [:span {:style {:color color/link-active :cursor "pointer"}} " (PDF)"]]]])]]

    (section-divider)]])

(defc card
  [{style     :style
    key       :key
    image-url :image-url
    content   :content}]
  [:div {:key   key
         :style (merge style/card
                       style)}
   [:div [:img {:src   image-url
                :style style/card-image}]]
   [:div {:class "card-content"}
    content]])

;Card to show devices assigned
(defc device-card [{item :item}]
  (card {:key       (:id item)
         :image-url (cond (and (:photo item) (not= (:photo item) "")) (:photo item)
                          :else  "image/no-image.png")

         :content   [:div
                     [:span {:style style/card-title}
                      (str (:brand item) " " (:model-name item))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:serial-number item) " - " (:color item)) [:br]
                      (str "Date: " (:date item))]]}))


;Card to show Person
(defc person-card [{person :person}]
  (card {:key       (:id person)
         :image-url (cond (and (:image person) (not= (:image person) "")) (:image person)
                          :else (cond (= (:sex person) "f") "image/person-f-placeholder.png"
                                      (= (:sex person) "m") "image/person-m-placeholder.png"))
         :content   [:div
                     [:span {:style style/card-title}
                      (str (:fname person) " " (:lname person))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:type person) " - " (:group person)) [:br]
                      (str "Date: " (:date person))]]}))
