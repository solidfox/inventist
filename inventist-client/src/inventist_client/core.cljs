(ns inventist-client.core
  (:require [authentication.core :as auth]
            [inventist-client.page.inventory.core :as inventory-page]
            [inventist-client.page.people.core :as people-page]
            [inventist-client.page.dashboard.core :as dashboard-page]
            [inventist-client.page.contractors.core :as contractors-page]
            [cljs.pprint]
            [inventist-client.page.inventory.core :as inventory-core]
            [remodular.core :as rem]))

(def authentication-state-path [:view-modules :authentication])
(def inventory-page-state-path [:pages :inventory])
(def people-page-state-path [:pages :people])
(def dashboard-page-state-path [:pages :dashboard])
(def contractors-page-state-path [:pages :contractors])

(defn create-state
  [{path :path}]
  (let [selected-inventory-id (when (= (first path) :inventory) (second path))
        selected-person-id    (when (= (first path) :people) (second path))]
    (-> {:path                  [(or (first path) :dashboard)]
         :selected-inventory-id selected-inventory-id
         :selected-person-id    selected-person-id
         :internet-reachable    true}
        (assoc-in authentication-state-path (auth/create-state))
        (assoc-in inventory-page-state-path (inventory-page/create-state {:selected-inventory-id selected-inventory-id}))
        (assoc-in contractors-page-state-path (contractors-page/create-state))
        (assoc-in dashboard-page-state-path (dashboard-page/create-state))
        (assoc-in people-page-state-path (people-page/create-state {:selected-person-id selected-person-id})))))

(defn get-selected-inventory-id [state] (:selected-inventory-id state))

(defn show-inventory-item [state inventory-item-id]
  (-> state
      (assoc :selected-inventory-id inventory-item-id)
      (assoc :path [:inventory])
      (update-in inventory-page-state-path
                 inventory-core/set-selected-inventory-id
                 inventory-item-id)))

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

(defn set-path
  [state path]
  (assoc state :path path))
