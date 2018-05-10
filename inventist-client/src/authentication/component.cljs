(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]
            [symbols.color :as c]
            [oops.core :refer [oget ocall]]))

(def ^:private firebase-auth (oget js/firebase :auth))

(defn log-in-with-redirect
  []
  (let [provider (new (.-GoogleAuthProvider firebase-auth))]
    (js/console.log provider)
    (ocall (firebase-auth) :signInWithRedirect provider)))

(defn log-out []
  (ocall (firebase-auth) :signOut))

(defc login < (rem/modular-component identity)
  [{{state :state} :input}]
  [:div {:style {:padding        "3rem"
                 :display        "flex"
                 :flex-direction "column"
                 :color          c/black
                 :align-items    "center"}}
   [:h1 "Inventist"]
   [:h2 "Reinvented inventory."]
   (ant/button {:type     "primary"
                :size     "large"
                :loading  (= :loading (core/status state))
                :on-click log-in-with-redirect} "Sign in with Google")])

(defc bar-item-login-status < (rem/modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if-let [logged-in-user (core/get-authenticated-user state)]
    [:div {:style
           {:height        "100%"
            :display       "flex"
            :align-items   "center"
            :justify-items "space-between"}}

     [:div {:style {:margin "0 1rem" :text-align "right" :line-height "1rem"}}
      [:span {:style {:font-weight "500"}} (:display-name logged-in-user) [:br]]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color c/grey-dark}} "Admin • "]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color c/danger :cursor "pointer"} :on-click log-out} "Logout"]]

     [:img {:src   (:photo-url logged-in-user)
            :style {:height       "100%"
                    :borderRadius "1.5rem"}}]]))
