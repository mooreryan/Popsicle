(ns align_view.core-spec
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
