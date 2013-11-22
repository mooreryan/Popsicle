(ns align_view.plots-spec
  (:require [speclj.core :refer :all]
            [align_view.plots :refer :all]))

(describe "covered-bases"
          (should= [23 24 25 26]
                   (covered-bases {:start 23 :end 26 
                                   :ref "apple" :read "pie"})))
