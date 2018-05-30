(ns authentication.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  []
  {:fetching-login-status true
   :logged-in-user        nil
   :firebase-auth-state   (oget js/firebase :auth)})

(defn status
  [state]
  (cond (:fetching-login-status state)
        :loading
        (:logged-in-user state)
        :logged-in
        :else
        :not-logged-in))

; See firebase API: https://firebase.google.com/docs/reference/js/firebase.User
(defn get-authenticated-user
  [state]
  (when-let [js-user (:logged-in-user state)]
    {:display-name (oget js-user :displayName)
     :email        (oget js-user :email)
     :photo-url    (oget js-user :photoURL)}))

(defn receive-new-auth-state
  [state user]
  (-> state
      (assoc :fetching-login-status false)
      (assoc :logged-in-user user)))
