(ns popsicle.alignment-info-spec
  (:require [speclj.core :refer :all]
            [align_view.alignment-info :refer :all]))

(def sorted-bam
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam")

(def bam-index
  "/Users/ryanmoore/projects/align_view/test_files/small.sorted.bam.bai")

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

