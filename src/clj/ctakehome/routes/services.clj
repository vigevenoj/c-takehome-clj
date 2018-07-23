(ns ctakehome.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ctakehome.owm :as owm]
            [ctakehome.elevation :as elevation]
            [ctakehome.timezone :as timezone]))

(defn query-apis [zipcode]
  (let [owm-response (owm/fetch zipcode)
        elevation (elevation/fetch (-> owm-response :coord :lat) (-> owm-response :coord :lon))
        timezone (timezone/fetch (-> owm-response :coord :lat) (-> owm-response :coord :lon))]
    {:city-name (-> owm-response :name)
     :temperature (-> owm-response :main :temp)
     :timezone timezone
     :elevation elevation}))


(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  
  (context "/api" []
    :tags ["zipcode"]

    (POST "/zipcode" []
      :summary "Given a zipcode, return city name, current temperature, timezone, and elevation"
      :body-params [zipcode :- String]
      (ok (query-apis zipcode)))))