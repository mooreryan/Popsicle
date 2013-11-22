(defproject align_view "0.0.0"
  :description "view a bam alignment"
  :url "www.udel.edu"
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.txt"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.sf/samtools "1.86"]
                 [net.sf/picard "1.86"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.apache.commons/commons-math3 "3.1.1"]
                 [incanter "1.5.4"]
                 ]
  :repositories {"picard-tools" ~(str (.toURI 
                                       (java.io.File. "maven_repo")))}
  :profiles {:dev {:dependencies [[speclj "2.8.0"]]}}
  :plugins [[speclj "2.8.0"]]
  :test-paths ["spec"]
  :main align_view.core)
