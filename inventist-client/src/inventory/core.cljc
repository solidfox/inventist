(ns inventory.core
  (:require [clojure.string :refer [lower-case]]))

(defn create-state []
  {:filter nil})


(defn create-inventory-summary
  []
  {:id               01
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
(defn create-inventory-summary-2
  []
  {:id               02
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

(def android-brands #{"samsung" "google" "android"})

(defn inventory-icon [{id            :id
                       brand         :brand
                       model         :model-name
                       color         :color
                       identifier    :model-identifier
                       class         :class
                       serial-number :serial-number}]
  (cond (= (lower-case brand) (lower-case "Apple"))
        (let [brand-map {:brand "https://image.flaticon.com/icons/svg/152/152752.svg"}]
          (cond (re-find #"macbook" (lower-case identifier))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/483/483147.svg")
                (re-find #"smartphone" (lower-case class))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/15/15874.svg")
                (re-find #"tablet" (lower-case class))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/0/319.svg")
                :else brand-map))
        (contains? android-brands (lower-case brand))
        (let [brand-map {:brand "https://image.flaticon.com/icons/svg/14/14415.svg"}]
          (cond (re-find #"laptop" (lower-case class))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/483/483147.svg")
                (re-find #"phone" (lower-case class))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/15/15874.svg")
                (re-find #"tab" (lower-case class))
                (assoc brand-map :model "https://image.flaticon.com/icons/svg/0/319.svg")
                :else brand-map))))



