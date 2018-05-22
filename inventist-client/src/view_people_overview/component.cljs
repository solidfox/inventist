(ns view-people-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :refer [scrollable search-toolbar footer person-list-card]]
            [view-people-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-people-overview.event :as event]
            [cljs-react-material-ui.core :as ui]))

(defc people-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people         (core/filtered-people state)
        limited-people (take 75 people)]
    (scrollable
      {:floating-header
       (search-toolbar
         {:search-field-value (core/get-free-text-search state)
          :shown-results      (count limited-people)
          :total-results      (count people)
          :on-change          (fn [e]
                                (trigger-event
                                  (rem/create-event {:name :search-string-changed
                                                     :data {:new-value (o/oget e [:target :value])}})))})
       :content
       [:div {:style {:background-color color/grey-light}}
        (for [person limited-people]
          (person-list-card {:person    person
                             :on-select (fn [] (trigger-event
                                                 (rem/create-event
                                                   {:name :person-selected
                                                    :data {:person person}})))}))]
       :floating-footer
       (footer)})))
;
;(defc person-selection-field < (remodular.core/modular-component event/handle-event)
;  [{{state :state} :input
;    trigger-event  :trigger-event}]
;  (let [people         (core/get-people state)]))

