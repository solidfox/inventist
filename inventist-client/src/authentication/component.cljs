(ns authentication.component
  (:require [rum.core :refer [defc]]
            [rum.core :as rum]
            [remodular.core :as rem]
            [authentication.core :as core]
            [symbols.color :as color]
            [cljs-react-material-ui.rum :as ui]
            [symbols.style :as style]
            [symbols.general :as s-general]
            [symbols.mixin :refer [hovered-mixin toggle-mixin]]
            [oops.core :refer [oget ocall]]
            [util.inventory.core :as util]))

(def ^:private firebase-auth (oget js/firebase :auth))

(defn log-in-with-redirect
  []
  (let [google-auth-provider (oget firebase-auth :GoogleAuthProvider)
        provider             (new google-auth-provider)]
    (js/console.log provider)
    (ocall (firebase-auth) :signInWithRedirect provider)))

(defn log-out []
  (cond (js/confirm "Do you wish to logout?") (ocall (firebase-auth) :signOut)))

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

(def menu-item-height "2.5rem")
(def menu-item-y-padding "0.5rem")
(def menu-item-total-height (str "(" menu-item-height "+" menu-item-y-padding" * 2)"))

(defc user-menu-item
  [{title    :title
    icon     :icon
    color    :color
    on-click :on-click}]
  [:div {:class    (style/user-bar-item)
         :style    {:height                menu-item-height
                    :color                 (or color color/light-context-primary-text)
                    :width                 "auto"
                    :padding               (str menu-item-y-padding " 1rem")
                    :cursor                "pointer"
                    :display               "grid"
                    :grid-template-columns "1.5rem auto"
                    :background-color      color/transparent}
         :on-click on-click}
   [:i {:class icon
        :style {:font-size  "1.25rem"
                :align-self "center"}}]

   [:div {:style {:font-size   "1rem"
                  :align-self  "center"
                  :font-weight "500"
                  :margin      "0 1rem"}}
    title]])

(rum/defc expandable-user-menu
  [{:keys [expanded menu-items]}]
  [:div {:style {:padding        (if expanded "0.5rem 1rem 1rem 1rem" "0 1rem")
                 :display        "flex"
                 :flex-direction "column"
                 :max-height     (if expanded (str "calc(" (count menu-items) "*" menu-item-total-height ")") 0) ;FIXME Padding not being counted in total height.
                 :transition     "all 0.7s"}}
   menu-items])





(rum/defcs user-bar < (rem/modular-component)
                      (toggle-mixin {:toggle-state-key :expanded
                                     :on-fn-key        :trigger-expand
                                     :off-fn-key       :trigger-collapse})
  [{:keys [expanded trigger-expand trigger-collapse]}
   {{state :state} :input
    trigger-event  :trigger-event}]
  (if-let [logged-in-user (core/get-authenticated-user state)]
    [:div {:style {:height           "auto"
                   :background-color color/light-context-background}}
     [:div {:style {:height                "2.5rem"
                    :background-color      color/light-context-background
                    :padding               "0.5rem 1rem"
                    :display               "grid"
                    :grid-template-columns "3.5rem auto 2rem"}}

      [:img {:src   (:photo-url logged-in-user)
             :style {:height       "2.5rem"
                     :width        "2.5rem"
                     :object-fit   "cover"
                     :borderRadius "1.5rem"}}]
      [:div {:style {:display        "flex"
                     :flex-direction "column"
                     :margin-top     "0.25rem"}}
       [:div {:style {:font-size   "1rem"
                      :font-weight "500"
                      :color       color/light-context-title-text}}
        (:display-name logged-in-user)]
       [:div {:style {:font-size "0.75rem"
                      :color     color/light-context-primary-text}}
        "Admin"]]
      [:div {:style    {:margin "auto 1rem"
                        :color  color/light-context-secondary-text}
             :on-click (if expanded trigger-collapse trigger-expand)}
       [:i {:class "fas fa-bars"}]]]
     (expandable-user-menu {:expanded expanded
                            :menu-items [(user-menu-item {:title    "Profile"
                                                          :icon     "far fa-user-circle"
                                                          :on-click (fn [] (println "Profile"))})
                                         (user-menu-item {:title    "Settings"
                                                          :icon     "far fa-sun"
                                                          :on-click (fn [] (println "Settings"))})
                                         (user-menu-item {:title    "Logout"
                                                          :icon     "fas fa-sign-out-alt"
                                                          :color    color/danger
                                                          :on-click log-out})]})]))




;(navbar/navigation-badge {:title         "Profile"
;                          :on-click      log-out
;                          :icon          "fas fa-user"
;                          :selected-item {:name        (:display-name logged-in-user)
;                                          :photo       (:photo-url logged-in-user)
;                                          :custom-text "Admin"
;                                          :on-click    log-out
;                                          :subtitle    "Logout"}})))


;[:div {:style {:margin "0 1rem 0 0.5rem" :text-align "left" :line-height "1rem"}}
; [:span {:style {:font-weight "500"}}  [:br]]
; [:span {:style {:font-weight "400" :font-size "0.8rem" :color color/grey-dark}} "Admin • "]
; [:span {:style {:font-weight "400" :font-size "0.8rem" :color color/danger :cursor "pointer"} :on-click log-out} "Logout"]]]))


