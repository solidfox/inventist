(ns inventist-client.navbar.component
  (:require [rum.core :refer [defc]]
            [remodular.core :as rem]
            [authentication.core :as core]
            [antizer.rum :as ant]))

(defc navigation-icon
  [{title    :title
    image    :image}]
  [:div {:style {:height "100%" :text-align "center" :margin "0 1rem" :cursor "pointer"}}
   [:div {:style {:height "60%" :text-align "center"}}
    [:img {:src image :style {:height       "100%"
                              :borderRadius "1.5rem"}}]]
   [:div title]])

(defc navigation-bar < rem/modular-component
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (if-let [logged-in-user (core/get-authenticated-user state)]
    [:div {:style
           {:margin         "1rem"
            :height         "3rem"
            :display        "flex"
            :flex-direction "row"
            :align-items    "center"
            :justify-content  "space-between"}}
     [:div {:style {:height "100%" :text-align "left"}}
      [:span [:img {:src   "image/ghs-icon.svg"
                    :style {:height       "100%"
                            :borderRadius "1.5rem"}}]]
      [:span {:style {:font-size   "24px"
                      :font-weight "bold"
                      :color       "black"}} " Inventist"]]

     [:div {:style {:height "100%" :text-align "center" :display "flex"}}
      (navigation-icon {:title "Dashboard" :image "image/ghs-icon.svg"})
      (navigation-icon {:title "People" :image "image/ghs-icon.svg"})
      (navigation-icon {:title "Inventory" :image "image/ghs-icon.svg"})
      (navigation-icon {:title "Contractor" :image "image/ghs-icon.svg"})]



     [:div {:style {:height "100%" :text-align "right"}}
      [:span {:style {:margin "0 1rem"}} (ant/button "Sign out")]
      [:img {:src   (:photo-url logged-in-user)
             :style {:height       "100%"
                     :borderRadius "1.5rem"}}]]]))



;(:display-name logged-in-user)