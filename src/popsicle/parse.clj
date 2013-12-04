(ns popsicle.parse
  (:require [clojure.java.io :as io]))

(defn read-file 
  "Read that file!"
  [fname]
  (let [refs (atom [])] 
    (with-open [rdr (io/reader fname)]
      (doseq [line (line-seq rdr)]
        (swap! refs conj line)))
    @refs))

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
