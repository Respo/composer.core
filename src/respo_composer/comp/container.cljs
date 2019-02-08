
(ns respo-composer.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core
             :refer
             [defcomp cursor-> action-> mutation-> <> div button textarea span]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]
            [respo-md.comp.md :refer [comp-md]]
            [respo-composer.config :refer [dev?]]
            [respo-composer.comp.previewer :refer [comp-previewer]]
            [respo-composer.comp.data-panel :refer [comp-data-panel]]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       template (:template store)
       mock-data (:data store)
       shadows? (:shadows? store)]
   (div
    {:style (merge ui/global ui/fullscreen ui/row)}
    (comp-previewer template mock-data shadows?)
    (cursor-> :data-panel comp-data-panel states template mock-data shadows?)
    (when dev? (cursor-> :reel comp-reel states reel {})))))
