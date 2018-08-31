(ns view-people-overview.component
  (:require [rum.core :refer [defc with-key] :as rum]
            [symbols.overview :refer [search-toolbar second-column-header list-card]]
            [view-people-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-people-overview.event :as event]
            [cljs-react-material-ui.icons :as icon]
            [symbols.general :as s-general]
            [symbols.style :as style]
            [clojure.string :as str]
            [util.inventory.core :as util]))


(defc person-list-card < rum/static
  [{:keys [person
           selected
           hidden]}]
  (list-card {:selected selected
              :hidden   hidden}
             [:div {:key 1
                    :class (style/list-item-left-column)}
              (cond (and (:image person) (not= (:image person) ""))
                    [:img {:src   (:image person)
                           :class (style/card-image)}]
                    :else
                    [:span {:style {:width            "3rem"
                                    :height           "3rem"
                                    :background-color color/shaded-context-secondary-text
                                    :border-radius    "0.25rem"
                                    :display          "grid"
                                    :font-size        "1.5rem"
                                    :align-items      "center"
                                    :text-align       "center"
                                    :color            color/shaded-context-highlight-bg}}
                     (str (subs (or (:first-name person) "") 0 1) (subs (or (:last-name person) "") 0 1))])]
             [:div {:key 2}
              [:span {:style style/card-title}
               (str (:first-name person) " " (:last-name person))] [:br]
              [:span {:style style/card-subtitle}
               (str (:occupation person) " - " (str/join ", " (for [group (:groups person)] (:name group))))] [:br]
              [:span {:style (merge style/card-title
                                    {:display   "flex"
                                     :flex-wrap "wrap"})}
               (for [item (:inventory person)]
                 [:div {:key   (:id item)
                        :style {:margin    "0.1rem 0.5rem 0rem 0"
                                :font-size "0.75rem"}}
                  (s-general/device-icon-set {:item item})])]]))

(defc people-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (core/get-people state)
        filtered-people (core/filtered-people state)
        n-results (count filtered-people)
        search-terms (:search-terms state)]
    (s-general/scrollable
      {:floating-header
       [:div
        (second-column-header "People")
        (search-toolbar
          {:search-field-value (core/get-free-text-search state)
           :total-results      n-results
           :on-change          (fn [e]
                                 (trigger-event
                                   (rem/create-event {:name :search-string-changed
                                                      :data {:new-value (o/oget e [:target :value])}})))})]

       :content
       (cond
         (:fetching-people-list state)
         (s-general/centered-loading-indicator "people")
         (core/get-people-list-failed? state)
         (s-general/centered-message {:icon       (icon/alert-error-outline {:color color/shaded-context-primary-text
                                                                             :style {:height "3rem"
                                                                                     :width  "3rem"}})
                                      :message    "Error while fetching people-list from server."
                                      :text-color color/shaded-context-primary-text
                                      :actions    (s-general/button {:text     "Retry"
                                                                     :bg-color color/shaded-context-highlight-bg
                                                                     :on-click (fn [] (trigger-event (rem/create-event {:name :retry-fetching-data})))})})
         :else
         [:div {:style {:height           "auto"
                        :background-color color/transparent
                        :padding          "0.25rem"}}
          (let [person-selected-event (fn [person] (trigger-event (rem/create-event {:name :selected-person
                                                                                     :data {:person person}})))]
            (->> people
                 (map
                   (fn [person]
                     [:div {:key      (:id person)
                            :on-click (fn [] (person-selected-event person))}
                      (person-list-card {:person   person
                                         :selected (= (:selected-person-id state) (:id person))
                                         :hidden   (not (core/person-matches person search-terms))})]))))
          (when (= n-results 0)
            [:div {:style {:width            "100%"
                           :height           "100%"
                           :color            color/shaded-context-primary-text
                           :background-color color/transparent
                           :text-align       "center"
                           :margin-top       "2rem"}}
             "No matches found!"])])})))


