(ns symbols.general
  (:require
    [rum.core :refer [defc]]
    [cljs-time.format :as time-format]
    [cljs-time.core :as time-core]
    [symbols.color :as color]
    [symbols.style :as style]
    [util.inventory.core :as inventory]
    [cljss.core :as cljss]
    [oops.core :as oops]
    [cljs-react-material-ui.rum :as ui])
  (:require-macros
    [cljss.core :refer [defstyles]]))

;Returns brand and model icon together
(defc device-icon-set [{item :item}]
  (let [icon-map (inventory/inventory-icon item)]
    [:span
     ;(:brand icon-map) " "
     (:model icon-map)]))

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

(defc centered-message [{:keys [icon message text-color actions]}]
  [:div {:style {:height           "100%"
                 :color            text-color
                 :display          "flex"
                 :background-color color/shaded-context-background
                 :box-shadow       (str "0 0 0.25rem " color/shadow)
                 :flex-direction   "column"
                 :align-items      "center"
                 :justify-content  "center"}}
   icon
   [:div {:style {:text-align "center"
                  :width      "10rem"}} message]
   [:div actions]])

(defc centered-loading-indicator [content-description]
  [:div {:style {:height          "100%"
                 :display         "flex"
                 :flex-direction  "column"
                 :align-items     "center"
                 :justify-content "center"}}
   (ui/circular-progress {:size 50})
   [:div (str "Loading " content-description "...")]])

;Tooltip
(defc tooltip [{tooltip-text :tooltip-text                  ;Text-string
                position     :position                      ;top, right, bottom, left
                alignment    :alignment                     ;start, center, end
                color-bg?    :color-bg
                color?       :color}]
  (let [color-bg (or color-bg? color/shaded-context-background)
        color (or color? color/shaded-context-primary-text)
        transparent " transparent "]
    [:div {:style (merge (cond (= position "top") {:bottom "120%"}
                               (= position "right") {:left "130%" :top "0.125rem"}
                               (= position "left") {:right "130%" :top "0.125rem"}
                               :else {:top "120%"})         ;bottom
                         (cond (and (not= position "right") (not= position "left"))
                               (cond (= alignment "end") {:right 0}
                                     (= alignment "start") {:left 0}
                                     :else {:left "-100%"})) ;center
                         {:position         "absolute"
                          :min-width        "2rem"
                          :width            "max-content"
                          :max-width        "5rem"
                          :background-color color-bg
                          :color            color
                          :font-size        "0.75rem"
                          :text-align       "center"
                          :padding          "0.25rem 0.75rem"
                          :border-radius    "0.25rem"
                          :z-index          40})}
     ;Arrow
     [:div {:style (merge (cond (= position "top")
                                {:top          "100%"
                                 :border-color (str color-bg transparent transparent transparent)}
                                (= position "right")
                                {:right        "100%"
                                 :top          "calc(50% - 0.25rem)"
                                 :border-color (str transparent color-bg transparent transparent)}
                                (= position "left")
                                {:left         "calc(100% + 0.25rem)"
                                 :top          "calc(50% - 0.25rem)"
                                 :border-color (str transparent transparent transparent color-bg)}
                                :else
                                {:bottom       "100%"       ;bottom
                                 :border-color (str transparent transparent color-bg transparent)})
                          (cond (and (not= position "right") (not= position "left"))
                                (cond (= alignment "end") {:right "0.25rem"}
                                      (= alignment "start") {:left "0.5rem"}
                                      :else {:left "50%"})) ;center
                          {:position     "absolute"
                           :margin-left  "-0.25rem"
                           :border-width "0.25rem"
                           :border-style "solid"})}]


     [:span tooltip-text]]))


;button general
(defc button [{text     :text
               icon     :icon
               color    :color
               bg-color :bg-color
               title    :title
               style    :style
               on-click :on-click}]
  (cond (not text)
        [:div {:on-click on-click
               :style    (merge {:margin    "0.5rem"
                                 :color     (or color color/grey-dark)
                                 :font-size "1.5rem"
                                 :cursor    "pointer"}
                                style)}

         [:i {:class icon :title title}]]
        :else
        [:div {:style    style
               :on-click on-click
               :class    (style/button {:bg-color   (or bg-color color/grey-dark)
                                        :text-color color})}
         ;(cond (= color color/white) color/grey-dark
         ;      (= color "white") color/grey-dark
         ;      (= color "#ffffff") color/grey-dark
         ;      (= color color/transparent) color/grey-dark
         ;      :else color)})}
         (cond (not= icon nil)
               [:span {:style {:margin "0 0.5rem 0 0"}} [:i {:class icon}]])
         [:span text]]))


;Edit/Comment button
(defc section-title-button [{icon     :icon
                             text     :text
                             color    :color
                             on-click :on-click}]
  [:span {:on-click on-click
          :style    {:color     (or color color/light-context-secondary-text)
                     :font-size "1rem"
                     :cursor    "pointer"}}

   [:i {:class icon}] " " text])

;Title for sections
(defc section-title [{title    :title
                      buttons  :buttons
                      viewport :viewport}]

  [:div {:id    "header"
         :style (merge {:font-size "1.25rem"
                        :color     color/light-context-title-text
                        :display   "flex"}
                       (cond (= viewport "mobile")
                             {:flex-direction "column"
                              :align-items    "start"}
                             :else {:flex-direction  "row"
                                    :align-items     "center"
                                    :justify-content "space-between"}))}
   title buttons])

;Empty div on left of section
(defc section-left []
  [:div {:style {:minWidth   "6rem"
                 :text-align "right"
                 :margin     0}}])

;Divider after sections
(defc section-divider []
  [:div {:id    "divider"
         :style {:margin          "1rem 0"
                 :backgroundColor color/light-context-highlight-bg
                 :width           "100%"
                 :height          "1px"}}])


;Division Title - Title
(defc division-title [{title :title}]

  [:div {:style {:margin        "2.5rem 0 0 1.5rem"
                 :border-bottom (str "1px solid " color/light-context-highlight-bg)}
         :id    "header"}
   [:span {:style {:font-size      "1.5rem"
                   :color          color/light-context-primary-text
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
(defc input-field [{id          :id
                    placeholder :placeholder
                    value       :value
                    height      :height
                    width       :width
                    minWidth    :minWidth
                    maxWidth    :maxWidth
                    type        :type
                    disabled    :disabled
                    required    :required
                    style       :style
                    on-change   :on-change
                    on-enter    :on-enter}]
  [:input {:id           id
           :style        (merge {:font-size       "1rem"
                                 :minWidth        minWidth
                                 :maxWidth        maxWidth
                                 :width           (or width "100%")
                                 :height          (or height "2rem")
                                 :color           color/light-context-secondary-text
                                 :backgroundColor (cond (= disabled true) color/highlight
                                                        :esle color/light-context-background)
                                 :box-shadow      "0px 0px 2px rgba(0,0,0,0.5) inset"
                                 :borderRadius    "5px"
                                 :border          0
                                 :padding-left    "0.5rem"}
                                style)
           :type         (or type "text")
           :disabled     (or disabled false)
           :required     (or required true)
           :placeholder  (or placeholder "Enter here...")
           :value        (or value "")
           :on-change    on-change
           :on-key-press (fn [e]
                           (when (= (oops/oget e [:key]) "Enter")
                             (on-enter e)))}])

(defc text-area [{id          :id
                  placeholder :placeholder
                  value       :value
                  width       :width
                  maxWidth    :maxWidth
                  type        :type
                  disabled    :disabled
                  required    :required
                  on-change   :on-change}]
  [:textarea {:id          id
              :style       {:font-size       "1rem"
                            :width           (or width "100%")
                            :maxWidth        maxWidth
                            :height          "1.5rem"
                            :color           color/light-context-secondary-text
                            :backgroundColor (cond (= disabled true) color/highlight
                                                   :esle color/light-context-background)
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
              :on-change   on-change}])

;UPLOAD BUTTON
(defc upload-button [{id        :id
                      width     :width
                      minWidth  :minWidth
                      maxWidth  :maxWidth
                      disabled  :disabled
                      required  :required
                      style     :style
                      on-change :on-change}]
  [:input {:id        id
           :style     (merge {:font-size "1rem"
                              :minWidth  minWidth
                              :maxWidth  maxWidth
                              :width     (or width "100%")
                              :height    "auto"}
                             style)
           :type      "file"
           :disabled  (or disabled false)
           :required  (or required true)
           :on-change on-change}])


;INPUT Section
(defc input-section [{id          :id
                      field       :field
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
                      on-change   :on-change
                      on-click    :on-click}]
  [:div {:style {:width  (or width "100%")
                 :margin "0 0.5rem 0.5rem 0"}}
   [:div {:style {:line-height "1.5rem"
                  :color       color/shaded-context-primary-text
                  :margin      "0"}}
    (or field "Field")
    (cond (= required false) [:span {:style {:color      color/shaded-context-secondary-text
                                             :font-size  "0.9rem"
                                             :margin     "0"
                                             :font-style "italic"}}
                              " (Optional)"])]

   (cond (= type "textarea")
         (text-area {:width       width
                     :value       (or value "")
                     :placeholder placeholder
                     :type        type
                     :disabled    disabled
                     :required    required
                     :on-change   on-change})

         (= type "button")
         (button {:id       id
                  :text     value
                  :color    color
                  :icon     icon
                  :on-click on-click
                  :style    style})

         (= type "upload")
         (upload-button {:id        id
                         :style     {:color color/shaded-context-title-text}
                         :on-change on-change})

         :else (input-field {:width       width
                             :value       value
                             :placeholder placeholder
                             :type        type
                             :disabled    disabled
                             :required    required
                             :on-change   on-change}))


   (cond (not= text nil) [:div {:style {:color      color/shaded-context-secondary-text
                                        :font-size  "0.9rem"
                                        :margin     "0"
                                        :font-style "italic"}}
                          text])])

(defc timeline-item [{icon     :icon
                      title    :title
                      content  :content
                      on-click :on-click}]
  [:div {:style    {:border-left   (str "0.1rem solid " color/light-context-highlight-bg)
                    :padding-top   "1rem"
                    :padding-left  "1.5rem"
                    :width         "100%"
                    :min-height    "2.5rem"
                    :margin-bottom "0rem"
                    :position      "relative"
                    :cursor        "pointer"}
         :on-click on-click}
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
                   :color       color/light-context-primary-text}} title [:br]
     [:span {:style {:margin-top "0.25rem"
                     :font-size  "0.75rem"
                     :color      color/light-context-secondary-text}} content]]]])



(defc timeline [{timeline-items :timeline-items
                 enable-comment :enable-comment
                 viewport-width :viewport-width}]
  [:div {:style {:margin-top  "0.5rem"
                 :margin-left (cond (< viewport-width style/viewport-mobile) 0
                                    :else "7.5rem")}
         :id    "timeline"}
   [:div {:style {:margin         0
                  :display        "flex"
                  :flex-direction "column"
                  :width          "100%"}}
    (section-title {:title   "Timeline"
                    :buttons [(cond (= enable-comment true)
                                    (section-title-button {:icon     "far fa-comment"
                                                           :text     "Add Comment"
                                                           :on-click ""}))]})
    [:div {:style {:margin-top     "1rem"
                   :margin-left    (cond (< viewport-width style/viewport-mobile) 0
                                         :else "-1.5rem")
                   :padding-top    "1rem"
                   :text-transform "capitalize"}}
     timeline-items]
    (section-divider)]])

(defc circle-icon [{icon  :icon
                    color :color}]
  [:div {:style {:maxWidth         "2rem" :maxHeight "2rem"
                 :minWidth         "2rem" :minHeight "2rem"
                 :background-color (or color color/light-context-secondary-text)
                 :borderRadius     "1rem"
                 :display          "flex"
                 :justify-content  "center"
                 :text-align       "center"}}
   [:i {:class icon
        :style {:font-size  "1rem"
                :align-self "center"
                :color      color/white}}]])
