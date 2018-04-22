(ns inventory.core
  (:require [clojure.string :refer [lower-case]]))

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



