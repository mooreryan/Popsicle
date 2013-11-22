(ns align_view.alignment-info-spec
  (:require [speclj.core :refer :all]
            [align_view.alignment-info :refer :all]))

(def sorted-bam
  "/Users/ryanmoore/projects/align_view/test_files/ex1.sorted.bam")

(def bam-index
  "/Users/ryanmoore/projects/align_view/test_files/ex1.sorted.bam.bai")

(describe "new-sam-file-reader"
  (let [new-bam (new-sf-reader sorted-bam bam-index)]
    (it (str  "takes a bam and an index and return "
              "SAMFileReader object w/index")
      (should= net.sf.samtools.SAMFileReader
               (type new-bam))
      (should (.hasIndex new-bam)))))

(def sfr
  (new-sf-reader sorted-bam bam-index))

(describe "print-stuff"
          (should= :apple (print-stuff sfr)))

(describe "align-info"
          (should= :apple (align-info sfr)))
