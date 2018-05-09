(ns inventist-client.core
  (:require [authentication.core :as auth]
            [inventist-client.page.inventory.core :as inventory-page]
            [inventist-client.page.people.core :as people-page]
            [cljs.pprint]))

(def authentication-state-path [:view-modules :authentication])
(def inventory-page-state-path [:pages :inventory])
(def people-page-state-path [:pages :people])

(defn create-state
  []
  (-> {:path [:inventory]}
      (assoc-in authentication-state-path (auth/create-state))
      (assoc-in inventory-page-state-path (inventory-page/create-state))
      (assoc-in people-page-state-path (people-page/create-state))))

(defn authentication-args [state]
  {:input      {:state (get-in state authentication-state-path)}
   :state-path authentication-state-path})

(defn create-inventory-page-args [state]
  {:input      {:state (get-in state inventory-page-state-path)}
   :state-path inventory-page-state-path})

(defn create-people-page-args [state]
  {:input      {:state (get-in state people-page-state-path)}
   :state-path people-page-state-path})

(defn get-authenticated-user [state]
  (auth/get-authenticated-user (get-in state authentication-state-path)))

(defn logged-in?
  [state]
  (not (nil? (get-authenticated-user state))))

(defn set-active-page
  [state page-id]
  (assoc state :path [page-id]))
