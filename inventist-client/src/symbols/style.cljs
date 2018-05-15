(ns symbols.style
  (:require
    [symbols.color :as color]))

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

(def list-item {:width                 "100%"
                :backgroundColor       list-bg-color
                :minHeight             "2rem"
                :padding               "0.75rem 1rem"
                :margin                "2px 0"
                :display               "grid"
                :grid-template-columns "auto 1fr"
                :cursor                "pointer"})
