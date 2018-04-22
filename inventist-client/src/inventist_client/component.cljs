(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [authentication.core :as auth.core]
            [people.component :as people]
            [inventory.component :as inventory]
            [inventist-client.page.inventory.component :as inventory-page]
            [rum.core :refer [defc with-key]]))


(defc app
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (js/console.log (core/get-authenticated-user state))
  [:div
   (if (not (core/logged-in? state))
     (auth/login (core/authentication-args state))
     ;(people/people-list (:people state))
     ;(inventory/inventory-list (:inventory-list state)))])
     ;(inventory/inventory-details (first (:inventory state))))])
     ;(people/people-details (first (:person state)))
     [(with-key (auth/toolbar-login-status (core/authentication-args state))
        "auth")
      (with-key (inventory-page/component (core/create-inventory-page-args state))
        "inventory")])])




