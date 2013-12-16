(ns popsicle.plots-spec
  (:require [speclj.core :refer :all]
            [popsicle.plots :refer :all]
            [popsicle.alignment-info :refer :all])
  (:use [incanter core stats charts]))

(def sorted-bam
  "/Users/ryanmoore/projects/popsicle/test_files/small.sorted.bam")
(def bam-index
  "/Users/ryanmoore/projects/popsicle/test_files/small.sorted.bam.bai")
(def sfr
  (new-sf-reader sorted-bam bam-index))
(def al-sfr
  (align-info sfr (make-ref-iter sfr "seq1") "seq1"))

#_(describe "graph"
          (it "prints the graphs"
              (should-not
               (graph al-sfr "seq1"))))

(defn build-tests
  "x: low end of non-peak section
   y: high end of non-peak section
   length: total length
   perc: percent of length in peak
   mult: how steep is the peak
   ramp: how steep is the peak (percent of len of peak)"
  [x y length perc mult ramp]
  (let [peak-length (* perc length)
        non-peak-half-length (Math/round (/ (- length peak-length) 2))
        ramp-half-length (Math/round (/ (* ramp peak-length) 2))
        left (sample (range x y) :size non-peak-half-length)
        peak (sample (range (* mult x) (* mult y)) 
                     :size (- peak-length (* 2 ramp-half-length)))
        right (sample (range x y) :size non-peak-half-length)
        ramp-left (sort (sample-uniform ramp-half-length 
                         :min (last left) :max (first peak)))
        ramp-right (reverse 
                    (sort (sample-uniform 
                           ramp-half-length 
                           :min (first right) :max (last peak))))]
    (println  (count left) 
              (count ramp-left) 
              (count peak) 
              (count ramp-right) 
              (count right))
    (flatten [left ramp-left peak ramp-right right])))

(defn view-me [col] 
  (view (xy-plot (range (count col)) col))
  [(peak col) (sign-change col)])


(describe "peak"
          (it (str "counts the number of elements below the mean and "
                   "divides that by the mean")
              (should= 0.4
                       (peak [1 2 3 4 5]))))

(describe "sign-change"
          (it "returns the ratio of sign changes"
              (should= 0.25
                       (sign-change [1 2 3 4 5]))
              (should= 1.0
                       (sign-change [1 3 1 3 1 3]))
              (should= 0.5
                       (sign-change [1 3 1 3 1 3 3 3 3 3 3]))))

(def regions {"ref1" [[2 5 "reg1"] [8 23 "reg2"]]})
(def ys (lazy-seq [2 3 4 5 4 3 5 6 7 8 7 6 4 6 4 6 7 8 
                   9 7 5 3 4 5 4 2 1 0 0 0 0 0 0 0 0 0]))

(describe "region-stats"
          (it "returns a string with info about orfs"
              (should= ["ref1\treg1\t3\t3\t5\t2\t4.0\t0.816496580927726\t0.2041241452319315\t0.408248290463863\t0.25\t0.3333333333333333\n" 
                        "ref1\treg2\t15\t3\t9\t6\t6.0625\t1.691892431568863\t0.27907504025878155\t0.28198207192814384\t0.5625\t0.2666666666666667\n"] 
                       (region-stats ys (regions "ref1") "ref1"))))

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

