(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [authentication.core :as auth.core]
            [people.component :as people]
            [inventory.componen :as inventory]
            [rum.core :refer [defc]]))


(defc app
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (js/console.log (core/get-authenticated-user state))
  [:div
   (if (not (core/logged-in? state))
     (auth/login (core/authentication-args state))
     ;(people/people-list (:people state))
     ;(inventory/inventory-list (:inventory-list state))])
     ;(inventory/inventory-details (first (:inventory state)))])
     (people/people-details (first (:person state))))])




