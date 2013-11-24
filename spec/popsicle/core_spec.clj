(ns popsicle.core-spec
  (:require [speclj.core :refer :all]
            [align_view.core :refer :all]))

(def sorted-bam
  "/Users/ryanmoore/projects/align_view/test_files/ex1.sorted.bam")

(def bam-index
  "/Users/ryanmoore/projects/align_view/test_files/ex1.sorted.bam.bai")


(describe "-main"
          #_(it "the main fun!"
              (-main "-b" sorted-bam "-i" bam-index))
          #_(it "with help arg"
              (-main "-h"))
          (it "can choose which to print"
              (-main "-b" sorted-bam "-i" bam-index
                     "-r" "seq1")))

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

