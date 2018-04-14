(ns people.component
  (:require [rum.core :refer [defc]]))

(defc people-details [person]
      (println person)
      [:div {:class "detail-container"}
       [:div {:class "detail-header"} "header"]

       [:div {:class "detail-main"}
        [:div {:class "detail-image"}
         [:img {:src (:image person) :style {:width "100px"}}]]
        [:div {:class "detail-identity"}
         [:h1 {:class "capitalize line-ht-none"} (str (:fname person) " " (:lname person))]
         [:p {:class "capitalize bold"} (:type person)]
         [:p {:class "description"}
          (str "Username: " (:username person)) [:br]
          (str "Email: " (:email person)) [:br]
          (str "Phone: " (:phone person)) [:br]
          (str "Gender: " (cond (= (:sex person) "f") "Female"
                                (= (:sex person) "m") "Male")) [:br]]]
        [:div {:class "detail-inventory"}
         [:h3 "Products Assigned"]
         [:ul {:class "inventory-list"}
          (for [{id    :id
                 photo :photo
                 brand :brand
                 model :model-name
                 serial-number :serial-number} (:inventory person)]
            [:li {:key id} [:dl
                            [:dt "Image "]
                            [:dd [:img {:src photo :style {:width "100px"}}]]
                            [:dt "Name"]
                            [:dd brand " " model]
                            [:dt "Serial Number"]
                            [:dd serial-number]]])]]


        [:div {:class "detail-timeline"}
         [:h3 "Timeline"]
         [:table {:class "timeline"} [:tbody
                                      (for [{id          :id
                                             date        :date
                                             description :comment} (:history person)]
                                        [:tr {:key id}
                                         [:td "• " date " --- "] [:td description]])]]]]

       [:div {:class "detail-footer"} "footer"]])

