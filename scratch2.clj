(defn sam-pair 
  "bit flag meanings: 
  65  --read paired, first in pair 
  129 --read paired, second in pair 
  73  --read paired, mate unmapped, first in pair
  137 --read paired, mate unmapped, second in pair
  0   --nothing! specificall it's not paired"
  [read-name ref-name start len insert-size]
  (let [ins-size (+ insert-size (sample (range -5 6) :size 1))
        read1-flag 65
        read2-flag 129
        cigar (str len "M")
        map-qual "*"
        mate-start ()
        read1-start start
        read1-end (+ read1-start (dec len))
        read2-start (+ read1-end (inc insert-size))
        read2-end (+ read2-start (dec len))
        rnext "="
        tlen (inc (- read2-end read1-start))
        seq "*"
        qual "*"]
    (str (clojure.string/join 
          "\t" 
          [read-name read1-flag ref-name read1-start map-qual cigar
           rnext read2-start tlen seq qual])
         "\n"
         (clojure.string/join 
          "\t" 
          [read-name read2-flag ref-name read2-start map-qual cigar
           rnext read1-start (- tlen) seq qual]))))




123456789012345678901234567890

  b      e           b      e

read1 3 - 10, len 8

read2 start = 22 = (+ read1-end (inc insert-size))
read2 22 - 29, len (inc (- end start)) 8

tlen 3 - 29, len 27

ins size = 11
