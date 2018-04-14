(ns inventist-client.main
  (:require [inventist-client.components :as c]
            [rum.core :as rum]
            [authentication.core :as auth]))

(enable-console-print!)

(println "This text is printed from src/inventist-client/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:authentication (auth/create-state)
                          :person  {:id       44
                                    :type     "teacher"
                                    :image    "http://www.suthersgeorge.com/wp-content/uploads/2017/06/person-placeholder.jpg"
                                    :fname    "Hanna"
                                    :lname    "Alenius"
                                    :email    "hanna.alenius@gripsholmsskolan.se"
                                    :username "hanna.alenius"
                                    :sex      "m"
                                    :phone    "0701039070"}
                          :history [
                                    {:id   01
                                     :date "2017-09-20"
                                     :desc "Assigned Macbook Pro 13"}
                                    {:id   02
                                     :date "2017-10-10"
                                     :desc "Returned Macbook Pro 13"}]}))

(rum/mount (c/app (deref app-state))
           (. js/document (getElementById "app")))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

