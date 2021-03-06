
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
 (let [context {:data mock-data, :templates templates, :level 1, :cursor %cursor}]
   (div
    {:style (merge ui/flex ui/center {:background-color (hsl 0 0 90)}),
     :class-name (if shadows? "use-shadows" nil)}
    (render-markup
     template
     context
     (fn [d! op props op-data]
       (println "action in markup:" (pr-str op) (pr-str props) (pr-str op-data)))))))
