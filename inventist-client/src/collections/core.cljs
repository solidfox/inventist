(ns collections.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  []
  {:fetching-login-status true
   :logged-in-user        nil
   :firebase-auth-state   (oget js/firebase :auth)})
