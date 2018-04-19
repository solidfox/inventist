(ns inventist-client.main
  (:require [inventist-client.components :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [people.core :as people]
            [inventory.core :as inventory]))

(enable-console-print!)

(println "This text is printed from src/inventist-client/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:authentication   (auth/create-state)
                          :persons          [(people/create-person-detail)
                                             (people/create-person-detail)]
                          :inventory        [(inventory/create-inventory-detail)
                                             (inventory/create-inventory-detail)]
                          :ownership-ledger [{:owner-id     44
                                              :inventory-id 01
                                              :date         "2018-04-14T10:37:46Z"}
                                             {:owner-id     44
                                              :inventory-id 01
                                              :date         "2018-04-14T10:37:46Z"}]}))

(rum/mount (c/app (deref app-state))
           (. js/document (getElementById "app")))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

