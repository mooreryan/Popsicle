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

;;;; functions from BioSPORC

(defn get-orf-coverage
  [query-iter-map]
  (let [posns (range (:orf-start query-iter-map)
                     (inc (:orf-end query-iter-map)))
        mapped-reads (->> query-iter-map
                          :q-iter
                          iterator-seq
                          (filter #(complement 
                                    (.getReadUnmappedFlag %))))]))

(defn new-overlapping-query-iter 
  "Takes a SamFileReader obj, contig name, start position, end position
   and returns a map like so:

   {:contig-name contig-name :orf-number orf-name :q-iter
    overlapping-query-iterator :type :overlapping :orf-start 324
    :orf-end 3258}

   This iterator will return all reads overlapping the given interval
   on the given contig.

   Basically you will need to create one iter for each orf for each
   contig. TODO: Could these could be done as agents?"  
  [sam-file-reader contig-name orf-number start-pos end-pos]
  {:contig-name contig-name
   :orf-number orf-number
   :orf-start start-pos
   :orf-end end-pos
   :type :overlapping
   :q-iter (.queryOverlapping sam-file-reader contig-name 
                              start-pos end-pos)})

(defn new-contained-query-iter
  "Takes a SamFileReader obj, contig name, start position, end position
   and returns a map like so:

   {:contig-name contig-name :orf-number orf-name :q-iter
    overlapping-query-iterator :type :contained :orf-start 324
    :orf-end 3258}

   This iterator will return all reads contained the given interval on
   the given contig.

   Basically you will need to create one iter for each orf for each
   contig. TODO: Could these could be done as agents?"
  [sam-file-reader contig-name orf-number start-pos end-pos]
  {:contig-name contig-name
   :orf-number orf-number
   :orf-start start-pos
   :orf-end end-pos
   :type :islander
   :q-iter (.queryContained sam-file-reader contig-name 
                            start-pos end-pos)})



(defn get-reads-from-iter 
  "Returns a sequence of ORF maps.

   You will need to run this function once for every overlapping
   query iterator. Done in agents?"
  [query-iter-map]
  (let [mapped-reads 
        (->> query-iter-map
             :q-iter
             iterator-seq
             (filter #(complement (.getReadUnmappedFlag %))))]
    (map (fn [read]
           (hash-map :contig-name (.getReferenceName read)
                     :orf-number (:orf-number query-iter-map)
                     :read-name (.getReadName read)
                     :align-start (.getAlignmentStart read)
                     :align-end (.getAlignmentEnd read)
                     :type (:type query-iter-map)))
         mapped-reads)))

;;;; the funs below will be called from main ;;;;

(defn get-overlappers-from-orf-maps
  "Get the overlapping read maps from a seq of orf-maps. There will be
  multiple read maps (likely) per ORF map.

  Really this is a convience function that combines all the above into
  one easy-to-call package."
  [orf-maps b-name i-name]
  (->> orf-maps
       (map (fn [orf-map]
              (let [overlap-rdr (new-sam-file-reader b-name i-name)
                    overlap-q-iter-map (new-overlapping-query-iter
                                        overlap-rdr
                                        (:contig-name orf-map)
                                        (:orf-number orf-map)
                                        (:orf-start orf-map)
                                        (:orf-end orf-map))]
                (get-reads-from-iter overlap-q-iter-map))))
       (reduce into)))


(defn get-islanders-from-orf-maps
  "Get the islanders read maps from a seq of orf-maps. There will be
  multiple read maps (likely) per ORF map.

  Really this is a convience function that combines all the above into
  one easy-to-call package."
  [orf-maps b-name i-name]
  (->> orf-maps 
       (map (fn [orf-map]
              (let [contain-rdr (new-sam-file-reader b-name i-name)
                    contain-q-iter-map (new-contained-query-iter
                                        contain-rdr
                                        (:contig-name orf-map)
                                        (:orf-number orf-map)
                                        (:orf-start orf-map)
                                        (:orf-end orf-map))]
                (get-reads-from-iter contain-q-iter-map))))
       (reduce into)))

(defn get-bridgers
  "Uses set difference...Subtracts islander reads from overlappers to
  give you bridgers.

  Unfortunately you have to realize the whole lazy seq to check if
  it's in there or not"
  [overlappers islanders]
  (lazy-seq 
   (map #(assoc % :type :bridger) 
        (set/difference (into #{} (map #(dissoc % :type) 
                                       overlappers)) 
                        (into #{} (map #(dissoc % :type) 
                                       islanders))))))

(defn combine-read-maps
  "Takes the islander maps and the bridger maps and combines them."
  [map1 map2]
  (into map1 map2))


(defn make-read-maps
  "Does the work of getting alignment info!"
  [orf-maps bam bai]
  (let [overlappers (get-overlappers-from-orf-maps orf-maps bam bai)
        islanders (get-islanders-from-orf-maps orf-maps bam bai)
        bridgers (get-bridgers overlappers islanders)]
    (into islanders bridgers)))



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
