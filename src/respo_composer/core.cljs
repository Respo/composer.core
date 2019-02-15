
(ns respo-composer.core
  (:require [respo.core :refer [defcomp cursor-> list-> <> div button textarea span a i]]
            [respo.comp.space :refer [=<]]
            [hsl.core :refer [hsl]]
            ["feather-icons" :as icons]
            [respo-ui.core :as ui]
            [clojure.string :as string]))

(declare render-children)

(declare render-box)

(declare render-template)

(declare render-markup)

(defn get-layout [layout]
  (case layout
    :row ui/row
    :row-center ui/row-center
    :center ui/center
    :row-middle ui/row-middle
    :row-parted ui/row-parted
    :column ui/column
    :column-parted ui/column-parted
    {}))

(defn parse-token [x]
  (cond
    (string/starts-with? x ":") (keyword (subs x 1))
    (string/starts-with? x "|") (subs x 1)
    :else (do (js/console.error "Failed to parse token:" x) nil)))

(defn read-by-marks [xs scope]
  (if (nil? scope)
    nil
    (if (empty? xs)
      scope
      (let [x (first xs), v (parse-token x)] (recur (rest xs) (get scope v))))))

(defn read-token [x scope]
  (if (string? x)
    (if (string/starts-with? x "@") (read-by-marks (string/split (subs x 1) " ") scope) x)
    nil))

(defn render-button [markup context on-action]
  (let [props (:props markup), text (read-token (get props "text") (:data context))]
    (button
     {:style (merge ui/button (:style markup)),
      :inner-text (or text "Submit"),
      :on-click (fn [e d! m!] (on-action (or (get props "action") "button-click") props))})))

(defn render-icon [markup on-action]
  (let [props (:props markup)
        icon-name (get props "name" "feather")
        size (js/parseFloat (get props "size" "16"))
        color (get props "color" (hsl 200 80 70))
        obj (aget (.-icons icons) icon-name)]
    (if (some? obj)
      (i
       {:style {:display :inline-block},
        :innerHTML (.toSvg obj (clj->js {:width size, :height size, :color color}))})
      (<>
       (str "No icon: " icon-name)
       {:color :white, :background-color :red, :font-size 12}))))

(defn render-input [markup context] (<> "TODO: input"))

(defn render-link [markup context on-action]
  (let [props (:props markup)
        text (read-token (get props "text") (:data context))
        href (read-token (get props "href") (:data context))]
    (a
     {:style (merge ui/link (:style markup)),
      :inner-text (or text "Submit"),
      :href (or href "#"),
      :on-click (fn [e d! m!] (on-action (get props "action" "link-click") props))})))

(defn render-list [markup context] (<> "TODO: list"))

(defn render-some [markup context] (<> "TODO: some"))

(defn use-number [x] (if (nil? x) nil (js/parseFloat x)))

(defn render-space [markup]
  (let [props (:props markup)]
    (=< (use-number (get props "width")) (use-number (get props "height")))))

(defn render-text [markup context]
  (let [props (:props markup), value (read-token (get props "value") (:data context))]
    (<> value (:style markup))))

(def style-unknown {"font-size" 12, "color" :red})

(defn render-template [markup context on-action]
  (let [templates (:templates context), data (:data context), props (:props markup)]
    (render-markup
     (get templates (get props "name"))
     (-> context (assoc :data (read-token (get props "data") data)) (update :level inc))
     on-action)))

(defn render-markup [markup context on-action]
  (case (:type markup)
    :box (render-box markup context on-action)
    :space (render-space markup)
    :button (render-button markup context on-action)
    :icon (render-icon markup on-action)
    :link (render-link markup context on-action)
    :text (render-text markup context)
    :some (render-some markup context)
    :template (render-template markup context on-action)
    :input (render-input markup context)
    :list (render-list markup context)
    (div {:style style-unknown} (<> (str "Unknown type:" (:type markup))))))

(defn render-children [children context on-action]
  (->> children
       (sort-by first)
       (map (fn [[k child]] [k (render-markup child context on-action)]))))

(defn render-box [markup context on-action]
  (list->
   (merge (:attrs markup) {:style (merge (get-layout (:layout markup)) (:style markup))})
   (render-children (:children markup) context on-action)))

(defn str-keys [x] (->> x (map (fn [[k v]] [(name k) v])) (into {})))
