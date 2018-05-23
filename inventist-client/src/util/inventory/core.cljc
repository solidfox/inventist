(ns util.inventory.core
  (:require [clojure.string :refer [lower-case]]
            [clojure.pprint :refer [pprint]]
            [camel-snake-kebab.core :refer [->kebab-case-keyword
                                            ->snake_case]]
            [graphql-query.core]
            [ysera.test :as test]
            [clojure.string :as str]))

(def android-brands #{"samsung" "google" "android"})

(defn inventory-icon [{id            :id
                       brand?        :brand
                       model?        :model-name
                       color         :color
                       identifier?   :model-identifier
                       class?        :class
                       serial-number :serial-number
                       :as           item-data}]
  (let [brand      (or brand? "")
        identifier (or identifier? "")
        class      (or class? "")
        model      (or model? "")]
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

(defn spy [x]
  (pprint x)
  x)

(defn transform-keys
  "Recursively transforms all map keys in coll with t."
  [t coll]
  (let [f (fn [[k v]] [(t k) v])]
    (clojure.walk/postwalk (fn [x] (if (map? x)
                                     (into {} (map f x))
                                     x))
                           coll)))

(defn ->clojure-keys
  {:test (fn [] (test/is= (->clojure-keys {:a_b {:c_d :e_f}})
                          {:a-b {:c-d :e_f}}))}
  [coll]
  (transform-keys ->kebab-case-keyword coll))

(defn graphql-string
  {:test (fn [] (test/is= (graphql-string {:queries [[:people-photos [:name]]]})
                          "{people_photos{name}}"))}
  [edn-query]
  (str/replace (graphql-query.core/graphql-query edn-query) #"-" "_"))
