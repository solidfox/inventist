(ns view-inventory-detail.mock-data)

(defn create-inventory-detail
  []
  {:id               01
   :date             "2018-04-14T10:37:46Z"
   :brand            "Apple"
   :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
   :color            "Space Gray"
   :model-identifier "MacBookPro13,2"
   :serial-number    "C02SVXXXXF1R"
   :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"
   :purchase-details {:id                1
                      :documents         [""]
                      :supplier          "MediaMarkt"
                      :delivery-date     "2018-04-14T10:37:46Z"
                      :insurance-expires "2020-04-14T10:37:46Z"
                      :warranty-expires  "2019-04-14T10:37:46Z"}
   :history          [{:person-id 33
                       :date      "2018-04-14T10:37:46Z"
                       :comment   "New MacBook"
                       :type      "student"
                       :group     "Vargar"
                       :image     ""
                       :sex       "m"
                       :fname     "Winston"
                       :lname     "Hill"}
                      {:person-id 34
                       :date      "2018-04-14T10:37:46Z"
                       :comment   ""
                       :type      "student"
                       :group     "Vargar"
                       :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
                       :sex       "f"
                       :fname     "Winston"
                       :lname     "Hill"}]})
