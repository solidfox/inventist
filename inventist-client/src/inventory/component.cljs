(ns inventory.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]
            [clojure.string :refer [index-of lower-case]]))

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

(defn filter [inputValue path]
  (prinln inputValue path)
  (.some path (fn [option]
                (not (nil? (index-of (lower-case (.-label option)) (lower-case inputValue)))))))

(defc inventory-list [items]
  [:div {:id "list-container"}
   (ant/layout
     (ant/layout-sider {:style {:backgroundColor "#f2f2f2"} :width 300}
                       ;Normal Search
                       (ant/input-search {:placeholder "Type here to Print in console" :size "large" :on-search (fn [value] (println value))})
                       ;Filter Search
                       (ant/cascader {:showSearch     filter
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

