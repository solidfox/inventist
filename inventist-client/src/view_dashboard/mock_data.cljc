(ns view-dashboard.mock-data)

(defn create-person-detail
  [{id :id}]
  {:id         id
   :occupation "teacher"
   :group      "Lågstadiet"
   :image      "http://www.theswiftlift.com/wp-content/uploads/2017/06/person-placeholder.png"
   :first-name "Hanna"
   :last-name  "Alenius"
   :email      "hanna.alenius@gripsholmsskolan.se"
   :username   "hanna.alenius"
   :gender     "f"
   :phone      "0701039070"
   :address    "Stockholm"
   :inventory  [{:id               01
                 :date             "2018-04-14T10:37:46Z"
                 :brand            "Apple"
                 :supplier         "MediaMarkt"
                 :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                 :color            "Space Gray"
                 :model-identifier "MacBookPro13,2"
                 :serial-number    "C02SVXXXXF1R"
                 :class            "macbook"
                 :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"}
                {:id               02
                 :date             "2018-04-14T10:37:46Z"
                 :brand            "Android"
                 :supplier         "MediaMarkt"
                 :model-name       "Samsung Galaxy S9"
                 :color            "Blue"
                 :model-identifier "Phone"
                 :serial-number    "C02SVXXXXF1R"
                 :class            "phone"
                 :photo            ""}]
   :history    [{:inventory-item-id 01
                 :date              "2018-04-14T10:37:46Z"
                 :brand             "Apple"
                 :model-name        "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                 :model-identifier  "MacBookPro13,2"
                 :serial-number     "C02SVXXXXF1R"
                 :comment           "New MacBook"}
                {:inventory-item-id 02
                 :date              "2018-04-14T10:37:46Z"
                 :brand             "Apple"
                 :model-name        "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                 :model-identifier  "MacBookPro13,2"
                 :serial-number     "C02SVXXXXF1R"
                 :comment           "New MacBook"}]})
