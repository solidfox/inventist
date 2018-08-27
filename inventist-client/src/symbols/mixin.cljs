(ns symbols.mixin
  (:require [rum.core :as rum]
            [util.inventory.core :as util]))

(defn toggle-mixin
  "Injects a toggle state and two functions to alter that state into the local state of a component."
  [{:keys [toggle-state-key on-fn-key off-fn-key initial-state]}]
  (let [toggle-state-atom-key (keyword (namespace ::whatever) (str (name toggle-state-key) "-atom"))]
    {:will-mount    (fn [state]
                      (let [toggle-state-atom (atom initial-state)
                            component         (:rum/react-component state)]
                        (add-watch toggle-state-atom toggle-state-atom-key
                                   (fn [_ _ _old _new]
                                     (rum/request-render component)))
                        (assoc state toggle-state-atom-key toggle-state-atom)))
     :before-render (fn [state]
                      state
                      (assoc state toggle-state-key (deref (get state toggle-state-atom-key))
                                   on-fn-key (fn [] (reset! (get state toggle-state-atom-key) true))
                                   off-fn-key (fn [] (reset! (get state toggle-state-atom-key) false))))}))

(defn hovered-mixin [hovered-key]
  (let [on-fn-key  (keyword (namespace ::whatever) (str (name hovered-key) "-on-fn"))
        off-fn-key (keyword (namespace ::whatever) (str (name hovered-key) "-off-fn"))]
    (merge
      (toggle-mixin {:toggle-state-key hovered-key
                     :on-fn-key        on-fn-key
                     :off-fn-key       off-fn-key})
      {:wrap-render (fn [render-fn]
                      (fn [state]
                        (let [[dom' state'] (render-fn state)]
                          [(js/React.createElement
                             "div"
                             (clj->js {:onMouseEnter (fn [] ((on-fn-key state)))
                                       :onMouseLeave (fn [] ((off-fn-key state)))})
                             dom') state'])))})))
