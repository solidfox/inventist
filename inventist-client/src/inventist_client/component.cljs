(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [inventist-client.navbar.component :as navbar]
            [inventist-client.page.inventory.component :as inventory-page]
            [inventist-client.page.people.component :as people-page]
            [inventist-client.page.contractors.component :as contractors-page]
            [rum.core :refer [defc with-key]]
            [remodular.core :refer [modular-component]]
            [inventist-client.event :refer [handle-event]]
            [symbols.color :as c]))


(defc app < (modular-component handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if (not (core/logged-in? state))
    (auth/login (core/authentication-args state))
    [:div {:style {:height             "100vh"
                   :display            "grid"
                   :backgroundColor    c/white
                   :grid-template-rows "3.5rem calc(100% - 3.5rem)"}}
     (navbar/navigation-bar
       {:auth-status-item (auth/bar-item-login-status (core/authentication-args state))
        :current-path     (:path state)
        :trigger-event    trigger-event})
     (condp = (first (:path state))
       :people
       (people-page/component (core/create-people-page-args state))
       :contractors
       (contractors-page/component (core/create-contractors-page-args state))
       :inventory
       (inventory-page/component (core/create-inventory-page-args state)))]))
