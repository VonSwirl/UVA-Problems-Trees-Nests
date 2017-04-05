(ns MoreScrap)


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
        newStation (inc current-station)                    ;increments through the stations
        people-getting-off (filter #(= (get % :end) current-station) (get current-state :current-passengers)) ;Filters where the end station is equal to the current station, if it is it will then get the current passengers
        people-still-on (filter #(not (= (get % :end) current-station)) (get current-state :current-passengers)) ;Filters where the end station is not equal to the current station passngers will be staying on so get the current passengers
        reduced-capacity (reduce #(+ %1 (get %2 :pass)) 0 people-getting-off) ;Start at 0 then adds the people that are getting off so it increases our capacity so that more people can get on
        capacity-with-people-taken-off (- (get current-state :current-capacity) reduced-capacity) ;Gets the current capacity and reduced then takes them off leaving the current capacity when the train has dropped people off
        ;(TEST?) Can if and true/false be removed because it will just automatically do that
        will-take-order (if (<= (+ capacity-with-people-taken-off (get new-order :pass)) (get current-state :max-capacity)) true false) ;If the capacity that is currently on the train + the new orders passengers is less than the max capacity it is true. Else false
        final-capacity (if (true? will-take-order) (+ capacity-with-people-taken-off (get new-order :pass)) capacity-with-people-taken-off) ;If true set accept the order and add the current capacity with the new order passengers
        value (if (true? will-take-order) (+ (get current-state :value) (get new-order :value)) (get current-state :value)) ;If order taken update the value
        current-route (if (true? will-take-order) (conj (get current-state :route) new-order) (get current-state :route)) ;If order taken conj the current route with the new order route, create a long list of the route it has taken
        current-pass (if (true? will-take-order) (conj people-still-on new-order) people-still-on) ;If order taken add the current passengers to the new order passengers
        ]
    ;for if true? is there a better way to do this?

    (hash-map :station newStation                           ;Put all the values into a hashmap so that they can be seen and used
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
    (if (= (get fir :station) (get fir :route-end)) states  ;When start and finish are the same, so the orders have ended
                                                    (let [new-states (for [x states] (map #(move x %) (filter #(= (get % :start) (get x :station)) order)))] ;for each state map all of the legal moves that can be done
                                                      (recur (apply concat new-states) order))))) ;Concatinating all of the different station states into one large list


