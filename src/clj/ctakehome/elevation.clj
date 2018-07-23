(ns ctakehome.elevation
  (:require [cheshire.core :refer :all]
            [clj-http.client :as client]
            [ctakehome.config :refer [env]]))

(def api-url "https://maps.googleapis.com/maps/api/elevation/json?locations=%s,%s&key=%s")

(defn fetch
  "Make a request to Google Elevation API and return the elevation in meters"
  [latitude longitude]
  (let [response
        (:body (client/get (format api-url latitude longitude (env :google-elevation-api-key))))]
    (-> (cheshire.core/parse-string response true) :results first :elevation)))
