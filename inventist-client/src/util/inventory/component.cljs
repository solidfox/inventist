(ns util.inventory.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]
            [symbols.style :as style]))

;INVENTORY ADD / NEW ORDER
(defc inventory-add []
  [:div {:id    "detail-container"
         :style {:height             "100%"
                 :display            "grid"
                 :backgroundColor    color/silver
                 :grid-template-rows "auto 1fr"}}


   ;Toolbar
   (s-detailview/toolbar {:items-left  (s-detailview/breadcrumb {:type "back"})
                          :items-right [(s-general/button {:color color/grey-normal
                                                           :text  "Help"
                                                           :icon  "fas fa-help"})]})
   ;Form Page
   [:div {:style {:overflow-x      "hidden"
                  :overflow-y      "scroll"}}
    [:div {:style style/form-box}
     [:div {:style (merge {:display         "flex"
                           :justify-content "center"}
                          style/header-title)}
      "New Contractor"]
     (s-detailview/section-divider)

     [:div {:id    "form"
            :style {:display   "flex"
                    :flex-wrap "wrap"
                    :justify-content "space-between"}}

      (s-general/input-filed {:width "25%"
                              :field "Brand"
                              :text  "Eg. Apple"
                              :value "Apple"})
      (s-general/input-filed {:width "70%"
                              :field "Device Name"
                              :text  "Enter full Device Name"
                              :value "MacBook Pro"})
      (s-general/input-filed {:width "45%"
                              :field "Serial Number"})
      (s-general/input-filed {:width "45%"
                              :field "Model Identifier/Class"
                              :text  "Eg. laptop, smartphone"})
      (s-general/input-filed {:width "100%"
                              :field "Supplier"
                              :text  "From where the device is purchased"
                              :value "MediaMarkt"})]


     (s-detailview/section-divider)
     [:div {:style {:display         "flex"
                    :justify-content "center"}}
      (s-general/button {:color color/theme
                         :text  "Add Contractor"
                         :icon  "fas fa-check-circle"})
      (s-general/button {:color color/grey-normal
                         :text  "Cancel"
                         :icon  "fas fa-times-circle"})]]]])

