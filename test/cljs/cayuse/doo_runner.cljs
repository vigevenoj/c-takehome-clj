(ns ctakehome.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [ctakehome.core-test]))

(doo-tests 'ctakehome.core-test)

