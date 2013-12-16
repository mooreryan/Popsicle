(ns popsicle.parse
  (:require [clojure.java.io :as io]))

(defn read-ref-file 
  "Read that file!"
  [fname]
  (let [refs (atom [])] 
    (with-open [rdr (io/reader fname)]
      (doseq [line (line-seq rdr)]
        (swap! refs conj line)))
    @refs))

(defn read-region-file
  "Read the file with regions. { 'ref1' [23 55 89 233] }"
  [fname]
  (let [info-map (atom {})] 
    (with-open [rdr (io/reader fname)]
      (doseq [line (line-seq rdr)]
        (let [split-line (clojure.string/split line #",")
              ref (first split-line)
              region (second split-line)
              start (Integer. (split-line 2))
              end (Integer. (last split-line))]
          (if (contains? @info-map ref)
            (swap! info-map assoc ref 
                   (conj (@info-map ref) 
                         [start end region]))
            (swap! info-map assoc ref [[start end region]])))))
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
