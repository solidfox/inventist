(ns server.router
  (:require [clojure.string :refer [starts-with?]]
            [ring.middleware.resource :refer [wrap-resource]]
            [clojure.java.io :as io]
            [util.inventory.core :as util]))

(defn- wrap-default-index [next-handler]
  (fn [request]
    (next-handler
      (if (or (starts-with? (:uri request) "/css/")
              (starts-with? (:uri request) "/js/")
              (starts-with? (:uri request) "/image/"))
        request
        (assoc request :uri "/index.html")))))              ;; wrap-resource will find index.html for us

(def handle-request
  (util/spy (-> (fn [_] {:status 404 :body "static asset not found"})
                (wrap-resource "public")
                wrap-default-index)))
