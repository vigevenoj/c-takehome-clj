(ns ctakehome.owm
  (:require [cheshire.core :refer :all]
            [clj-http.client :as client]
            [ctakehome.config :refer [env]]))

(def api-url "https://api.openweathermap.org/data/2.5/weather?zip=%s,us&APPID=%s")

(defn fetch
  "Make a request to openweathermap API and return a map with latitude, longitude, city name and temperature"
  [zipcode]
  (let [response
          (:body (client/get (format api-url zipcode (env :openweathermap-api-key))))]
    (cheshire.core/parse-string response true)))