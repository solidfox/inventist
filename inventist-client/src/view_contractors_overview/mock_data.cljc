(ns view-contractors-overview.mock-data)

(defn create-contractor-summary [{id :id}]
  {:id        (or id "00")
   :type      "supplier"
   :image     "https://www.dropque.com/assets/placeholder-company-5f3438282f524800f1d49cd2921bb0a56101e1aa16097ebd313b64778fc7c4bd.png"
   :name      "MediaMarkt"
   :inventory [{:id               01
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :class            "macbook"
                :model-identifier "MacBookPro13,2"}
               {:id               02
                :brand            "Apple"
                :supplier         "MediaMarkt"
                :model-name       "MacBook Pro (13-inch, 2016, Four Thunderbolt 3 Ports)"
                :color            "Space Gray"
                :class            "macbook"
                :model-identifier "MacBookPro13,2"}
               {:id               03
                :brand            "Android"
                :supplier         "MediaMarkt"
                :model-name       "Samsung Galaxy S9"
                :class            "phone"
                :color            "Blue"
                :model-identifier "phone"}]})



(defn create-get-contractors-list-response
  []
  {:items [(create-contractor-summary {:id "01"})
           (create-contractor-summary {:id "02"})
           (create-contractor-summary {:id "03"})
           (create-contractor-summary {:id "04"})
           (create-contractor-summary {:id "05"})
           (create-contractor-summary {:id "06"})
           (create-contractor-summary {:id "07"})
           (create-contractor-summary {:id "08"})
           (create-contractor-summary {:id "09"})
           (create-contractor-summary {:id "10"})
           (create-contractor-summary {:id "11"})
           (create-contractor-summary {:id "12"})]})
