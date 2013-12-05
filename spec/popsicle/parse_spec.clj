(ns popsicle.parse-spec
  (:require [speclj.core :refer :all]
            [popsicle.parse :refer :all]))

(def test-file 
  "/Users/ryanmoore/projects/popsicle/test_files/ref_list.txt")
(def regions-file 
  "/Users/ryanmoore/projects/popsicle/test_files/regions.tab")

(describe "read-file"
          (it "returns a vector of the lines"
              (should= ["seq1" "seq2"]
                       (read-ref-file test-file))))

(describe "read-region-file"
          (it "reads the region file and returns a map"
              (should= {"seq2" [[250 750 "orf1"] [1000 1200 "orf2"]]
                        "seq1" [[100 300 "orf1"] [1000 1500 "orf2"]]}
                       (read-region-file regions-file))))
