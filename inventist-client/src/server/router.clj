(ns server.router
  (:require [clojure.java.io :as io]))

(defn handle-request [{uri :uri
                       :as request}]

  {:status 200
   ;:headers {}
   :body   (-> "public/index.html"
               io/resource
               io/file
               slurp)})
