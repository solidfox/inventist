(ns symbols.style
  (:require
    [symbols.color :as color]
    [cljss.core :as cljss])
  (:require-macros
    [cljss.core :refer [defstyles]]))

(def outer-border-radius "0.5rem")
(def inner-border-radius "0.25rem")

(def z-index-top-toolbar {:z-index 0})
(def z-index-details-section {:z-index 20})

(def box-shadow {:box-shadow (str "0 0 0.12rem " color/shadow)})

(def watermark {:background-image    "url(\"/image/GHS-watermark.svg\")"
                :background-position "50%"
                :background-size     "10rem"
                :background-repeat   "no-repeat"})

(def shaded-bar {:overflow-x      "hidden"
                 :overflow-y      "scroll"
                 :z-index         10
                 :box-shadow      (str "0 0 0.25rem " color/shadow)
                 :backgroundColor color/shaded-context-background})

(def card-title {:line-height    "1rem"
                 :color          color/shaded-context-primary-text
                 :text-transform "capitalize"})

(def card-subtitle {:font-size      ".75rem"
                    :color          color/shaded-context-secondary-text
                    :line-height    "0.8rem"
                    :text-transform "capitalize"})

(def header-title {:font-size      "2rem"
                   :color          color/light-context-title-text
                   :font-weight    "300"
                   :text-transform "capitalize"})

(defstyles user-bar-item []
           {:opacity 0.75
            :&:hover {:opacity 1}})

(defstyles card []
           {:border-radius         outer-border-radius
            :background-color      color/shaded-context-background
            :min-height            "2.5rem"
            :width                 "18.5rem"
            :padding               "0.75rem"
            :margin                "0.75rem 1rem 0.25rem 0"
            :display               "grid"
            :grid-template-columns "auto 1fr"
            :cursor                "pointer"
            :&:hover               {:background-color color/shaded-context-highlight-bg}})

(defstyles navbar-card []
           {:text-align       "left"
            :background-color color/white
            :width            "auto"
            :margin           "0rem 0.5rem"
            :border-radius    "0.5rem"                      ;"0.5rem 2rem 2rem 0.5rem"
            :display          "flex"
            :flex-direction   "row"
            :cursor           "pointer"
            :box-shadow       "0 0 0.2rem rgba(0,0,0,0.25)"
            :&:hover          {:box-shadow "0 0 0.5rem rgba(0,0,0,0.75)"}
            :&:active         {:box-shadow "0 0 0.25rem 0 #000000"}})

(defstyles card-image []
           {:border-radius   inner-border-radius
            :width           "3rem"
            :height          "3rem"
            :object-fit      "cover"
            :backgroundColor color/white})

(defstyles list-item []
           {:width                 "auto"
            :background-color      color/transparent
            :min-height            "3rem"
            :padding               "0.75rem"
            :margin                "0.125rem 0"
            :border-radius         "0.5rem"
            :display               "grid"
            :grid-template-columns "auto 1fr"
            :grid-gap              "1rem"
            :cursor                "pointer"
            :&:hover               {:background-color color/shaded-context-highlight-bg}
            :&:active              {:background-color color/shaded-context-highlight-bg}})

(defstyles list-item-left-column []
           {:width "3rem"})

(defstyles button [{bg-color   :bg-color
                    text-color :text-color}]
           {:margin           "0rem"
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
               :backgroundColor color/light-context-background
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
