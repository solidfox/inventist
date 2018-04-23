(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [authentication.core :as auth.core]
            [people.component :as people]
            [inventory.component :as inventory]
            [inventist-client.navbar.component :as navbar]
            [inventist-client.page.inventory.component :as inventory-page]
            [rum.core :refer [defc with-key]]))


(defc app
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if (not (core/logged-in? state))
    (auth/login (core/authentication-args state))
    ;(people/people-list (:people state))
    ;(inventory/inventory-list (:inventory-list state)))])
    ;(inventory/inventory-details (first (:inventory state))))])
    ;(people/people-details (first (:person state)))
    [:div {:style {:height             "100vh"
                   :display            "grid"
                   :grid-template-rows "5rem calc(100% - 5rem)"}}
     (navbar/navigation-bar {:auth-status-item
                             (auth/bar-item-login-status (core/authentication-args state))})
     (inventory-page/component (core/create-inventory-page-args state))]))
