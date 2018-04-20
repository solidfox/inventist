(ns inventist-client.main
  (:require [inventist-client.component :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [people.core :as people]
            [inventory.core :as inventory]
            [remodular.runtime :as a]))

(enable-console-print!)

(println "This text is printed from src/inventist-client/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom {:authentication (auth/create-state)
                               :person              [(people/create-person-detail)]
                               :people              [(people/create-person-summary)
                                                     (people/create-person-summary)
                                                     (people/create-person-summary)]
                               :inventory           [(inventory/create-inventory-detail)]
                               :inventory-list      [(inventory/create-inventory-summary)
                                                     (inventory/create-inventory-summary-2)
                                                     (inventory/create-inventory-summary)]
                               :ownership-ledger    [{:owner-id     44
                                                      :inventory-id 01
                                                      :date         "2018-04-14T10:37:46Z"}
                                                     {:owner-id     44
                                                      :inventory-id 01
                                                      :date         "2018-04-14T10:37:46Z"}]}))

(a/run-modular-app! {:get-view       c/app
                     :get-services   nil
                     :app-state-atom app-state-atom
                     :logging        {:state-updates true
                                      :services      true
                                      :events        true}})

(rum/mount (c/app (deref app-state-atom))
           (. js/document (getElementById "app")))
