
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
            [cljs.reader :refer [read-string]]))

(defcomp
 comp-data-panel
 (states template mock-data shadows?)
 (div
  {:style {:width 100, :background-color (hsl 0 0 95)}}
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
     :initial (pr-str mock-data),
     :text "Paste data for template"}
    (fn [result d! m!]
      (try
       (let [data (read-string result)] (d! :data data))
       (catch js/Error. error (js/console.error error) (js/alert "Failed to add data"))))))
  (div
   {}
   (cursor->
    :template
    comp-prompt
    states
    {:trigger (a {:style ui/link, :inner-text "Set template"}),
     :multiline? true,
     :input-style {:font-family ui/font-code},
     :initial (pr-str template),
     :text "Paste template markup"}
    (fn [result d! m!]
      (try
       (let [template (read-string result)] (d! :template template))
       (catch js/Error. error (js/console.error error) (js/alert "Failed to add template"))))))
  (div
   {}
   (<> "Shadows:")
   (input
    {:type "checkbox",
     :checked shadows?,
     :on-change (fn [e d! m!] (d! :toggle-shadows nil))}))))
