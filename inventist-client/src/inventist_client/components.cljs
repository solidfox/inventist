(ns inventist-client.components)

(defn people-details [person]
  [:div
   [:table
    [:tr
     [:td
      [:img {:src (:image person)
             :style {:width "100px"}}]]
     [:td
      [:h2 (str (:fname person) " " (:lname person))]
      [:h4 (:type person)]]]]])



