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

#_(defn print-stuff
  [sf-reader]
  (let [iter (take 10 (->> sf-reader
                           .iterator
                           iterator-seq
                           #_(filter #(complement 
                                       (.getReadUnmappedFlag %)))))]
    (apply str 
           (map #(str (.getReferenceName %) "\t" 
                      (.getReadName %) "\t"
                      (.getAlignmentStart %) "\t"
                      (.getAlignmentEnd %) "\n") 
                iter))))

#_(defn align-info-old
    "Parses the sam file reader and returns a map with the reference,
  read, start and end."  
    [sf-reader]
    (let [iter (->> sf-reader
                    .iterator
                    iterator-seq
                    (filter #(complement 
                              (.getReadUnmappedFlag %))))]
      (map #(hash-map :ref (.getReferenceName %)
                      :read (.getReadName %)
                      :start (.getAlignmentStart %)
                      :end (.getAlignmentEnd %)) 
           iter)))

#_(def info-map (atom {}))

(defn make-iter
  "Takes the sf-reader and makes an iterator containing only reads
  mapping to the references in question."
  [sf-reader ref-name]
  (.queryOverlapping sf-reader ref-name 0 0))

#_(defn align-info
  "Parses the sam file reader and returns a map with the reference,
  read, start and end.

  Call this once for each overlapping iterator needed.

  Outputs a map: {'seq1' [[2 3 4 5] [4 5 6 7 8] [10 11 12]]}"  
  [iterator]
  (println (str "\nStarting align-info at " ) 
           (java.util.Date.))
  (let [iter (iterator-seq iterator) 
        info-map (atom {})]
    (println (str "Building the info map at ") (java.util.Date.))
    (doseq [elem iter]
      (let [ref (.getReferenceName elem)
            read (.getReadName elem)
            start (.getAlignmentStart elem)
            end (.getAlignmentEnd elem)]
        (print ".")
        (if (contains? @info-map ref)
          (swap! info-map assoc ref (conj (@info-map ref) (range start (inc end))))
          (swap! info-map assoc ref [(range start (inc end))]))))
    @info-map))

(defn align-info
  "Parses the sam file reader and returns a map with the reference,
  read, start and end.

  Outputs a map: {'seq1' [[2 3 4 5] [4 5 6 7 8] [10 11 12]]}"  
  [sf-reader ref-name]
  (println (str "\nStarting align-info at " ) 
           (java.util.Date.))
  (let [iter (iterator-seq (make-iter sf-reader ref-name)) 
        info-map (atom {})]
    (println (str "Building the info map at ") (java.util.Date.))
    (println (str "Num matching queries: " (count iter)))
    (doseq [elem iter]
      (let [ref (.getReferenceName elem)
            read (.getReadName elem)
            start (.getAlignmentStart elem)
            end (.getAlignmentEnd elem)]
        (if (contains? @info-map ref)
          (swap! info-map assoc ref (conj (@info-map ref) (range start (inc end))))
          (swap! info-map assoc ref [(range start (inc end))]))))
    @info-map))
