(ns inventist-client.core
  (:require [authentication.core :as auth]
            [people.core :as people]
            [inventory.core :as inventory]
            [cljs.pprint]))

(def authentication-state-path [:view-modules :authentication])
(def person-details-state-path [:person 0])

(defn create-state
  []
  (-> {:path               "/"
       :person             [(people/create-person-detail)]
       :people             [(people/create-person-summary)
                            (people/create-person-summary)
                            (people/create-person-summary)]
       :inventory          [(inventory/create-inventory-detail)]
       :inventory-list     [(inventory/create-inventory-summary)
                            (inventory/create-inventory-summary-2)
                            (inventory/create-inventory-summary)]
       :ownership-ledger   [{:owner-id     44
                             :inventory-id 01
                             :date         "2018-04-14T10:37:46Z"}
                            {:owner-id     44
                             :inventory-id 01
                             :date         "2018-04-14T10:37:46Z"}]}
      (assoc-in authentication-state-path (auth/create-state))))

(defn authentication-args [state]
  {:input      {:state (get-in state authentication-state-path)}
   :state-path authentication-state-path})

(defn get-authenticated-user [state]
  (js/console.log (.-currentUser (js/firebase.auth)))
  (auth/get-authenticated-user (get-in state authentication-state-path)))

(defn logged-in?
  [state]
  (not (nil? (get-authenticated-user state))))
