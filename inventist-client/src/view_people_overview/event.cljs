(ns view-people-overview.event
  (:require [remodular.core :as rem]
            [view-people-overview.core :as core]))

(defn handle-event
  [_state event]
  (if (rem/triggered-by-me? event)
    (case (:name event)
      :search-string-changed
      (rem/append-action
        event
        (rem/create-action {:name :search-string-changed
                            :fn-and-args
                                  [core/set-free-text-search (get-in event [:data :new-value])]})))
    event))


