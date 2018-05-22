(ns view-people-overview.core
  (:require [clojure.string :as str]
            [ysera.test :as test]
            [util.inventory.core :as util]))

(defn create-state
  []
  {:selected-person-id       nil
   :search-terms             nil
   :fetching-people-list     false
   :get-people-list-response nil})

(defn started-get-people-list-service-call [state]
  (assoc state :fetching-people-list true))

(defn should-get-people-list? [state]
  (and (not (:fetching-people-list state))
       (not (get-in state [:get-people-list-response :data]))))

(defn receive-get-people-list-service-response [state response request]
  (-> state
      (assoc :get-people-list-response (util/->clojure-keys response))
      (assoc :fetching-people-list false)))

(defn set-free-text-search
  [state free-text-search]
  (assoc-in state [:search-terms :free-text-search] free-text-search))

(defn set-selected-person-id
  [state new-id]
  (assoc state :selected-person-id new-id))

(defn person-matches
  {:test (fn [] (let [kalle-anka {:first-name "Kalle"
                                  :last-name  "Anka"
                                  :groups     [{:name "Quack-squad"}]
                                  :occupation "unemployed"}]
                  (test/is (person-matches kalle-anka
                                           {:free-text-search "kalle Anka"}))
                  (test/is (person-matches kalle-anka
                                           {:free-text-search "Quack kalle"}))
                  (test/is-not (person-matches kalle-anka
                                               {:free-text-search "anka mimmi"}))))}
  [person {search-string :free-text-search}]
  (let [person-string (-> (str/join " " (concat [(:first-name person)
                                                 (:last-name person)
                                                 (:occupation person)]
                                                (for [group (:groups person)]
                                                  (:name group))))
                          (str/lower-case))]
    (every? (fn [search-string-word]
              (str/includes? person-string search-string-word))
            (-> search-string
                (str/lower-case)
                (str/split #"\s")))))

(defn get-free-text-search [state]
  (get-in state [:search-terms :free-text-search]))

(defn get-people
  [state]
  (get-in state [:get-people-list-response :data :people]))

(defn filtered-people
  {:test (fn []
           (let [kalle   {:first-name "Kalle"
                          :last-name  "Anka"
                          :groups     [{:name "Quack-squad"}]}
                 scrooge {:first-name "Uncle"
                          :last-name  "Scrooge"
                          :occupation "capitalist"}
                 people  [kalle
                          scrooge]
                 state   (-> (create-state)
                             (receive-get-people-list-service-response
                               {:data {:people people}}
                               nil))]
             (test/is= (filtered-people state
                                        {:free-text-search "le capitalist"})
                       [scrooge])
             (test/is= (filtered-people state
                                        {:free-text-search "le s"})
                       [kalle
                        scrooge])
             (test/is= (filtered-people state)
                       [kalle
                        scrooge])))}
  [state & [search-terms]]
  (when-let [people       (get-in state [:get-people-list-response :data :people])]
    (if-let [search-terms (or search-terms
                              (:search-terms state))]
      (->> people
           (filter (fn [person] (person-matches person search-terms))))
      people)))
