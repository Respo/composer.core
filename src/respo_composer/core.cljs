
(ns respo-composer.core
  (:require [respo.core :refer [defcomp cursor-> list-> <> div button textarea span a]]
            [respo.comp.space :refer [=<]]
            [hsl.core :refer [hsl]]
            [feather.core :refer [comp-icon]]
            [respo-ui.core :as ui]))

(declare render-children)

(declare render-box)

(declare render-markup)

(defn str-keys [x] (->> x (map (fn [[k v]] [(name k) v])) (into {})))

(defn get-layout [layout]
  (str-keys
   (case layout
     :row ui/row
     :row-center ui/row-center
     :row-middle ui/row-middle
     :row-parted ui/row-parted
     :column ui/column
     :column-parted ui/column-parted
     {})))

(defn render-button [markup mock-data]
  (let [props (:props markup)]
    (button
     {:style (merge ui/button (:style markup)), :inner-text (get props "text" "Submit")})))

(defn render-icon [markup mock-data]
  (let [props (:props markup)
        icon-name (get props "name")
        size (get props "size" 16)
        color (get props "color" (hsl 200 80 70))]
    (comp-icon icon-name (merge {:font-size size, :color color} (:style markup)) nil)))

(defn render-if [markup mock-data] (<> "TODO: if"))

(defn render-input [markup mock-data] (<> "TODO: input"))

(defn render-link [markup mock-data]
  (let [props (:props markup)]
    (a {:style (merge ui/link (:style markup)), :inner-text (get props "text" "Submit")})))

(defn use-number [x] (if (nil? x) nil (js/parseFloat x)))

(defn render-space [markup mock-data]
  (let [props (:props markup)]
    (=< (use-number (get props "width")) (use-number (get props "height")))))

(defn render-template [markup mock-data] (<> "TODO: template"))

(defn render-text [markup mock-data]
  (let [props (:props markup)] (<> (get props "value") (:style markup))))

(defn render-value [markup mock-data] (<> "TODO: value"))

(def style-unknown {"font-size" 12, "color" :red})

(defn render-markup [markup mock-data templates]
  (case (:type markup)
    :box (render-box markup mock-data templates)
    :space (render-space markup mock-data)
    :icon (render-icon markup mock-data)
    :text (render-text markup mock-data)
    :template (render-template markup mock-data)
    :input (render-input markup mock-data)
    :button (render-button markup mock-data)
    :link (render-link markup mock-data)
    :if (render-if markup mock-data)
    :value (render-value markup mock-data)
    (div {:style style-unknown} (<> (str "Unknown type:" (:type markup))))))

(defn render-children [children mock-data templates]
  (->> children
       (sort-by first)
       (map (fn [[k child]] [k (render-markup child mock-data templates)]))))

(defn render-box [markup mock-data templates]
  (println
   (merge (:attrs markup) {:style (merge (get-layout (:layout markup)) (:style markup))}))
  (list->
   (merge (:attrs markup) {:style (merge (get-layout (:layout markup)) (:style markup))})
   (render-children (:children markup) mock-data templates)))
