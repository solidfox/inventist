(ns inventist-client.components)

(defn people-details [person]
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
[:div {:class "detail-image" }
    [:img {:src   (:image person)}]]]
   [:div {:class "detail-footer"} "footer"]])




