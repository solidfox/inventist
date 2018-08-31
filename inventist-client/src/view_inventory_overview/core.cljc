(ns view-inventory-overview.core
  (:require [clojure.string :as str]
            [ysera.test :as test]
            [util.inventory.core :as util]
            [#?(:cljs cljs-time.core :clj clj-time.core) :as time]
            [#?(:cljs cljs-time.coerce :clj clj-time.coerce) :as coerce]))

(defn create-long-timestamp []
  (coerce/to-long (time/now)))

(defn create-state
  []
  {:selected-inventory-id                    nil
   :search-terms                             nil

   ;Services
   :latest-acceptable-cache-fetch-time       (create-long-timestamp)
   :should-retry-on-fetch-error              false
   :fetching-inventory-list                  false
   :get-inventory-list-response              nil

   :ongoing-reassign-inventory-item-requests {}})

(defn started-get-inventory-list-service-call [state]
  (-> state
      (assoc :fetching-inventory-list true)
      (assoc :should-retry-on-fetch-error false)))

(defn cache-dirty?
  [state]
  (> (:latest-acceptable-cache-fetch-time state)
     (get-in state [:get-inventory-list-response ::reception-timestamp])))

(defn has-good-get-inventory-list-response [state]
  (and (get-in state [:get-inventory-list-response :body :data])
       (not (cache-dirty? state))))

(defn get-inventory-list-failed? [state]
  (and (not (nil? (:get-inventory-list-response state)))
       (or (not (= 0 (get-in state [:get-inventory-list-response :error-code])))
           (and (:get-inventory-list-response state)
                (not (= 200 (get-in state [:get-inventory-list-response :status])))))))

(defn waiting-for-retry-impulse [state]
  (and (get-inventory-list-failed? state)
       (not (:should-retry-on-fetch-error state))))

(defn set-should-retry-on-fetch-error [state value]
  (assoc state :should-retry-on-fetch-error value))

(defn should-get-inventory-list? [state]
  (and (not (:fetching-inventory-list state))
       (not (has-good-get-inventory-list-response state))
       (not (waiting-for-retry-impulse state))))
;(not (get-in state [:get-inventory-list-response :body :data]))))

(defn receive-get-inventory-list-service-response [state response request]
  (-> state
      (assoc :get-inventory-list-response (-> (util/->clojure-keys response)
                                              (assoc ::reception-timestamp (create-long-timestamp))))
      (assoc :fetching-inventory-list false)))



(defn add-pending-item-reassignment
  [state {:keys [inventory-item-id
                 new-assignee-id]
          :as   reassignment-data}]
  (-> state
      (assoc-in [:ongoing-reassign-inventory-item-requests inventory-item-id] reassignment-data)))

(defn get-unstarted-reassign-inventory-item-requests [state]
  (->> (:ongoing-reassign-inventory-item-requests state)
       (vals)
       (filter (fn [request] (not (:fetching request))))))

(defn should-fetch-reassign-inventory-item [state]
  (< 0 (get-unstarted-reassign-inventory-item-requests state)))

(defn started-fetching-reassign-inventory-item [state inventory-item-id]
  (assoc-in state [:ongoing-reassign-inventory-item-requests inventory-item-id :fetching] true))

(defn receive-reassign-inventory-item-response [state _response _request {:keys [inventory-item-id]}]
  (update state :ongoing-reassign-inventory-item-requests dissoc inventory-item-id))




(defn on-remote-state-mutation
  {:test (fn []
           (as-> (create-state) $
                 (receive-get-inventory-list-service-response $ {} {})
                 (do (test/is-not (cache-dirty? $)) $)
                 (on-remote-state-mutation $ nil)
                 (do (test/is (cache-dirty? $)) $)
                 (do (test/is (should-get-inventory-list? $)) $)))}
  [state _]
  (assoc state :latest-acceptable-cache-fetch-time (create-long-timestamp)))

(defn set-free-text-search
  [state free-text-search]
  (assoc-in state [:search-terms :free-text-search] free-text-search))

(defn set-selected-inventory-id
  [state new-id]
  (assoc state :selected-inventory-id new-id))

(defn inventory-matches
  [inventory {search-string :free-text-search}]
  (let [inventory-string (-> (str/join " " (concat [(:brand inventory)
                                                    (:serial-number inventory)
                                                    (:model-name inventory)
                                                    (:color inventory)]
                                                   (for [group (get-in inventory [:user :groups])]
                                                     (:name group))))
                             (str/lower-case))]
    (every? (fn [search-string-word]
              (str/includes? inventory-string search-string-word))
            (-> search-string
                (str/lower-case)
                (str/split #"\s")))))

(defn get-free-text-search [state]
  (get-in state [:search-terms :free-text-search]))

(defn filtered-inventory
  [state & [search-terms]]
  (when-let [inventory (get-in state [:get-inventory-list-response :body :data :computers])]
    (if-let [search-terms (or search-terms
                              (:search-terms state))]
      (->> inventory
           (filter (fn [item] (inventory-matches item search-terms))))
      inventory)))
