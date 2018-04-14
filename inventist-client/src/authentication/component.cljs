(ns authentication.component
  (:require [rum.core :refer [defc]]))

(defc login < {:did-mount
               (fn [state]
                 (let [ui-config
                          (clj->js
                            {:signInSuccessUrl "/"
                             :signInOptions    [js/firebase.auth.GoogleAuthProvider.PROVIDER_ID]})
                       ; firebase.auth.FacebookAuthProvider.PROVIDER_ID,
                       ; firebase.auth.TwitterAuthProvider.PROVIDER_ID,
                       ; firebase.auth.GithubAuthProvider.PROVIDER_ID,
                       ; firebase.auth.EmailAuthProvider.PROVIDER_ID,
                       ui (js/firebaseui.auth.AuthUI. (js/firebase.auth))]
                   (js/console.log ui)
                   (.start ui "#login" ui-config))
                   ;(js/firebase.auth. onAuthStateChanged js/console.log))
                 state)}
  []
  [:div {:id "login"}])
