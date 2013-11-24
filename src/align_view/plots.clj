(ns align_view.plots
  (:require [align_view.alignment-info :as al])
  (:use [incanter core stats charts]))

#_(defn covered-bases
    "Takes the read start end map and produces a seq with the covered bases."  
    [entry]
    (range (:start entry) (+ 1 (:end entry))))

#_(defn find-bases
    "Takes the info map from the align-info file and outputs a hash map
  with the reference sequence as key and the bases covered as value."
    [info]
    (println (str "\nStarting find-bases at " ) 
             (java.util.Date.))
    (let [g (println (str "\tStarting groups at " ) 
                     (java.util.Date.))
          groups (group-by :ref info)
          r (println (str "\tStarting refs at " ) 
                     (java.util.Date.))
          refs (keys groups)
          rf (println (str "\tStarting ref-reads at " ) 
                      (java.util.Date.))
          ref-reads (map #(groups %) refs)
          cb (println (str "\tStarting covered bases at " ) 
                      (java.util.Date.))
          covered-bases (map (fn [info-hashes] 
                               (flatten (map #(covered-bases %) 
                                             info-hashes))) 
                             ref-reads)]
      (println (str "\tStarting into at " ) 
               (java.util.Date.))
      (into {} (map #(hash-map %1 %2) refs covered-bases))))

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

