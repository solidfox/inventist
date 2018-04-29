(ns inventist-client.core
  (:require [authentication.core :as auth]
            [people.core :as people]
            [inventist-client.page.inventory.core :as inventory-page]
            [cljs.pprint]))

(def authentication-state-path [:view-modules :authentication])
(def inventory-page-state-path [:pages :inventory])

(defn create-state
  []
  (-> {:path             "/"
       :person           [(people/create-person-detail)]
       :people           [(people/create-person-summary)
                          (people/create-person-summary)
                          (people/create-person-summary)]
       :ownership-ledger [{:owner-id     44
                           :inventory-id 01
                           :date         "2018-04-14T10:37:46Z"}
                          {:owner-id     44
                           :inventory-id 01
                           :date         "2018-04-14T10:37:46Z"}]}
      (assoc-in authentication-state-path (auth/create-state))
      (assoc-in inventory-page-state-path (inventory-page/create-state))))

(defn authentication-args [state]
  {:input      {:state (get-in state authentication-state-path)}
   :state-path authentication-state-path})

(defn create-inventory-page-args [state]
  {:input      {:state (get-in state inventory-page-state-path)}
   :state-path inventory-page-state-path})

(defn get-authenticated-user [state]
  (auth/get-authenticated-user (get-in state authentication-state-path)))

(defn logged-in?
  [state]
  (not (nil? (get-authenticated-user state))))
