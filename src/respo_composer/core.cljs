
(ns respo-composer.core
  (:require [respo.core :refer [defcomp cursor-> list-> <> div button textarea span]]))

(declare render-children)

(declare render-box)

(declare render-markup)

(defn render-button [markup mock-data] (<> "TODO: button"))

(defn render-icon [markup mock-data] (<> "TODO: icon"))

(defn render-if [markup mock-data] (<> "TODO: if"))

(defn render-input [markup mock-data] (<> "TODO: input"))

(defn render-link [markup mock-data] (<> "TODO: link"))

(defn render-space [markup mock-data] (<> "TODO space"))

(defn render-template [markup mock-data] (<> "TODO: template"))

(defn render-text [markup mock-data]
  (let [props (:props markup)] (<> (get props "value") (:style markup))))

(defn render-value [markup mock-data] (<> "TODO: value"))

(def style-unknown {:font-size 12, :color :red})

(defn render-markup [markup mock-data]
  (case (:type markup)
    :box (render-box markup mock-data)
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

(defn render-children [children mock-data]
  (->> children (sort-by first) (map (fn [[k child]] [k (render-markup child mock-data)]))))

(defn render-box [markup mock-data]
  (list->
   (merge (:attrs markup) {:style (:style markup)})
   (render-children (:children markup) mock-data)))
