(ns symbols.people
  (:require [rum.core :as rum :refer [defc]]
            [symbols.color :as color]
            [symbols.style :as style]
            [clojure.string :as str]
            [symbols.general :as s-general]
            [symbols.overview :as s-overview]))

(defc person-list-card < rum/static
      [{:keys [person
               selected
               hidden]}]
      (s-overview/list-card {:selected selected
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
