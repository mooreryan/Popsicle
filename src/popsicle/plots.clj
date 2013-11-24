(ns popsicle.plots
  (:require [popsicle.alignment-info :as al])
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
                     :y-label "Relative coverage (per Len/100 base bin)"
                     :title seq
                     :nbins 100)
          :width 800 :height 600)))

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

