(ns view-inventory-overview.component
  (:require [rum.core :as rum :refer [defc defcs with-key]]
            [symbols.overview :refer [search-toolbar second-column-header list-card]]
            [symbols.style :as style]
            [view-inventory-overview.core :as core]
            [symbols.color :as color]
            [remodular.core :as rem]
            [oops.core :as o]
            [view-inventory-overview.event :as event]
            [cljs-react-material-ui.icons :as icon]
            [symbols.general :as s-general]
            [util.inventory.core :as util]
            [symbols.mixin :as mixin]))

(defcs inventory-list-card < {:key-fn (fn [{{id :id} :item}] id)}
  [{:keys []}
   {:keys [item
           selected
           hidden
           on-drag-dropped
           on-select]}]
  (list-card {:selected selected
              :on-drag-enter (fn [drag-data])
              :on-drag-dropped on-drag-dropped
              :on-click on-select}
             [:div {:key 1
                    :class (style/list-item-left-column)
                    :style {:font-size "3rem"}}
              (cond (and (:image item) (not= (:image item) ""))
                    [:img {:class (style/card-image)
                           :src   (:image item)}]
                    :else
                    (s-general/device-icon-set {:item item}))]

             [:div {:key 2
                    :style {:margin "0 0 0 1rem"
                            :width  "auto"}}
              [:span {:style style/card-title}
               (str (:brand item) " " (:model-name item))] [:br]
              [:span {:style style/card-subtitle}
               (str (:serial-number item) " - " (:color item))]]))

(defc inventory-list < (remodular.core/modular-component event/handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [inventory (core/filtered-inventory state)
        limited-inventory (take 75 inventory)
        n-results (count limited-inventory)
        search-terms (:search-terms state)]
    (s-general/scrollable
      {:floating-header
       [:div
        (second-column-header "Inventory")
        (search-toolbar
          {:search-field-value (core/get-free-text-search state)
           :shown-results      (count limited-inventory)
           :total-results      (count inventory)
           :on-change          (fn [e]
                                 (trigger-event
                                   (rem/create-event {:name :search-string-changed
                                                      :data {:new-value (o/oget e [:target :value])}})))})]
       :content
       (cond
         (:fetching-inventory-list state)
         (s-general/centered-loading-indicator "inventory")
         (core/get-inventory-list-failed? state)
         (s-general/centered-message {:icon       (icon/alert-error-outline {:color color/shaded-context-primary-text
                                                                             :style {:height "3rem"
                                                                                     :width  "3rem"}})
                                      :message    "Error while fetching inventory-list from server."
                                      :text-color color/shaded-context-primary-text
                                      :actions    (s-general/button {:text     "Retry"
                                                                     :bg-color color/shaded-context-highlight-bg
                                                                     :on-click (fn [] (trigger-event (rem/create-event {:name :retry-fetching-data})))})})
         :else
         [:div {:style {:height           "auto"
                        :background-color color/transparent
                        :padding          "0.25rem"}}
          (let [item-selected-event (fn [item] (trigger-event (rem/create-event {:name :selected-inventory-item
                                                                                 :data {:item item}})))]
            (->> limited-inventory
                 (map
                   (fn [item]
                     [:div {:key      (:id item)
                            :on-click (fn [] (item-selected-event item))}
                      (inventory-list-card {:item     item
                                            :selected (= (:selected-inventory-id state) (:id item))})]))))
          ;:hidden    (not (core/inventory-matches item search-terms))})]))))

          (when (= n-results 0)
            [:div {:style {:width            "100%"
                           :height           "100%"
                           :color            color/shaded-context-primary-text
                           :background-color color/transparent
                           :text-align       "center"
                           :margin-top       "2rem"}}
             "No matches found!"])])})))

