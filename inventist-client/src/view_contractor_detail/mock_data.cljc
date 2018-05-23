(ns view-contractor-detail.mock-data)


(defn create-contractor-detail
  [{id :id}]
  {:id         id
   :occupation "supplier"
   :image      "https://www.dropque.com/assets/placeholder-company-5f3438282f524800f1d49cd2921bb0a56101e1aa16097ebd313b64778fc7c4bd.png"
   :name       "MediaMarkt"
   :email      "info@mediamarkt.se"
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
                 :brand            "Apple"
                 :supplier         "MediaMarkt"
                 :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                 :color            "Space Gray"
                 :model-identifier "MacBookPro13,2"
                 :serial-number    "C02SVXXXXF1R"
                 :class            "macbook"
                 :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"}
                {:id               03
                 :date             "2018-04-14T10:37:46Z"
                 :brand            "Android"
                 :supplier         "MediaMarkt"
                 :model-name       "Samsung Galaxy S9"
                 :color            "Blue"
                 :model-identifier "Phone"
                 :serial-number    "C02SVXXXXF1R"
                 :class            "phone"
                 :photo            "https://i.gadgets360cdn.com/products/large/1519585124_635_samsung_galaxy_s9_blue.jpg"}]
   :history    [{:inventory-id     01
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
