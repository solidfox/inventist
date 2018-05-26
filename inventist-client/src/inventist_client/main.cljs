(ns inventist-client.main
  (:require [inventist-client.component :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [remodular.runtime :as a]
            [inventist-client.core :as core]
            [oops.core :refer [oget ocall]]
            [inventist-client.services :as services]
            [finja.core :as finja]
            [clojure.string :as str]
            [clojure.browser.event :as event]))

(enable-console-print!)

(defonce app-state-atom (atom (core/create-state)))

(a/run-modular-app! {:get-view         c/app
                     :get-services     services/get-services
                     :perform-services services/perform-services
                     :app-state-atom   app-state-atom
                     :logging          {:state-updates true
                                        :services      true
                                        :events        true}})

(def firebase-auth (oget js/firebase :auth))

(defonce _
         (do
           (ocall
             (firebase-auth)
             :onAuthStateChanged
             (fn [user]
               (swap! app-state-atom
                      update-in
                      core/authentication-state-path
                      auth/recieve-new-auth-state
                      user)))
           (ocall js/window :addEventListener "offline" (fn []
                                                          (swap! app-state-atom assoc :internet-reachable false)))
           (ocall js/window :addEventListener "online" (fn []
                                                         (swap! app-state-atom assoc :internet-reachable true)))))


             ;(as-> (finja/get-current-path) $
             ;      (str/split $ "/"))
;      (remove empty? $)
;      (map keyword $)
;      (swap! app-state-atom
;             core/set-path
;             $))

