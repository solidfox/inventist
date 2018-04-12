(ns inventist-client.core
  (:require [reagent.core :as reagent :refer [atom]]
            [inventist-client.components :as c]))

(enable-console-print!)

(println "This text is printed from src/inventist-client/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text  "Hello world!"
                          :person
                          {:id 44
                           :type "teacher"
                           :image "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
                           :fname "Hanna"
                           :lname "Alenius"
                           :email "hanna.alenius@gripsholmsskolan.se"
                           :username "hanna.alenius"
                           :sex "m"
                           :phone "0701039070"}}))




(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this and watch it change!"]])

(reagent/render-component [c/people-details (:person @app-state)]
                          (. js/document (getElementById "app")))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)
