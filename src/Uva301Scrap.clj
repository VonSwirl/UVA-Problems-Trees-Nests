(ns Uva301Scrap)
;TO-DO
;Test harness, test each method and maybe single values from each method.  !!!;assert (= (get (move state )) :current-capacity)!!!
;Final overall check of the functions - this includes OOB
;Do we need validation for over 22 orders?
;
;Manually work through the problem and check what values we get
;Look at the route, why is it not conjing on all of the the other orders


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
  (list (make-order 0 2 10)
        (make-order 0 2 1)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 2 3 10)))


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


(defn move [current-state new-order]

  (let [current-station (get current-state :station)
        newStation (inc current-station) ;increments through the stations
        people-getting-off (filter #(= (get % :end) current-station) (get current-state :current-passengers)) ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
        people-still-on  (filter #(not (= (get % :end) current-station)) (get current-state :current-passengers)) ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
        reduced-capacity (reduce  #(+ %1 (get %2 :pass)) 0 people-getting-off) ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
        capacity-with-people-taken-off (- (get current-state :current-capacity) reduced-capacity) ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
        ;(TEST?) Can if and true/false be removed because it will just automatically do that
        will-take-order (if (<= (+ capacity-with-people-taken-off (get new-order :pass))  (get current-state :max-capacity)) true false) ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
        final-capacity  (if (true? will-take-order) (+ capacity-with-people-taken-off (get new-order :pass)) capacity-with-people-taken-off) ;If true set accept the order and add the current capacity with the new order passengers
        value (if (true? will-take-order) (+ (get current-state :value) (get new-order :value)) (get current-state :value)) ;If order taken update the value
        current-route (if (true? will-take-order) (conj (get current-state :route) new-order) (get current-state :route)) ;If order taken conj the current route with the new order route, create a long list of the route it has taken
        current-pass (if (true? will-take-order) (conj  people-still-on  new-order) people-still-on) ;If order taken add the current passengers to the new order passengers
        ]
        ;for if true? is there a better way to do this?

(hash-map :station newStation ;Put all the values into a hashmap so that they can be seen and used
          :value value
          :current-capacity final-capacity
          :max-capacity (get current-state :max-capacity)
          :route current-route
          :route-end (get current-state :route-end)
          :current-passengers current-pass)
)
    )

;assert (= (get (move state )) :current-capacity)

(defn legal-move-gen [states order]
  "has a list of states"
  (let [fir (first states)]
    (if (= (get fir :station) (get fir :route-end) ) states ;When start and finish are the same, so the orders have ended
     (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))] ;for each state map all of the legal moves that can be done
     (recur (apply concat new-states) order))))) ;Concatinating all of the different station states into one large list



;==================================HISTORIC EFFORT===============================================================

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
  (list (make-order 0 2 10)
        (make-order 0 2 1)
        (make-order 0 3 1)
        (make-order 1 3 5)
        (make-order 1 2 7)
        (make-order 2 3 10)))


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


(defn move [current-state new-order]

  (let [current-station (get current-state :station)
        newStation (inc current-station) ;increments through the stations
        people-getting-off (filter #(= (get % :end) current-station) (get current-state :current-passengers)) ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
        people-still-on  (filter #(not (= (get % :end) current-station)) (get current-state :current-passengers)) ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
        reduced-capacity (reduce  #(+ %1 (get %2 :pass)) 0 people-getting-off) ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
        capacity-with-people-taken-off (- (get current-state :current-capacity) reduced-capacity) ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
        ;(TEST?) Can if and true/false be removed because it will just automatically do that
        will-take-order (if (<= (+ capacity-with-people-taken-off (get new-order :pass))  (get current-state :max-capacity)) true false) ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
        final-capacity  (if (true? will-take-order) (+ capacity-with-people-taken-off (get new-order :pass)) capacity-with-people-taken-off) ;If true set accept the order and add the current capacity with the new order passengers
        value (if (true? will-take-order) (+ (get current-state :value) (get new-order :value)) (get current-state :value)) ;If order taken update the value
        current-route (if (true? will-take-order) (conj (get current-state :route) new-order) (get current-state :route)) ;If order taken conj the current route with the new order route, create a long list of the route it has taken
        current-pass (if (true? will-take-order) (conj  people-still-on  new-order) people-still-on) ;If order taken add the current passengers to the new order passengers
        ]
    ;for if true? is there a better way to do this?

    (hash-map :station newStation ;Put all the values into a hashmap so that they can be seen and used
              :value value
              :current-capacity final-capacity
              :max-capacity (get current-state :max-capacity)
              :route current-route
              :route-end (get current-state :route-end)
              :current-passengers current-pass)
    )
  )

;assert (= (get (move state )) :current-capacity)

(defn legal-move-gen [states order]
  "has a list of states"
  (let [fir (first states)]
    (if (= (get fir :station) (get fir :route-end) ) states ;When start and finish are the same, so the orders have ended
                                                     (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))] ;for each state map all of the legal moves that can be done
                                                       (recur (apply concat new-states) order))))) ;Concatinating all of the different station states into one large list




;logically this
;let people off the bus - so filter people due to come off this station
;reduce current capacity
;go to next station
;see if this guy can come on
;if he can add him on to current-pass
;add to current capacity
;update value
;finish move fn - work out how to make into hash maps objects



;
;(defn i-filter-stuff [state valid-order]
;  "just to demo that it works
;  filter:- takes a predicate (if true keep)"
;
;  ;Gets all orders and sorts them by station 0..1..2..3
;  ;data is then passed to make-states??? or move or lmg
;
;  (filter #(= (get % :start) (get state :station)) valid-order)
;  (make-state state valid-order)
;  )

;
;(defn validate-order [created-orders start-state]
;  "Helper function to validate that the given map/hashmap etc..
;   does not exceed the maximum of 22. true is returned if it is within the limit
;    else false"
;  (if (< (count (flatten created-orders)) 22)
;    (i-filter-stuff start-state created-orders)
;    '(TooManyOrders)))



;
;(def orders-too-large
;  ;Makes oversized order for test
;  "Creates mock orders that is over the size limit"
;  (validate-order (list (make-order 0 2 1)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 0 3 1)
;                        (make-order 1 3 5)
;                        (make-order 1 2 7)
;                        (make-order 2 3 10)) nil))



;need to have a scenario where pasng get off but no new passng board to allow function to continue processing.
;;
;;
;; # == anonyamous function
;; set == #{}
;; % == e.g (move state %) is (move state order)
;lmg currently only takes one state needs to deal with all states
;need a for-loop or something that iterates through all the states that are passed in.  do first
;applying move to all orders and need to be able to capture the possibility that no orders are made at that station
;add an end state - if current station is = to final-station that pass back all the states as a list
;after lmg need mapped across all states retrieving the max value.


;------------------------------------------------Stuff that doesnt work ------------------------------------------------


;------------------------------------------------------------------------------------------------------
;Not sure if these uncommented def's below are needed at the moment....work in progress monday 3rd jay

;(def test-order (make-order 2 3 10))

;(defn current-check [state order]
;  "Needs to recursively stage through the stations and implement the lmg and move function for update"
;  (recur (map #(move state %) order)))

;base-case to be used for testing
;(defn test-start-state [state] (state 0 10))

;(defn solution [start end capacity ] (let [initial-state (state start capacity)])

;(defn move
;  "Update after each station firstly incrementing the station mumber,
; checking of any passengers get off\nthen on and finally going to the new order"
;  [current-state new-order end-station]
;
;  (do
;    (update current-state :station inc)
;    (update current-state :current-capacity (- (get current-state :curent-capacity) (get end-station :pass)))
;    (update current-state :current-capacity (+ (get current-state :current-capacity) (get new-order :pass)))
;    (update current-state :current-passengers new-order) ()
;    current-state ()
;    ))





;Helper to validate make-order. Returns TRUE if the data argument is compatable with the uva301 problem
;(defn is-valid [map-we-give]
;  "Helper function to validate that the given map/hashmap etc..
;  if it contains 0 orders/values false is returned else true"
;  (every? seq [map-we-give]))


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

;(def i-will-break-bysize
;  "Defines a map of orders"
;  {:order1   {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
;   :order2   {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
;   :order3   {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
;   :order4   {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
;   :order5   {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
;   :order6   {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
;   :order7   {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
;   :order8   {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
;   :order9   {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
;   :order10  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
;   :order11  {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
;   :order12  {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
;   :order13  {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
;   :order44  {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
;   :order35  {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
;   :order66  {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
;   :order27  {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
;   :order78  {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
;   :order79  {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
;   :order110 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
;   })

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





