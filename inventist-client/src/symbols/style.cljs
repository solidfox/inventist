(ns symbols.style
  (:require
    [symbols.color :as color]
    [symbols.branding :as branding]
    [cljss.core :as cljss])
  (:require-macros
    [cljss.core :refer [defstyles defkeyframes]]))

(def viewport-mobile 1015)                                  ;Should be ~400 but kept more for testing purpose
(def outer-border-radius "0.5rem")
(def inner-border-radius "0.25rem")

(def z-index-top-toolbar {:z-index 0})
(def z-index-details-section {:z-index 20})

(def box-shadow {:box-shadow (str "0 0 0.25rem " color/shadow)})

(def watermark {:background-image    (str "url(" branding/logo-watermark-url ")")
                :background-position "50%"
                :background-size     "10rem"
                :background-repeat   "no-repeat"})

(def shaded-bar {:overflow-x      "hidden"
                 :overflow-y      "scroll"
                 :box-shadow      (str "0 0 0.25rem " color/shadow)
                 :backgroundColor color/shaded-context-background})

(def card-title {:color color/shaded-context-primary-text})

(def card-subtitle {:font-size ".75rem"
                    :color     color/shaded-context-secondary-text})

(def header-title {:font-size      "2rem"
                   :color          color/light-context-title-text
                   :font-weight    "300"
                   :text-transform "capitalize"})

;Bounce-in animation
(def cubic-bezier "cubic-bezier(0.215, 0.61, 0.355, 1)")
(defkeyframes bounce-in []
              {:from {:animation-timing-function cubic-bezier}
               :0%   {:opacity                   0
                      :transform                 "scale3d(0.3, 0.3, 0.3)"
                      :animation-timing-function cubic-bezier}
               :20%  {:transform                 "scale3d(1.1, 1.1, 1.1)"
                      :animation-timing-function cubic-bezier}
               :40%  {:transform                 "scale3d(0.9, 0.9, 0.9)"
                      :animation-timing-function cubic-bezier}
               :60%  {:opacity                   1
                      :transform                 "scale3d(1.03, 1.03, 1.03)"
                      :animation-timing-function cubic-bezier}
               :80%  {:transform                 "scale3d(0.97, 0.97, 0.97)"
                      :animation-timing-function cubic-bezier}
               :to   {:opacity                   1
                      :transform                 "scale3d(1, 1, 1)"
                      :animation-timing-function cubic-bezier}})
(def bounce-animation (str (bounce-in) " 750ms ease 1"))

(defstyles user-bar-item []
           {:opacity 0.75
            :&:hover {:opacity 1}})

(defstyles card [{:keys [actionable]}]
           {:border-radius         outer-border-radius
            :background-color      color/shaded-context-background
            :color                 color/shaded-context-primary-text
            :min-height            "2.5rem"
            :padding               "0.75rem"
            :margin                "0.75rem 1rem 0.25rem 0"
            :&:hover               (when actionable
                                     {:background-color color/shaded-context-highlight-bg})})

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
           {:border-radius    inner-border-radius
            :width            "3rem"
            :height           "3rem"
            :object-fit       "cover"
            :background-color color/white})

(def list-item-selected {:background-color color/shaded-context-highlight-bg})

(defstyles list-item-class []
           {:width            "auto"
            :background-color color/shaded-context-background
            :min-height       "3rem"
            :padding          "0.75rem"
            :margin           "0.125rem 0"
            :border-radius    "0.5rem"
            :cursor           "pointer"
            :&:hover          {:background-color color/shaded-context-highlight-bg}})

(defstyles list-item-left-column []
           {:width  "3rem"
            :height "3rem"})

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
               :padding         "0.75rem"
               :backgroundColor color/light-context-background
               :border          (str "1px solid " color/light-context-highlight-bg)
               :borderRadius    "0rem"})

(def float-box {:height             "auto"
                :maxHeight          "50rem"
                :width              "22rem"
                :position           "absolute"
                :top                "5rem"
                :right              "1rem"
                :display            "grid"
                :grid-template-rows "auto 1fr"
                :backgroundColor    color/shaded-context-highlight-bg
                :color              color/shaded-context-title-text
                :border-radius      "0.5rem"
                :box-shadow         "0rem 0.25rem 0.25rem 0 rgba(0,0,0,0.5)"})
