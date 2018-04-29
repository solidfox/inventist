(ns inventist-client.page.inventory.core
  (:require [view-inventory-detail.core :as view-inventory-detail]
            [view-inventory-list.core :as view-inventory-list]
            [clojure.string :refer [lower-case]]))

(defn inventory-detail-state-path [item-id] [:view-modules :view-inventory-detail item-id])
(defn inventory-list-state-path [{filter :filter}] [:view-modules :view-inventory-list filter])

(defn create-state
  []
  (-> {}
      (assoc-in (inventory-detail-state-path "mock-id") (view-inventory-detail/create-state "mock-id"))
      (assoc-in (inventory-list-state-path {}) (view-inventory-list.core/create-state))))

(defn create-inventory-detail-args
  [state item-id]
  (let [state-path (inventory-detail-state-path item-id)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(defn create-inventory-list-args
  [state state-path-deps]
  (let [state-path (inventory-list-state-path state-path-deps)]
    {:input      {:state (get-in state state-path)}
     :state-path state-path}))

(def android-brands #{"samsung" "google" "android"})

(defn inventory-icon [{id            :id
                       brand         :brand
                       model         :model-name
                       color         :color
                       identifier    :model-identifier
                       class         :class
                       serial-number :serial-number}]
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
                :else brand-map))))



