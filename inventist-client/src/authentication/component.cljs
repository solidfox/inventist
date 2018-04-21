(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]))

(defn log-in-with-redirect
  []
  (let [provider (js/firebase.auth.GoogleAuthProvider.)]
    (.signInWithRedirect (js/firebase.auth) provider)))

(defc login < rem/modular-component
  [{{state :state} :input}]
  [:div {:style {:padding        "3rem"
                 :display        "flex"
                 :flex-direction "column"
                 :align-items    "center"}}
   [:h1 {:style {}} "Welcome to inventist!"]
   (ant/button {:on-click log-in-with-redirect} "Log in with Google")
   (when (= :loading (core/status state))
     (ant/spin {:size "large"
                :tip "Checking login status..."
                :style {:margin "1rem"}}))])

(defc toolbar-login-status < rem/modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}])
