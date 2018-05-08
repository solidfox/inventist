(ns inventist-client.main
  (:require [inventist-client.component :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [remodular.runtime :as a]
            [inventist-client.core :as core]
            [oops.core :refer [oget ocall]]))

(enable-console-print!)

(defonce app-state-atom (atom (core/create-state)))

(a/run-modular-app! {:get-view       c/app
                     :get-services   nil
                     :app-state-atom app-state-atom
                     :logging        {:state-updates true
                                      :services      true
                                      :events        true}})

(def firebase-auth (oget js/firebase :auth))

(ocall
  (firebase-auth)
  :onAuthStateChanged
  (fn [user]
    (swap! app-state-atom
           update-in
           core/authentication-state-path
           auth/recieve-new-auth-state
           user)))
