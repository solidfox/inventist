(ns symbols.detailview
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.color :as color]
            [symbols.style :as style]
            [symbols.branding :as branding]
            [rum.core :as rum]
            [util.inventory.core :as util]
            [clojure.string :refer [lower-case upper-case]]
            [symbols.mixin :refer [hovered-mixin]]))

(def field-col-width "10rem")

(rum/defcs section-button < (hovered-mixin :hovered)
  [{:keys [hovered]}
   {:keys [key content tooltip-text tooltip-position tooltip-align on-click scroll-to]}]
  [:div {:key      key
         :on-click on-click
         :style    {:position  "relative"
                    :margin    "0 0.5rem"
                    :font-size "1.25rem"
                    :cursor    "pointer"
                    :color     color/light-context-secondary-text}}
   [:a {:href scroll-to} content]
   (when hovered
     (s-general/tooltip {:tooltip-text tooltip-text
                         :position     tooltip-position
                         :alignment    tooltip-align}))])

(defc detail-drag-zone []
  [:div {:style {:position         "absolute"
                 :width            "calc(100% - 48px)"                ;(str (- (js/parseInt viewport-width) 690) "px")
                 :height           "calc(100% - 96px)"
                 :display          "grid"
                 :text-align       "center"
                 :align-self       "center"
                 :align-items      "center"
                 :color            color/shaded-context-primary-text
                 :background-color color/shaded-context-background
                 :opacity          "0.95"
                 :border-radius    "0.25rem"
                 :border           (str "2px dashed " color/shaded-context-secondary-text)
                 :z-index          100
                 :cursor           "pointer"}}
   [:div
    [:span {:style {:font-size "3rem"}} [:i {:class "fas fa-box-open"}]]
    [:br] "Drop here to assign value."]])

;Information Section
(defc section-information [{on-change      :on-change
                            image          :image
                            heading        :heading
                            actions        :actions
                            fields         :fields
                            edit-mode      :edit-mode
                            enable-edit    :enable-edit
                            viewport-width :viewport-width}]

  (cond (< viewport-width style/viewport-mobile)
        [:div {:id    "info"
               :style {:display            "grid"
                       :height             "auto"
                       :width              "100%"
                       :grid-template-rows "6rem auto 1fr"
                       :grid-gap           "0.5rem"}}

         [:div {:style {:display         "flex"
                        :flex-direction  "row"
                        :justify-content "center"}}

          image]                                            ;Image/Initials/Icon

         [:div {:style (merge style/header-title
                              {:text-align "center"})}
          (cond (and (= edit-mode true) (= enable-edit true))
                (s-general/text-area {:value       (or heading "")
                                      :placeholder "Name"
                                      :on-change   ""
                                      :style       {:height         "3rem"
                                                    :font-size      "2rem"
                                                    :color          color/light-context-title-text
                                                    :font-weight    "300"
                                                    :minWidth       "40rem"
                                                    :text-transform "capitalize"}})
                :else heading)]
         [:div {:style {:display         "flex"
                        :flex-direction  "row"
                        :justify-content "center"}}
          (for [action actions]
            (section-button {:key              (:title action)
                             :content          [:i {:class (:icon action)}]
                             :tooltip-text     (:title action)
                             :tooltip-position "bottom"
                             :tooltip-align    "start"
                             :on-click         (:on-click action)
                             :scroll-to        (:scroll-to action)}))]
         [:div {:style {:margin         "0.25rem 0 0"
                        :display        "flex"
                        :flex-direction "column"}
                :id    "information"}

          [:div {:style {:display        "flex"
                         :flex-direction "column"
                         :align-items    "start"
                         :text-align     "left"
                         :overflow-x     "scroll"}}
           (->> fields
                (remove nil?)
                (map (fn [field]
                       [:div {:style {:margin-right "0.5rem"}}
                        [:div {:style {:margin "0.75rem 0 0"
                                       :color  color/light-context-secondary-text}} (:label field)]
                        [:div {:style {:margin     "0.25rem 0 0"
                                       :word-break "break-all"
                                       :color      color/light-context-primary-text}}
                         (if (and (= edit-mode true) (= (:editable field) true))
                           (s-general/text-area {:value    (:value field)
                                                 :maxWidth "30rem"
                                                 :minWidth "20rem"})
                           [:span (:value field) " " (:side-value field)])]])))]
          (s-general/section-divider)]]


        :else                                               ;Desktop Viewport
        [:div {:id    "info"
               :style {:display               "grid"
                       :width                 "100%"
                       :grid-template-columns "6rem auto"}}
         ;Image / Left Column
         image

         ;Right Column
         [:div {:style {:display        "flex"
                        :flex-direction "column"
                        :margin-left    "1.5rem"
                        :width          "calc(100% - 1.5rem"}}
          ;Header
          [:div {:style {:display         "flex"
                         :flex-direction  "row"
                         :justify-content "space-between"}}
           ;Heading
           [:span {:style style/header-title}
            (cond (and (= edit-mode true) (= enable-edit true))
                  (s-general/input-field {:value       (or heading "")
                                          :placeholder "Name"
                                          :on-change   ""
                                          :style       {:height         "3rem"
                                                        :font-size      "2rem"
                                                        :color          color/light-context-title-text
                                                        :font-weight    "300"
                                                        :minWidth       "40rem"
                                                        :text-transform "capitalize"}})
                  :else heading)]
           ;Actions
           [:div {:style {:display        "flex"
                          :flex-direction "row"}}
            (for [action actions]
              (section-button {:key              (:title action)
                               :content          [:i {:class (:icon action)}]
                               :tooltip-text     (:title action)
                               :tooltip-position "bottom"
                               :tooltip-align    "end"
                               :on-click         (:on-click action)}))]]


          ;Information Details
          [:div {:style {:margin         "0.25rem 0 0"
                         :display        "flex"
                         :flex-direction "column"}
                 :id    "information"}

           [:div {:style {:display               "grid"
                          :grid-template-columns (str field-col-width " 1fr")
                          :align-items           "start"
                          :text-align            "left"}}
            (->> fields
                 (remove nil?)
                 (map (fn [field]
                        [
                         [:div {:style {:margin "0.75rem 0 0"
                                        :color  color/light-context-secondary-text}} (:label field)]
                         [:div {:style {:margin "0.75rem 0 0"
                                        :color  color/light-context-primary-text}}
                          (if (and (= edit-mode true) (= (:editable field) true))
                            (s-general/text-area {:value    (:value field)
                                                  :maxWidth "30rem"
                                                  :minWidth "20rem"})
                            [:span (:value field) " " (:side-value field)])]])))]
           (s-general/section-divider)]]]))

(defc card
  [{id       :id
    style    :style
    key      :key
    image    :image
    content  :content
    on-click :on-click}]
  [:div {:key       key
         :id        id
         :class     (style/card)
         :on-click  on-click
         :draggable true}
   image
   content])

(defc input-card [{id          :id
                   placeholder :placeholder
                   value       :value
                   on-change   :on-change
                   on-enter    :on-enter
                   on-click    :on-click}]
  [:div {:style {:animation style/bounce-animation}}
   (card {:id      id
          :content [:div {:style {:display         "flex"
                                  :width           "18.5rem"
                                  :max-height      "2.5rem"
                                  :flex-wrap       "wrap"
                                  :justify-content "space-between"}}
                    [:div
                     (s-general/input-field {:placeholder placeholder
                                             :width       "15rem"
                                             :height      "2.5rem"
                                             :value       value
                                             :on-change   on-change
                                             :on-enter    on-enter})]
                    [:div {:on-click on-click
                           :style    {:width              "2.5rem"
                                      :height             "2.5rem"
                                      :border-radius      "0.25rem"
                                      :display            "grid"
                                      :grid-template-rows "1.5rem 1rem"
                                      :justify-content    "center"
                                      :color              color/shaded-context-background
                                      :background-color   color/shaded-context-secondary-text}}
                     [:span {:style {:font-size  "1rem"
                                     :text-align "center"
                                     :align-self "end"}}
                      [:i {:class "fas fa-plus-circle"}]]
                     [:span {:style {:font-size  "0.75rem"
                                     :align-self "center"}} "Add"]]]})])

;404 Styled Placholder
(defc no-selection-view [{viewport-width  :viewport-width
                          viewport-height :viewport-height
                          image-url       :image-url
                          heading         :heading
                          sub-heading     :sub-heading
                          learn-more-link :learn-more-link}]
  [:div {:id    "no-selection-view"
         :style {:height             (or viewport-height "auto")
                 :width              "auto"
                 :margin             "2rem"
                 :display            "grid"
                 :grid-template-rows "auto 1px auto"
                 :grid-gap           "2rem"
                 :text-align         "center"}}

   [:div {:style {:align-self "end"}}
    [:img {:src    (or image-url branding/logo-default-url)
           :height "250rem"}]]

   ;Divider
   ;[:div {:style {:background-color color/light-context-highlight-bg}}]

   ;Error text
   [:div {:style {:align-self "start"
                  :color      color/light-context-secondary-text}}
    [:h3 {:style {:color       color/light-context-primary-text
                  :font-weight "500"}}
     (upper-case (or heading "Nothing to show here"))]
    [:span (or sub-heading "Select something to show the details here.")]
    [:br] [:br]
    (when learn-more-link [:a {:href learn-more-link} "Learn More."])]])
