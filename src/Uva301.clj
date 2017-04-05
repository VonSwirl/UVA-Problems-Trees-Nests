(ns Uva301)


(defn make-order [start end passengers]
  "Creates a map start, end (station) and number of passengers.
  Also contains a function to calculate value."
  (hash-map :start start :end end :pass passengers :value (* (- end start) passengers)))


(def empty-orders
  "Empty list for test purposes"
  (list ()))


(def orders
  "Creates mock orders"
  (list (make-order 0 2 10)                                 ;20
        (make-order 0 2 1)                                  ;2
        (make-order 0 3 1)                                  ;3
        (make-order 1 3 5)                                  ;10
        (make-order 1 2 7)                                  ;7
        (make-order 2 3 10)                                 ;10
        ))


(def orders2
  "Creates more mock orders"
  (list (make-order 0 2 4)
        (make-order 0 1 7)
        (make-order 1 3 5)
        (make-order 1 2 10)
        (make-order 1 2 3)
        (make-order 2 4 1)
        (make-order 2 3 2)
        (make-order 3 4 7)
        (make-order 3 4 9)
        (make-order 3 4 4)
        ))


(def orders3
  "Creates more mock orders"
  (list (make-order 0 3 4)
        (make-order 0 1 1)
        (make-order 1 3 6)
        (make-order 1 2 10)
        (make-order 1 2 1)
        (make-order 2 4 6)
        (make-order 2 3 2)
        (make-order 3 4 7)
        (make-order 3 4 9)
        (make-order 3 4 4)
        ))

(def orders4
  "Creates more mock orders"
  (list (make-order 0 2 1)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 2 3 10)))

(defn make-state [current-station end-station max-capacity]
  "Defines the current state of station, capacity, route and passengers onboard.
   To work out passenger calculations"
  (hash-map :station current-station :value 0 :current-capacity 0 :max-capacity max-capacity
            :route (cons current-station '()) :route-end end-station :current-passengers '()))

(def start-state
  "Defines the problem default start state"
  (make-state 0 4 10))




(defn move [c-state new-o]
  "Moves through the orders updating the current status accepting or
  rejecting orders subject to define conditions. Stores accept order states in hash map"
  (let [c-station (get c-state :station)                    ;Sets the current station
        n-station (inc c-station)                           ;increments through the stations
        p-off (filter #(= (get % :end) c-station) (get c-state :current-passengers)) ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
        remaining-p (filter #(not (= (get % :end) c-station)) (get c-state :current-passengers)) ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
        reduce-cap (reduce #(+ %1 (get %2 :pass)) 0 p-off)  ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
        dep-cap (- (get c-state :current-capacity) reduce-cap) ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
        accept-o (if (<= (+ dep-cap (get new-o :pass)) (get c-state :max-capacity)) true false) ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
        final-cap (if (true? accept-o) (+ dep-cap (get new-o :pass)) dep-cap) ;If true set accept the order and add the current capacity with the new order passengers
        val (if (true? accept-o) (+ (get c-state :value) (get new-o :value)) (get c-state :value)) ;If order taken update the value
        c-route (if (true? accept-o) (conj (get c-state :route) new-o) (get c-state :route)) ;If order taken conj the current route with the new order route, create a long list of the route it has taken
        c-pass (if (true? accept-o) (conj remaining-p new-o) remaining-p) ;If order taken add the current passengers to the new order passengers
        ]

    (hash-map :station n-station
              :value val
              :current-capacity final-cap
              :max-capacity (get c-state :max-capacity)
              :route c-route
              :route-end (get c-state :route-end)
              :current-passengers c-pass)))


(defn lmg [states order]
  "For each state maps all of the legal moves that can be reached.
  Concatinating all of the different station states into one large list"
  (let [fir (first states)]
    (if (= (get fir :station)
           (get fir :route-end))
      states
      (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))]
        (recur (apply concat new-states) order)))))


(defn map-val [state order]
  "Pulls values produced by the legal move generator. Values are mapped for comparison"
  (let [end (lmg (list (state)) order)]
    (apply hash-map (map #(select-keys % [:value]) end))))

(defn max-val [state order]
  "Once the value has been pulled this will apply the max on the value to find the highest"
  (let [maximum (map-val state order)]
    (apply max (map :value
                    (filter #(if (and (map? %) (:value %)) true false)
                            (tree-seq #(or (map? %) (vector? %)) identity maximum))))))


;(max-val #(make-state 0 3 10) orders)
;=> 30
;(max-val #(make-state 0 3 10) orders2)
;=> 20
;(max-val #(make-state 0 3 10) orders3)
;=> 25
;(max-val #(make-state 0 3 10) orders4)
;=> 19
;(max-val #(make-state 0 4 10) orders2)
;=> 29

;This would be used to run our test harness
;(require '[cgsx.tools.matcher :refer :all])
;(defn run-all-tests [tests]
;  (mfor ['(?id ?test => ?res) tests]
;            (if-not (= (eval (? test)) (eval (? res)))
;                    (println (mout '(FAILED ?id ?test => ?res)))
;                    ))
;  'end-of-testing)


(def tests '([t1 (max-val #(make-state 0 3 10) orders) => 30] ;Standard tests
              [t2 (max-val #(make-state 0 3 10) orders2) => 20]
              [t3 (max-val #(make-state 0 4 10) orders2) => 25]
              [t4 (max-val #(make-state 0 3 10) orders3) => 19]
              [t5 (max-val #(make-state 0 3 10) orders4) => 29]
              [t6 (max-val #(make-state 0 4 5) orders2) => 14] ;Tests with small capacity
              [t7 (max-val #(make-state 0 3 5) orders4) => 3]
              [t7 (max-val #(make-state 0 10 5) orders4) => "crashes"] ;More stations than there are in the order
              ))
;;
;;(defn make-order [start end passengers]
;;  ;Makes an order
;;  "Creates a map start, end (station) and number of passengers.
;;  Also contains a function to calculate value."
;;  (hash-map :start start :end end :pass passengers :value (* (- end start) passengers)))
;;
;;
;;(def empty-orders
;;  "Empty list for test purposes"
;;  (list ()))
;;
;;
;;(def orders
;;  "Creates mock orders"
;;  (list (make-order 0 2 10)                                 ;20
;;        (make-order 0 2 1)                                  ;2
;;        (make-order 0 3 1)                                  ;3
;;        (make-order 1 3 5)                                  ;10
;;        (make-order 1 2 7)                                  ;7
;;        (make-order 2 3 10)                                 ;10
;;        ))
;;
;;
;;(def orders2
;;  "Creates more mock orders"
;;  (list (make-order 0 2 4)
;;        (make-order 0 1 7)
;;        (make-order 1 3 5)
;;        (make-order 1 2 10)
;;        (make-order 1 2 3)
;;        (make-order 2 4 1)
;;        (make-order 2 3 2)
;;        (make-order 3 4 7)
;;        (make-order 3 4 9)
;;        (make-order 3 4 4)
;;        ))
;;
;;
;;(defn make-state [current-station end-station max-capacity]
;;  "Defines the current state of station, capacity, route and passengers onboard.
;;   To work out passenger calculations"
;;  (hash-map :station current-station :value 0 :current-capacity 0 :max-capacity max-capacity
;;            :route (cons current-station '()) :route-end end-station :current-passengers '()))
;;
;;
;;(def start-state
;;  "Defines the problem default start state"
;;  (make-state 0 4 10))
;;
;;
;;(defn move [c-state new-o]
;;  "Moves through the orders updating the current status accepting or
;;  rejecting orders subject to define conditions. Stores accept order states in hash map"
;;  (let [c-station (get c-state :station)
;;        n-station (inc c-station)
;;        p-off (filter #(= (get % :end) c-station) (get c-state :current-passengers))
;;        remaining-p (filter #(not (= (get % :end) c-station)) (get c-state :current-passengers))
;;        reduce-cap (reduce #(+ %1 (get %2 :pass)) 0 p-off)
;;        dep-cap (- (get c-state :current-capacity) reduce-cap)
;;        accept-o (if (<= (+ dep-cap (get new-o :pass)) (get c-state :max-capacity)) true false)
;;        final-cap (if (true? accept-o) (+ dep-cap (get new-o :pass)) dep-cap)
;;        val (if (true? accept-o) (+ (get c-state :value) (get new-o :value)) (get c-state :value))
;;        c-route (if (true? accept-o) (conj (get c-state :route) new-o) (get c-state :route))
;;        c-pass (if (true? accept-o) (conj remaining-p new-o) remaining-p)
;;        ]
;;
;;    (hash-map :station n-station
;;              :value val
;;              :current-capacity final-cap
;;              :max-capacity (get c-state :max-capacity)
;;              :route c-route
;;              :route-end (get c-state :route-end)
;;              :current-passengers c-pass)))
;;
;;
;;(defn lmg [states order]
;;  "For each state maps all of the legal moves that can be reached.
;;  Concatinating all of the different station states into one large list"
;;  (let [fir (first states)]
;;    (if (= (get fir :station)
;;           (get fir :route-end))
;;      states
;;      (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))]
;;        (recur (apply concat new-states) order)))))
;;
;;
;;(defn map-val [state order]
;;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;;  (let [end (lmg (list (state)) order)]
;;    (apply hash-map (map #(select-keys % [:value]) end))))
;;
;;(defn max-val [state order]
;;  "gets the maxium from the list"
;;  (let [maximum (map-val state order)]
;;    (apply max (map :value
;;                    (filter #(if (and (map? %) (:value %)) true false)
;;                            (tree-seq #(or (map? %) (vector? %)) identity maximum))))))
;;
;
;
;;======================================================================================================================
;(ns GroupF-UVA301)
;
;(defn make-order [start end passengers]
;  ;Makes an order
;  "Creates a map start, end (station) and number of passengers.
;  Also contains a function to calculate value."
;  (hash-map :start start :end end :pass passengers :value (* (- end start) passengers)))
;
;
;(def empty-orders
;  "Empty list for test purposes"
;  (list ()))
;
;
;(def orders
;  "Creates mock orders"
;  (list (make-order 0 2 10)                                 ;20
;        (make-order 0 2 1)                                  ;2
;        (make-order 0 3 1)                                  ;3
;        (make-order 1 3 5)                                  ;10
;        (make-order 1 2 7)                                  ;7
;        (make-order 2 3 10)                                 ;10
;        ))
;
;
;(def orders2
;  "Creates more mock orders"
;  (list (make-order 0 2 4)
;        (make-order 0 1 7)
;        (make-order 1 3 5)
;        (make-order 1 2 10)
;        (make-order 1 2 3)
;        (make-order 2 4 1)
;        (make-order 2 3 2)
;        (make-order 3 4 7)
;        (make-order 3 4 9)
;        (make-order 3 4 4)
;        ))
;
;
;(def orders3
;  "Creates more mock orders"
;  (list (make-order 0 3 4)
;        (make-order 0 1 1)
;        (make-order 1 3 6)
;        (make-order 1 2 10)
;        (make-order 1 2 1)
;        (make-order 2 4 6)
;        (make-order 2 3 2)
;        (make-order 3 4 7)
;        (make-order 3 4 9)
;        (make-order 3 4 4)
;        ))
;
;(def orders4
;  ;Makes oversized order for test
;  "Creates mock orders that is over the size limit"
;  (list (make-order 0 2 1)
;        (make-order 0 3 1)
;        (make-order 1 3 5)
;        (make-order 1 2 7)
;        (make-order 0 3 1)
;        (make-order 1 3 5)
;        (make-order 1 2 7)
;        (make-order 0 3 1)
;        (make-order 1 3 5)
;        (make-order 1 2 7)
;        (make-order 0 3 1)
;        (make-order 1 3 5)
;        (make-order 1 2 7)
;        (make-order 2 3 10)))
;
;
;(defn make-state [current-station end-station max-capacity]
;  "Defines the current state of station, capacity, route and passengers onboard.
;   To work out passenger calculations"
;  (hash-map :station current-station :value 0 :current-capacity 0 :max-capacity max-capacity
;            :route (cons current-station '()) :route-end end-station :current-passengers '()))
;
;(def start-state
;  "Defines the problem default start state"
;  (make-state 0 4 10))
;
;
;
;
;(defn move [c-state new-o]
;  "Moves through the orders updating the current status accepting or
;  rejecting orders subject to define conditions. Stores accept order states in hash map"
;  (let [c-station (get c-state :station)                                                                                ;Sets the current station
;        n-station (inc c-station)                                                                                       ;increments through the stations
;        p-off (filter #(= (get % :end) c-station) (get c-state :current-passengers))                                    ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
;        remaining-p (filter #(not (= (get % :end) c-station)) (get c-state :current-passengers))                        ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
;        reduce-cap (reduce #(+ %1 (get %2 :pass)) 0 p-off)                                                              ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
;        dep-cap (- (get c-state :current-capacity) reduce-cap)                                                          ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
;        accept-o (if (<= (+ dep-cap (get new-o :pass)) (get c-state :max-capacity)) true false)                         ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
;        final-cap (if (true? accept-o) (+ dep-cap (get new-o :pass)) dep-cap)                                           ;If true set accept the order and add the current capacity with the new order passengers
;        val (if (true? accept-o) (+ (get c-state :value) (get new-o :value)) (get c-state :value))                      ;If order taken update the value
;        c-route (if (true? accept-o) (conj (get c-state :route) new-o) (get c-state :route))                            ;If order taken conj the current route with the new order route, create a long list of the route it has taken
;        c-pass (if (true? accept-o) (conj remaining-p new-o) remaining-p)                                               ;If order taken add the current passengers to the new order passengers
;        ]
;
;    (hash-map :station n-station
;              :value val
;              :current-capacity final-cap
;              :max-capacity (get c-state :max-capacity)
;              :route c-route
;              :route-end (get c-state :route-end)
;              :current-passengers c-pass)))
;
;
;(defn lmg [states order]
;  "For each state maps all of the legal moves that can be reached.
;  Concatinating all of the different station states into one large list"
;  (let [fir (first states)]
;    (if (= (get fir :station)
;           (get fir :route-end))
;      states
;      (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))]
;        (recur (apply concat new-states) order)))))
;
;
;(defn map-val [state order]
;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;  (let [end (lmg (list (state)) order)]
;    (apply hash-map(map #(select-keys % [:value]) end))))
;
;(defn max-val [state order]
;  (let [maximum (map-val state order)]
;    (apply max(map :value
;                   (filter #(if (and (map? %) (:value %)) true false)
;                           (tree-seq #(or (map? %) (vector? %)) identity maximum))))))
;
;
;;(max-val #(make-state 0 3 10) orders)
;;=> 30
;;(max-val #(make-state 0 3 10) orders2)
;;=> 20
;;(max-val #(make-state 0 3 10) orders3)
;;=> 25
;;(max-val #(make-state 0 3 10) orders4)
;;=> 19
;;(max-val #(make-state 0 4 10) orders2)
;;=> 29
;
;
;
;(def tests '([t1 (max-val #(make-state 0 3 10) orders) => 30] ;Standard tests
;              [t2 (max-val #(make-state 0 3 10) orders2) => 20]
;              [t3 (max-val #(make-state 0 4 10) orders2) => 25]
;              [t4 (max-val #(make-state 0 3 10) orders3) => 19]
;              [t5 (max-val #(make-state 0 3 10) orders4) => 29]
;              [t6 (max-val #(make-state 0 4 5) orders2) => 14] ;Tests with small capacity
;              [t7 (max-val #(make-state 0 3 5) orders4) => 3]
;              ))
;
;
;
;
;
;;(map :value
;;     (filter #(if (and (map? %) (:value %)) true  false)
;;             (tree-seq #(or (map? %) (vector? %)) identity mymap)))
;;=> (30 30 12 19 13 10)
;
;
;;BELOW IS WHAT NEEDS DOING==============================================================================================
;; test harness completion
;; test data to break
;; need to get the max value from the map of values
;;Create a test harness that will check each of the values indivually
;
;;---------TESTING-----------
;; assert 😊 (get (move state )) :current-capacity)
;;(lmg (list (make-state 0 3 10)) orders)
;;(lmg (list (make-state 0 4 10)) order2)
;
;
;
;;(defn max-val [state order]
;;  (let [end (lmg (list (state)) order)]
;;    (map #(select-keys % [:value]) end)
;;    ))
;;
;;(def tests '([t1 ((max-val #(make-state 0 3 10) orders)
;;                   => ({:value 30} {:value 30} {:value 12} {:value 19} {:value 13} {:value 10}))]
;;              [t1 (first ((max-val #(make-state 0 3 10) orders))) => {:value 30}]
;;
;;              ;(lmg (list (make-state 0 4 10)) order2)
;;
;;              ))
;;
;;(def tests '([t1 (+ 2 2) => 4]
;;              [t2 (- 3 2) => 'banana]
;;              [t3 (first '(cat bat rat)) => 'cat]
;;              [t4 (first '(cat bat rat)) => 'sat]))
;
;;(def data [{:date1 "20131007", :data "object1", :counter 1000}
;;           {:date1 "20131007", :data "object2", :counter 50}
;;           {:date1 "20131007", :data "object3", :counter 230}])
;;
;;(def counters (map :counter data))      ; => (100 50 230)
;;(def min-value (apply min counters))    ; => 50
;;(def max-value (apply max counters))    ; => 1000
;;(def val-range (- max-value min-value)) ; => 950
;;(def val-pc (* (/ val-range 100) 10))   ; => 95
;;(def x (- max-value val-pc))            ; => 905
;
;;BELOW IS WHAT NEEDS DOING==============================================================================================
;;(defn max-val [state order]
;;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;;  (let [end (lmg (list (state)) order)]
;;    (map #(select-keys % [:value]) end))
;;
;;  )
;;(defn maxx-val [state order]
;;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;;  (let [end (lmg (list (state)) order)]
;;    (apply hash-map(map #(select-keys % [:value]) end))))
;;
;;
;;
;;(defn omgffs [state order]
;;  (let [please-work (maxx-val state order)]
;;    (map :value
;;         (filter #(if (and (map? %) (:value %)) true false)
;;                 (tree-seq #(or (map? %) (vector? %)) identity please-work)))))
;; test harness completion
;; test data to break
;; need to get the max value from the map of values
;;Create a test harness that will check each of the values indivually
;
;;---------TESTING-----------
;; assert (= (get (move state )) :current-capacity)
;;(lmg (list (make-state 0 3 10)) orders)
;;(lmg (list (make-state 0 4 10)) order2)
;
;;(defn run-all-tests [tests]
;;  (mfor ['(?id ?test => ?res) tests]
;;        (if-not (= (eval (? test)) (eval (? res)))
;;          (println (mout '(FAILED ?id ?test => ?res))))) 'end-of-testing)
;;
;;
;;(def tests '([t1 (+ 2 2) => 4] [t2 (- 3 2) => 'banana]
;;              [t3 (first '(cat bat rat)) => 'cat]
;;              [t4 (first '(cat bat rat)) => 'sat]))
;;
;;;(defn max-val [state order]
;;;  (let [end (lmg (list (state)) order)]
;;;    (map #(select-keys % [:value]) end)
;;;    ))
;;
;;(def tests '([t1 ((max-val #(make-state 0 3 10) orders) => ({:value 30} {:value 30} {:value 12} {:value 19} {:value 13} {:value 10}))]
;;              [t1 (first ((max-val #(make-state 0 3 10) orders))) => {:value 30}]
;;
;;              ;(lmg (list (make-state 0 4 10)) order2)
;;
;;              ))
;;
;;(def tests '([t1 (+ 2 2) => 4]
;;              [t2 (- 3 2) => 'banana]
;;              [t3 (first '(cat bat rat)) => 'cat]
;;              [t4 (first '(cat bat rat)) => 'sat]))
;
;;(def data [{:date1 "20131007", :data "object1", :counter 1000}
;;           {:date1 "20131007", :data "object2", :counter 50}
;;           {:date1 "20131007", :data "object3", :counter 230}])
;;
;;(def counters (map :counter data))      ; => (100 50 230)
;;(def min-value (apply min counters))    ; => 50
;;(def max-value (apply max counters))    ; => 1000
;;(def val-range (- max-value min-value)) ; => 950
;;(def val-pc (* (/ val-range 100) 10))   ; => 95
;;(def x (- max-value val-pc))            ; => 905
;
;
;;(defn max-val [state order]
;;  (let [end (legal-move-gen (list (state)) order)]
;;    end))
;
;
;
;;Old shit
;
;;(defn max-val [state order]
;;(let [end (legal-move-gen (list (state)) order)]
;;  (for [n end] (get (nth end n) :value)
;;    )))
;
;;(defn max-val [state order]
;;(let [end (legal-move-gen (list (state)) order)]
;;  (get (nthrest end 0) :value)
;;    ))
;
;;(defn max-val [state order]
;;  (let [end (legal-move-gen (list (state)) order)]
;;    (first end)))
;
;;(defn maxx-val [state order]
;;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;;  (let [end (lmg (list (state)) order)]
;;    (apply hash-map(map #(select-keys % [:value]) end))
;;    (map :value
;;         (filter #(if (and (map? %) (:value %)) true  false)
;;                 (tree-seq #(or (map? %) (vector? %)) identity end)))
;;    ))
;;(defn max-val [state order]
;;  "Pulls values produced by the legal move generator. Values are mapped for comparison"
;;  (let [end (lmg (list (state)) order)]
;;    (#(select-keys % [:value]) end)
;;    (map :value
;;         (filter #(if (and (map? %) (:value %)) true  false)
;;                 (tree-seq #(or (map? %) (vector? %)) identity end)))
;;    ))