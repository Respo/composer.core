
(ns respo-composer.comp.previewer
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core
             :refer
             [defcomp cursor-> action-> mutation-> <> div button textarea span]]
            [respo.comp.space :refer [=<]]
            [respo-md.comp.md :refer [comp-md]]
            [respo-composer.config :refer [dev?]]
            [respo-composer.core :refer [render-markup]]))

(defcomp
 comp-previewer
 (template mock-data shadows? templates)
 (let [context {:data mock-data, :templates templates, :level 1}]
   (div
    {:style (merge ui/flex ui/center {:background-color (hsl 0 0 90)}),
     :class-name (if shadows? "use-shadows" nil)}
    (render-markup
     template
     context
     (fn [op op-data] (println "action in markup:" op op-data))))))
