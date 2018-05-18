(ns symbols.style
  (:require
    [symbols.color :as color]
    [cljss.core :refer [defstyles]]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

(def outer-border-radius {:borderRadius "0.5rem"})
(def inner-border-radius {:borderRadius "0.25rem"})

(def card-title {:color          color/grey-dark
                 :line-height    "1rem"
                 :text-transform "capitalize"})

(def card-subtitle {:font-size      ".8rem"
                    :color          color/grey-blue
                    :line-height    "1rem"
                    :text-transform "capitalize"})

(def header-title {:font-size      "2rem"
                   :color          color/black
                   :font-weight    "300"
                   :text-transform "capitalize"})

(def card (merge
            outer-border-radius
            {:backgroundColor       color/grey-light
             :minHeight             "4rem"
             :width                 "20rem"
             :padding               "1rem"
             :margin                "0.5rem 1rem 0.5rem 0"
             :display               "grid"
             :grid-template-columns "auto 1fr"
             :cursor                "pointer"}))

(def card-image (merge
                  inner-border-radius
                  {:width           "3rem"
                   :margin-right    "1rem"
                   :height          "3rem"
                   :object-fit      "cover"
                   :backgroundColor color/white}))

(defstyles list-item []
           {:width                 "100%"
            :background-color      color/highlight
            :min-height            "2rem"
            :padding               "0.75rem 1rem"
            :margin                "2px 0"
            :display               "grid"
            :grid-template-columns "auto 1fr"
            :cursor                "pointer"
            :&:hover               {:background-color color/white}
            :&:active              {:background-color color/white}})

(def item-stats {:width                 "100%"
                 :backgroundColor       color/white
                 :borderRadius          "5px"
                 :border                (str "1px solid " color/silver)
                 ;:box-shadow            "0 0 2px 0 rgba(0,0,0,0.5)"
                 :minHeight             "2rem"
                 :padding               "0.75rem 1rem"
                 :margin-bottom         "1rem"
                 :display               "grid"
                 :grid-template-columns "1fr auto"
                 :cursor                "pointer"})

(def form-box {:width           "auto"
               :height          "auto"
               :margin          "auto"
               :padding         "1rem 1rem"
               :backgroundColor color/white
               :border          (str "1px solid " color/grey-light)
               :borderRadius    "0rem"})