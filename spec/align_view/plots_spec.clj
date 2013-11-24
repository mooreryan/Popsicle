(ns align_view.plots-spec
  (:require [speclj.core :refer :all]
            [align_view.plots :refer :all]
            [align_view.alignment-info :refer :all]))




#_(describe "covered-bases"
          (it "returns a seq of bases"
              (should= (range 23 101)
                       (covered-bases {:start 23 :end 100 
                                       :ref "apple" :read "pie"}))))




(def sorted-bam
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam")
(def bam-index
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam.bai")
(def sfr
  (new-sf-reader sorted-bam bam-index))
(def al-sfr
  (align-info sfr "seq1"))

#_(describe "find-bases"
          (it "returns a hash-map with as many entries as ref seqs"
              (should= 2
                       (find-bases al-sfr))))


#_(describe "hist"
          (it "prints the graphs"
              (should-not
               (hist al-sfr)))
          (it "prints only specified graphs"
              (should-not
               (hist al-sfr ["seq1"]))))
