(ns popsicle.plots-spec
  (:require [speclj.core :refer :all]
            [align_view.plots :refer :all]
            [align_view.alignment-info :refer :all]))

(def sorted-bam
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam")
(def bam-index
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam.bai")
(def sfr
  (new-sf-reader sorted-bam bam-index))
(def al-sfr
  (align-info sfr "seq1"))

#_(describe "hist"
          (it "prints the graphs"
              (should-not
               (hist al-sfr)))
          (it "prints only specified graphs"
              (should-not
               (hist al-sfr ["seq1"]))))

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

