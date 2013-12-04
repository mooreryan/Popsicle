(ns popsicle.parse-spec
  (:require [speclj.core :refer :all]
            [popsicle.parse :refer :all]))

(def test-file 
  "/Users/ryanmoore/projects/popsicle/test_files/ref_list.txt")

(describe "read-file"
          (it "returns a vector of the lines"
              (should= ["seq1" "seq2"]
                       (read-ref-file test-file))))
