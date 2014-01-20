(ns popsicle.alignment-info
  (:require [clojure.java.io :as io]
            [clojure.set :as set])
  (:import (net.sf.samtools SAMFileReader
                            SAMFileWriter
                            SAMFileWriterFactory
                            SAMRecord
                            SAMRecordIterator
                            SAMFileHeader
                            SAMSequenceRecord))
  (:use [incanter core stats charts]))

(defn new-sf-reader
  "Take a bam and an index and return SAMFileReader object w/index."
  [b-name i-name]
  (let [reader (SAMFileReader. (io/file b-name)
                               (io/file i-name))
        ; returns nil -> stateful
        lenient-reader 
        (.setValidationStringency 
         reader 
         (net.sf.samtools.SAMFileReader$ValidationStringency/valueOf 
          "LENIENT"))]
    reader))

(defn make-ref-iter
  "Takes the sf-reader and makes an iterator containing only reads
  mapping to the references in question. Use this function to get an
  iter taht returns all reads mapping to a reference regardless of the
  regions that reference contains."  
  [sf-reader ref-name]
  (.queryOverlapping sf-reader ref-name 0 0))

(defn make-overlap-reg-iter
  "Use this for when you want all the read that overlap a particular
  region on a reference sequence."  
  [sf-reader ref-name reg-name start end]
  (.queryOverlapping sf-reader ref-name start end))

(defn make-contained-reg-iter
  "Use this for when you want all the read that are contained in a
  particular region on a reference sequence. These will be 'bridgers'"
  [sf-reader ref-name reg-name start end]
  (.queryContained sf-reader ref-name start end))

(defn get-ref-len
  "Gets the reference sequence length. `ref` is either the reference
  sequence name or the index. This will throw
  clojure.lang.Reflector.invokeNoArgInstanceMember if the reference
  isn't present in the bam file which leads to a NullPointerException."
  [sf-reader ref]
  (let [header (.getFileHeader sf-reader)
        seq-record (.getSequence header ref)
        seq-len (.getSequenceLength seq-record)]
    seq-len))

(defn align-info
  "Parses the samtools iterator and returns a map with the reference
  pointing to a vector of vectors one for each read containing the
  bases covered for each read that maps to that reference, and the
  length of the reference sequence.

  Outputs a map: {'seq1' [[2 3 4 5] [4 5 6 7 8] [10 11 12]]
                  :length 809}

  Closes the iterator at the end."  
  [sf-reader sf-iter ref-name]
  (let [iter (iterator-seq sf-iter)
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
    (.close sf-iter)
    @info-map))

;; these used to be in the plots namespace

(defn peak
  "Counts the number of elements in `col` below the mean and divides
  that by the number of elements in the collection.

  This function is slow."  
  [col]
  (double (/ (count (filter #(< % (mean col)) col)) 
             (count col))))

(defn num-peaks
  "Finds local mins and maxes. Based on
  https://gist.github.com/sixtenbe/1178136" 
  [ys min-width]
  (let [mid (fn [v] (nth v (quot (count v) 2)))
        ]))

(defn sign-change
  "Gives a ratio of sign changes by total possible sign changes about
  the mean. Lower should be better.

  This function is slow."  
  [col]
  (double (/ (count (filter #(not= (first %) (second %)) 
                            (partition 2 1 (map #(if (< % (mean col)) 1 0)
                                                col)))) 
             (dec (count col)))))

(defn region-stats
  "Returns a lazy-seq of strings with info about the ORFs:

   ref-name\treg-name\tlength\tmin\tmax\trange\tmean\tsd\t
   sd/mean\tsd/range\tpeak\tsign-change

  This function could be performed during the (graph) doseq call."
  [ys regions ref-name]
  (for [[x y reg-name] regions]
    (let [cov (if (and (zero? x) (zero? y))
                (into [] ys)
                (subvec (into [] ys) (dec x) y))
          length (count cov)
          min (apply min cov)
          max (apply max cov)
          the-range (- max min)
          the-mean (mean cov)
          the-sd (sd cov)
          the-peak (peak cov)
          the-sign-change (sign-change cov)]
      (println (str (clojure.string/join 
            "\t" 
            [ref-name
             reg-name
             the-peak
             the-sign-change
             (count (filter #(not= (first %) (second %)) 
                            (partition 2 1 (map #(if (< % (mean cov)) 1 0)
                                                cov))))
             (dec (count cov))])))
      (str (clojure.string/join 
            "\t" 
            [ref-name
             reg-name
             length
             min
             max
             the-range
             the-mean
             the-sd
             (if-not (zero? the-mean) 
               (/ the-sd the-mean)
               "X")
             (if-not (zero? the-range)
               (/ the-sd the-range)
               "X")
             the-peak
             the-sign-change])
           "\n"))))

(defn biosporc-stats
  [ys regions ref-name])


;;;; functions from BioSPORC

#_(defn get-orf-coverage
  [query-iter-map]
  (let [posns (range (:orf-start query-iter-map)
                     (inc (:orf-end query-iter-map)))
        mapped-reads (->> query-iter-map
                          :q-iter
                          iterator-seq
                          (filter #(complement 
                                    (.getReadUnmappedFlag %))))]))



#_(defn get-reads-from-iter 
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

#_(defn get-overlappers-from-orf-maps
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


#_(defn get-islanders-from-orf-maps
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

#_(defn get-bridgers
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

#_(defn combine-read-maps
  "Takes the islander maps and the bridger maps and combines them."
  [map1 map2]
  (into map1 map2))


#_(defn make-read-maps
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
