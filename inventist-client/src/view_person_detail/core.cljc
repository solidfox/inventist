(ns view-person-detail.core
  (:require [view-person-detail.mock-data :as mock-data]))

(defn create-state
  [{person-id :person-id}]
  {:person-id                   person-id
   :fetching-person-details     false
   :get-person-details-response {:status 200
                                 :response (mock-data/create-person-detail {:id "mock-person-id"})}})

(defn started-get-person-details-service-call [state]
  (assoc state :fetching-person-details true))

(defn should-get-person-details? [state]
  (and (not (:fetching-people-list state))
       (not (get-in state [:get-people-list-response :data]))))

(defn receive-get-people-list-service-response [state response request]
  (let [photo-base-url (str/replace (:url request)
                                    #"/[^/]+/?$" "")]
    (as-> response $
          (update-in $ [:data :people]
                     (fn [people]
                       (for [person people]
                         (update person :image
                                 (fn [image-name]
                                   (when (not-empty image-name)
                                     (str/join "/"
                                               [photo-base-url
                                                "photos"
                                                (str/replace image-name
                                                             #"^/" "")])))))))
          (assoc state :get-people-list-response $))))
