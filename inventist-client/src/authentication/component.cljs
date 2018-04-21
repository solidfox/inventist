(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]))

(defn log-in-with-redirect
  []
  (let [provider (js/firebase.auth.GoogleAuthProvider.)]
    (.signInWithRedirect (js/firebase.auth) provider)))

(defn log-out []
  (.signOut (js/firebase.auth)))

(defc login < rem/modular-component
  [{{state :state} :input}]
  [:div {:style {:padding        "3rem"
                 :display        "flex"
                 :flex-direction "column"
                 :align-items    "center"}}
   [:h1 "Inventist"]
   [:h2 "Reinvented inventory."]
   (ant/button {:on-click log-in-with-redirect} "Sign in with Google")
   (when (= :loading (core/status state))
     (ant/spin {:size  "large"
                :tip   "Checking login status..."
                :style {:margin "1rem"}}))])

(defc toolbar-login-status < rem/modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if-let [logged-in-user (core/get-authenticated-user state)]
    [:div {:style
           {:margin "1rem"
            :height        "3rem"
            :display       "flex"
            :align-items   "center"
            :justify-items "center"}}

     [:img {:src   (:photo-url logged-in-user)
            :style {:height       "100%"
                    :borderRadius "1.5rem"}}]
     [:span {:style {:margin "0 1rem"}} (:display-name logged-in-user)]
     (ant/button {:on-click log-out} "Sign out")]))
