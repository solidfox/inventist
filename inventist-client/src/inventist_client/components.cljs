(ns inventist-client.components
  (:require [authentication.component :as auth]
            [people.component :as people]
            [inventory.component :as inventory]
            [rum.core :refer [defc]]))


(defc app [state]
  [:div]
  ;(auth/login)
  ;(inventory/inventory-details (first (:inventory state)))
  (people/people-details (first (:persons state))))




