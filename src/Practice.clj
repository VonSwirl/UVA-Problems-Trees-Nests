;;;!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!README!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
;;;HI GUYS, IVE REARRANGED THE CODE TO MAKE IT A BIT MORE UNDERSTANDABLE
;;;PLEASE ADD YOUR CODE CONTRIBUTION TO YOUR OWN AREA SO WE CAN ALL SEE WHOS DONE WHAT.
;;;COMMENTS ARE NEEDED TO EXPLAIN WHAT YOUR CODE DOES.
;;;I HOPE THIS HELPS.
;;;THE #PREVIOUS-CODE HAS BEEN MOVED THE BOTTOM OF THIS FILE SO DONT PANIC LOLZ.


;==================================FRESH START WENDESDAY===================

;..................................START ROBS ADDITION......................
;PUT YOUR HERE
;..................................END ROBS ADDITION........................


;..................................START REECES ADDITION....................
; PUT YOUR CODE HERE
;..................................END REECES ADDITION......................


;..................................START MAGGIES ADDITION...................
; PUT YOUR CODE HERE
;..................................END MAGGIE ADDITION......................


;..................................START JAYS ADDITION......................

(def lots-of-orders-map
  "Defines a simple map of orders"
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
(-> lots-of-orders-map :order1 :seats)
(-> lots-of-orders-map :order2 :start)
(-> lots-of-orders-map :order3 :stop)
(-> lots-of-orders-map :order4 :passengers)
(-> lots-of-orders-map :order5 :order-num)
;Test Successful
;--------------------------------------------------------------------------

(def lots-of-orders-hash-v1
  "Defines a hash-map of orders version 1"
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
;Copy block below to REPL to test lots-of-orders-hash
(-> lots-of-orders-hash-v1 :order1 :seats)
(-> lots-of-orders-hash-v1 :order2 :start)
(-> lots-of-orders-hash-v1 :order3 :stop)
(-> lots-of-orders-hash-v1 :order4 :passengers)
(-> lots-of-orders-hash-v1 :order5 :order-num)
;Test Successful
;---------------------------------------------------------------------------

(def lots-of-orders-hash-v2
  "Defines a hash-map of orders version 2"
  (hash-map :order1 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 1}
            :order2 {:seats 10 :start 1 :stop 3 :passengers 5 :order-num 2}
            :order3 {:seats 10 :start 1 :stop 2 :passengers 7 :order-num 3}
            :order4 {:seats 10 :start 2 :stop 3 :passengers 10 :order-num 4}
            :order5 {:seats 10 :start 3 :stop 4 :passengers 2 :order-num 5}
            :order6 {:seats 10 :start 2 :stop 6 :passengers 12 :order-num 6}
            :order7 {:seats 10 :start 3 :stop 5 :passengers 10 :order-num 7}
            :order8 {:seats 10 :start 1 :stop 5 :passengers 2 :order-num 8}
            :order9 {:seats 10 :start 4 :stop 5 :passengers 4 :order-num 9}
            :order10 {:seats 10 :start 0 :stop 1 :passengers 1 :order-num 10}
            ))
;TEST
;Copy block below to REPL to test lots-of-orders-hash
(-> lots-of-orders-hash-v2 :order1 :seats)
(-> lots-of-orders-hash-v2 :order2 :start)
(-> lots-of-orders-hash-v2 :order3 :stop)
(-> lots-of-orders-hash-v2 :order4 :passengers)
(-> lots-of-orders-hash-v2 :order5 :order-num)
;Test Successful
;---------------------------------------------------------------------------

(def stations
  "Robs defined stations and corresponding values"
  {
   :0-1 1 :0-2 2 :0-3 3 :0-4 4                              ;station 0 values
   :1-2 1 :1-3 2 :1-4 3                                     ;station 1 values
   :2-3 1 :2-4 2                                            ;station 2 values
   :3-4 1                                                   ;station 3 values
   })


(defn order-value [stations passengers]
  "get value from station which means its
  turned into its value then you can calculate upon it"
  (* stations passengers))
;TEST
;Copy block below to REPL to test order-value
(order-value (stations :1-4) 5)
(order-value (stations :0-3) 15)
(order-value (stations :2-4) 1)
;Test Successful
;---------------------------------------------------------------------------


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

;..................................END JAYS ADDITION........................








;=================================HISTORIC==================================
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
