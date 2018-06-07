(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [symbols.color :as c]
            [cljs-react-material-ui.rum :as ui]
            [oops.core :refer [oget ocall]]))

(def ^:private firebase-auth (oget js/firebase :auth))

(defn log-in-with-redirect
  []
  (let [google-auth-provider (oget firebase-auth :GoogleAuthProvider)
        provider             (new google-auth-provider)]
    (js/console.log provider)
    (ocall (firebase-auth) :signInWithRedirect provider)))

(defn log-out []
  (ocall (firebase-auth) :signOut))

(defc loading-indicator []
  [:img {:style {:margin     "6px 0 0 8px"
                 :max-height "24px"
                 :max-width  "24px"
                 :float      "left"}
         :src   "/image/svg-loaders/tail-spin.svg"}])

(defc login < (rem/modular-component)
  [{{state :state} :input}]
  (let [loading (= :loading (core/status state))]
    [:div {:style {:padding        "3rem"
                   :display        "flex"
                   :flex-direction "column"
                   :color          c/black
                   :align-items    "center"}}
     [:h1 "Inventist"]
     [:h2 "Inventory reinvented."]
     (ui/raised-button {:label    (if loading "Signing in with Google" "Sign in with Google")
                        :primary  true
                        :disabled loading
                        :icon     (when loading (loading-indicator))
                        :on-click log-in-with-redirect})]))

(defc bar-item-login-status < (rem/modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if-let [logged-in-user (core/get-authenticated-user state)]
    [:div {:style
           {:height        "100%"
            :display       "flex"
            :align-items   "center"
            :justify-items "space-between"}}
     [:img {:src   (:photo-url logged-in-user)
            :style {:height       "90%"
                    :borderRadius "1.5rem"}}]
     [:div {:style {:margin "0 1rem 0 0.5rem" :text-align "left" :line-height "1rem"}}
      [:span {:style {:font-weight "500"}} (:display-name logged-in-user) [:br]]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color c/grey-dark}} "Admin • "]
      [:span {:style {:font-weight "400" :font-size "0.8rem" :color c/danger :cursor "pointer"} :on-click log-out} "Logout"]]]))


