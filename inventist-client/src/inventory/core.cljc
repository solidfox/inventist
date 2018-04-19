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
                       :lname     "Hill"}
                      {:person-id 34
                       :date      "2018-04-14T10:37:46Z"
                       :comment   ""
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
        (let [brand-map {:brand "https://image.flaticon.com/icons/svg/152/152752.svg"}]
          (cond (= "macbook" (re-find #"macbook" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/483/483147.svg")
                (= "iphone" (re-find #"iphone" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/15/15874.svg")
                (= "ipad" (re-find #"ipad" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/0/319.svg")
                :else brand-map))
        (= (lower-case brand) (lower-case "Android"))
        (let [brand-map {:brand "https://image.flaticon.com/icons/svg/14/14415.svg"}]
          (cond (= "notebook" (re-find #"notebook" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/483/483147.svg")
                (= "phone" (re-find #"phone" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/15/15874.svg")
                (= "tablet" (re-find #"pad" (lower-case identifier)))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/0/319.svg")
                :else brand-map))))





