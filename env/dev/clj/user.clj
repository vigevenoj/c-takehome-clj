(ns user
  (:require [ctakehome.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [ctakehome.figwheel :refer [start-fw stop-fw cljs]]
            [ctakehome.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'ctakehome.core/repl-server))

(defn stop []
  (mount/stop-except #'ctakehome.core/repl-server))

(defn restart []
  (stop)
  (start))


