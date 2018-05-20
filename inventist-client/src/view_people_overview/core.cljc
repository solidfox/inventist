(ns view-people-overview.core
  (:require [view-people-overview.mock-data :as mock-data]
            [clojure.string :as str]
            [ysera.test :as test]))

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
      (assoc :get-people-list-response response)
      (assoc :fetching-people-list false)))

(defn set-free-text-search
  [state free-text-search]
  (assoc-in state [:search-terms :free-text-search] free-text-search))

(defn set-selected-person-id
  [state new-id]
  (assoc state :selected-person-id new-id))

(defn person-matches
  {:test (fn [] (let [kalle-anka {:fname  "Kalle"
                                  :lname  "Anka"
                                  :groups [{:name "Quack-squad"}]
                                  :type   "unemployed"}]
                  (test/is (person-matches kalle-anka
                                           {:free-text-search "kalle Anka"}))
                  (test/is (person-matches kalle-anka
                                           {:free-text-search "Quack kalle"}))
                  (test/is-not (person-matches kalle-anka
                                               {:free-text-search "anka mimmi"}))))}
  [person {search-string :free-text-search}]
  (let [person-string (-> (str/join " " (concat [(:fname person)
                                                 (:lname person)
                                                 (:type person)]
                                                (for [group (:groups person)]
                                                  (:name group))))
                          (str/lower-case))]
    (every? (fn [search-string-word]
              (str/includes? person-string search-string-word))
            (-> search-string
                (str/lower-case)
                (str/split #"\s")))))

(defn filtered-people
  {:test (fn []
           (let [kalle   {:fname  "Kalle"
                          :lname  "Anka"
                          :groups [{:name "Quack-squad"}]}
                 scrooge {:fname "Uncle"
                          :lname "Scrooge"
                          :type  "capitalist"}
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
