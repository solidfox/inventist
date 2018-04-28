(ns view-inventory-list.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]
            [clojure.string :refer [index-of lower-case]]))

(def inventory-filter [{:value    "apple"
                        :label    "Apple"
                        :children [{:value "macbook" :label "MacBook"}
                                   {:value "ipad" :label "iPad"}
                                   {:value "iphone" :label "iPhone"}]}
                       {:value    "android"
                        :label    "Android"
                        :children [{:value "notebook" :label "Noteook"}
                                   {:value "tab" :label "Tablet"}
                                   {:value "phone" :label "Phone"}]}])

;(defn showSearch [inputValue path]
;  (println inputValue path))
;  (.some path (fn [option]
;               (not (nil? (index-of (lower-case (.-label option)) (lower-case inputValue)))))))

(defc inventory-list < remodular.core/modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [items (get-in state [:get-inventory-list-response :response :items])]
    [:div {:style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto auto 1fr"}}
     ;Normal Search
     (ant/input-search {:placeholder "Type here to Print in console" :size "large" :on-search (fn [value] (println value))})
     ;Filter Search
     ;(ant/cascader {:showSearch     showSearch
     ;               :changeOnSelect true
     ;               :size           "large"
     ;               :placeholder    "Filter Search"
     ;               :style          {:width "100%"}
     ;               :options        inventory-filter})

     ;listing people
     [:div {:style {:overflow-x "hidden"
                    :overflow-y "scroll"}}

      (for [item items]
         (ant/row {:gutter 16 :style {:padding "1rem"}}
                  (ant/col {:span 6}
                           [:p
                            [:span
                             [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
                             [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]]]
                           [:p [:img {:src (:photo item) :style {:width "100%" :borderRadius "5px"}}]])

                  (ant/col {:span 18}
                           [:p {:class "bold capitalize"} (str (:model-name item) " - " (:color item))]
                           [:p [:span {:class "italic capitalize"} (str (:assignee item))]])))]]))
