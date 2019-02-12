
(ns respo-composer.core
  (:require [respo.core :refer [defcomp cursor-> list-> <> div button textarea span a]]
            [respo.comp.space :refer [=<]]
            [hsl.core :refer [hsl]]
            [feather.core :refer [comp-icon]]
            [respo-ui.core :as ui]))

(declare render-children)

(declare render-box)

(declare render-markup)

(defn get-layout [layout]
  (case layout
    :row ui/row
    :row-center ui/row-center
    :row-middle ui/row-middle
    :row-parted ui/row-parted
    :column ui/column
    :column-parted ui/column-parted
    {}))

(defn render-button [markup]
  (let [props (:props markup)]
    (button
     {:style (merge ui/button (:style markup)), :inner-text (get props "text" "Submit")})))

(defn render-icon [markup]
  (let [props (:props markup)
        icon-name (get props "name")
        size (get props "size" 16)
        color (get props "color" (hsl 200 80 70))]
    (comp-icon icon-name (merge {:font-size size, :color color} (:style markup)) nil)))

(defn render-if [markup context] (<> "TODO: if"))

(defn render-input [markup context] (<> "TODO: input"))

(defn render-link [markup]
  (let [props (:props markup)]
    (a {:style (merge ui/link (:style markup)), :inner-text (get props "text" "Submit")})))

(defn use-number [x] (if (nil? x) nil (js/parseFloat x)))

(defn render-space [markup]
  (let [props (:props markup)]
    (=< (use-number (get props "width")) (use-number (get props "height")))))

(defn render-template [markup context] (<> "TODO: template"))

(defn render-text [markup]
  (let [props (:props markup)] (<> (get props "value") (:style markup))))

(defn render-value [markup context] (<> "TODO: value"))

(def style-unknown {"font-size" 12, "color" :red})

(defn render-markup [markup context]
  (case (:type markup)
    :box (render-box markup context)
    :space (render-space markup)
    :icon (render-icon markup)
    :text (render-text markup)
    :template (render-template markup context)
    :input (render-input markup context)
    :button (render-button markup)
    :link (render-link markup)
    :if (render-if markup context)
    :value (render-value markup context)
    (div {:style style-unknown} (<> (str "Unknown type:" (:type markup))))))

(defn render-children [children context]
  (->> children (sort-by first) (map (fn [[k child]] [k (render-markup child context)]))))

(defn render-box [markup context]
  (list->
   (merge (:attrs markup) {:style (merge (get-layout (:layout markup)) (:style markup))})
   (render-children (:children markup) context)))

(defn str-keys [x] (->> x (map (fn [[k v]] [(name k) v])) (into {})))
