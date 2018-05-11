(ns view-inventory-detail.component
  (:require [rum.core :refer [defc]]
            [symbols.general :as s-general]
            [symbols.detailview :as s-detailview]
            [remodular.core :refer [modular-component]]
            [symbols.color :as color]))

(def col-width "11rem")

(defc inventory-detail < (modular-component identity)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  (let [item (get-in state [:get-inventory-details-response :response])]

    ;INVENTORY DETAILS
    [:div {:id    "detail-container"
           :style {:height             "100%"
                   :display            "grid"
                   :grid-template-rows "auto 1fr"}}
     ;Toolbar
     (s-detailview/toolbar {:items-left (s-detailview/breadcrumb {:type "inventory"
                                                                  :item item})
                            :items-right [(s-general/button {:color color/white
                                                             :icon  "fas fa-share-square"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Transfer Device"
                                                             :icon  "fas fa-share-square"})
                                          (s-general/button {:color color/grey-normal
                                                             :text  "Transfer Device"})]})

     ;Main Details Container
     [:div {:style {:overflow-x      "hidden"
                    :overflow-y      "scroll"
                    :backgroundColor color/white}}

      ;Page Header
      (s-detailview/detail-header
        {:image   (:photo item)
         :heading (str (:brand item) " " (:model-name item))})

      ;Information
      (s-detailview/section-information
        {:fields ["Serial Number"
                  "Color"
                  "Identifier"
                  "Insaurance expiry"
                  "Warranty expiry"]
         :values [(:serial-number item)
                  (:color item)
                  (:model-identifier item)
                  (:insurance-expires (:purchase-details item))
                  (:warranty-expires (:purchase-details item))]})

      ;Assignee
      [:div {:style {:margin         "1rem 2.5rem 1rem"
                     :display        "flex"
                     :flex-direction "row"}
             :id    "assignee"}
       (s-detailview/section-left)

       [:div {:style {:margin "0 0 0 1rem" :width "100%"}}
        (s-detailview/section-title {:title "Current Assignee"})
        [:div {:style {:display        "flex"
                       :flex-direction "row"
                       :flex-wrap      "wrap"}}
         (s-detailview/person-card {:person (first (:history item))})]
        ;(s-detailview/person-card {:person (last (:history item))})]
        (s-detailview/section-divider)]]

      ;Timeline
      (s-detailview/section-timeline {:type     "inventory"
                                      :history  (:history item)
                                      :purchase (:purchase-details item)})]]))
