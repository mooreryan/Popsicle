(ns popsicle.plots
  (:require [popsicle.alignment-info :as al])
  (:use [incanter core stats charts]))

(defn graph
  "Takes the base cov map from find-bases and outputs a histogram of
  the coverage for each reference sequence."
  [base-map ref-name]
  (let [bases (base-map ref-name)
        freqs (frequencies (flatten bases))
        xs (range 1 (inc (:length base-map)))
        ys (map (fn [x]
                  (if (contains? freqs x)
                    (freqs x)
                    0))
                xs)
        max-y (apply max ys)]
    (doto (xy-plot xs ys
                   :x-label "Base position"
                   :y-label "Coverage"
                   :title ref-name)
      #_(add-lines (repeat max-y 100) (range max-y))
      (view :width 1200 :height 800))))

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

