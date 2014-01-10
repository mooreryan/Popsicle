(ns popsicle.plots
  (:require [popsicle.alignment-info :as al])
  (:import (org.jfree.chart.plot ValueMarker))
  (:use [incanter core stats charts]))


(defn graph
  "Takes the base cov map from find-bases and outputs a histogram of
  the coverage for each reference sequence. Returns the ys for
  processing by the region-stats function.

  ys looks like this [1 2 3 2 3 4 5 7 8 6 5 3 2 4 2]. It is the per
  base coverage."  
  [base-map ref-name regions directory]
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
    (save plot1 (str directory 
                     "/" 
                     (clojure.string/replace
                      ref-name #"[/|]" "_") 
                     ".png") 
          :width 1200 :height 800)
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

