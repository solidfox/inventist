(ns inventist-client.main
  (:require [inventist-client.component :as component]
            [authentication.core :as auth]
            [remodular.engine :as engine]
            [remodular.core :as rem]
            [inventist-client.core :as core]
            [oops.core :refer [oget ocall]]
            [inventist-client.services :as services]
            [goog.net.XhrIo :as xhr]
            [util.inventory.core :as util]))

(enable-console-print!)

(def firebase-auth (oget js/firebase :auth))

(defonce initialized
         (let [path           (as-> (oget js/window :location) $
                                    (oget $ :pathname))
               app-state-atom (atom (core/create-state
                                      {:path path
                                       :mode :prod}))]
           (println "Initializing Runtime")
           (def app-state-atom app-state-atom)
           (ocall js/window :addEventListener "popstate" (fn [event]
                                                           (let [path (oget event [:target :location :pathname])]
                                                             (swap! app-state-atom
                                                                    core/set-path
                                                                    path))))
           (ocall (firebase-auth) :onAuthStateChanged (fn [user]
                                                        (swap! app-state-atom
                                                               update-in
                                                               (concat [::engine/render-input] core/authentication-state-path)
                                                               auth/receive-new-auth-state
                                                               user)))
           (ocall js/window :addEventListener "resize" (fn []
                                                         (swap! app-state-atom assoc-in [::engine/render-input :viewport-width] (max js/document.documentElement.clientWidth js/window.innerWidth))
                                                         (swap! app-state-atom assoc-in [::engine/render-input :viewport-height] (max js/document.documentElement.clientHeight js/window.innerHeight))))
           (ocall js/window :addEventListener "offline" (fn []
                                                          (swap! app-state-atom assoc-in [::engine/render-input :internet-reachable] false)))
           (ocall js/window :addEventListener "online" (fn []
                                                         (swap! app-state-atom assoc-in [::engine/render-input :internet-reachable] true)))
           true))

(def services-sanity-check-atom (atom {:n-rapid-fetches 0
                                       :last-request    nil}))

(defn same-request [req1 req2]
  (= {:data       (:data req1)
      :state-path (:state-path req1)}
     {:data       (:data req2)
      :state-path (:state-path req2)}))

(defn service-is-sane!? [service]
  (if (same-request (:last-request (deref services-sanity-check-atom))
                    service)
    (swap! services-sanity-check-atom update
           :n-rapid-fetches inc)
    (swap! services-sanity-check-atom assoc
           :n-rapid-fetches 0
           :last-request service))
  (> 10 (:n-rapid-fetches (deref services-sanity-check-atom))))

(defn http-json-request [{:keys [url
                                 body
                                 callback]}]
  (xhr/send url
            (fn [xhr-event]
              (let [xhr-request-object (.-target xhr-event)
                    status             (.getStatus xhr-request-object)
                    service-response   {:body          (when (= status 200)
                                                         (-> (.getResponseJson xhr-request-object)
                                                             (js->clj {:key-fn keyword})))
                                        :status        status
                                        :error-code    (.getLastErrorCode xhr-request-object)
                                        :error-message (.getLastError xhr-request-object)}]
                (callback service-response)))               ;; handler
            "POST"
            (js/JSON.stringify (clj->js body))
            #js {:Content-Type "application/json"}))

(defmethod engine/perform-services :prod
  [_ services handle-event]
  {:pre [(not-empty services)]}

  (handle-event
    {:actions
     (doall
       (for [{{url    :url
               params :params
               :as    data}         :data
              before-fn             :before
              on-response           :after
              remote-state-mutation :remote-state-mutation
              state-path            :state-path
              :as                   service} services]
         (when (service-is-sane!? service)
           (do
             (http-json-request {:url      url
                                 :body     params
                                 :callback (fn [service-response]
                                             (handle-event (cond-> (rem/create-anonymous-event)
                                                                   true
                                                                   (rem/append-action
                                                                     {:fn-and-args (concat [(first on-response)]
                                                                                           [service-response data]
                                                                                           (rest on-response))
                                                                      :state-path  state-path})
                                                                   remote-state-mutation
                                                                   (rem/append-action
                                                                     (rem/create-action
                                                                       {:name        :on-remote-state-mutation
                                                                        :fn-and-args [core/on-remote-state-mutation remote-state-mutation]})))))})
             {:fn-and-args before-fn
              :state-path  (or state-path [])}))))}))
;; headers



(engine/run-modular-app! {:get-view        component/app
                          :get-services   services/get-services
                          :app-state-atom app-state-atom
                          :logging        {:state-updates false
                                           :services      true
                                           :events        true}})




