(ns util.inventory.services
  (:require [util.inventory.core :as util]))

(def reallocation-fragment
  [{:fragment/name   :fragment/person-history-reallocation
    :fragment/type   :Reallocation
    :fragment/fields [:instant
                      [:inventory_item [:id
                                        :brand
                                        :model-name
                                        :model-identifier
                                        :serial-number]]]}])

(defn reassign-inventory-item
  [{:keys [id
           serial-number
           new-user-id
           before-fn-and-args
           after-fn-and-args
           new-user-graphql]}]
  {:pre [(or id
             serial-number)]}
  {:name                  :reassign-inventory-item
   :remote-state-mutation true
   :before                before-fn-and-args
   :data                  {:url    "http://backend.inventory.gripsholmsskolan.se:8888/graphql"
                           :params {:query
                                    (util/graphql-string
                                      {:operation {:operation/type :mutation
                                                   :operation/name "ReassignDevice"}
                                       :fragments reallocation-fragment
                                       :queries   [[:set-user-of-inventory-item {:inventory-item-serial-number serial-number
                                                                                 :new-user-id                  new-user-id}
                                                    [[:new-user (or new-user-graphql [:id])]]]]})}}
   :after                 after-fn-and-args})
