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

(describe "align-info"
          (it "returns a map of alignmet info" 
              (should= '({:read "B7_591:4:96:693:509", 
                          :ref "seq1", :start 1, :end 36} 
                         {:read "EAS54_65:7:152:368:113", :ref "seq1", 
                          :start 3, :end 37} 
                         {:read "EAS51_64:8:5:734:57", :ref "seq1", 
                          :start 5, :end 39} 
                         {:read "B7_591:1:289:587:906", :ref "seq1", 
                          :start 6, :end 41} 
                         {:read "EAS56_59:8:38:671:758", :ref "seq1", 
                          :start 9, :end 43} 
                         {:read "EAS56_61:6:18:467:281", :ref "seq1", 
                          :start 13, :end 47} 
                         {:read "EAS114_28:5:296:340:699", :ref "seq1", 
                          :start 13, :end 48} 
                         {:read "B7_597:6:194:894:408", :ref "seq1", 
                          :start 15, :end 49} 
                         {:read "EAS188_4:8:12:628:973", :ref "seq1", 
                          :start 18, :end 52} 
                         {:read "EAS51_66:7:68:402:50", :ref "seq1", 
                          :start 22, :end 56}) 
                       (count (align-info sfr)))))

