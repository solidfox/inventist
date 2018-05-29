(ns inventist-client.core
  (:require [authentication.core :as auth]
            [inventist-client.page.inventory.core :as inventory-page]
            [inventist-client.page.people.core :as people-page]
            [inventist-client.page.dashboard.core :as dashboard-page]
            [inventist-client.page.contractors.core :as contractors-page]
            [cljs.pprint]
            [inventist-client.page.inventory.core :as inventory-core]
            [remodular.core :as rem]
            [clojure.string :as str]
            [util.inventory.core :as util]))

(def authentication-state-path [:view-modules :authentication])
(def inventory-page-state-path [:pages :inventory])
(def people-page-state-path [:pages :people])
(def dashboard-page-state-path [:pages :dashboard])
(def contractors-page-state-path [:pages :contractors])

(defn parse-path
  [path]
  (let [[head & tail] (remove empty? (str/split path #"/"))
        page (keyword head)]
    (merge {:path [page]}
           (when (= page :inventory) {:selected-inventory-id (first tail)})
           (when (= page :people) {:selected-person-id (first tail)}))))

(defn create-state
  [{path :path
    mode :mode}]
  (let [{selected-inventory-id :selected-inventory-id
         selected-person-id    :selected-person-id
         path                  :path
         :as                   initial-state} (parse-path path)]
    (-> initial-state
        (assoc :internet-reachable true
               :path [(or (first path) :dashboard)]
               :mode mode)
        (assoc-in authentication-state-path (auth/create-state))
        (assoc-in inventory-page-state-path (inventory-page/create-state {:selected-inventory-id selected-inventory-id}))
        (assoc-in contractors-page-state-path (contractors-page/create-state))
        (assoc-in dashboard-page-state-path (dashboard-page/create-state))
        (assoc-in people-page-state-path (people-page/create-state {:selected-person-id selected-person-id})))))

(defn get-selected-inventory-id [state] (:selected-inventory-id state))

(defn get-current-location-bar-path [state]
  (case (first (:path state))
    :inventory
    (str/join "/" [""
                   (subs (str :inventory) 1)
                   (:selected-inventory-id state)])
    :people
    (str/join "/" [""
                   (subs (str :people) 1)
                   (:selected-person-id state)])
    "/"))

(defn show-inventory-item [state inventory-item-id]
  (-> state
      (assoc :selected-inventory-id inventory-item-id)
      (assoc :path [:inventory])
      (update-in inventory-page-state-path
                 inventory-core/set-selected-inventory-id
                 inventory-item-id)))

(defn show-person [state person-id]
  (-> state
      (assoc :selected-person-id person-id)
      (assoc :path [:people])
      (update-in people-page-state-path
                 people-page/set-selected-person-id
                 person-id)))

(defn authentication-args [state]
  {:input      {:state (get-in state authentication-state-path)}
   :state-path authentication-state-path})

(defn create-inventory-page-args [state]
  {:input      {:state                 (get-in state inventory-page-state-path)
                :selected-inventory-id (get-selected-inventory-id state)}
   :state-path inventory-page-state-path})

(defn create-dashboard-page-args [state]
  {:input      {:state (get-in state dashboard-page-state-path)}
   :state-path dashboard-page-state-path})

(defn create-people-page-args [state]
  {:input      {:state (get-in state people-page-state-path)}
   :state-path people-page-state-path})

(defn create-contractors-page-args [state]
  {:input      {:state (get-in state contractors-page-state-path)}
   :state-path contractors-page-state-path})

(defn get-authenticated-user [state]
  (auth/get-authenticated-user (get-in state authentication-state-path)))

(defn logged-in?
  [state]
  (not (nil? (get-authenticated-user state))))

(defn set-active-page
  [state page-id]
  (assoc state :path [page-id]))

(defn set-path [state path & [push-state?]]
  (let [{selected-inventory-id :selected-inventory-id
         selected-person-id    :selected-person-id
         [page]                :path} (parse-path path)]
    (case page
      :inventory
      (show-inventory-item state selected-inventory-id)
      (set-active-page state page))))
