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
                                   (str " - " (:brand item) " " (:model_name item))]
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
(defc detail-header [{edit-mode     :edit-mode
                      on-change     :on-change
                      image         :image
                      heading       :heading
                      sub-heading-1 :sub-heading-1
                      sub-heading-2 :sub-heading-2}]
  [:div {:style {:margin "2.5rem 2.5rem 0" :display "flex" :flex-direction "row"}
         :id    "header"}
   [:div [:img {:src   (cond (and image (not= image "")) image
                             :else "image/no-image.png")
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


;Edit/Comment button
(defc section-title-button [{icon     :icon
                             text     :text
                             on-click :on-click}]
  [:span {:on-click on-click
          :style    {:color     color/link-active
                     :margin    "0 1rem"
                     :font-size "1rem"
                     :cursor    "pointer"}}

   [:i {:class icon}] " " text])

;Title for sections
(defc section-title [{title   :title
                      buttons :buttons}]
  [:div {:id    "header"
         :style {:font-size "1.5rem" :color color/grey-blue}}
   title
   buttons])

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
(defc section-information [{fields      :fields
                            edit-mode   :edit-mode
                            enable-edit :enable-edit}]
  [:div {:style {:margin         "1rem 2.5rem 1rem"
                 :display        "flex"
                 :flex-direction "row"}
         :id    "information"}
   (section-left)
   [:div {:style {:margin         "0 0 0 1rem"
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    (section-title {:title   "Information"
                    :buttons [(cond (= enable-edit true)
                                    (section-title-button {:icon     "far fa-edit"
                                                           :text     "Edit"
                                                           :on-click ""}))]})

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

    (section-divider)]])

;Timeline Section


(defc section-timeline [{type           :type
                         enable-comment :enable-comment
                         history        :history
                         purchase       :purchase}]
  [:div {:style {:margin         "1rem 2.5rem 1rem"
                 :display        "flex"
                 :flex-direction "row"}
         :id    "timeline"}
   (section-left)
   [:div {:style {:margin         "0 0 0 1rem"
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    (section-title {:title   "Timeline"
                    :buttons [(cond (= enable-comment true)
                                    (section-title-button {:icon     "far fa-comment"
                                                           :text     "Add Comment"
                                                           :on-click ""}))]})
    [:div {:style {:display        "flex"
                   :flex-direction "row"}}
     [:div {:style {:text-transform "capitalize"}}
      (cond
        (= type "inventory")
        [[:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
          [:div {:style {:color color/grey-blue :width field-col-width}} (s-general/time-format-string {:time (:delivery-date purchase)})]
          [:div {:style {:color color/grey-dark :margin "0 0 0 0"}}
           (s-general/input-field {:placeholder "Enter comment here..."})]]

         (for [{id      :person-id
                date    :date
                comment :comment
                fname   :fname
                lname   :lname
                type    :type
                group   :group} history]
           [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"} :key id}
            [:div {:style {:color color/grey-blue :width field-col-width}} (s-general/time-format-string {:time date})]
            [:div {:style {:color color/grey-dark :margin "0 0 0 0"}}
             [:span "Allotted to "]
             [:span {:style {:font-weight "500"}} fname " " lname]
             [:span " (" type " - " group ")"]
             [:br]
             [:span {:class "italic"} comment]]])
         [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}}
          [:div {:style {:color color/grey-blue :width field-col-width}} (s-general/time-format-string {:time (:delivery-date purchase)})]
          [:div {:style {:color color/grey-dark :margin "0 0 0 0rem"}}
           [:span "Purchased from "]
           [:span {:style {:font-weight "500"}} (:supplier purchase)]
           [:span {:style {:color color/link-active :cursor "pointer"}} " (PDF)"]]]]

        :else
        (for [item history]
          [:div {:style {:margin "0.5rem 0" :display "flex" :flex-direction "row"}
                 :key   (:inventory-id item)}
           [:div {:style {:color color/grey-blue :width field-col-width}} (s-general/time-format-string {:time (:date item)})]
           [:div {:style {:color color/grey-dark :margin "0 0 0 0rem"}}
            [:span (:comment item)]
            [:br]
            [:span (:brand item) " " (:model-name item)
             " (" (s-general/device-icon-set {:item item}) ")"]]]))]]
    (section-divider)]])

(defc card
  [{id        :id
    style     :style
    key       :key
    image-url :image-url
    content   :content}]
  [:div {:key   key
         :id    id
         :style (merge style/card
                       style)}

   (cond (not= image-url nil) [:div [:img {:src   image-url
                                           :style style/card-image}]])
   [:div {:class "card-content"}
    content]])

;Card to show devices assigned
(defc device-card [{item :item}]
  (card {:key       (:id item)
         :image-url (cond (and (:photo item) (not= (:photo item) "")) (:photo item)
                          :else "image/no-image.png")
         :content   [:div
                     [:span {:style style/card-title}
                      (str (:brand item) " " (:model_name item))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:serial_number item) " - " (:color item)) [:br]]]}))
;(str "Date: " (:date item))]]}))


;Card to show Person
(defc person-card [{user :user}]
  (card {:key       (:id user)
         :image-url (cond (:image user) (:image user)
                          :else
                          (when (:sex user) (cond (= (:sex user) "f") "image/person-f-placeholder.png"
                                                  :else "image/person-m-placeholder.png")))
         :content   [:div
                     [:span {:style style/card-title}
                      (str (:first_name user) " " (:last_name user))] [:br]
                     [:span {:style style/card-subtitle}
                      (str (:occupation user) " - ")
                      (for [group (:groups user)]
                        [:span {:key (:id group)}
                         (str (:name group) " ")])]]}))
;[:br]
;(str "Date: " (:date person))]]}))
