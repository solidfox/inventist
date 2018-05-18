(ns inventist-client.main
  (:require [inventist-client.component :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [remodular.runtime :as a]
            [inventist-client.core :as core]
            [oops.core :refer [oget ocall]]
            [inventist-client.services :as services]))

(enable-console-print!)

(defonce app-state-atom (atom (core/create-state)))

(defn perform-services
  [services handle-event]
  {:pre [(not-empty services)]}
  (doseq [{data       :data
           after      :after
           state-path :state-path} services]
    (mock-url-request! data
                       (fn [response]
                         (handle-event {:actions [{:fn-and-args (concat after [response])
                                                   :state-path  state-path}]}))))
  (handle-event {:actions (map (fn [service]
                                 {:fn-and-args (:before service)
                                  :state-path  (or (:state-path service) [])})
                               services)}))

(a/run-modular-app! {:get-view         c/app
                     :get-services     services/get-services
                     :perform-services perform-services
                     :app-state-atom   app-state-atom
                     :logging          {:state-updates true
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
