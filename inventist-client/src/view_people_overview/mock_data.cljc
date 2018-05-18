(ns view-people-overview.mock-data)

(defn create-person-summary [{id :id}]
  {:id        (or id "00")
   :type      "teacher"
   :groups     ["Lågstadiet"]
   :image     "http://www.theswiftlift.com/wp-content/uploads/2017/06/person-placeholder.png"
   :sex       "f"
   :fname     "Hanna"
   :lname     "Alenius"
   :inventory [{:id               01
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :class            "macbook"
                :model-identifier "MacBookPro13,2"}
               {:id               02
                :brand            "Android"
                :supplier         "MediaMarkt"
                :model-name       "Samsung Galaxy S9"
                :class            "phone"
                :color            "Blue"
                :model-identifier "phone"}]})



(defn create-get-people-list-response
  []
  {:items [(create-person-summary {:id "01"})
           (create-person-summary {:id "02"})
           (create-person-summary {:id "03"})
           (create-person-summary {:id "04"})
           (create-person-summary {:id "05"})
           (create-person-summary {:id "06"})
           (create-person-summary {:id "07"})
           (create-person-summary {:id "08"})
           (create-person-summary {:id "09"})
           (create-person-summary {:id "10"})
           (create-person-summary {:id "11"})
           (create-person-summary {:id "12"})]})
