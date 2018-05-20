(ns view-inventory-overview.core
  (:require [clojure.string :as str]
            [ysera.test :as test]))

(defn create-state
  []
  {:selected-item-id            nil
   :search-terms                nil
   :fetching-inventory-list     false
   :get-inventory-list-response nil})

(defn started-get-inventory-list-service-call [state]
  (assoc state :fetching-inventory-list true))

(defn should-get-inventory-list? [state]
  (and (not (:fetching-inventory-list state))
       (not (get-in state [:get-inventory-list-response :data]))))

(defn receive-get-inventory-list-service-response [state response request]
  (-> state
      (assoc :get-inventory-list-response response)
      (assoc :fetching-inventory-list false)))

(defn set-free-text-search
  [state free-text-search]
  (assoc-in state [:search-terms :free-text-search] free-text-search))

(defn set-selected-inventory-id
  [state new-id]
  (assoc state :selected-inventory-id new-id))

(defn inventory-matches
  {:test (fn [] (let [kalle-anka {:brand      "Kalle"
                                  :model_name "Anka"
                                  :color      "silver"}]
                  (test/is (inventory-matches kalle-anka
                                              {:free-text-search "kalle Anka"}))
                  (test/is (inventory-matches kalle-anka
                                              {:free-text-search "silver kalle"}))
                  (test/is-not (inventory-matches kalle-anka
                                                  {:free-text-search "anka silver"}))))}
  [inventory {search-string :free-text-search}]
  (let [inventory-string (-> (str/join " " (concat [(:brand inventory)
                                                    (:model_name inventory)
                                                    (:color inventory)]))
                             (str/lower-case))]
    (every? (fn [search-string-word]
              (str/includes? inventory-string search-string-word))
            (-> search-string
                (str/lower-case)
                (str/split #"\s")))))

(defn get-free-text-search [state]
  (get-in state [:search-terms :free-text-search]))

(defn filtered-inventory
  {:test (fn []
           (let [kalle {:brand      "Kalle"
                        :model_name "Anka"}
                 scrooge {:brand      "Uncle"
                          :model_name "Scrooge"}
                 inventory [kalle
                            scrooge]
                 state (-> (create-state)
                           (receive-get-inventory-list-service-response
                             {:data {:inventory inventory}}
                             nil))]
             (test/is= (filtered-inventory state
                                           {:free-text-search "le Uncle"})
                       [scrooge])
             (test/is= (filtered-inventory state
                                           {:free-text-search "le n"})
                       [kalle
                        scrooge])
             (test/is= (filtered-inventory state)
                       [kalle
                        scrooge])))}
  [state & [search-terms]]
  (when-let [inventory (get-in state [:get-inventory-list-response :data :inventory])]
    (if-let [search-terms (or search-terms
                              (:search-terms state))]
      (->> inventory
           (filter (fn [item] (inventory-matches item search-terms))))
      inventory)))
