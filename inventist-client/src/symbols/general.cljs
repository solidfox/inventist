(ns symbols.general
  (:require
    [rum.core :refer [defc]]
    [cljs-time.format :as time-format]
    [cljs-time.core :as time-core]
    [symbols.color :as color]
    [symbols.style :as style]
    [util.inventory.core :as inventory]
    [cljss.core :as cljss])
  (:require-macros
    [cljss.core :refer [defstyles]]))

;Returns brand and model icon together
(defc device-icon-set [{item :item}]
  [:span
   [:i {:class (:brand (inventory/inventory-icon item))}] " "
   [:i {:class (:model (inventory/inventory-icon item))}]])

(defn time-format-object [time]
  (time-format/parse (time-format/formatters :date-time-no-ms) time))

(defn days-to-expiry [time]
  (cond
    (> (time-format-object time) (time-core/now))
    (time-core/in-days (time-core/interval (time-core/now) (time-format-object time)))
    ;To get inverse value
    ;(> (time-format-object time) (time-core/now))
    ;(time-core/in-days (time-core/interval (time-format-object time) (time-core/now)))
    :else "0"))

(defn time-format-string [{time   :time
                           format :format}]
  (time-format/unparse (time-format/formatter (or format "yyyy-MM-dd hh:mm")) (time-format-object time)))


;button general
(defc button [{text     :text
               icon     :icon
               color    :color
               title    :title
               style    :style
               on-click :on-click}]
  (cond (not text)
        [:div {:on-click on-click
               :style (merge {:margin    "0.5rem"
                              :color     (or color color/grey-dark)
                              :font-size "1.5rem"
                              :cursor    "pointer"}
                             style)}

         [:i {:class icon :title title}]]
        :else
        [:div {:style style
               :on-click on-click
               :class (style/button {:bg-color   (or color color/grey-dark)
                                     :text-color (cond (= color color/white) color/grey-dark
                                                       (= color "white") color/grey-dark
                                                       (= color "#ffffff") color/grey-dark
                                                       (= color color/transparent) color/grey-dark
                                                       :else color/white)})}
         (cond (not= icon nil)
               [:span {:style {:margin "0 0.5rem 0 0"}} [:i {:class icon}]])
         [:span text]]))


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


;Division Title - Title
(defc division-title [{title :title}]

  [:div {:style {:margin        "2.5rem 0 0 2.5rem"
                 :border-bottom (str "1px solid " color/silver)}
         :id    "header"}
   [:span {:style {:font-size      "1.5rem"
                   :color          color/grey-normal
                   :font-weight    "300"
                   :text-transform "capitalize"}}
    title]])

;DASHBOARD STAT CARD
(defc stat-card [{value   :value
                  text    :text
                  subtext :subtext}]
  [:div {:style style/item-stats}
   [:div {:style (merge style/card-subtitle
                        {:text-align    "left"
                         :margin-top    "auto"
                         :margin-bottom "auto"})}
    text [:br]
    subtext]
   [:div {:style (merge style/header-title
                        {:text-align    "right"
                         :margin-top    "auto"
                         :margin-bottom "auto"
                         :margin-left   "1rem"})}
    value]])

;Input Field
(defc input-field [{placeholder :placeholder
                    value       :value
                    width       :width
                    minWidth    :minWidth
                    maxWidth    :maxWidth
                    type        :type
                    disabled    :disabled
                    required    :required
                    style       :style
                    on-change   :on-change}]
  [:input {:style       (merge {:font-size       "1rem"
                                :minWidth        minWidth
                                :maxWidth        maxWidth
                                :width           (or width "100%")
                                :height          "2rem"
                                :backgroundColor (cond (= disabled true) color/highlight
                                                       :esle color/white)
                                :box-shadow      "0px 0px 2px rgba(0,0,0,0.5) inset"
                                :borderRadius    "5px"
                                :border          0
                                :padding-left    "0.5rem"}
                               style)
           :type        (or type "text")
           :disabled    (or disabled false)
           :required    (or required true)
           :placeholder (or placeholder "Enter here...")
           :value       value
           :on-change   on-change}])

(defc text-area [{placeholder :placeholder
                  value       :value
                  width       :width
                  maxWidth    :maxWidth
                  type        :type
                  disabled    :disabled
                  required    :required
                  on-change   :on-change}]
  [:textarea {:style       {:font-size       "1rem"
                            :width           (or width "100%")
                            :maxWidth        maxWidth
                            :height          "1.5rem"
                            :backgroundColor (cond (= disabled true) color/highlight
                                                   :esle color/white)
                            :box-shadow      "0px 0px 2px rgba(0,0,0,0.5) inset"
                            :borderRadius    "5px"
                            :border          0
                            :padding-left    "0.5rem"
                            :paddingTop      "0.5rem"}
              :type        (or type "text")
              :disabled    (or disabled false)
              :required    (or required true)
              :placeholder (or placeholder "Enter here...")
              :value       value
              :on-change   (fn [e] (.. e -target -value))}])

;INPUT Section
(defc input-section [{field       :field
                      text        :text
                      placeholder :placeholder
                      value       :value
                      width       :width
                      type        :type
                      color       :color
                      icon        :icon
                      style       :style
                      disabled    :disabled
                      required    :required
                      on-change   :on-change}]
  [:div {:style {:width  (or width "100%")
                 :margin "0 0.5rem 0.5rem 0"}}
   [:div {:style {:line-height "1.5rem"
                  :color       color/theme
                  :margin      "0"}}
    (or field "Field")
    (cond (= required false) [:span {:style {:color      color/grey-blue
                                             :font-size  "0.9rem"
                                             :margin     "0"
                                             :font-style "italic"}}
                              " (Optional)"])]

   (cond (= type "textarea")
         (text-area {:width       width
                     :value       value
                     :placeholder placeholder
                     :type        type
                     :disabled    disabled
                     :required    required
                     :on-change   on-change})

         (= type "button")
         (button {:text  value
                  :color color
                  :icon  icon
                  :style style})

         :else (input-field {:width       width
                             :value       value
                             :placeholder placeholder
                             :type        type
                             :disabled    disabled
                             :required    required
                             :on-change   on-change}))


   (cond (not= text nil) [:div {:style {:color      color/grey-blue
                                        :font-size  "0.9rem"
                                        :margin     "0"
                                        :font-style "italic"}}
                          text])])

(defc timeline-item [{icon    :icon
                      title   :title
                      content :content}]
  [:div {:style {:border-left   (str "0.1rem solid " color/grey-light)
                 :padding-top   "1rem"
                 :padding-left  "1.5rem"
                 :width         "100%"
                 :min-height    "3rem"
                 :margin-bottom "1rem"
                 :position      "relative"}}
   [:div {:style {:position       "absolute"
                  :width          "100%"
                  :top            "-1rem"
                  :left           "-1rem"
                  :display        "flex"
                  :flex-direction "row"
                  :align-items    "center"}}
    icon
    [:div {:style {:margin      "0 0 0 .5rem"
                   :font-size   "1rem"
                   :font-weight "normal"
                   :color       color/grey-dark}} title]]
   [:span {:style {:color color/grey-blue}} content]])


(defc timeline [{timeline-items :timeline-items
                 enable-comment :enable-comment}]
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
    [:div {:style {:margin-left    "-1.5rem"
                   :padding-top    "1rem"
                   :text-transform "capitalize"}}
     timeline-items]]])

(defc circle-icon [{icon  :icon
                    color :color}]
  [:div {:style {:maxWidth         "2rem" :maxHeight "2rem"
                 :minWidth         "2rem" :minHeight "2rem"
                 :background-color color
                 :borderRadius     "1rem"
                 :display          "flex"
                 :justify-content  "center"
                 :text-align       "center"}}
   [:i {:class icon
        :style {:font-size  "1rem"
                :align-self "center"
                :color      color/white}}]])
