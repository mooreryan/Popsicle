(ns align_view.core
  (:require [align_view.alignment-info :refer :all]
            [align_view.plots :refer :all]
            [clojure.tools.cli :as cli])
  (:gen-class :main true))

(def usage-str
  (str "\nExample: \njava -jar align_view-0.0.0.jar -b <bam-file> "
       "-i <index-file>"
       " [list of reads to include in graphs (optional)]\n\n"))

(defn -main
  [& args]
  (println (str "\nStarting -main at " ) 
           (java.util.Date.))
  (let [[options extras banner]
        (try
          (cli/cli args 
                   ["-h" "--help" "Show help." 
                    :default false :flag true]
                   ["-b" "--bam-file" "Sorted bam file"]
                   ["-i" "--bam-index" "Index file"])
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

      
      (let [sf-reader (new-sf-reader (:bam-file options)
                                     (:bam-index options))
            alignment-info (align-info sf-reader)
            align-bases (find-bases alignment-info)] 
        (if (seq extras)
          (hist align-bases extras)
          (hist align-bases))
        (println (str "\nDone!" (java.util.Date.)) "\n\n")
        (System/exit 0)))))
