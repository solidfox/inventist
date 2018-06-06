(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]))

(def field-col-width "10rem")

;Breadcrumb
(defc breadcrumb [{type :type
                   item :item}]
  [:div {:style {:margin "0.75rem 2.5rem" :cursor "pointer"}}
   (cond
     (= type "back")
     [:span (s-general/button {:color color/white
                               :icon  "fas fa-arrow-circle-left"
                               :title "Go Back"})]
     (= type "dashboard")
     [:span "Hi " (:first-name item) " " (:last-name item) ", welcome to Inventist."]
     :else
     [:span
      ;2-Main Navigation
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}}
       (cond (= type "people") "People"
             (= type "inventory") "Inventory"
             (= type "contractors") "Contractors")]
      [:span {:style {:opacity "0.75"
                      :margin  "0 0.5rem 0 0"}} "/"]
      ;3-Current Item
      (when item [:span
                  (cond (= type "people") [:span (str (:first-name item) " " (:last-name item))]
                        (= type "inventory") [:span
                                              (str (:brand item) " " (:model-name item))]
                        (= type "contractors") [:span (str (:name item))])])])])

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
(defc detail-header [{edit-mode     :edit-mode
                      on-change     :on-change
                      image         :image
                      heading       :heading
                      sub-heading-1 :sub-heading-1
                      sub-heading-2 :sub-heading-2}]
  [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
         :id    "header"}
   [:div [:img {:src   (cond (and image (not= image "")) image
                             :else "/image/no-image.png")
                :style {:width        "6rem"
                        :height       "6rem"
                        :borderRadius "1rem"
                        :object-fit   "cover" :backgroundColor color/grey-light}}]]
   [:div {:style {:margin "0 0 0 1rem"}}
    [:span {:style style/header-title}
     (cond (= edit-mode true)
           (s-general/input-field {:value       heading
                                   :placeholder "Name"
                                   :on-change   ""
                                   :style       {:height         "3rem"
                                                 :font-size      "2rem"
                                                 :color          color/black
                                                 :font-weight    "300"
                                                 :minWidth       "40rem"
                                                 :text-transform "capitalize"}})
           :else heading)]
    [:br]
    [:span {:style {:font-weight    "400"
                    :color          color/grey-blue
                    :text-transform "capitalize"}}
     (cond (= edit-mode true)
           (s-general/input-field {:value       sub-heading-1
                                   :placeholder "Type"
                                   :on-change   ""})
           :else
           sub-heading-1 [:br] sub-heading-2)]]])


;Information Section
(defc section-information [{title     :title
                            fields    :fields
                            edit-mode :edit-mode}]
  [:div {:style {:margin         "1rem 2.5rem 1rem"
                 :display        "flex"
                 :flex-direction "row"}
         :id    "information"}
   (s-general/section-left)
   [:div {:style {:margin         "0 0 0 1rem"
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    title                                                   ;Section Title

    [:div {:style {:display               "grid"
                   :grid-template-columns (str field-col-width " 1fr")
                   :align-items           "start"
                   :text-align            "left"}}
     (->> fields
          (remove nil?)
          (map (fn [field]
                 [
                  [:div {:style {:margin "0.25rem 0"
                                 :color  color/grey-blue}} (:label field)]
                  [:div {:style {:margin "0.25rem 0"
                                 :color  color/grey-dark}}
                   (if (and (= edit-mode true) (= (:editable field) true))
                     (s-general/text-area {:value    (:value field)
                                           :maxWidth "30rem"
                                           :minWidth "20rem"})
                     [:span (:value field) " " (:side-value field)])]])))]

    (s-general/section-divider)]])

(defc card
  [{id        :id
    style     :style
    key       :key
    image-url :image-url
    content   :content
    on-click  :on-click}]
  [:div {:key      key
         :id       id
         :on-click on-click
         :style    (merge style/card
                          style)}

   (cond (not= image-url nil) [:div [:img {:src   image-url
                                           :class (style/card-image)}]])
   [:div {:class "card-content"}
    content]])

;Card to show devices assigned
(defc device-card [{item     :item
                    on-click :on-click}]
  (card {:key       (:id item)
         :on-click  on-click
         :image-url (cond (and (:photo item) (not= (:photo item) "")) (:photo item)
                          :else "/image/no-image.png")
         :content   [:div
                     [:span {:style style/card-title}
                      (str (:brand item) " " (:model-name item))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:serial-number item) " - " (:color item)) [:br]]]}))
;(str "Date: " (:date item))]]}))


;Card to show Person
(defc person-card [{user     :user
                    on-click :on-click}]
  (card {:key       (:id user)
         :on-click  on-click
         :image-url (cond (:image user) (:image user)
                          :else
                          (when (:gender user) (cond (= (:gender user) "f") "/image/person-f-placeholder.png"
                                                     :else "/image/person-m-placeholder.png")))
         :content   [:div
                     [:span {:style style/card-title}
                      (str (:first-name user) " " (:last-name user))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:occupation user) " - ")
                      (for [group (:groups user)]
                        [:span {:key (:id group)}
                         (str (:name group) " ")])]]}))
;[:br]
;(str "Date: " (:date person))]]}))
