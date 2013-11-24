(ns popsicle.alignment-info-spec
  (:require [speclj.core :refer :all]
            [popsicle.alignment-info :refer :all]))

(def sorted-bam
  "/Users/ryanmoore/projects/popsicle/test_files/small.sorted.bam")

(def bam-index
  "/Users/ryanmoore/projects/popsicle/test_files/small.sorted.bam.bai")

#_(describe "new-sam-file-reader"
  (let [new-bam (new-sf-reader sorted-bam bam-index)]
    (it (str  "takes a bam and an index and return "
              "SAMFileReader object w/index")
      (should= net.sf.samtools.SAMFileReader
               (type new-bam))
      (should (.hasIndex new-bam)))))

(def sfr
  (new-sf-reader sorted-bam bam-index))

;; should actually check these values against the sam?
#_(describe "align-info"
          (it "returns a map of alignmet info" 
              (should= :a
                       (align-info sfr))))

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

