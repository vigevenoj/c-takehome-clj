(ns ctakehome.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [ctakehome.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[ctakehome started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[ctakehome has shut down successfully]=-"))
   :middleware wrap-dev})
