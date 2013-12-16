(ns popsicle.plots
  (:require [popsicle.alignment-info :as al])
  (:import (org.jfree.chart.plot ValueMarker))
  (:use [incanter core stats charts]))

(defn peak
  "Counts the number of elements in `col` below the mean and divides
  that by the number of elements in the collection.

  This function is slow."  
  [col]
  (double (/ (count (filter #(< % (mean col)) col)) 
             (count col))))

(defn sign-change
  "Gives a ratio of sign changes by total possible sign changes about
  the mean.

  This function is slow."  
  [col]
  (double (/ (count (filter #(not= (first %) (second %)) 
                            (partition 2 1 (map #(if (< % (mean col)) 1 0)
                                                col)))) 
             (dec (count col)))))

(defn region-stats
  "Returns a lazy-seq of strings with info about the ORFs:

   ref-name\treg-name\tlength\tmin\tmax\trange\tmean\tsd\t
   sd/mean\tsd/range\tpeak\tsign-change

  This function could be performed during the (graph) doseq call."
  [ys regions ref-name]
  (for [[x y reg-name] regions]
    (let [cov (subvec (into [] ys) (dec x) y)
          length (- y x)
          min (apply min cov)
          max (apply max cov)
          the-range (- max min)
          the-mean (mean cov)
          the-sd (sd cov)
          the-peak (peak cov)
          the-sign-change (sign-change cov)]
      (println (str (clojure.string/join 
            "\t" 
            [ref-name
             reg-name
             the-peak
             the-sign-change
             (count (filter #(not= (first %) (second %)) 
                            (partition 2 1 (map #(if (< % (mean cov)) 1 0)
                                                cov))))
             (dec (count cov))])))
      (str (clojure.string/join 
            "\t" 
            [ref-name
             reg-name
             length
             min
             max
             the-range
             the-mean
             the-sd
             (/ the-sd the-mean)
             (/ the-sd the-range)
             the-peak
             the-sign-change])
           "\n"))))

(defn graph
  "Takes the base cov map from find-bases and outputs a histogram of
  the coverage for each reference sequence. Returns the ys for
  processing by the region-stats function."  
  [base-map ref-name regions]
  (let [bases (base-map ref-name)
        freqs (frequencies (flatten bases))
        xs (range 1 (inc (:length base-map)))
        ys (map (fn [x]
                  (if (contains? freqs x)
                    (freqs x)
                    0))
                xs)
        max-y (apply max ys)
        plot1 (xy-plot xs ys
                   :x-label "Base position"
                   :y-label "Coverage"
                   :title ref-name)
        xy (.getXYPlot plot1)]
    (set-stroke-color plot1 java.awt.Color/black)
    (doseq [[x-val y-val] regions]
      (.addDomainMarker xy (ValueMarker. 
                            x-val 
                            java.awt.Color/green 
                            (java.awt.BasicStroke. 2)))
      (.addDomainMarker xy (ValueMarker. 
                            y-val 
                            java.awt.Color/red 
                            (java.awt.BasicStroke. 2))))
    #_(view plot1 :width 1200 :height 800)
    (save plot1 (str ref-name ".png") :width 1200 :height 800)
    ys))

;; Copyright 2013 Ryan Moore

;; This file is part of Popsicle.

;; Popsicle is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; Popsicle is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with Popsicle.  If not, see <http://www.gnu.org/licenses/>.

