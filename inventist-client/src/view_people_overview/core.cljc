(ns view-people-overview.core
  (:require [clojure.string :as str]
            [ysera.test :as test]
            [util.inventory.core :as util]
            [#?(:cljs cljs-time.core :clj clj-time.core) :as time]
            [#?(:cljs cljs-time.coerce :clj clj-time.coerce) :as coerce]))

(defn create-long-timestamp []
  (coerce/to-long (time/now)))

(defn create-state
  []
  {:selected-person-id                 nil
   :search-terms                       nil

   ;Services
   :latest-acceptable-cache-fetch-time (create-long-timestamp)
   :should-retry-on-fetch-error        false
   :fetching-people-list               false
   :get-people-list-response           nil})

(defn started-get-people-list-service-call [state]
  (-> state
      (assoc :fetching-people-list true)
      (assoc :should-retry-on-fetch-error false)))

(defn cache-dirty?
  [state]
  (> (:latest-acceptable-cache-fetch-time state)
     (get-in state [:get-people-list-response ::reception-timestamp])))

(defn has-good-get-people-list-response [state]
  (and (get-in state [:get-people-list-response :body :data])
       (not (cache-dirty? state))))

(defn get-people-list-failed? [state]
  (and (not (nil? (:get-people-list-response state)))
       (or (not (= 0 (get-in state [:get-people-list-response :error-code])))
           (and (:get-people-list-response state)
                (not (= 200 (get-in state [:get-people-list-response :status])))))))

(defn waiting-for-retry-impulse [state]
  (and (get-people-list-failed? state)
       (not (:should-retry-on-fetch-error state))))

(defn set-should-retry-on-fetch-error [state value]
  (assoc state :should-retry-on-fetch-error value))

(defn should-get-people-list? [state]
  (and (not (:fetching-people-list state))
       (not (has-good-get-people-list-response state))
       (not (waiting-for-retry-impulse state))))


(defn receive-get-people-list-service-response [state response request]
  (-> state
      (assoc :get-people-list-response (-> (util/->clojure-keys response)
                                           (assoc ::reception-timestamp (create-long-timestamp))))
      (assoc :fetching-people-list false)))

(defn on-remote-state-mutation
  {:test (fn []
           (as-> (create-state) $
                 (receive-get-people-list-service-response $ {} {})
                 (do (test/is-not (cache-dirty? $)) $)
                 (on-remote-state-mutation $ nil)
                 (do (test/is (cache-dirty? $)) $)
                 (do (test/is (should-get-people-list? $)) $)))}
  [state _]
  (assoc state :latest-acceptable-cache-fetch-time (create-long-timestamp)))

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
  (if (empty? search-string)
    true
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
                  (str/split #"\s"))))))

(defn get-free-text-search [state]
  (get-in state [:search-terms :free-text-search]))

(defn get-people
  [state]
  (get-in state [:get-people-list-response :body :data :people]))

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
  (when-let [people (get-in state [:get-people-list-response :body :data :people])]
    (if-let [search-terms (or search-terms
                              (:search-terms state))]
      (->> people
           (filter (fn [person] (person-matches person search-terms))))
      people)))

(defn get-person [state person-id]
  (as-> state $
        (get-people $)
        (filter (fn [person] (= (:id person) person-id)) $)
        (first $)))

(defn get-selected-item-peek-data [state]
  (let [person (get-person state (:selected-person-id state))]
    {:image-url (:image person)
     :title (str (:first-name person) " " (:last-name person))
     :subtitle (->> (:groups person)
                    (map :name)
                    (str/join " "))}))

