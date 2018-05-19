(ns util.inventory.core
  (:require [clojure.string :refer [lower-case]]))

(def android-brands #{"samsung" "google" "android"})

(defn inventory-icon [{id            :id
                       brand?        :brand
                       model         :model-name
                       color         :color
                       identifier?   :model-identifier
                       class?        :class
                       serial-number :serial-number
                       :as           item-data}]
  (let [brand      (or brand? "")
        identifier (or identifier? "")
        class      (or class? "")]
    (cond (= (lower-case brand) (lower-case "Apple"))
          (let [brand-map {:brand "fab fa-apple"}]
            (cond (re-find #"macbook" (lower-case identifier))
                  (assoc brand-map :model "fas fa-laptop")
                  (re-find #"smartphone" (lower-case class))
                  (assoc brand-map :model "fas fa-mobile-alt")
                  (re-find #"tablet" (lower-case class))
                  (assoc brand-map :model "fas fa-tablet-alt")
                  :else brand-map))
          (contains? android-brands (lower-case brand))
          (let [brand-map {:brand "fab fa-android"}]
            (cond (re-find #"laptop" (lower-case class))
                  (assoc brand-map :model "fas fa-laptop")
                  (re-find #"phone" (lower-case class))
                  (assoc brand-map :model "fas fa-mobile-alt")
                  (re-find #"tab" (lower-case class))
                  (assoc brand-map :model "fas fa-tablet-alt")
                  :else brand-map)))))

