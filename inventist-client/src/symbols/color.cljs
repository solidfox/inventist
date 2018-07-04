(ns symbols.color)

;Common-Palette
(def white "#FFFFFF")                                       ;Page BG
(def theme "#e67e22")                                       ;Theme Color - GMS orange
(def transparent "transparent")                             ;Search Bar / No color
(def shadow "rgba(0,0,0,0.25)")

;Define Hue here to compose theme colors.
;Blue - 200
;Green - 100
;Red - 0
;Orange - 30
;Pink - 300
;Purple - 275
(def theme-hue "200")

;Theme Colours
(def theme-900 (str "hsla(" theme-hue " , 100% , 10% , 1)"))      ;Darkest shade (Heading text)
(def theme-700 (str "hsla(" theme-hue " , 100% , 25% , 1)"))      ;Dark Shade (Primary text, Dark Background)
(def theme-500 (str "hsla(" theme-hue " , 100% , 40% , 1)"))      ;Mid Shade (Secondary text, Image background, CTA)
(def theme-300 (str "hsla(" theme-hue " , 100% , 80% , 1)"))      ;Light Shade (Highlight, Mouse-over)
(def theme-100 (str "hsla(" theme-hue " , 100% , 95% , 1)"))      ;Lightest Shade (Light Backgrounds)

;Semantic colors
(def light-context-primary-text theme-700)
(def light-context-secondary-text theme-500)
(def light-context-highlight-bg theme-300)
(def light-context-bg theme-100)

(def shaded-context-primary-text theme-900)
(def shaded-context-secondary-text theme-700)
(def shaded-context-highlight-bg theme-500)
(def shaded-context-bg theme-300)

(def dark-context-primary-text theme-100)
(def dark-context-secondary-text theme-300)
(def dark-context-highlight-bg theme-700)
(def dark-context-bg theme-900)

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
(def black "#000000")                                       ;Page Heading & shadows
(def grey-normal "#7F7F7F")                                 ;UnSelected Nav-Icon
(def grey-dark "#4A4A4A")                                   ;Text-Primary
(def grey-blue "#7F8C8D")                                   ;Text-Secondary
(def grey-light "#F6F6F6")                                  ;BG for cards, overview, etc.

;Semantic colors
(def danger "#FF0000")                                      ;CTA-logout / red
(def link-active "#4A90E2")                                 ;CTA-link / blue
(def highlight "#e5e5e5")                                   ;Highlight (overview-list)
(def de-emphasize grey-normal)


