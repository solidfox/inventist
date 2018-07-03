(ns view-people-overview.component
  (:require [rum.core :refer [defc with-key] :as rum]
            [symbols.overview :refer [scrollable search-toolbar search-header person-list-card]]
            [view-people-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-people-overview.event :as event]
            [cljs-react-material-ui.core :as ui]
            [symbols.general :as s-general]))

(defc people-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (core/get-people state)
        filtered-people (core/filtered-people state)
        n-results (count filtered-people)
        search-terms (:search-terms state)]
    (scrollable
      {:floating-search
       (search-toolbar
         {:search-field-value (core/get-free-text-search state)
          :total-results      n-results
          :on-change          (fn [e]
                                (trigger-event
                                  (rem/create-event {:name :search-string-changed
                                                     :data {:new-value (o/oget e [:target :value])}})))})
       :content
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
        (cond (not= n-results 0)
              nil
              (:fetching-people-list state)
              (s-general/full-view-loading "people")
              :else
              [:div {:style {:width            "100%"
                             :height           "100%"
                             :color            color/grey-blue
                             :background-color color/transparent
                             :text-align       "left"
                             :margin           "2rem"}}
               "No matches found!"])]

       :floating-header
       (search-header "People")})))


