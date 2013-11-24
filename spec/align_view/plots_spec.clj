(ns align_view.plots-spec
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
