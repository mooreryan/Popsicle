(ns popsicle.core
  (:require [popsicle.alignment-info :refer :all]
            [popsicle.plots :refer :all]
            [popsicle.parse :refer :all]
            [clojure.tools.cli :as cli])
  (:use [incanter core stats charts])
  (:gen-class :main true))

(def usage-str
  (str "\nExample: \njava -jar popsicle-x.y.z.jar -b <bam-file> "
       "-i <index-file> "
       "-r <regions-file> -s <stats-output-file>"
       "-d <image-output-folder>"))

(defn -main
  [& args]
  (println ";reference\tlength\tmatching-queries\taligned-queries")
  (let [[options extras banner]
        (try
          (cli/cli args 
                   ["-h" "--help" "Show help." 
                    :default false :flag true]
                   ["-b" "--bam-file" "Sorted bam file"]
                   ["-i" "--bam-index" "Index file"]
                   ["-r" "--regions-file" 
                    "File with regions"]
                   ["-s" "--stats-file"
                    "Path to output stats info."]
                   ["-d" "--directory"
                    "Folder to output images"]
                   ["-c" "--cov-file"
                    "Path to output coverage info."])
          (catch java.lang.Exception e))]

    (do
      (when (:help options)
        (println usage-str banner)
        (System/exit 0))

      (when-not (:bam-file options)
        (println "\nSpecify a sorted bam file!")
        (println usage-str)
        (System/exit 0))

      (when-not (:bam-index options)
        (println "\nSpecify a bam index file!")
        (println usage-str)
        (System/exit 0))

      ;; todo - check for file existing
      (when-not (:regions-file options)
        (println "\nSpecify a regions file")
        (println usage-str)
        (System/exit 0))

      (when-not (:stats-file options)
        (println "\nSpecify a stats output file")
        (println usage-str)
        (System/exit 0))

      (when-not (:directory options)
        (println "\nSpecify a folder for the images")
        (println usage-str)
        (System/exit 0))

      (when-not (:cov-file options)
        (println "\nSpecify a cov output file")
        (println usage-str)
        (System/exit 0))


      (spit (:stats-file options)
            (str ";ref\tregion\tlength\tmin\tmax\trange\tmean\tsd"
                    "\tsd/mean\tsd/range\tpeak\tsign-change\n"))

      (let [sf-reader (new-sf-reader (:bam-file options)
                                     (:bam-index options))
            regions (read-region-file (:regions-file options))
            ref-queries (keys regions)
            stats-strings (atom [])]
        ;; to get the new stats
        (doseq [ref ref-queries]
                                        ; ys is the coverage vector
          (let [ys (graph (align-info sf-reader 
                                      (make-ref-iter sf-reader ref) 
                                      ref) 
                          ref 
                          (regions ref)
                          (:directory options))
                reg-stats (region-stats ys (regions ref) ref)]
            (spit (:cov-file options)
                  (apply str 
                         ref 
                         ",[" 
                         (clojure.string/join " " ys) "]")
                  :append true)
            (spit (:stats-file options)
                  (apply str reg-stats)
                  :append true)
            #_(swap! stats-strings
                   conj 
                   (apply str reg-stats))))
        #_(spit (:stats-file options) 
              (apply 
               str 
               (str ";ref\tregion\tlength\tmin\tmax\trange\tmean\tsd"
                    "\tsd/mean\tsd/range\tpeak\tsign-change\n") 
               @stats-strings))
        ;; to get the old biosporc stats
        ;; each region-vec will have multiple vectors
        #_(doseq [[ref regions-vec] regions]
          (dorun
           (map (fn [region]
                  (let [start (first region)
                        end (second region)
                        name (last region)
                        overlap-ys (align-info 
                                    sf-reader
                                    (make-overlap-reg-iter sf-reader
                                                           ref
                                                           name
                                                           start
                                                           end)
                                    ref)
                        overlap-read-info (into #{} (overlap-ys ref))
                        contained-ys (align-info 
                                      sf-reader
                                      (make-contained-reg-iter
                                       sf-reader
                                       ref
                                       name
                                       start
                                       end)
                                      ref)
                        contained-read-info (into #{} (contained-ys ref))
                        bridger-set (clojure.set/difference
                                     overlap-read-info
                                     contained-read-info)
                        bridgers {ref (vec bridger-set)}
                        islanders {ref (vec (clojure.set/difference
                                             contained-read-info
                                             bridger-set))}]
                    (def foo (fn [align-info-output]
                               (let [freqs
                                     (frequencies (flatten 
                                                   (align-info-output 
                                                    ref)))
                                     xs (range start (inc end))
                                     ys (map (fn [x]
                                               (if (contains? freqs x)
                                                 (freqs x)
                                                 0))
                                             xs)]
                                 ys)))
                    (println "overlapping ref " ref "name " name 
;                             "ys " (foo overlap-ys) 
                             "count " (count (foo overlap-ys)) 
                             "total" (sum (foo overlap-ys)))
                    (println "contained ref " ref "name " name 
;                             "ys " (foo contained-ys) 
                             "count " (count (foo contained-ys)) 
                             "total" (sum (foo contained-ys)))
                    (println "bridgers " ref "name " name 
;                            "ys " (foo bridgers)
                             "count " (count (foo bridgers)) 
                             "total" (sum (foo bridgers)))
                    (println "islanders " ref "name " name 
;                             "ys " (foo islanders)
                             "count " (count (foo islanders)) 
                             "total" (sum (foo islanders)))
))
                regions-vec)))))))
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

