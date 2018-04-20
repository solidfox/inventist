(ns inventist-client.component
  (:require [authentication.component :as auth]
            [people.component :as people]
            [inventory.componen :as inventory]
            [rum.core :refer [defc]]))


(defc app [state]
  [:div
   ;(auth/login)
   ;(people/people-list (:people state))
   ;(inventory/inventory-list (:inventory-list state))])
   ;(inventory/inventory-details (first (:inventory state)))])
   (people/people-details (first (:person state)))])




