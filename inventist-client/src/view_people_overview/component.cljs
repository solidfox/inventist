(ns view-people-overview.component
  (:require [rum.core :refer [defc] :as rum]
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
  (let [people          (core/get-people state)
        filtered-people (core/filtered-people state)
        search-terms    (:search-terms state)]
    (scrollable
      {:floating-header
       (search-toolbar
         {:search-field-value (core/get-free-text-search state)
          :total-results      (count filtered-people)
          :on-change          (fn [e]
                                (trigger-event
                                  (rem/create-event {:name :search-string-changed
                                                     :data {:new-value (o/oget e [:target :value])}})))})
       :content
       [:div {:style {:background-color color/grey-light}}
        (for [person people]
          (rum/with-key
            (person-list-card {:person    person
                               :hidden    (not (core/person-matches person search-terms))
                               :on-select (fn [] (trigger-event
                                                   (rem/create-event
                                                     {:name :person-selected
                                                      :data {:person person}})))})
            (:id person)))]
       :floating-footer
       (footer)})))
;
;(defc person-selection-field < (remodular.core/modular-component event/handle-event)
;  [{{state :state} :input
;    trigger-event  :trigger-event}]
;  (let [people         (core/get-people state)]))

