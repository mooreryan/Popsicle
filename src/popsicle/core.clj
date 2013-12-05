(ns popsicle.core
  (:require [popsicle.alignment-info :refer :all]
            [popsicle.plots :refer :all]
            [popsicle.parse :refer :all]
            [clojure.tools.cli :as cli])
  (:gen-class :main true))

(def usage-str
  (str "\nExample: \njava -jar popsicle-x.y.z.jar -b <bam-file> "
       "-i <index-file> -r <reference-seq-file> "
       "-e <regions-file> -s <stats-output-file>"))

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
                   ["-r" "--references-file" 
                    "Reference sequenes to query"]
                   ["-e" "--regions-file" 
                    "File with regions"]
                   ["-s" "--stats-file"
                    "Path to output stats info."])
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
      (when-not (:references-file options)
        (println "\nSpecify a reference file")
        (println usage-str)
        (System/exit 0))

      (when-not (:regions-file options)
        (println "\nSpecify a regions file")
        (println usage-str)
        (System/exit 0))

      (when-not (:stats-file options)
        (println "\nSpecify a stats output file")
        (println usage-str)
        (System/exit 0))

      
      (let [ref-queries (read-ref-file (:references-file options))
            sf-reader (new-sf-reader (:bam-file options)
                                     (:bam-index options))
            regions (read-region-file (:regions-file options))
            stats-strings (atom [])]
        (doseq [ref ref-queries]
          (let [ys (graph 
                    (align-info sf-reader ref) ref (regions ref))
                region-stats (stats ys (regions ref))]
            (swap! stats-strings
                   conj 
                   (apply str region-stats))))
        (spit (:stats-file options) 
              (apply 
               str 
               ";length\tmin\tmax\trange\tmean\tsd\tmean/sd\tlength/sd\n" 
               @stats-strings))))))

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

