(ns ctakehome.timezone
  (:require [cheshire.core :refer :all]
            [clj-http.client :as client]
            [ctakehome.config :refer [env]]))

(def api-url "https://maps.googleapis.com/maps/api/timezone/json?location=%s,%s&timestamp=%s&key=%s")

(defn fetch
  "Make a request to Google Timezone API and return the name of the timezone at the specified location"
  [latitude longitude]
  (let [response
        (:body (client/get (format api-url latitude longitude (quot (System/currentTimeMillis) 1000) (env :google-timezone-api-key))))]
    (-> (cheshire.core/parse-string response true) :timeZoneName)))
