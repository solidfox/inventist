(ns view-people-overview.component
  (:require [rum.core :refer [defc with-key] :as rum]
            [symbols.overview :refer [scrollable search-toolbar second-column-header person-list-card]]
            [view-people-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-people-overview.event :as event]
            [cljs-react-material-ui.icons :as icon]
            [symbols.general :as s-general]
            [util.inventory.core :as util]))

(defc people-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people          (core/get-people state)
        filtered-people (core/filtered-people state)
        n-results       (count filtered-people)
        search-terms    (:search-terms state)]
    (scrollable
      {:floating-header
       [(second-column-header "People")
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
                                      :message    "Error while fetching people from server."
                                      :text-color color/shaded-context-primary-text
                                      :actions    (s-general/button {:text     "Retry"
                                                                     :on-click (fn [] (trigger-event (rem/create-event {:name :retry-fetching-data})))})})
         :else
         [:div {:style {:height           "100%"
                        :background-color color/transparent
                        :padding          "0.25rem"}}
          (let [person-selected-event (fn [person] (trigger-event (rem/create-event {:name :person-selected
                                                                                     :data {:person person}})))]
            (->> people
                 (map
                   (fn [person]
                     [:div {:key      (:id person)
                            :on-click (fn [] (person-selected-event person))}
                      (person-list-card {:person person
                                         :hidden (not (core/person-matches person search-terms))})]))))
          (when (= n-results 0)
            [:div {:style {:width            "100%"
                           :height           "100%"
                           :color            color/grey-blue
                           :background-color color/transparent
                           :text-align       "left"
                           :margin           "2rem"}}
             "No matches found!"])])})))


