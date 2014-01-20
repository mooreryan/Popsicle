(map #(= (mid %) (max %)) (partition 3 1 [1 2 3 4 5]))

(defn graph-it [ys]
  (view (xy-plot (range 1 (inc (count ys)))
                 ys)))

(defn mid-index [v] (quot (count v) 2))

(defn mid [v] (nth v (mid-index v)))
(defn pre-mid [v] (nth v (dec (mid-index v))))
(defn post-mid [v] (nth v (inc (mid-index v))))

(defn remove-mid [v]
  {:pre [(vector? v)]}
  (into (subvec v 0 (mid-index v)) (subvec v (inc (mid-index v)))))

;; prolly no good
(defn maxes [window ys] 
  {:pre [(odd? window)]}
  (let [flags (map #(> (mid %) (apply max (remove-mid (vec %)))) 
                   (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    (map first (filter #(> (abs (- (first %) (second %))) window)
                       (partition 2 1 [0] posns)))))

;; probably the best so far
(defn maxes [window ys] 
  {:pre [(odd? window)]}
  (let [flags (map #(= (mid %) (apply max %)) 
                   (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    (map first (filter #(> (abs (- (first %) (second %))) window)
                       (partition 2 1 [0] posns)))))

(defn maxes [window ys] 
  {:pre [(odd? window)]}
  (let [flags (map #(and (= (mid %) (apply max %))
                         (> 0.5 (peak %))) 
                   (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    (map first (filter #(> (abs (- (first %) (second %))) window)
                       (partition 2 1 [0] posns)))))

(defn maxes-all-old [window ys] 
  {:pre [(odd? window)]}
  (let [midpoint ()
        flags (map #(and (= (mid %) (apply max %))
                         (not (or (= (mid %) (pre-mid %))
                                  (= (mid %) (post-mid %))))) 
                   (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    posns))

(defn maxes-all [window mult ys] 
  {:pre [(odd? window)]}
  (let [midpoint ()
        flags (map #(and (= (mid %) (apply max %))
                         (> (mid %) (* mult (mean %)))) 
                   (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    posns))


(defn mins [window ys] 
  {:pre [(odd? window)]}
  (let [peaks 
        (map #(+ % (+ 1 (/ (dec window) 2)))
             (map first 
                  (filter #(= (second %) true) 
                          (map-indexed vector 
                                       (map #(= (mid %) (apply min %)) 
                                            (partition window 1 ys))))))]
    (map first (filter #(> (abs (- (first %) (second %))) window)
                       (partition 2 1 [0] peaks)))))

(defn maxes-old [window ys] 
  {:pre [(odd? window)]}
  (let [peaks 
        (map #(+ % (+ 1 (/ (dec window) 2)))
             (map first 
                  (filter #(= (second %) true) 
                          (map-indexed vector 
                                       (map #(= (mid %) (apply max %)) 
                                            (partition window 1 ys))))))]
    ))

(defn mins [ys] 
  (map #(= (mid %) (apply min %)) 
       (partition 3 1 ys)))

(defn smooth [window ys]
  (map mean (partition window 1 ys)))



(map first (filter #(> (- (second %) (first %)) window) 
                   (partition 2 1 (maxes2 window c))))


(defn view-peaks [window ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)] 
    (doseq [x (maxes window ys)] 
      (.addDomainMarker xy
                        (ValueMarker.
                         x
                         java.awt.Color/blue
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))

(defn view-peaks-all [window mult ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)] 
    (doseq [x (maxes-all window mult ys)] 
      (.addDomainMarker xy
                        (ValueMarker.
                         x
                         java.awt.Color/blue
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))

(defn view-valleys [window ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)] 
    (doseq [x (mins window ys)] 
      (.addDomainMarker xy
                        (ValueMarker.
                         x
                         java.awt.Color/green
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))

(defn view-both [window ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)] 
    (doseq [the-max (maxes window ys)
            the-min (mins window ys)] 
      (.addDomainMarker xy
                        (ValueMarker.
                         the-max
                         java.awt.Color/blue
                         (java.awt.BasicStroke. 2)))
      (.addDomainMarker xy
                        (ValueMarker.
                         the-min
                         java.awt.Color/green
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))


(defn get-xy [ys]
  (.getXYPlot (xy-plot (range 1 (inc (count ys))) ys)))





;; http://stackoverflow.com/questions/3260/
;; peak-detection-of-measured-signal
;; travel and rise from ^
;; doesnt really seem to work very well


(defn travel [ys]
  (apply + (map #(abs (- (first %) (second %))) (partition 2 1 ys))))

(defn rise 
  "Magic 0.0001 is to avoid dividing by one later."
  [ys]
  (abs (- (last ys) (first ys))))

(defn flag-peak
  "Is there a peak at the center point?"
  [ys]
  (let [t (travel ys)
        r (rise ys)]
    (cond 
     (and (zero? t) (zero? r)) false
     (zero? r) false
     :else (> (/ (travel ys) (rise ys)) 1))))

(defn maxes2 [window ys] 
  {:pre [(odd? window)]}
  (let [flags (map flag-peak (partition window 1 ys))
        flags-posns (map-indexed vector flags)
        peaks (filter #(= (second %) true) flags-posns)
        posns (map #(+ (first %) (+ 1 (/ (dec window) 2))) peaks)]
    posns))





(defn view-peaks2 [window ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)] 
    (doseq [x (maxes2 window ys)] 
      (.addDomainMarker xy
                        (ValueMarker.
                         x
                         java.awt.Color/blue
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))

(defn view-peaks-two [window ys]
  (let [the-plot (xy-plot (range 1 (inc (count ys))) ys)
        xy (.getXYPlot the-plot)
        maxes1 (into #{} (maxes window ys))
        maxes2 (into #{} (maxes2 window ys))
        peaks (clojure.set/intersection maxes1 maxes2)] 
    (doseq [x peaks] 
      (.addDomainMarker xy
                        (ValueMarker.
                         x
                         java.awt.Color/blue
                         (java.awt.BasicStroke. 2))))
    (view the-plot :width 1200 :height 800)))
