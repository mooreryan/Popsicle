(ns popsicle.plots
  (:require [align_view.alignment-info :as al])
  (:use [incanter core stats charts]))

(defn hist
  "Takes the base cov map from find-bases and outputs a histogram of
  the coverage for each reference sequence."
  [base-map]
  (println (str "\nStarting hist at " ) 
           (java.util.Date.))
  (doseq [[seq bases] base-map]
    (view (histogram (flatten bases) 
                     :x-label "Base position"
                     :y-label "Coverage"
                     :title seq
                     :nbins 100)
          :width 800 :height 600)))

