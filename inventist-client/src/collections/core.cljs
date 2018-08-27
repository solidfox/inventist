(ns collections.core
  (:require [oops.core :refer [oget ocall]]))

(defn create-state
  []
  {:selected-collection nil})
