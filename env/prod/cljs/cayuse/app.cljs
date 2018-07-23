(ns ctakehome.app
  (:require [ctakehome.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
