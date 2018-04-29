(ns view-inventory-list.component
  (:require [rum.core :refer [defc]]
            [inventory.core :as inventory]
            [clojure.string :refer [index-of lower-case]]))


;To change color on click/hover
(def listBGcolor "#f6f6f6")

;to calculate length of an array
(defn length
  [list]
  (if (empty? list) 0
                    (+ 1 (length (rest list)))))


(defc inventory-list < remodular.core/modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [items (get-in state [:get-inventory-list-response :response :items])]
    [:div {:style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr auto"}}

     ;Normal Search
     [:div
      [:div {:style {:width                 "100%"
                     :height                "3rem"
                     :backgroundColor       "#ffffff"
                     :display               "grid"
                     :grid-template-columns "auto 1fr auto"
                     :box-shadow            "0px 0px 5px rgba(0,0,0,0.25) inset"}}
       [:div {:style {:margin "0.75rem" :font-size "1rem" :color "rgba(0,0,0,0.5)"}}
        [:i {:class "fas fa-search"}]]
       [:input {:type        "search"
                :id          "inventory-search"
                :name        "inventory-search"
                :autoFocus   true
                :placeholder "Search Inventory"
                :style       {:width           "100%"
                              :margin          "0"
                              :height          "3rem"
                              :font-size       "1rem"
                              :backgroundColor "transparent"
                              :border          0}}]]
      [:div {:style {:height "2rem" :padding "0.25rem 1rem" :backgroundColor "#e5e5e5" :display "flex" :justify-content "space-between"}}
       [:div (str "Total " (length items) " results")]
       [:div {:style {:color "#4A90E2" :cursor "pointer"}} (str "View Table")]]]


     ;listing people
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor "#e5e5e5"}}

      (for [item items]
        [:div {:key   (:id item)
               :style {:width                 "100%"
                       :backgroundColor       listBGcolor
                       :minHeight             "3rem"
                       :padding               "1rem"
                       :margin                "0 0 2px 0"
                       :display               "grid"
                       :grid-template-columns "auto 1fr"
                       :cursor                "pointer"}}
         [:div {:style {:width "2.5rem"}}
          [:span {:style {:font-size "1.2rem"}} [:i {:class (:brand (inventory/inventory-icon item))}] " "]
          [:span {:style {:font-size "1rem"}} [:i {:class (:model (inventory/inventory-icon item))}]]]
         [:div {:style {:margin "0 0 0 1rem"}}
          [:span {:style {:font-size "1rem" :color "#4a4a4a" :line-height "1rem"}}
           (str (:model-name item) " - " (:color item))] [:br]
          [:span {:style {:font-size "1rem" :color "#7F8C8D"}}
           (str (:assignee item))]]])]
     ;(ant/row {:gutter 16 :style {:padding "1rem"}}
     ;         (ant/col {:span 6}
     ;                  [:div
     ;                   [:span
     ;                    [:i {:class (:brand (inventory/inventory-icon item))}]
     ;                    [:i {:class (:model (inventory/inventory-icon item))}]]]
     ;                  [:div [:img {:src (:photo item) :style {:width "100%" :borderRadius "5px"}}]])
     ;
     ;         (ant/col {:span 18}
     ;                  [:div {:class "bold capitalize"} (str (:model-name item) " - " (:color item))]
     ;                  [:div [:span {:class "italic capitalize"} (str (:assignee item))]])))]

     ;Normal Search
     [:div {:style {:width                 "100%"
                    :height                "3rem"
                    :backgroundColor       "#ECF0F1"
                    :display               "grid"
                    :grid-template-columns "auto 1fr auto"
                    :box-shadow            "0 0 5px rgba(0,0,0,0.25)"}}
      [:div {:style {:margin "0.75rem" :opacity "0.75"}}
       [:img {:src "/image/inventist_icon.svg" :style {:width "1rem"}}]]
      [:div {:style {:margin "0.75rem 0" :font-size "1rem" :opacity "0.75"}}
       [:span "Powered by Inventist"]]
      [:div {:style {:margin "0.75rem" :font-size "1.2rem" :opacity "0.75" :cursor "pointer"}}
       [:i {:class "fas fa-caret-left"}]]]]))




;Filter Search
;(ant/cascader {:showSearch     showSearch
;               :changeOnSelect true
;               :size           "large"
;               :placeholder    "Filter Search"
;               :style          {:width "100%"}
;               :options        inventory-filter})

;(def inventory-filter [{:value    "apple"
;                        :label    "Apple"
;                        :children [{:value "macbook" :label "MacBook"}
;                                   {:value "ipad" :label "iPad"}
;                                   {:value "iphone" :label "iPhone"}]}
;                       {:value    "android"
;                        :label    "Android"
;                        :children [{:value "notebook" :label "Noteook"}
;                                   {:value "tab" :label "Tablet"}
;                                   {:value "phone" :label "Phone"}]}])
;
;(defn showSearch [inputValue path]
;  (println inputValue path))
;  (.some path (fn [option]
;               (not (nil? (index-of (lower-case (.-label option)) (lower-case inputValue)))))))
