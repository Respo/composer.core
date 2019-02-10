
(ns respo-composer.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update store :states (mutate op-data))
    :templates (assoc store :templates op-data)
    :pointer (assoc store :pointer op-data)
    :data (assoc store :data op-data)
    :toggle-shadows (update store :shadows? not)
    :hydrate-storage op-data
    store))
