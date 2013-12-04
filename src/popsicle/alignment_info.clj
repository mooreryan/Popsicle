(ns popsicle.alignment-info
  (:require [clojure.java.io :as io]
            [clojure.set :as set])
  (:import (net.sf.samtools SAMFileReader
                            SAMFileWriter
                            SAMFileWriterFactory
                            SAMRecord
                            SAMRecordIterator
                            SAMFileHeader
                            SAMSequenceRecord)))

(defn new-sf-reader
  "Take a bam and an index and return SAMFileReader object w/index."
  [b-name i-name]
  (SAMFileReader. (io/file b-name)
                  (io/file i-name)))

(defn make-iter
  "Takes the sf-reader and makes an iterator containing only reads
  mapping to the references in question."
  [sf-reader ref-name]
  (.queryOverlapping sf-reader ref-name 0 0))

(defn get-ref-len
  "Gets the reference sequence length. `ref` is either the reference
  sequence name or the index."  
  [sf-reader ref]
  (let [header (.getFileHeader sf-reader)
        seq-record (.getSequence header ref)
        seq-len (.getSequenceLength seq-record)]
    seq-len))

(defn align-info
  "Parses the sam file reader and returns a map with the reference,
  read, start and end.

  Outputs a map: {'seq1' [[2 3 4 5] [4 5 6 7 8] [10 11 12]]
                  :length 809}"  
  [sf-reader ref-name]
  (let [j-iter (make-iter sf-reader ref-name)
        iter (iterator-seq j-iter)
        num-queries (count iter)
        info-map (atom {})
        ref-len (get-ref-len sf-reader ref-name)]
    (swap! info-map assoc :length ref-len)
    (doseq [elem iter]
      (let [ref (.getReferenceName elem)
            read (.getReadName elem)
            start (.getAlignmentStart elem)
            end (.getAlignmentEnd elem)]
        (if-not (.getReadUnmappedFlag elem)
          (if (contains? @info-map ref)
            (swap! info-map assoc ref (conj (@info-map ref) 
                                            (range start (inc end))))
            (swap! info-map assoc ref [(range start (inc end))])))))
    (println (clojure.string/join "\t"
              [ref-name 
               ref-len 
               num-queries 
               (count (@info-map ref-name))]))
    (.close j-iter)
    @info-map))

;; Copyright 2013 Ryan Moore

;; This file is part of Popsicle.

;; Popsicle is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; Popsicle is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with Popsicle.  If not, see <http://www.gnu.org/licenses/>.
