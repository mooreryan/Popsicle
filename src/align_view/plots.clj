(ns align_view.plots
  (:require [align_view.alignment-info :as al])
  (:use [incanter core stats charts]))

(defn covered-bases
  "Takes the an entry from the map generated by align-info and outputs
  the covered bases in a sequence."  
  [entry]
  (range (:start entry) (+ 1 (:end entry))))

(defn find-bases
  [info]
  (let [groups (group-by :ref info)
        refs (keys groups)
        ref-reads (map #(groups %) refs)
        covered-bases (map (fn [info-hashes] 
                             (flatten (map #(covered-bases %) 
                                           info-hashes))) 
                           ref-reads)]
    (into {} (map #(hash-map %1 %2) refs covered-bases))))

(defn hist 
  "Takes the base cov map from find-bases and outputs a histogram of
  the coverage for each reference sequence."  
  [base-map]
  (doall (for [[seq bases] base-map]
           #_(print seq bases)
           (view (histogram bases)))))
