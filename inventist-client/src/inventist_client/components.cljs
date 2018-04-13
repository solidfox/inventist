(ns inventist-client.components
  (:require [authentication.component :as auth]
            [rum.core :refer [defc]]))


(defc people-details [person history]
      [:div {:class "detail-container"}
       [:div {:class "detail-header"} "header"]
       [:div {:class "detail-main"}
        [:div {:class "detail-identity"}
         [:h1 {:class "capitalize line-ht-none"} (str (:fname person) " " (:lname person))]
         [:p {:class "capitalize bold"} (:type person)]
         [:p {:class "description"}
          (str "Username: " (:username person)) [:br]
          (str "Email: " (:email person)) [:br]
          (str "Phone: " (:phone person)) [:br]
          (str "Gender: " (cond (= (:sex person) "f") "Female"
                                (= (:sex person) "m") "Male")) [:br]]]
        [:div {:class "detail-image"}
         [:img {:src (:image person)}]]
        [:table {:class "timeline"} [:tbody
                                     (for [timeline history]
                                       ^{:key (:id timeline)}
                                       [:tr [:td "• " (:date timeline) " --- "] [:td (:desc timeline)]])]]]
       [:div {:class "detail-footer"} "footer"]])

(defc app [state]
      [:div
       (people-details (:person state))])




