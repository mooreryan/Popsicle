(defproject popsicle "0.0.1-3"
  :description "view a bam alignment"
  :url "http://bioinformatics.udel.edu/"
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.txt"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.sf/samtools "1.86"]
                 [net.sf/picard "1.86"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.apache.commons/commons-math3 "3.1.1"]
                 [incanter "1.5.4"]
                 [jfree/jfreechart "1.0.13"]]
  :repositories {"picard-tools" ~(str (.toURI 
                                       (java.io.File. "maven_repo")))}
  :profiles {:dev {:dependencies [[speclj "2.8.0"]]}}
  :plugins [[speclj "2.8.0"]]
  :test-paths ["spec"]
  :jvm-opts ["-Xms256m" "-Xmx2g"]
  :main popsicle.core)

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

