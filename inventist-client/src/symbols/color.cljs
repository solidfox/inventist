(ns symbols.color)

;Common-Palette
(def white "#FFFFFF")                                       ;Page BG
(def black "#000000")
(def danger "#FF0000")
(def theme "#e67e22")                                       ;Theme Color - GMS orange
(def transparent "transparent")                             ;Search Bar / No color
(def shadow "rgba(0,0,0,0.25)")

;Define Hue here to compose theme colors.
;Blue - 200
;Green - 100
;Red - 0
;Orange - 30
;Yellow - 56
;Pink - 300
;Purple - 275
(def theme-hue "200")

;Complementary Hue (opposite color)
(def theme-hue-int (js/parseInt theme-hue))
(def complementary-hue-int (cond (> theme-hue-int 179) (- theme-hue-int 180)
                                 :else (+ theme-hue-int 180)))
(def complementary-hue (str complementary-hue-int))

;Theme Colours & their complementary
(def theme-900 (str "hsla(" theme-hue " , 100% , 10% , 1)")) ;Darkest shade (Heading text)
(def theme-700 (str "hsla(" theme-hue " , 100% , 25% , 1)")) ;Dark Shade (Primary text, Dark Background)
(def theme-500 (str "hsla(" theme-hue " , 100% , 40% , 1)")) ;Mid Shade (Secondary text, Image background, CTA)
(def theme-300 (str "hsla(" theme-hue " , 100% , 85% , 1)")) ;Light Shade (Highlight, Mouse-over)
(def theme-100 (str "hsla(" theme-hue " , 100% , 95% , 1)")) ;Lightest Shade (Light Backgrounds)
(def theme-900-comp (str "hsla(" complementary-hue " , 100% , 10% , 1)"))
(def theme-700-comp (str "hsla(" complementary-hue " , 100% , 25% , 1)"))
(def theme-500-comp (str "hsla(" complementary-hue " , 100% , 40% , 1)"))
(def theme-300-comp (str "hsla(" complementary-hue " , 100% , 85% , 1)"))
(def theme-100-comp (str "hsla(" complementary-hue " , 100% , 95% , 1)"))

;Semantic colors
(def light-context-title-text theme-900)
(def light-context-primary-text theme-700)
(def light-context-secondary-text theme-500)
(def light-context-secondary-negative danger)
(def light-context-highlight-bg theme-100)
(def light-context-background white)

(def shaded-context-title-text theme-900)
(def shaded-context-primary-text theme-700)
(def shaded-context-secondary-text theme-500)
(def shaded-context-secondary-negative danger)
(def shaded-context-highlight-bg theme-300)
(def shaded-context-background theme-100)

(def dark-context-title-text theme-100)
(def dark-context-primary-text theme-300)
(def dark-context-secondary-text theme-500)
(def dark-context-secondary-negative danger)
(def dark-context-highlight-bg theme-900)
(def dark-context-background theme-700)

;;Theme-Blue
;(def theme-900 "#00171F") ;Darkest shade (Heading text)
;(def theme-700 "#003459") ;Dark Shade (Primary text, Dark Background)
;(def theme-500 "#007EA7") ;Mid Shade (Secondary text, Image background, CTA)
;(def theme-300 "#A0DEF6") ;Light Shade (Highlight, Mouse-over)
;(def theme-100 "#E1F4FC") ;Lightest Shade (Light Backgrounds)

;;Theme-Pink
;(def theme-900 "#442B2D") ;Darkest shade (Heading text)
;(def theme-700 "#84565E") ;Dark Shade (Primary text, Dark Background)
;(def theme-500 "#E29896") ;Mid Shade (Secondary text, Image background, CTA)
;(def theme-300 "#FFDACF") ;Light Shade (Highlight, Mouse-over)
;(def theme-100 "#FFF0E9") ;Lightest Shade (Light Backgrounds)

;Color-Palette
(def silver "#ECF0F1")                                      ;Primary Color - Selected Nav-Icon
(def grey-normal "#7F7F7F")                                 ;UnSelected Nav-Icon
(def grey-dark "#4A4A4A")                                   ;Text-Primary
(def grey-blue "#7F8C8D")                                   ;Text-Secondary
(def grey-light "#F6F6F6")                                  ;BG for cards, overview, etc.

;Semantic colors
(def link-active "#4A90E2")                                 ;CTA-link / blue
(def highlight "#e5e5e5")                                   ;Highlight (overview-list)
(def de-emphasize grey-normal)


