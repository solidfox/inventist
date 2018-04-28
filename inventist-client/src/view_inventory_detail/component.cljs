(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]
            [remodular.core :refer [modular-component]]))

(defc person-card
  [{fname :fname
    lname :lname
    image :image
    type  :type
    group :group
    date  :date}]
  (ant/card {:bordered true :class "card-photo"}
            (ant/col {:span 12}
                     [:div [:img {:src image :style {:width "80%"}}]])

            (ant/col {:span 12} [:dl
                                 [:dt "Name"]
                                 [:dd fname " " lname]
                                 [:dt "Type"]
                                 [:dd type " - " group]
                                 [:dt "Assigned on"]
                                 [:dd date]])))

(defc inventory-detail < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}

     [:div {:style {:height          "2.5rem"
                    :padding         "0.5rem"
                    :backgroundColor "#f6f6f6"
                    :color           "white"}}
      (ant/breadcrumb
        (ant/breadcrumb-item [:a {:href "#"} "Home"])
        (ant/breadcrumb-item [:a {:href "#"} "Inventory"])
        (ant/breadcrumb-item [:span {:class "capitalize" :cursor "pointer"}
                              [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
                              [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]
                              (str " (" (:fname (first (:history item))) " " (:lname (first (:history item))) ")")]))]

     [:div {:style {:overflow-x "hidden"
                    :overflow-y "scroll"}}
      (ant/card {:title [(ant/avatar {:icon  "desktop"
                                      :shape "square"
                                      :size  "large"})
                         (str " " (:brand item) " " (:model-name item))]}
                (ant/row {:gutter 16}
                         (ant/col {:span 8} (ant/card {:title "Information" :type "inner"}
                                                      [:div {:class "capitalise"}
                                                       (ant/table
                                                         {:showHeader false :pagination false
                                                          :columns    [{:title "Field" :dataIndex "field" :width 130}
                                                                       {:title "Value" :dataIndex "value"}]
                                                          :dataSource [{:key "1" :field "Brand" :value (:brand item)}
                                                                       {:key "2" :field "Model" :value (:model-name item)}
                                                                       {:key "3" :field "Color" :value (:color item)}
                                                                       {:key "4" :field "Identifier" :value (:model-identifier item)}
                                                                       {:key "5" :field "Serial Number" :value (:serial-number item)}]})
                                                       [:img {:src (:photo item) :style {:width "100%"}}]]))

                         (ant/col {:span 8} (ant/card {:title "Assigned to" :type "inner"}
                                                      (person-card (first (:history item)))))

                         (ant/col {:span 8} (ant/card {:title "Timeline" :type "inner"}
                                                      (ant/timeline {:pending [[:p (:delivery-date (:purchase-details item))]
                                                                               [:p "Purchased from " (:supplier (:purchase-details item))]]}
                                                                    (for [{id      :person-id
                                                                           date    :date
                                                                           comment :comment
                                                                           fname   :fname
                                                                           lname   :lname
                                                                           type    :type
                                                                           group   :group} (:history item)]
                                                                      (ant/timeline-item {:key id}
                                                                                         [:p date]
                                                                                         [:p "Allotted to " fname " " lname " (" type " - " group ")"]
                                                                                         [:p {:class "italic"} comment])))))

                         (ant/col {:span 8} (ant/card {:title "Loading Animation" :type "inner" :loading true}))))]]))




