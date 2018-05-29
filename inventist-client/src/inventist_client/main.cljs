(ns inventist-client.main
  (:require [inventist-client.component :as c]
            [rum.core :as rum]
            [authentication.core :as auth]
            [remodular.runtime :as a]
            [inventist-client.core :as core]
            [oops.core :refer [oget ocall]]
            [inventist-client.services :as services]
            [clojure.string :as str]
            [clojure.browser.event :as event]
            [util.inventory.core :as util]
            [ajax.core :refer [GET POST]]))

(enable-console-print!)

(def firebase-auth (oget js/firebase :auth))

(defonce initialized
         (let [path (as-> (oget js/window :location) $
                          (oget $ :pathname))]
           (println "reload")
           (def app-state-atom (atom (core/create-state
                                       {:path path
                                        :mode :prod})))
           (ocall js/window :addEventListener "popstate" (fn [event]
                                                           (let [path (oget event [:target :location :pathname])]
                                                             (swap! app-state-atom
                                                                    core/set-path
                                                                    path))))
           (ocall
             (firebase-auth)
             :onAuthStateChanged
             (fn [user]
               (swap! app-state-atom
                      update-in
                      core/authentication-state-path
                      auth/receive-new-auth-state
                      user)))
           (ocall js/window :addEventListener "offline" (fn []
                                                          (swap! app-state-atom assoc :internet-reachable false)))
           (ocall js/window :addEventListener "online" (fn []
                                                         (swap! app-state-atom assoc :internet-reachable true)))
           true))

(defmethod a/perform-services :prod
  [_ services handle-event]
  {:pre [(not-empty services)]}
  (doseq [{{url    :url
            params :params
            :as    data} :data
           on-response   :after
           state-path    :state-path} services]
    (POST url
          {:body            (:query params)
           :response-format :json
           :keywords?       true
           :handler         (fn [response]
                              (handle-event {:actions
                                             [{:fn-and-args (concat on-response [response data])
                                               :state-path  state-path}]}))}))
  (handle-event {:actions (map (fn [service]
                                 {:fn-and-args (:before service)
                                  :state-path  (or (:state-path service) [])})
                               services)}))

(a/run-modular-app! {:get-view         c/app
                     :get-services     services/get-services
                     :app-state-atom   app-state-atom
                     :logging          {:state-updates true
                                        :services      true
                                        :events        true}})




