(ns view-inventory-overview.mock-data)

(defn create-apple-summary
  [{id :id}]
  {:id               (or id "mock-macbook")
   :date             "2018-04-14T10:37:46Z"
   :brand            "Apple"
   :supplier         "MediaMarkt"
   :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
   :color            "Space Gray"
   :model-identifier "MacBookPro13,2"
   :class            "laptop"
   :serial-number    "C02SVXXXXF1R"
   :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"
   :assignee         "Hill Winston"})

;Test summary
(defn create-android-summary
  [{id :id}]
  {:id               (or id "mock-android")
   :date             "2018-04-14T10:37:46Z"
   :brand            "Android"
   :supplier         "MediaMarkt"
   :model-name       "Samsung Galaxy S9"
   :color            "Blue"
   :model-identifier "A198"
   :class            "smartphone"
   :serial-number    "C02SVXXXXF1R"
   :photo            "https://i.gadgets360cdn.com/products/large/1519585124_635_samsung_galaxy_s9_blue.jpg"
   :assignee         "Hill Winston"})

(defn create-get-inventory-list-response
  []
  {:items [(create-apple-summary {:id "01"})
           (create-android-summary {:id "02"})
           (create-apple-summary {:id "03"})
           (create-android-summary {:id "04"})
           (create-apple-summary {:id "05"})
           (create-android-summary {:id "06"})
           (create-apple-summary {:id "07"})
           (create-android-summary {:id "08"})]})
