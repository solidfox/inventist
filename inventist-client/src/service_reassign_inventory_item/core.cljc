(ns service-reassign-inventory-item.core)

(defn create-state []
  {:ongoing-reassign-inventory-item-requests {}})


(defn add-pending-item-reassignment
  [state {:keys [inventory-item-id
                 new-assignee-id]
          :as   reassignment-data}]
  (-> state
      (assoc-in [:ongoing-reassign-inventory-item-requests inventory-item-id] reassignment-data)))

(defn get-unstarted-reassign-inventory-item-requests [state]
  (->> (:ongoing-reassign-inventory-item-requests state)
       (vals)
       (filter (fn [request] (not (:fetching request))))))

(defn should-fetch-reassign-inventory-item [state]
  (< 0 (get-unstarted-reassign-inventory-item-requests state)))

(defn started-fetching-reassign-inventory-item [state inventory-item-id]
  (assoc-in state [:ongoing-reassign-inventory-item-requests inventory-item-id :fetching] true))

(defn receive-reassign-inventory-item-response [state _response _request {:keys [inventory-item-id]}]
  (update state :ongoing-reassign-inventory-item-requests dissoc inventory-item-id))
