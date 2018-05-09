(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]
            [oops.core :refer [oget ocall]]))

(def ^:private firebase-auth (oget js/firebase :auth))

(defn log-in-with-redirect
  []
  (let [provider ((oget firebase-auth :GoogleAuthProvider).)]
    (ocall (firebase-auth) :signInWithRedirect provider)))

(defn log-out []
  (ocall (firebase-auth) :signOut))

(defc login < (rem/modular-component identity)
  [{{state :state} :input}]
  [:div {:style {:padding        "3rem"
                 :display        "flex"
                 :flex-direction "column"
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
           {;:margin        "1rem"
            :height        "100%"
            :display       "flex"
            :align-items   "center"
            :justify-items "space-between"}}

     ;(ant/button {:style {:margin "1rem"} :type "danger" :on-click log-out} "Sign out")

     [:div {:style {:margin "0 1rem" :text-align "right" :line-height "1rem"}}
      [:span {:style {:font-weight "500" :font-size "1rem"}} (:display-name logged-in-user)[:br]]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color "#4a4a4a"}} "Admin • "]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color "red" :cursor "pointer"}
              :on-click log-out} "Logout"]]

     [:img {:src   (:photo-url logged-in-user)
            :style {:height       "100%"
                    :borderRadius "1.5rem"}}]]))
