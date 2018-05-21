(ns util.inventory.core
  (:require [clojure.string :refer [lower-case]]))

(def android-brands #{"samsung" "google" "android"})

(defn inventory-icon [{id            :id
                       brand?        :brand
                       model?         :model_name
                       color         :color
                       identifier?   :model_identifier
                       class?        :class
                       serial-number :serial_number
                       :as           item-data}]
  (let [brand (or brand? "")
        identifier (or identifier? "")
        class (or class? "")
        model (or model? "")]
    (cond (= (lower-case brand) (lower-case "Apple"))
          (let [brand-map {:brand "fab fa-apple"}]
            (cond (or (re-find #"laptop" (lower-case class)) (re-find #"macbook" (lower-case model)))
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

