(ns align_view.alignment-info
  (:require [clojure.java.io :as io]
            [clojure.set :as set])
  (:import (net.sf.samtools SAMFileReader
                            SAMFileWriter
                            SAMFileWriterFactory
                            SAMRecord
                            SAMRecordIterator)))

(defn new-sf-reader
  "Take a bam and an index and return SAMFileReader object w/index."
  [b-name i-name]
  (SAMFileReader. (io/file b-name)
                  (io/file i-name)))

(defn print-stuff
  [sf-reader]
  (let [iter (take 10 (->> sf-reader
                          .iterator
                          iterator-seq
                          (filter #(complement 
                                    (.getReadUnmappedFlag %)))))]
    (apply str 
           (map #(str (.getReferenceName %) "\t" 
                      (.getReadName %) "\t"
                      (.getAlignmentStart %) "\t"
                      (.getAlignmentEnd %) "\n") 
                iter))))

(defn align-info
  "Parses the sam file reader and returns a map with the reference,
  read, start and end."  
  [sf-reader]
  (let [iter (take 10 (->> sf-reader
                          .iterator
                          iterator-seq
                          (filter #(complement 
                                    (.getReadUnmappedFlag %)))))]
    (apply str 
           (map #(hash-map :ref (.getReferenceName %)
                           :read (.getReadName %)
                           :start (.getAlignmentStart %)
                           :end (.getAlignmentEnd %)) 
                iter))))
