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
            [symbols.color :as c]
            [inventist-client.notifications.component :as notifications]
            [cljs-react-material-ui.rum :as ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [oops.core :as oops]
            [symbols.general :as s-general]))


(defc app < (modular-component handle-event)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (when (not= (oops/oget js/window [:location :pathname]) (core/get-current-location-bar-path state))
    (oops/ocall js/window [:history :pushState] nil "" (core/get-current-location-bar-path state)))
  (ui/mui-theme-provider
    {:mui-theme (get-mui-theme)}
    (if (not (core/logged-in? state))
      (auth/login (core/authentication-args state))
      ; Else Logged in!

      (cond (> (:viewport-width state) 800)                 ;Desktop-view
            [:div {:style {:height                (:viewport-height state)
                           :display               "grid"
                           :backgroundColor       c/light-context-background
                           :grid-template-columns "20rem auto"}}

             (navbar/collection-sidebar
               {:auth-status-item (auth/bar-item-login-status (core/authentication-args state))
                :current-path     (:path state)
                :trigger-event    trigger-event})

             (condp = (first (:path state))
               :dashboard
               (dashboard-page/component (assoc (core/create-dashboard-page-args state)
                                           :trigger-parent-event trigger-event
                                           :viewport-height (:viewport-height state)
                                           :viewport-width  (:viewport-width state)))
               :people
               (people-page/component (assoc (core/create-people-page-args state)
                                        :trigger-parent-event trigger-event
                                        :viewport-height (:viewport-height state)
                                        :viewport-width  (:viewport-width state)))
               :contractors
               (contractors-page/component (assoc (core/create-contractors-page-args state)
                                             :trigger-parent-event trigger-event
                                             :viewport-height (:viewport-height state)
                                             :viewport-width  (:viewport-width state)))
               :inventory
               (inventory-page/component (assoc (core/create-inventory-page-args state)
                                           :trigger-parent-event trigger-event
                                           :viewport-height (:viewport-height state)
                                           :viewport-width  (:viewport-width state))))

             (when (not (:internet-reachable state)) (notifications/connection-bar))
             (notifications/size-bar {:viewport-height (:viewport-height state)
                                      :viewport-width  (:viewport-width state)})]

            (< (:viewport-width state) 800)                 ;mobile-view
            [:div {:style {:height              (:viewport-height state)
                           :display             "grid"
                           :backgroundColor     c/light-context-background
                           :grid-template-rows  "calc(100% - 3.5rem) 3.5rem"
                           :background-image    "url(\"/image/GHS-watermark.svg\")"
                           :background-position "50%"
                           :background-size     "15%"
                           :background-repeat   "no-repeat"}}

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
                                           :trigger-parent-event trigger-event)))

             (navbar/navigation-bar-desktop
               {:auth-status-item (auth/bar-item-login-status (core/authentication-args state))
                :current-path     (:path state)
                :trigger-event    trigger-event})

             (when (not (:internet-reachable state)) (notifications/connection-bar))
             (notifications/size-bar {:viewport-height (:viewport-height state)
                                      :viewport-width  (:viewport-width state)})]))))


