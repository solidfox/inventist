(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [inventist-client.navbar.component :as navbar]
            [inventist-client.page.inventory.component :as inventory-page]
            [inventist-client.page.people.component :as people-page]
            [inventist-client.page.dashboard.component :as dashboard-page]
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
       :dashboard
       (dashboard-page/component (assoc (core/create-dashboard-page-args state)
                                   :trigger-parent-event trigger-event))
       :people
       (people-page/component (assoc (core/create-people-page-args state)
                                :trigger-parent-event trigger-event))
       :contractors
       (contractors-page/component (assoc (core/create-contractors-page-args state)
                                     :trigger-parent-event trigger-event))
       :inventory
       (inventory-page/component (assoc (core/create-inventory-page-args state)
                                   :trigger-parent-event trigger-event)))]))
