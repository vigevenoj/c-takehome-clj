(ns ctakehome.core
  (:require [reagent.core :as r]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ctakehome.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [secretary.core :as secretary :include-macros true])
  (:import goog.History))

(defonce session (r/atom {:page :home}))
(defonce app-state (r/atom {:zipcode-info {} :zipcode nil}))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page (:page @session)) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "Takehome"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/" "Home" :home]]]])

(defn get-zipcode-info
  "Get information about a zipcode"
  [zipcode]
  (.log js/console "Submitting request for" zipcode)
  (let [data {"zipcode" zipcode}]
    (POST "/api/zipcode"
          {:format :json
           :params data
           :handler #(swap! app-state assoc :zipcode-info %)})))

(defn results-container
  "Display results about a zipcode lookup"
  []
  [:div.results
   (if-not (empty? (:zipcode-info @app-state))
     (let [zipcode-info (:zipcode-info @app-state)]
       [:span.text (str "At the location " (:city-name zipcode-info)
                        ", the temperature is " (:temperature zipcode-info)
                        ", the timezone is " (:timezone zipcode-info)
                        ", and the elevation is " (:elevation zipcode-info))])
     (.log js/console "No zipcode data to display"))])

(defn home-page []
  [:div.container
   [:input {:type "text"
            :value (:zipcode @app-state)
            :on-change #(swap! app-state assoc :zipcode (-> % .-target .-value))
            :placeholder "Zip code"}]
   [:button {:type "submit"
             :on-click #(get-zipcode-info (:zipcode @app-state))}
    "Look up"]
   (results-container)])

(def pages
  {:home #'home-page})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (swap! session assoc :page :home))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
            (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(swap! session assoc :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
