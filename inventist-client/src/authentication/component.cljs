(ns authentication.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [symbols.color :as color]
            [cljs-react-material-ui.rum :as ui]
            [symbols.style :as style]
            [inventist-client.navbar.component :as navbar]
            [oops.core :refer [oget ocall]]))

(def ^:private firebase-auth (oget js/firebase :auth))

(defn log-in-with-redirect
  []
  (let [google-auth-provider (oget firebase-auth :GoogleAuthProvider)
        provider (new google-auth-provider)]
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
                   :color          color/black
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
    (navbar/navigation-badge {:title         "Profile"
                              :on-click      log-out
                              :icon          "fas fa-user"
                              :selected-item {:name        (:display-name logged-in-user)
                                              :photo       (:photo-url logged-in-user)
                                              :custom-text "Admin"
                                              :on-click    log-out
                                              :subtitle    "Logout"}})))
;[:div {:class (style/navbar-card)
;       :style {:height        "2.5rem"
;               :display       "flex"
;               :align-items   "center"
;               :justify-items "space-between"}}
; [:img {:src   (:photo-url logged-in-user)
;        :style {:height       "2.5rem"
;                :width        "2.5rem"
;                :object-fit   "cover"
;                :borderRadius "1.5rem"}}]
;
; [:div {:style        {:margin "0rem 0.5rem"}
;        :border-right (str "0.1rem solid " color/grey-dark)}]
;
;
; [:div {:style     {:display        "flex"
;                    :flex-direction "column"
;                    :margin-top     "0rem"
;                    :color          color/grey-dark
;                    :border-radius  "0.5rem"}
;        :draggable true}
;  [:div {:style {:font-size   "0.5rem"
;                 :font-weight "600"
;                 :color       color/theme}}
;   "Admin"]
;  [:div {:style {:font-size   "0.9rem"
;                 :font-weight "600"}}
;   (:display-name logged-in-user)]
;  [:div {:style {:font-size "0.7rem"
;                 :color     color/danger}}
;   "Logout"]]]))

;[:div {:style {:margin "0 1rem 0 0.5rem" :text-align "left" :line-height "1rem"}}
; [:span {:style {:font-weight "500"}}  [:br]]
; [:span {:style {:font-weight "400" :font-size "0.8rem" :color color/grey-dark}} "Admin • "]
; [:span {:style {:font-weight "400" :font-size "0.8rem" :color color/danger :cursor "pointer"} :on-click log-out} "Logout"]]]))


