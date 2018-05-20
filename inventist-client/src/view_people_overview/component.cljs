(ns view-people-overview.component
  (:require [rum.core :refer [defc]]
            [symbols.overview :refer [scrollable search footer person-list-card]]
            [view-people-overview.core :as core]
            [symbols.color :as color]))

(defc people-list < (remodular.core/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [people (core/filtered-people state)]
    (scrollable
      {:floating-header (search [{:list-items people}])
       :content         [:div {:style {:background-color color/grey-light}}
                         (for [person people]
                           (person-list-card {:person person}))]
       :floating-footer (footer)})))
