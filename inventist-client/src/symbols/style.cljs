(ns symbols.style
  (:require
    [symbols.color :as color]
    [cljss.core :as cljss])
  (:require-macros
    [cljss.core :refer [defstyles]]))

;To change listing bg color on hover.
(def list-bg-color color/highlight)

(def outer-border-radius "0.5rem")
(def inner-border-radius "0.25rem")

(def z-index-top-toolbar {:z-index 10})
(def z-index-details-section {:z-index 5})

(def box-shadow {:box-shadow (str "0 0 0.12rem " color/shadow)})

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

(def card {:border-radius         outer-border-radius
           :backgroundColor       color/grey-light
           :minHeight             "3rem"
           :width                 "20rem"
           :padding               "1rem"
           :margin                "0.5rem 1rem 0.5rem 0"
           :display               "grid"
           :grid-template-columns "auto 1fr"
           :cursor                "pointer"})

(defstyles navbar-card []
           {:text-align       "left"
            :background-color color/white
            :width            "auto"
            :margin           "0rem 0.5rem"
            :border-radius    "0.5rem 2rem 2rem 0.5rem"
            :display          "flex"
            :flex-direction   "row"
            :cursor           "pointer"
            :box-shadow       "0 0 0.2rem rgba(0,0,0,0.25)"
            :&:hover          {:box-shadow "0 0 0.5rem rgba(0,0,0,0.75)"}
            :&:active         {:box-shadow "0 0 0.25rem 0 #000000"}})

(defstyles card-image []
           {:border-radius   inner-border-radius
            :width           "3rem"
            :margin-right    "1rem"
            :height          "3rem"
            :object-fit      "cover"
            :backgroundColor color/white})

(defstyles list-item []
           {:width                 "100%"
            :background-color      color/highlight
            :min-height            "2rem"
            :padding               "0.75rem 1rem"
            :margin                "2px 0"
            :display               "grid"
            :grid-template-columns "auto 1fr"
            :grid-gap              "1rem"
            :cursor                "pointer"
            :&:hover               {:background-color color/white}
            :&:active              {:background-color color/white}})

(defstyles list-item-left-column []
           {:width "3rem"})

(defstyles button [{bg-color   :bg-color
                    text-color :text-color}]
           {:margin           "0.5rem"
            :padding          "0.25rem 0.5rem"
            :cursor           "pointer"
            :border-radius    "0.25rem"
            :color            text-color
            :background-color bg-color
            :&:hover          {:box-shadow "0 0 0.25rem 0 #000000"}})

(defstyles email-li []
           {:display "block"})


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

(def float-box {:height             "auto"
                :maxHeight          "50rem"
                :width              "22rem"
                :position           "absolute"
                :top                "3.5rem"
                :right              "0.5rem"
                :display            "grid"
                :backgroundColor    color/silver
                :grid-template-rows "auto 1fr"
                :box-shadow         "0rem 0.25rem 0.25rem 0 rgba(0,0,0,0.5)"})
