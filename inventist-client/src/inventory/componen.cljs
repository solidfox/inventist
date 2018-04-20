(ns inventory.componen
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]))

;React function to filter (cascader) [case-insensitive]
;function filter(inputValue, path) {return (path.some(option => (option.label).toLowerCase().indexOf(inputValue.toLowerCase()) > -1))};

(def inventory-filter [{:value    "apple"
                        :label "Apple"
                        :children [{:value "macbook" :label "MacBook"}
                                   {:value "ipad" :label "iPad"}
                                   {:value "iphone" :label "iPhone"}]}
                       {:value    "android"
                        :label "Android"
                        :children [{:value "notebook" :label "Noteook"}
                                   {:value "tab" :label "Tablet"}
                                   {:value "phone" :label "Phone"}]}])

(defc inventory-list [items]
  [:div {:id "list-container"}
   (ant/layout
     (ant/layout-sider {:style {:backgroundColor "#f2f2f2"} :width 300}
                       ;Normal Search
                       (ant/input-search {:placeholder "Type here to Print in console" :size "large" :on-search (fn [value] (println value))})
                       ;Filter Search
                       (ant/cascader {:showSearch     true
                                      :changeOnSelect true
                                      :size           "large"
                                      :placeholder    "Filter Search"
                                      :style          {:width "100%"}
                                      :options        inventory-filter})
                       ;listing people
                       (for [item items]
                         (ant/card {:bordered true}
                                   (ant/row {:gutter 16}
                                            (ant/col {:span 6}
                                                     [:p
                                                      [:span
                                                       [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
                                                       [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]]]
                                                     [:p [:img {:src (:photo item) :style {:width "100%" :borderRadius "5px"}}]])

                                            (ant/col {:span 18}
                                                     [:p {:class "bold capitalize"} (str (:model-name item) " - " (:color item))]
                                                     [:p [:span {:class "italic capitalize"} (str (:assignee item))]])))))

     ;this is a fake body to be replaced by detail-view
     (ant/layout
       (ant/layout-header {:style {:color "white"}} "This is Header")
       (ant/layout-content "This is content body" (ant/card {:title "Loading Animation" :type "inner" :loading true}))
       (ant/layout-footer "This is footer")))])


(defc inventory-details [item]
  [:div {:id "detail-container"}
   (ant/layout
     (ant/affix (ant/layout-header {:style {:color "white"}} "Assign to New User"))

     (ant/layout-content
       (ant/card {:title [(ant/avatar {:icon "desktop" :shape "square" :size "large"})
                          (str " " (:brand item) " " (:model-name item))]}
                 (ant/row {:gutter 16}
                          (ant/col {:span 8} (ant/card {:title "Information" :type "inner"}
                                                       [:p {:class "capitalise"}
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
                                                           (str (:brand item) " " (:model-name item) " (" (:serial-number item) ")")])))))])
