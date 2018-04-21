(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]))

(def firebase-auth-ui-config
  (clj->js
    {:signInSuccessUrl "/"
     :signInOptions    [js/firebase.auth.GoogleAuthProvider.PROVIDER_ID]}))
; firebase.auth.FacebookAuthProvider.PROVIDER_ID,
; firebase.auth.TwitterAuthProvider.PROVIDER_ID,
; firebase.auth.GithubAuthProvider.PROVIDER_ID,
; firebase.auth.EmailAuthProvider.PROVIDER_ID,

(defc login < rem/modular-component
              {:after-render
               (fn [rum-state]
                 (.start (get-in (first (:rum/args rum-state)) [:input :state :firebase-auth-ui]) "#login" firebase-auth-ui-config)
                 rum-state)}
  [_]
  [:div
   [:h1 "Welcome to inventist!"]
   [:div {:id "login"}]])

(defc toolbar-login-status < rem/modular-component
  [{{state :state} :input
    trigger-event :trigger-event}])
