(ns inventist-client.core
  (:require [authentication.core :as auth]
            [people.core :as people]
            [inventist-client.page.inventory.core :as inventory-page]
            [inventist-client.page.people.core :as people-page]
            [cljs.pprint]))

(def authentication-state-path [:view-modules :authentication])
(def inventory-page-state-path [:pages :inventory])
(def people-page-state-path [:pages :people])

(defn create-state
  []
  (-> {:path             [:people]
       :person           [(people/create-person-detail)]
       :people           [(people/create-person-summary {:id "01"})
                          (people/create-person-summary {:id "02"})
                          (people/create-person-summary {:id "03"})]
       :ownership-ledger [{:owner-id     44
                           :inventory-id 01
                           :date         "2018-04-14T10:37:46Z"}
                          {:owner-id     44
                           :inventory-id 01
                           :date         "2018-04-14T10:37:46Z"}]}
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
