(ns people.core)

(defn create-person-summary []
  {:id        44
   :type      "teacher"
   :group     "Lågstadiet"
   :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
   :fname     "Hanna"
   :lname     "Alenius"
   :inventory [{:id               01
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :model-identifier "MacBookPro13,2"}
               {:id               02
                :brand            "Android"
                :supplier         "MediaMarkt"
                :model-name       "Samsung Galaxy S9"
                :color            "Blue"
                :model-identifier "phone"}]})

(defn create-person-detail []
  {:id        44
   :type      "teacher"
   :group     "Lågstadiet"
   :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
   :fname     "Hanna"
   :lname     "Alenius"
   :email     "hanna.alenius@gripsholmsskolan.se"
   :username  "hanna.alenius"
   :sex       "f"
   :phone     "0701039070"
   :inventory [{:id               01
                :date             "2018-04-14T10:37:46Z"
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :model-identifier "MacBookPro13,2"
                :serial-number    "C02SVXXXXF1R"
                :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"}
               {:id               02
                :date             "2018-04-14T10:37:46Z"
                :brand            "Android"
                :supplier         "MediaMarkt"
                :model-name       "Samsung Galaxy S9"
                :color            "Blue"
                :model-identifier "Phone"
                :serial-number    "C02SVXXXXF1R"
                :photo            "https://i.gadgets360cdn.com/products/large/1519585124_635_samsung_galaxy_s9_blue.jpg"}]
   :history   [{:inventory-id     01
                :date             "2018-04-14T10:37:46Z"
                :brand            "Apple"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :model-identifier "MacBookPro13,2"
                :serial-number    "C02SVXXXXF1R"
                :comment          "New MacBook"}
               {:inventory-id     02
                :date             "2018-04-14T10:37:46Z"
                :brand            "Apple"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :model-identifier "MacBookPro13,2"
                :serial-number    "C02SVXXXXF1R"
                :comment          "New MacBook"}]})