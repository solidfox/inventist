(ns authentication.core)

(defn create-state
  []
  (let [firebase-auth-state (js/firebase.auth)]
    {:fetching-login-status true
     :logged-in-user        nil
     :firebase-auth-state   firebase-auth-state}))

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
    {:display-name (.-displayName js-user)
     :email        (.-email js-user)
     :photo-url    (.-photoURL js-user)}))

(defn recieve-new-auth-state
  [state user]
  (-> state
      (assoc :fetching-login-status false)
      (assoc :logged-in-user user)))
