(ns people.component
  (:require [rum.core :refer [defc]]
            [antizer.rum :as ant]
            [inventory.core :as inventory]))

(defc people-details [person]
  (println person)
  [:div {:class "detail-container"}
   (ant/layout
     (ant/affix (ant/layout-header "Header"))

     (ant/layout-content
       (ant/card {:title [(ant/avatar {:icon "user" :shape "square" :size "large"})
                          (str " " (:fname person) " " (:lname person))]}
                 (ant/row {:gutter "16"}
                          (ant/col {:span "8"} (ant/card {:title "Information" :type "inner"}
                                                         [:p {:class "description"}
                                                          [:span {:class "capitalize"} (str "Type: " (:type person))] [:br]
                                                          (str "Username: " (:username person)) [:br]
                                                          (str "Email: " (:email person)) [:br]
                                                          (str "Phone: " (:phone person)) [:br]
                                                          (str "Gender: " (cond (= (:sex person) "f") "Female"
                                                                                (= (:sex person) "m") "Male")) [:br]]))

                          (ant/col {:span "8"} (ant/card {:title "Products Assigned" :type "inner"}
                                                         [:ul {:class "inventory-list"}
                                                          (for [{id            :id
                                                                 photo         :photo
                                                                 brand         :brand
                                                                 model         :model-name
                                                                 serial-number :serial-number
                                                                 :as           item} (:inventory person)]
                                                            [:li {:key id} [:img {:src photo :style {:width "100px"}}]
                                                             [:dl
                                                              [:dt "Name"]
                                                              [:dd brand " " model]
                                                              [:dt "Serial Number"]
                                                              [:dd serial-number]
                                                              [:dt "Icon"]
                                                              [:dd [:img {:class "icon" :src (:brand (inventory/inventory-icon item))}]
                                                               [:img {:class "icon" :src (:model (inventory/inventory-icon item))}]]]])]))

                          (ant/col {:span "8"} (ant/card {:title "Timeline" :type "inner"}
                                                         (ant/timeline {:pending "Timeline Begins"}
                                                                       (for [{id          :inventory-id
                                                                              date        :date
                                                                              description :comment} (:history person)]
                                                                         (ant/timeline-item {:key id} date " --- " description)))))

                          (ant/col {:span "8"} (ant/card {:title "Loading Animation" :type "inner" :loading true})))))



     (ant/layout-footer "footer"))])


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


