(ns Uva301)


(defn make-order [start end passengers]
  ;Makes an order
  "Creates a map start, end (station) and number of passengers.
  Also contains a function to calculate value."
  (hash-map :start start :end end :pass passengers :value (* (- end start) passengers)))


(def empty-orders
  ;Makes an empty for test
  (list ()))


(def orders
  ;Order Values
  "Creates mock orders"
  (list (make-order 0 2 10)                                 ;20
        (make-order 0 2 1)                                  ;2
        (make-order 0 3 1)                                  ;3
        (make-order 1 3 5)                                  ;10
        (make-order 1 2 7)                                  ;7
        (make-order 2 3 10)))                               ;10

(def order2
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


(defn make-state [current-station end-station max-capacity]
  ;Function used to create all possible states
  ;
  "Defines the current state of station, capacity, route and passengers onboard.
   To work out passenger calculations"
  (hash-map :station current-station :value 0 :current-capacity 0 :max-capacity max-capacity
            :route (cons current-station '()) :route-end end-station :current-passengers '()))

(def start-state
  ;Defines the problems default start state
  ;           S E CAP
  (make-state 0 4 10))


(defn move [c-state new-o]

  (let [c-station (get c-state :station)
        n-station (inc c-station)                           ;increments through the stations
        p-off (filter #(= (get % :end) c-station) (get c-state :current-passengers)) ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
        remaining-p (filter #(not (= (get % :end) c-station)) (get c-state :current-passengers)) ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
        reduce-cap (reduce #(+ %1 (get %2 :pass)) 0 p-off)  ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
        dep-cap (- (get c-state :current-capacity) reduce-cap) ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
        ;(TEST?) Can if and true/false be removed because it will just automatically do that
        accept-o (if (<= (+ dep-cap (get new-o :pass)) (get c-state :max-capacity)) true false) ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
        final-cap (if (true? accept-o) (+ dep-cap (get new-o :pass)) dep-cap) ;If true set accept the order and add the current capacity with the new order passengers
        val (if (true? accept-o) (+ (get c-state :value) (get new-o :value)) (get c-state :value)) ;If order taken update the value
        c-route (if (true? accept-o) (conj (get c-state :route) new-o) (get c-state :route)) ;If order taken conj the current route with the new order route, create a long list of the route it has taken
        c-pass (if (true? accept-o) (conj remaining-p new-o) remaining-p) ;If order taken add the current passengers to the new order passengers
        ]

    (hash-map :station n-station                            ;Put all the values into a hashmap so that they can be seen and used
              :value val
              :current-capacity final-cap
              :max-capacity (get c-state :max-capacity)
              :route c-route
              :route-end (get c-state :route-end)
              :current-passengers c-pass)
    )
  )

;assert (= (get (move state )) :current-capacity)

(defn lmg [states order]
  "has a list of states"
  (let [fir (first states)]
    (if (= (get fir :station)
           (get fir :route-end))
      states                                                ;When start and finish are the same, so the orders have ended
      (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))] ;for each state map all of the legal moves that can be done
        (recur (apply concat new-states) order)))))         ;Concatinating all of the different station states into one large list






;---------TESTING-----------

;(lmg (list (make-state 0 3 10)) orders)
;(lmg (list (make-state 0 4 10)) order2)



;(defn max-val [state order]
;  (let [end (lmg (list (state)) order)]
;    (map #(select-keys % [:value]) end)
;    ))

(defn max-val [state order]
  (let [end (lmg (list (state)) order)]
    (map #(select-keys % [:value]) end))
  )

(def tests '([t1 ((max-val #(make-state 0 3 10) orders)
                   => ({:value 30} {:value 30} {:value 12} {:value 19} {:value 13} {:value 10}))]
              [t1 (first ((max-val #(make-state 0 3 10) orders))) => {:value 30}]

              ;(lmg (list (make-state 0 4 10)) order2)

              ))


(def tests '([t1 (+ 2 2) => 4]
              [t2 (- 3 2) => 'banana]
              [t3 (first '(cat bat rat)) => 'cat]
              [t4 (first '(cat bat rat)) => 'sat]))



;(def data [{:date1 "20131007", :data "object1", :counter 1000}
;           {:date1 "20131007", :data "object2", :counter 50}
;           {:date1 "20131007", :data "object3", :counter 230}])
;
;(def counters (map :counter data))      ; => (100 50 230)
;(def min-value (apply min counters))    ; => 50
;(def max-value (apply max counters))    ; => 1000
;(def val-range (- max-value min-value)) ; => 950
;(def val-pc (* (/ val-range 100) 10))   ; => 95
;(def x (- max-value val-pc))            ; => 905


;(defn max-val [state order]
;  (let [end (legal-move-gen (list (state)) order)]
;    end))

;Create a test harness that will check each of the values indivually


;Old shit

;(defn max-val [state order]
;(let [end (legal-move-gen (list (state)) order)]
;  (for [n end] (get (nth end n) :value)
;    )))

;(defn max-val [state order]
;(let [end (legal-move-gen (list (state)) order)]
;  (get (nthrest end 0) :value)
;    ))

;(defn max-val [state order]
;  (let [end (legal-move-gen (list (state)) order)]
;    (first end)))

;==================================HISTORIC EFFORT===============================================================
;(def orders-map
;  "Defines a map of orders"
;  '{:station0 {:order0 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
;               :order1 {:seats 10 :start 0 :stop 3 :passengers 5 :order-num 2}
;               }
;    :station1 {:order0 {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
;               :order1 {:seats 10 :start 1 :stop 3 :passengers 10 :order-num 4}
;               }
;    :station2 {:order0 {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
;               ;:order1  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
;               }
;    :station3 {:order0 {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
;               :order1 {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
;               }
;    :station4 {:order0 {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
;               :order1 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
;               }})
;
;(defn cap-check [orders current-capacity max-capacity])
;(if (curent-capacity? == max-capacity)
;  (false)
;  (recur (inc orders ))
;  )

(def i-will-break-bysize
  "Defines a map of orders"
  {:order1   {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
   :order2   {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
   :order3   {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
   :order4   {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
   :order5   {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
   :order6   {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
   :order7   {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
   :order8   {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
   :order9   {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
   :order10  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
   :order11  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
   :order12  {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
   :order13  {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
   :order44  {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
   :order35  {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
   :order66  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
   :order27  {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
   :order78  {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
   :order79  {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
   :order110 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
   })

;................................START   JAYS    ADDITION...................
;

;
;;TEST
;;Copy block below to REPL to test lots-of-orders-map
;(type orders-map)
;(-> orders-map :order1 :seats)
;(-> orders-map :order2 :start)
;(-> orders-map :order3 :stop)
;(-> orders-map :order4 :passengers)
;(-> orders-map :order5 :order-num)
;;Test Successful
;;--------------------------------------------------------------------------
;
;(def orders-hash-v1
;  "Defines a hash-map of orders. Version 1"
;  {:order1  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
;   :order2  {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
;   :order3  {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
;   :order4  {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
;   :order5  {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
;   :order6  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
;   :order7  {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
;   :order8  {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
;   :order9  {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
;   :order10 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
;   })
;
;;TEST
;;Copy block below to REPL to test orders-hash v1
;(type orders-hash-v1)
;(-> orders-hash-v1 :order1 :seats)
;(-> orders-hash-v1 :order2 :start)
;(-> orders-hash-v1 :order3 :stop)
;(-> orders-hash-v1 :order4 :passengers)
;(-> orders-hash-v1 :order5 :order-num)
;;Test Successful
;
;
(def i-will-break-im-empty
  "Empty hashmap for test purposes"
  ())

(def Ayyy-clojure
  "Empty hashmap for test purposes"
  ())
;
;;TEST
;;Copy block below to REPL to test orders-hash-empty
;
;;Test Successful
;---------------------------------------------------------------------------
;
;
;
;
;(def stations
;  "defined stations and corresponding values"
;  {
;   :0-1 1 :0-2 2 :0-3 3 :0-4 4                            ;station 0 values
;   :1-2 1 :1-3 2 :1-4 3                                   ;station 1 values
;   :2-3 1 :2-4 2                                          ;station 2 values
;   :3-4 1                                                 ;station 3 values
;   })
;
;
;(defn order-value [stations passengers]
;  "Functions uses the above define stations to get the order value by
;   (station stops * passenger count)"
;  (* stations passengers))
;
;;TEST
;;Copy block below to REPL to test order-value
;(order-value (stations :1-4) 5)
;(order-value (stations :0-3) 15)
;(order-value (stations :2-4) 1)
;;Test Successful
;---------------------------------------------------------------------------

;;TEST
;;Copy block below to REPL to test order-empty
;(is-order-empty orders-hash-empty) ;true
;(is-order-empty orders-hash-empty) ;true
;(is-order-empty orders-hash-v1) ;false
;(is-order-empty orders-hash-empty) ;true
;(is-order-empty orders-hash-v2) ;false
;(is-order-empty orders-hash-empty) ;true
;;Test Successful
;---------------------------------------------------------------------------
;
;(defn validate-order [map-we-give]
;  "Helper function to validate that the given map/hashmap etc..
;   does not exceed the maximum of 17. true is returned if it is within the limit
;    else false"
;  (<= (count map-we-give) 17))
;
;;TEST
;;Copy block below to REPL to test order-oversize
;(validate-order orders-hash-empty) ;true
;(validate-order orders-map) ;true
;(validate-order orders-hash-v1) ;true
;(validate-order orders-hash-v2)                     ;true
;;Test Successful
; (ns Practice)
;(def english
;  '{one 1, two 2, three 3, four 4})
;
;
;(defn order [max-passengers end-station start-station num-orders num-passengers]
;  (hash-map :max-pass max-passengers
;            :end-stat end-station
;            :start-stat start-station
;            :num-ord num-orders
;            :num-pass num-passengers
;            :value (order-earnings end-station start-station num-passengers)
;            ))
;
;;=> #'user/order
;(defn order-earnings [dest-station start-station num-passengers]
;  (* (- dest-station start-station) num-passengers))
;;=> #'user/order-earnings
;
;
;;(def order
;;  {'(:max-passengers mp, :city-B-station bs, :num-orders 0)
;;   '(:start-station s, :dest-station ds, :num-passengers np)})
;;;=> #'user/order

;;(defn order-earnings [order]
;;  (* (- (:dest-station, :start-station)):num-passengers))
;;;=> #'user/order-earnings

;;(defn order-earnings [dest-station start-station num-passengers]
;;  (* (- (:dest-station, :start-station)):num-passengers))
;;;=> #'user/order-earnings

;;(order-earnings 6 0 10)
;;NullPointerException   clojure.lang.Numbers.ops (Numbers.java:1013)

;;(defn order-earnings [dest-station start-station num-passengers]
;;  (* (- (dest-station, start-station)) num-passengers))
;;;=> #'user/order-earnings

;;(order-earnings 6 0 10)
;;ClassCastException java.lang.Long cannot be cast to clojure.lang.IFn  user/order-earnings (form-init2149713550713844186.clj:2)


;;(defn order-earnings [dest-station start-station num-passengers]
;;  (* (- dest-station start-station) num-passengers))

;;;=> #'user/order-earnings

;;(order-earnings 6 0 10)
;;=> 60
;  (defn order [max-passengers end-station start-station num-orders num-passengers]
;    (hash-map :max-pass max-passengers
;              :end-stat end-station
;              :start-stat start-station
;              :num-ord num-orders
;              :num-pass num-passengers
;              :value (order-earnings end-station start-station num-passengers)
;              ))
;
;  ;=> #'user/order


; ; => #'user/order
; ; => #'user/order-earnings


;  (order 20 6 3 2 4)
; ; => {:num-pass 4, :value 12, :end-stat 6, :start-stat 3, :max-pass 20, :num-ord 2}

;  (list (order 20 6 3 2 4) (order 20 6 2 1 1))
; ; =>
;  ({:num-pass 4, :value 12, :end-stat 6, :start-stat 3, :max-pass 20, :num-ord 2}
;    {:num-pass 1, :value 4, :end-stat 6, :start-stat 2, :max-pass 20, :num-ord 1})
;
;  (def testy (list (order 20 6 3 2 4) (order 20 6 2 1 1)) )
;  ;=> #'user/testy
;  testy
;   =>
;  ({:num-pass 4, :value 12, :end-stat 6, :start-stat 3, :max-pass 20, :num-ord 2}
;    {:num-pass 1, :value 4, :end-stat 6, :start-stat 2, :max-pass 20, :num-ord 1})
;  (map #(get % :value) testy)
;  => (12 4)

;(defn order [total-s new-p start-s end-s num-o]
;  (hash-map :seats total-s
;            :new-passengers new-p
;            :start-station start-s
;            :destination-station end-s
;            :num-orders num-o
;            :empty-seats (avail-seats total-s new-p)
;            ))


;(defn value [dest-station start-station num-passengers]
;  (* (- dest-station start-station) num-passengers))
;
;;(defn avail-seats [total-s passng]
;; (- total-s passng))
;
;(def person {:name "Steve" :age 24 :salary 7886 :company "Acme"})

;(def creamchez {"seats" "total-s"
;                :start-station start-s
;                :destination-station end-s
;                :passengers passng
;                :num-orders num-or
;                :value (value end-s start-s passng)})

;(def order [total-s start-s end-s passng num-o]
;  {:seats total-s
;   :start-station start-s
;   :destination-station end-s
;   :passengers passng
;   :num-orders num-or
;   :value (value end-s start-s passng)})

;(def stations {"0-1" 1
;               :0-2  2
;               :0-3  3
;               :0-4  4
;
;               :1-2  1
;               :1-3  2
;               :1-4  3
;
;               :2-3  1
;               :2-4  2
;
;               :3-4  1})

;(order-value station :0-3 15)
;
;(defn order-value [stations passengers]
;  ;get value from station which means its turned into its value
;  ;then you can calculate upon it
;
;  (* stations passengers))


;SOMEWHERE up here get all the orders and give them a numbers
;(defn order-value [order station passengers]
;get maximum order number
;(*t passengers)
;(decrement order and recur method)



;
;(def testValue
;  t1(stations :0-1) :=> 1)
;  t2(stations :0-4) :=> 4)
;  t3(stations :2-4) :=> 2))

;(def city {"Seattle"  "cloudy"
;           "Phoenix"  "sunny"
;           "New York" "busy"})
;
;
;(defn order [total-s start-s end-s passng num-o]
;  (hash-map :seats total-s
;            :start-station start-s
;            :destination-station end-s
;            :passengers passng
;            :num-orders num-o
;            ;:empty-seats (avail-seats total-s passng)
;            :value (order-value stations passng)
;            ))

;(def english {one 1, two 2, three 3, four 4})
;; using a map as a fn2
;; user=> ('two english); using a symbol as a fn2


;(def testy (list (order 10 0 2 1 0)
;                 (order 10 1 3 5 0)
;                 (order 10 1 2 7 0)
;                 (order 10 2 3 10 0)
;                 (order 10 3 4 2 0)
;                 (order 10 2 6 12 0)
;                 (order 10 3 5 10 0)
;                 (order 10 1 5 2 0)
;                 (order 10 4 5 4 0)
;                 (order 10 2 3 3 0)
;                 ))

;(select-keys {order} [:value :passengers])
;(defn get
;  (select-keys % {:value :passengers} [order]))


;(map #(get % :value :passengers testy)


;(defn avail-seats [total-s new-p]
; (- total-s new-p))

;;(defn order-earnings [dest-station start-station num-passengers]
;;  (* (- dest-station start-station) num-passengers))

;;;=> #'user/order-earnings

;;(order-earnings 6 0 10)

;  (defn order [max-passengers end-station start-station num-orders num-passengers]
;    (hash-map :max-pass max-passengers
;              :end-stat end-station
;              :start-stat start-station
;              :num-ord num-orders
;              :num-pass num-passengers
;              :value (order-earnings end-station start-station num-passengers)
;              ))
;
;  ;=> #'user/order
;  (defn order-earnings [dest-station start-station num-passengers]
;    (* (- dest-station start-station) num-passengers))
; ; => #'user/order
; ; => #'user/order-earnings


;  (order 20 6 3 2 4)
; ; => {:num-pass 4, :value 12, :end-stat 6, :start-stat 3, :max-pass 20, :num-ord 2}

;  (list (order 20 6 3 2 4) (order 20 6 2 1 1))
; ; =>
;  ({:num-pass 4, :value 12, :end-stat 6, :start-stat 3, :max-pass 20, :num-ord 2}
;    {:num-pass 1, :value 4, :end-stat 6, :start-stat 2, :max-pass 20, :num-ord 1})
;
;; breadth first search mechanism
;; @args start start state
;; @args goal either a predicate to take a state determine if it is a goal
;;            or a state equal to the goal
;; @args LMG  legal move generator function which takes one state & returns
;;            a list of states
;; @args compare is a function which compares 2 states for equality,
;;            = is used by default
;; @arg debug prints some information

;(declare breadth-search-)

;-----------------------------------------------------------------------------------------------------

;(defn breadth-search
;  [start goal lmg & {:keys [debug compare]
;                     :or   {debug   false
;                            compare =}}]
;  (let [goal? (if (fn? goal)
;                #(when (goal %) %)
;                #(when (= % goal) %))
;        ]
;    ;; a daft check but required just in case
;    (or (goal? start)
;      (breadth-search- `((~start)) goal? lmg compare debug)
;      )))
;
;
;(defn breadth-search- [waiting goal? lmg compare debug]
;  (let [member? (fn [lis x] (some (partial compare x) lis))
;        visited #{}
;        ]
;    (when debug (println 'waiting= waiting 'visited= visited))
;    (loop [waiting waiting
;           visited visited
;           ]
;      (if (empty? waiting) nil
;        (let [[next & waiting] waiting
;              [state & path] next
;              visited? (partial member? visited)
;              ]
;          (if (visited? state)
;            (recur waiting visited)
;            (let [succs (remove visited? (lmg state))
;                  g (some goal? succs)
;                  ]
;              (if g (reverse (cons g next))
;                (recur (concat waiting (map #(cons % next) succs))
;                  (cons state visited)))
;              )))))))

;-----------------------------------------------------------------------------------------------------





