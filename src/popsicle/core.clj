(ns popsicle.core
  (:require [popsicle.alignment-info :refer :all]
            [popsicle.plots :refer :all]
            [popsicle.parse :refer :all]
            [clojure.tools.cli :as cli])
  (:gen-class :main true))

(def usage-str
  (str "\nExample: \njava -jar popsicle-x.y.z.jar -b <bam-file> "
       "-i <index-file> -r <reference seq file>"))

(defn -main
  [& args]
  (println ";reference\tmatching-queries\taligned-queries")
  (let [[options extras banner]
        (try
          (cli/cli args 
                   ["-h" "--help" "Show help." 
                    :default false :flag true]
                   ["-b" "--bam-file" "Sorted bam file"]
                   ["-i" "--bam-index" "Index file"]
                   ["-r" "--references-file" "Reference sequenes to query"])
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
        (println "\nSpecify a query")
        (println usage-str)
        (System/exit 0))

      
      (let [ref-queries (read-file (:references-file options))
            sf-reader (new-sf-reader (:bam-file options)
                                     (:bam-index options))]
        (doseq [ref ref-queries]
          (hist (align-info sf-reader ref)))))))

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

