
(ns respo-composer.comp.data-panel
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core
             :refer
             [defcomp cursor-> action-> mutation-> <> div button input textarea a span]]
            [respo.comp.space :refer [=<]]
            [respo-md.comp.md :refer [comp-md]]
            [respo-composer.config :refer [dev?]]
            [inflow-popup.comp.popup :refer [comp-popup]]
            [respo-alerts.comp.alerts :refer [comp-prompt]]
            [cljs.reader :refer [read-string]]
            [favored-edn.core :refer [write-edn]]))

(defcomp
 comp-data-panel
 (states templates pointer mock-data shadows?)
 (div
  {:style {:width 160, :padding 8, :background-color (hsl 0 0 95)}}
  (div {} (<> "Settings"))
  (=< nil 8)
  (div
   {}
   (cursor->
    :data
    comp-prompt
    states
    {:trigger (a {:style ui/link, :inner-text "Set data"}),
     :multiline? true,
     :input-style {:font-family ui/font-code},
     :initial (write-edn mock-data),
     :text "Paste mock data"}
    (fn [result d! m!]
      (try
       (let [data (read-string result)] (d! :data data))
       (catch js/Error. error (js/console.error error) (js/alert "Failed to add data"))))))
  (div
   {}
   (cursor->
    :templates
    comp-prompt
    states
    {:trigger (a {:style ui/link, :inner-text "Set templates"}),
     :multiline? true,
     :input-style {:font-family ui/font-code, :min-height 240, :font-size 12},
     :initial (write-edn templates),
     :text "Paste templates"}
    (fn [result d! m!]
      (try
       (let [templates (read-string result)] (d! :templates templates))
       (catch js/Error. error (js/console.error error) (js/alert "Failed to add template"))))))
  (div
   {}
   (cursor->
    :pointer
    comp-prompt
    states
    {:trigger (if (some? pointer) (<> pointer) (<> "nothing" {:color (hsl 0 0 80)})),
     :text "Template name:",
     :initial pointer}
    (fn [result d! m!] (if (some? result) (d! :pointer result)))))
  (div
   {}
   (<> "Shadows:")
   (input
    {:type "checkbox",
     :checked shadows?,
     :on-change (fn [e d! m!] (d! :toggle-shadows nil))}))))
