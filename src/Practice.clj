(declare breadth-search-)

(defn breadth-search
  [start goal lmg & {:keys [debug compare]
                     :or   {debug   false
                            compare =}}]
  (let [goal? (if (fn? goal)
                #(when (goal %) %)
                #(when (= % goal) %))
        ]
    ;; a daft check but required just in case
    (or (goal? start)
        (breadth-search- `((~start)) goal? lmg compare debug)
        )))

(defn breadth-search- [waiting goal? lmg compare debug]
  (let [member? (fn [lis x] (some (partial compare x) lis))
        visited #{}
        ]
    (when debug (println 'waiting= waiting 'visited= visited))
    (loop [waiting waiting
           visited visited
           ]
      (if (empty? waiting) nil
                           (let [[next & waiting] waiting
                                 [state & path] next
                                 visited? (partial member? visited)
                                 ]
                             (if (visited? state)
                               (recur waiting visited)
                               (let [succs (remove visited? (lmg state))
                                     g (some goal? succs)
                                     ]
                                 (if g (reverse (cons g next))
                                       (recur (concat waiting (map #(cons % next) succs))
                                              (cons state visited)))
                                 )))))))

(def orders-hash-v2
  "Defines a hash-map of orders. Version 2"
  (hash-map

    :station0 {:order1 {:start 0 :stop 1 :passengers 1 :order-num 1 :value 1}
               :order2 {:start 0 :stop 3 :passengers 1 :order-num 2 :value 3}
               }

    :station1 {:order1 {:start 1 :stop 3 :passengers 5 :order-num 1 :value 10}
               :order2 {:start 1 :stop 2 :passengers 7 :order-num 2 :value 7}
               :order3 {:start 1 :stop 3 :passengers 2 :order-num 3 :value 4}
               }

    :station2 {:order1 {:start 2 :stop 3 :passengers 9 :order-num 1 :value 9}
               :order2 {:start 2 :stop 3 :passengers 10 :order-num 2 :value 10}
               }

    :station3 {:order1 {:start 3 :stop 4 :passengers 2 :order-num 1 :value 2}
               :order2 {:start 3 :stop 4 :passengers 10 :order-num 2 :value 10}
               }
    :station4 {}
    ))

(def ordering
  '{station0 #{station1 station2}
    station1 #{station2 station3}
    station2 #{station3 station4}
    station3 #{station4}
    station4 #{}
    })

;TEST
;Copy block below to REPL to test orders-hash v2
(type orders-hash-v2)
(-> orders-hash-v2 :station0 :order1 :value)
(-> orders-hash-v2 :station1 :order2 :value)
(-> orders-hash-v2 :station2 :order1 :value)
(-> orders-hash-v2 :station3 :order2 :value)

;Test Successful

(defn make-order [start end passengers]
  "Creates a map start, end (station) and number of passengers.
  Also contains a function to calculate value."
  (hash-map :start start :end end :pass passengers :value (* (- end start) passengers)))

(def orders
  "Creates mock orders"
  (list (make-order 0 2 1)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 2 3 10)))

;(def test-order (make-order 2 3 10))

(defn state [current-station max-capacity end-station]
  "Defines the current state of station, capacity, route and passengers onboard. To work out passenger calculations"
  (hash-map :station current-station :value 0 :current-capacity 0 :max-capacity max-capacity
            :route cons current-station () :route-end cons end-station () :current-passengers '()))

;(defn solution [start end capacity ] (let [initial-state (state start capacity)])

(defn move [current-state new-order end-station]
  "Update after each station firstly incrementing the station mumber,
   checking of any passengers get off\nthen on and finally going to the new order"
  (do
    (update current-state :station inc)
    (update current-state :current-capacity (- (get current-state :curent-capacity) (get end-station :pass)))
    (update current-state :current-capacity (+ (get current-state :current-capacity) (get new-order :pass)))
    (update current-state :current-passengers new-order) ()
    current-state ()
    ))

;(defn current-check [state]
;  "Needs to  recursively stage through the stations and implement the lmg and move function for update"
;  (recur (map #(move state %) order)))

;(def start-state (state 0 10))
;basecase to be used for testing 
;...................................................

(def orders-map
  "Defines a map of orders"
  '{:station0 {:order0 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
               :order1 {:seats 10 :start 0 :stop 3 :passengers 5 :order-num 2}
               }
    :station1 {:order0 {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
               :order1 {:seats 10 :start 1 :stop 3 :passengers 10 :order-num 4}
               }
    :station2 {:order0 {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
               ;:order1  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
               }
    :station3 {:order0 {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
               :order1 {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
               }
    :station4 {:order0 {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
               :order1 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
               }})
;
;(defn cap-check [orders current-capacity max-capacity])
;(if (curent-capacity? == max-capacity)
;  (false)
;  (recur (inc orders ))
;  )
;................................START   JAYS    ADDITION...................

(def orders2
  "Defines a map of orders"
  '{:order1  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
    :order2  {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
    :order3  {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
    :order4  {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
    :order5  {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
    :order6  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
    :order7  {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
    :order8  {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
    :order9  {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
    :order10 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
    })

;TEST
;Copy block below to REPL to test lots-of-orders-map
(type orders-map)
(-> orders-map :order1 :seats)
(-> orders-map :order2 :start)
(-> orders-map :order3 :stop)
(-> orders-map :order4 :passengers)
(-> orders-map :order5 :order-num)
;Test Successful
;--------------------------------------------------------------------------

(def orders-hash-v1
  "Defines a hash-map of orders. Version 1"
  {:order1  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
   :order2  {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
   :order3  {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
   :order4  {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
   :order5  {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
   :order6  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
   :order7  {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
   :order8  {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
   :order9  {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
   :order10 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
   })

;TEST
;Copy block below to REPL to test orders-hash v1
(type orders-hash-v1)
(-> orders-hash-v1 :order1 :seats)
(-> orders-hash-v1 :order2 :start)
(-> orders-hash-v1 :order3 :stop)
(-> orders-hash-v1 :order4 :passengers)
(-> orders-hash-v1 :order5 :order-num)
;Test Successful


(def orders-hash-empty
  "Empty hashmap for test purposes"
  (hash-map))

;TEST
;Copy block below to REPL to test orders-hash-empty

;Test Successful
;---------------------------------------------------------------------------

(def stations
  "defined stations and corresponding values"
  {
   :0-1 1 :0-2 2 :0-3 3 :0-4 4                              ;station 0 values
   :1-2 1 :1-3 2 :1-4 3                                     ;station 1 values
   :2-3 1 :2-4 2                                            ;station 2 values
   :3-4 1                                                   ;station 3 values
   })


(defn order-value [stations passengers]
  "Functions uses the above define stations to get the order value by
   (station stops * passenger count)"
  (* stations passengers))

;TEST
;Copy block below to REPL to test order-value
(order-value (stations :1-4) 5)
(order-value (stations :0-3) 15)
(order-value (stations :2-4) 1)
;Test Successful
;---------------------------------------------------------------------------

(defn is-order-empty [map-we-give]
  "Helper function to validate that the given map/hashmap etc..
  if it contains 0 orders/values false is returned else true"
  (every? empty? [map-we-give]))

;TEST
;Copy block below to REPL to test order-empty
(is-order-empty orders-hash-empty)                          ;true
(is-order-empty orders-hash-empty)                          ;true
(is-order-empty orders-hash-v1)                             ;false
(is-order-empty orders-hash-empty)                          ;true
(is-order-empty orders-hash-v2)                             ;false
(is-order-empty orders-hash-empty)                          ;true
;Test Successful
;---------------------------------------------------------------------------

(defn is-order-within-size [map-we-give]
  "Helper function to validate that the given map/hashmap etc..
   does not exceed the maximum of 17. true is returned if it is within the limit
    else false"
  (<= (count map-we-give) 17))

;TEST
;Copy block below to REPL to test order-oversize
(is-order-within-size orders-hash-empty)                    ;true
(is-order-within-size orders-map)                           ;true
(is-order-within-size orders-hash-v1)                       ;true
(is-order-within-size orders-hash-v2)                       ;true
;Test Successful
;==================================HISTORIC EFFORT==========================
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
;; @args debug prints some information



