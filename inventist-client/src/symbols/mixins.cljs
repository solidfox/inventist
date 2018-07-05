(ns symbols.mixins
  (:require [rum.core :as rum]))

(defn hovered-mixin [hovered-key]
  (merge
    (rum/local nil ::hidden-local-state-atom)
    {:before-render (fn [state]
                      (assoc state hovered-key (:hovered (deref (::hidden-local-state-atom state)))))
     :wrap-render   (fn [render-fn]
                      (fn [state]
                        (let [[dom' state'] (render-fn state)]
                          [(js/React.createElement
                             "div"
                             (clj->js {:onMouseEnter (fn [] (swap! (::hidden-local-state-atom state) assoc :hovered true))
                                       :onMouseLeave (fn [] (swap! (::hidden-local-state-atom state) assoc :hovered false))})
                             dom') state'])))}))
