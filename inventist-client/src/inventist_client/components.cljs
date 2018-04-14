(ns inventist-client.components
  (:require [authentication.component :as auth]
            [people.component :as people]
            [rum.core :refer [defc]]))


(defc app [state]
      [:div]
   ;(auth/login)
   (people/people-details (first (:persons state))))




