(ns symbols.inventory
  (:require [symbols.style :as style]
            [symbols.general :as s-general]
            [rum.core :as rum :refer [defc]]
            [symbols.overview :as s-overview]))

(defc inventory-list-card < {:key-fn (fn [props] (get-in props [:item :id]))}
  [{:keys [item
           selected
           hidden
           on-drop
           on-select]}]
  (s-overview/list-card {:selected  selected
                         :on-drop   on-drop
                         :drop-zone [{:drag-data-type "inventist/person"
                                      :drop-zone-text (str "Assign " (:class item) " to the dragged person.")
                                      :drop-effect    "link"}]
                         :on-click  on-select}
                        [:div {:key   1
                               :style {:display "flex"}}
                         [:div {:key   1
                                :class (style/list-item-left-column)
                                :style {:font-size "3rem"}}
                          (cond (and (:image item) (not= (:image item) ""))
                                [:img {:class (style/card-image)
                                       :src   (:image item)}]
                                :else
                                (s-general/device-icon-set {:item item}))]
                         [:div {:key   2
                                :style {:margin-left "0.75rem"
                                        :width       "auto"}}
                          [:span {:style style/card-title}
                           (str (:brand item) " " (:model-name item))] [:br]
                          [:span {:style style/card-subtitle}
                           (str (:serial-number item) " - " (:color item))]]]))
