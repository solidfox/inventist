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
  (let [brand (or brand? "")
        identifier (or identifier? "")
        class (or class? "")
        model (or model? "")]
    (cond (= (lower-case brand) (lower-case "Apple"))
          {:brand ""
           :model (cond (or
                          (or (str/includes? (lower-case class) "laptop")
                              (str/includes? (lower-case model) "macbook"))
                          (str/includes? (lower-case identifier) "macbook"))
                        "💻"
                        (str/includes? (lower-case class) "phone")
                        "📱"
                        (str/includes? (lower-case class) "tablet")
                        "Tablet"
                        :else nil)}
          (contains? android-brands (lower-case brand))
          {:brand "🤖"
           :model (cond (str/includes? (lower-case class) "laptop")
                        "💻"
                        (str/includes? (lower-case class) "phone")
                        "📱"
                        (str/includes? (lower-case class) "tab")
                        "Tablet"
                        :else nil)})))

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
