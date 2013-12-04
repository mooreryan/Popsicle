(ns popsicle.parse
  (:require [clojure.java.io :as io]))


(defn read-file 
  "Read that file!"
  [fname]
  (let [refs (atom [])] 
    (with-open [rdr (io/reader fname)]
      (doseq [line (line-seq rdr)]
        (swap! refs conj line)))
    @refs))
