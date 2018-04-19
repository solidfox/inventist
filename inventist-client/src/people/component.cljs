(ns people.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]))

(defc people-details [person]
  (println person)
  [:div {:class "detail-container"}
   (ant/layout
     (ant/affix (ant/layout-header {:style {:color "white"}} "Action 1"))

     (ant/layout-content
       (ant/card {:title [(ant/avatar {:icon "user" :shape "square" :size "large"})
                          (str " " (:fname person) " " (:lname person))]}
                 (ant/row {:gutter "16"}
                          (ant/col {:span "8"} (ant/card {:title "Information" :type "inner"}
                                                         (ant/table
                                                           {:showHeader false :pagination false
                                                            :columns    [{:title "Field" :dataIndex "field" :width 100}
                                                                         {:title "Value" :dataIndex "value"}]
                                                            :dataSource [{:key "1" :field "Type" :value (:type person)}
                                                                         {:key "2" :field "Username" :value (:username person)}
                                                                         {:key "3" :field "Email" :value (:email person)}
                                                                         {:key "4" :field "Phone" :value (:phone person)}
                                                                         {:key "5" :field "Gender" :value (cond (= (:sex person) "f") "Female"
                                                                                                                (= (:sex person) "m") "Male")}]})))
                          (ant/col {:span "8"} (ant/card {:title "Products Assigned" :type "inner"}
                                                         (for [{id            :id
                                                                photo         :photo
                                                                brand         :brand
                                                                model         :model-name
                                                                color         :color
                                                                serial-number :serial-number
                                                                :as           item} (:inventory person)]
                                                           [(ant/card {:bordered true :class "card-photo"}
                                                                      (ant/col {:span 12}
                                                                               [:div [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
                                                                                [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]] [:br]
                                                                               [:div [:img {:src photo :style {:width "100px"}}]])

                                                                      (ant/col {:span 12} [:dl
                                                                                           [:dt "Name"]
                                                                                           [:dd brand " " model " - " color]
                                                                                           [:dt "Serial Number"]
                                                                                           [:dd serial-number]]))
                                                            [:br]])))



                          (ant/col {:span "8"} (ant/card {:title "Timeline" :type "inner"}
                                                         (ant/timeline {:pending "Timeline Begins"}
                                                                       (for [{id          :inventory-id
                                                                              date        :date
                                                                              description :comment} (:history person)]
                                                                         (ant/timeline-item {:key id} date " --- " description)))))

                          (ant/col {:span "8"} (ant/card {:title "Loading Animation" :type "inner" :loading true})))))



     (ant/affix {:offsetBottom 0}
                (ant/layout-footer (ant/breadcrumb
                                     (ant/breadcrumb-item [:a {:href "#"} "Home"])
                                     (ant/breadcrumb-item [:a {:href "#"} "Person"])
                                     (ant/breadcrumb-item [:span {:class "capitalize"}
                                                           (str (:fname person) " " (:lname person) " (" (:type person) ")")])))))])


;[:div {:class "detail-header"} "header"]
;
;[:div {:class "detail-main"}
; [:div {:class "detail-image"}
;  [:img {:src (:image person) :style {:width "100px"}}]]
; [:div {:class "detail-identity"}
;  [:h1 {:class "capitalize line-ht-none"} (str (:fname person) " " (:lname person))]
;  [:p {:class "capitalize bold"} (:type person)]
;  [:p {:class "description"}
;   (str "Username: " (:username person)) [:br]
;   (str "Email: " (:email person)) [:br]
;   (str "Phone: " (:phone person)) [:br]
;   (str "Gender: " (cond (= (:sex person) "f") "Female"
;                         (= (:sex person) "m") "Male")) [:br]])
; [:div {:class "detail-inventory"}
;  [:h3 "Products Assigned"]
;  [:ul {:class "inventory-list"}
;   (for [{id            :id
;          photo         :photo
;          brand         :brand
;          model         :model-name
;          serial-number :serial-number
;          :as           item} (:inventory person)]
;     [:li {:key id} [:img {:src photo :style {:width "100px"}}]
;      [:dl
;       [:dt "Name"]
;       [:dd brand " " model]
;       [:dt "Serial Number"]
;       [:dd serial-number]
;       [:dt "Icon"]
;       [:dd [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
;        [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]]]])])

; [:div {:class "detail-timeline"}
;  [:h3 "Timeline"]
;  (ant/timeline (for [{id          :id
;                       date        :date
;                       description :comment} (:history person)]
;                  (ant/timeline-item date "---" description))))

;
;
;[:div {:class "detail-footer"} "footer"]


