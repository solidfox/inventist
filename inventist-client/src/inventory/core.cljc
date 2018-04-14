(ns inventory.core
  (:require [clojure.string :refer [lower-case]]))

(defn create-inventory-summary
  []
  {:id               01
   :date             "2018-04-14T10:37:46Z"
   :brand            "Apple"
   :supplier         "MediaMarkt"
   :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
   :color            "Space Gray"
   :model-identifier "MacBookPro13,2"
   :serial-number    "C02SVXXXXF1R"
   :photo            "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP748/SP748-mbp13touch-gray.jpeg"})

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
                       :image     "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
                       :fname     "Winston"
                       :lname     "Hill"}]})

(defn inventory-icon [{id            :id
                       brand         :brand
                       model         :model-name
                       color         :color
                       identifier    :model-identifier
                       serial-number :serial-number}]
  (cond (= (lower-case brand) (lower-case "Apple"))
        (let [brand-map {:brand "https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg"}]
          (cond (= identifier "MacBookPro13,2")
                (assoc brand-map :model "https://www.shareicon.net/download/2016/12/30/867032_display.svg")
                :else brand-map))))





