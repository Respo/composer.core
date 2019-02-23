
(ns respo-composer.core
  (:require [respo.core
             :refer
             [defcomp
              cursor->
              list->
              <>
              div
              button
              textarea
              span
              a
              i
              input
              create-list-element]]
            [respo.comp.space :refer [=<]]
            [hsl.core :refer [hsl]]
            ["feather-icons" :as icons]
            [respo-ui.core :as ui]
            [clojure.string :as string]
            [cljs.reader :refer [read-string]]
            [respo.util.detect :refer [component? element?]])
  (:require-macros [clojure.core.strint :refer [<<]]))

(declare render-some)

(declare render-popup)

(declare render-children)

(declare render-box)

(declare render-element)

(declare render-template)

(declare render-list)

(declare render-markup)

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
    (cond
      (string/starts-with? x "@") (read-by-marks (string/split (subs x 1) " ") scope)
      (string/starts-with? x "~") (read-string (subs x 1))
      :else x)
    nil))

(defn eval-attrs [attrs data]
  (->> attrs (map (fn [[k v]] [k (read-token v data)])) (into {})))

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

(defn get-preset [preset]
  (case preset
    :flex ui/flex
    :font-code {:font-family ui/font-code}
    :font-fancy {:font-family ui/font-fancy}
    :font-normal {:font-family ui/font-normal}
    :fullscreen {:font-family ui/fullscreen}
    :scroll {:overflow :auto}
    (do (js/console.warning (str "Unknown preset: " preset)) nil)))

(defn style-presets [presets] (->> presets (map get-preset) (apply merge)))

(defn render-button [markup context on-action]
  (let [props (:props markup), text (read-token (get props "text") (:data context))]
    (button
     (merge
      (eval-attrs (:attrs markup) (:data context))
      {:style (merge ui/button (style-presets (:presets markup)) (:style markup)),
       :inner-text (or text "Submit"),
       :on-click (fn [e d! m!] (on-action (or (get props "action") "button-click") props))}))))

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

(defn render-input [markup context on-action]
  (let [props (:props markup)
        value (read-token (get props "value") (:data context))
        textarea? (some? (get props "textarea"))
        action (or (get props "action") "button-click")
        listener (fn [e d! m!] (on-action action props e))]
    (if textarea?
      (textarea
       {:value value,
        :style (merge ui/textarea (style-presets (:presets markup)) (:style markup)),
        :on-input listener})
      (input
       {:value value,
        :style (merge ui/input (style-presets (:presets markup)) (:style markup)),
        :on-input listener}))))

(defn render-inspect [markup context]
  (let [props (:props markup), value (read-token (get props "title") (:data context))]
    (span
     {:inner-text (str value),
      :style {:background-color (hsl 200 80 60),
              :color :white,
              :padding "0 8px",
              :font-size 12,
              :font-family ui/font-fancy,
              :line-height "20px",
              :height "20px",
              :display :inline-block},
      :on-click (fn [e d! m!] (js/console.log (clj->js (:data context))))})))

(defn render-link [markup context on-action]
  (let [props (:props markup)
        text (read-token (get props "text") (:data context))
        href (read-token (get props "href") (:data context))]
    (a
     (merge
      (eval-attrs (:attrs markup) (:data context))
      {:style (merge ui/link (:style markup)),
       :inner-text (or text "Submit"),
       :href (or href "#"),
       :on-click (fn [e d! m!] (on-action (get props "action" "link-click") props))}))))

(defn render-slot [markup context on-action]
  (let [props (:props markup), dom (or (get props "dom") (:dom props))]
    (cond
      (component? dom) dom
      (element? dom) dom
      (some? dom) (<> (str "<Bad slot: " (pr-str dom) ">"))
      :else (<> "<Empty slot>"))))

(defn use-number [x] (if (nil? x) nil (js/parseFloat x)))

(defn render-space [markup]
  (let [props (:props markup)]
    (=< (use-number (get props "width")) (use-number (get props "height")))))

(defn render-text [markup context]
  (let [props (:props markup), value (read-token (get props "value") (:data context))]
    (<> value (merge (style-presets (:presets markup)) (:style markup)))))

(def style-unknown {"font-size" 12, "color" :red})

(defn render-template [markup context on-action]
  (let [templates (:templates context), data (:data context), props (:props markup)]
    (render-markup
     (get templates (get props "name"))
     (-> context (assoc :data (read-token (get props "data") data)) (update :level inc))
     on-action)))

(defn render-some [markup context on-action]
  (let [props (:props markup)
        value (read-token (get props "value") (:data context))
        child-pair (->> (:children markup) (sort-by first) (vals))]
    (if (not= (count child-pair) 2)
      (do
       (js/console.warn "<Some> requires 2 children, but got" (count child-pair))
       (<> "<Bad some>"))
      (if (nil? value)
        (render-markup (first child-pair) context on-action)
        (render-markup (last child-pair) context on-action)))))

(defn render-popup [markup context on-action]
  (let [props (:props markup), value (read-token (get props "visible") (:data context))]
    (if value
      (div
       {:style {:position :fixed,
                :top 0,
                :left 0,
                :width "100%",
                :height "100%",
                :display :flex,
                :overflow :auto,
                :padding 32,
                :background-color (hsl 0 0 0 0.7)},
        :on-click (fn [e d! m!]
          (on-action (or (get props "action") "backdrop-click") props))}
       (list->
        (merge
         {:on-click (fn [e d! m!] )}
         (eval-attrs (:attrs markup) (:data context))
         {:style (merge
                  {:margin :auto,
                   :min-width 320,
                   :min-height 200,
                   :background-color (hsl 0 0 100)}
                  (get-layout (:layout markup))
                  (style-presets (:presets markup))
                  (:style markup))})
        (render-children (:children markup) context on-action)))
      (span {}))))

(defn render-markup [markup context on-action]
  (case (:type markup)
    :box (render-box markup context on-action)
    :space (render-space markup)
    :button (render-button markup context on-action)
    :icon (render-icon markup on-action)
    :link (render-link markup context on-action)
    :text (render-text markup context)
    :some (render-some markup context on-action)
    :template (render-template markup context on-action)
    :input (render-input markup context on-action)
    :list (render-list markup context on-action)
    :slot (render-slot markup context on-action)
    :popup (render-popup markup context on-action)
    :inspect (render-inspect markup context)
    :element (render-element markup context)
    (div {:style style-unknown} (<> (str "Unknown type:" (:type markup))))))

(defn render-list [markup context on-action]
  (let [props (:props markup)
        value (read-token (get props "value") (:data context))
        only-child (first (vals (:children markup)))]
    (cond
      (not (sequential? value)) (<> (<< "<Bad list: ~(pr-str value)>"))
      (> (count (:children markup)) 1) (<> "<Bad list: too many children>")
      (nil? only-child) (<> (<< "<Bad list: no children>"))
      :else
        (list->
         (merge
          (eval-attrs (:attrs markup) (:data context))
          {:style (merge
                   (get-layout (:layout markup))
                   (style-presets (:presets markup))
                   (:style markup))})
         (->> value
              (map-indexed
               (fn [idx x]
                 [idx
                  (let [new-context (assoc
                                     context
                                     :data
                                     {:index idx, :outer (:data context), :item x})]
                    (render-markup only-child new-context on-action))])))))))

(defn render-element [markup context on-action]
  (let [props (:props markup)
        value (read-token (get props "name") (:data context))
        tag-name (keyword (or value "div"))]
    (create-list-element
     tag-name
     (merge
      (eval-attrs (:attrs markup) (:data context))
      {:style (merge
               (get-layout (:layout markup))
               (style-presets (:presets markup))
               (:style markup))})
     (render-children (:children markup) context on-action))))

(defn render-children [children context on-action]
  (->> children
       (sort-by first)
       (map (fn [[k child]] [k (render-markup child context on-action)]))))

(defn render-box [markup context on-action]
  (list->
   (merge
    (eval-attrs (:attrs markup) (:data context))
    {:style (merge
             (get-layout (:layout markup))
             (style-presets (:presets markup))
             (:style markup))})
   (render-children (:children markup) context on-action)))
