(ns people.core)

(defn create-person-summary []
  {:id        44
   :type      "teacher"
   :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
   :fname     "Hanna"
   :lname     "Alenius"
   :inventory [{:id               01
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :model-identifier "MacBookPro13,2"}]})

(defn create-person-detail []
  {:id        44
   :type      "teacher"
   :group     "Lågstadiet"
   :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
   :fname     "Hanna"
   :lname     "Alenius"
   :email     "hanna.alenius@gripsholmsskolan.se"
   :username  "hanna.alenius"
   :sex       "m"
   :phone     "0701039070"
   :inventory [{:id               01
                :date             "2018-04-14T10:37:46Z"
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :model-identifier "MacBookPro13,2"
                :serial-number    "C02SVXXXXF1R"
                :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"}]
   :history   [{:inventory-id     01
                :date             "2018-04-14T10:37:46Z"
                :brand            "Apple"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :model-identifier "MacBookPro13,2"
                :serial-number    "C02SVXXXXF1R"
                :comment          "New MacBook"}]})
