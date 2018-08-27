(ns inventist-client.component
  (:require [inventist-client.core :as core]
            [authentication.component :as auth]
            [inventist-client.navbar.component :as navbar]
            [collections.component :as collections]
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
            [symbols.general :as s-general]
            [util.inventory.core :as util]))


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
      [:div {:style (merge {:height          (:viewport-height state)
                            :backgroundColor c/light-context-background}
                           (cond (> (:viewport-width state) 799) {:display               "grid"
                                                                  :grid-template-columns "20rem auto"}
                                 (< (:viewport-width state) 800) {:width "100%"}))}

       (navbar/collection-sidebar
         {:sections        [(collections/collections-view {:trigger-event      trigger-event
                                                           :current-path       (:path state)
                                                           :collection-list    collections/collections-list
                                                           :collection-heading "Collections"})]
          :user-bar        (auth/user-bar (core/authentication-args state))
          :current-path    (:path state)
          :trigger-event   trigger-event
          :viewport-height (:viewport-height state)
          :viewport-width  (:viewport-width state)})

       (condp = (first (:path state))
         :dashboard
         (dashboard-page/component (assoc (core/create-dashboard-page-args state)
                                     :trigger-parent-event trigger-event
                                     :viewport-height (:viewport-height state)
                                     :viewport-width (:viewport-width state)))
         :people
         (people-page/component (assoc (core/create-people-page-args state)
                                  :trigger-parent-event trigger-event
                                  :viewport-height (:viewport-height state)
                                  :viewport-width (:viewport-width state)))
         :contractors
         (contractors-page/component (assoc (core/create-contractors-page-args state)
                                       :trigger-parent-event trigger-event
                                       :viewport-height (:viewport-height state)
                                       :viewport-width (:viewport-width state)))
         :inventory
         (inventory-page/component (assoc (core/create-inventory-page-args state)
                                     :trigger-parent-event trigger-event
                                     :viewport-height (:viewport-height state)
                                     :viewport-width (:viewport-width state))))

       (when (not (:internet-reachable state)) (notifications/connection-bar))])))
