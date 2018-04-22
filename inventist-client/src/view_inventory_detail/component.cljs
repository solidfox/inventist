(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [remodular.core :refer [modular-component]]))

(defc inventory-detail < modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]
    [:div {:id "detail-container"}
     (ant/layout
       (ant/affix (ant/layout-header {:style {:color "white"}} "Assign to New User"))

       (ant/layout-content
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

                                                         (ant/card {:bordered true :class "card-photo"}
                                                                   (ant/col {:span 12}
                                                                            [:div [:img {:src (:image (first (:history item))) :style {:width "80%"}}]])

                                                                   (ant/col {:span 12} [:dl
                                                                                        [:dt "Name"]
                                                                                        [:dd (:fname (first (:history item))) " " (:lname (first (:history item)))]
                                                                                        [:dt "Type"]
                                                                                        [:dd (:type (first (:history item))) " - " (:group (first (:history item)))]
                                                                                        [:dt "Assigned on"]
                                                                                        [:dd (:date (first (:history item)))]]))))


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

                            (ant/col {:span 8} (ant/card {:title "Loading Animation" :type "inner" :loading true})))))

       (ant/affix {:offsetBottom 0}
                  (ant/layout-footer (ant/breadcrumb
                                       (ant/breadcrumb-item [:a {:href "#"} "Home"])
                                       (ant/breadcrumb-item [:a {:href "#"} "Inventory"])
                                       (ant/breadcrumb-item [:span {:class "capitalize"}
                                                             (str (:brand item) " " (:model-name item) " (" (:serial-number item) ")")])))))]))
