;--------------TO-DO-----------------------------------------------------
;more tests e.g nils, emptys, symbols, types.
;run time tracing/ scalability
;more funtions, matcher defmatch lenght, every seq, covert bitwise right?
;reece formattin :)
;review feedback for box ticking.
;NEED helper funtion.
;--------------TO-DO-----------------------------------------------------



(defn nested-average-denest-v2 [tree]                        ;define function takes sequence as argument
  (/ (reduce + (flatten tree)) (count (flatten tree))))      ;divide the total of the flattened tree by the number of elements in the flattened tree

                                                             ;Test 1 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                             ;Expected = 18
                                                             ;Test 2 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                             ;Expected = 15.375
;------------------------------------------------------------
(defn nested-average-denest-v1[tree]                         ;define function takes tree (nested list)
  (let[denest (flatten tree)                                 ;removes brackets and returns all elements in a single (flat)sequence
       nest-count (count denest)                             ;counts all elements in sequence
       sum-values (reduce + denest)]                         ;recursively sums all the values in sequence

    (float (/ sum-values nest-count))))

                                                             ;Test 1 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                             ;Expected = 18
                                                             ;Test 2 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 ()) 9) 40))
                                                             ;Expected = 123/8(15.375)
;------------------------------------------------------------


;Head recursive methods--------------------------------------
(defn head-sum [tree]
  (cond
    (number? tree)  tree                                     ;Check if value is a number, then passes the tree again
    (not (seq  tree))  0                                     ;If value is empty add 0
    :else
    (+ (head-sum (first tree)) (head-sum (rest tree)))))     ;Otherwise add the first to the tree to the last of the tree


(defn head-length [tree]
  (cond
    (nil? tree) 0
    (not (coll? tree)) 1                                     ;returns true if x implements a persistent collection eg (coll '()) = true but (coll 4) = false
                                                             ;Checks that its not one of them, so it means its a number. It will then add 1
    (empty? tree) 0                                          ;If the pulled element is empty add 0
    :else
    (+ (head-length (first tree)) (head-length (rest tree)))));Add the first node of tree, (current branch)
                                                             ;Recur through the rest of the tree


(defn head-tree-average [tree]
  (/ (head-sum [tree]) (head-length [tree])))  ;Uses the already created methods to find the average

;-------------------------------------------------------------

;ADD MORE TESTS WITH NIL AND 0 INCLUDED
;NEEDS TO CHECK AGAINST NIL!!!!!!
;USE TRACE AGAINST THE METHODS TO SEE IF THEY WORK CORRECTLY

;Tail recursion for the sum ---------------------------------
(defn tail-sum1
  ([tree] (tail-sum1 tree 0))                                ;Get the tree, recall the method with the tree and the total passed in. Starting from 0
  ([tree total]
   (if-not (seq tree) total                                  ;!!! EXPLAIN HERE !!!
     (tail-sum1 (rest tree) (+ (first tree) total)))))       ;Pass rest of tree then add the first of the tree to the total

(defn tail-sum2
  ([tree] (tail-sum2 tree 0))
  ([tree total]
   (if-not (seq tree) total
     (recur (rest tree) (+ (first tree) total)))))
;------------------------------------------------------------

;Tail recursion for the length ------------------------------
(defn tail-length1
  ([tree]   (tail-length1 tree 0))                           ;Get the tree, recall the method with the tree and the count passed in. Starting from 0
  ([tree count]
  (if-not (seq tree) count                                   ;
  (tail-length1 (rest tree) (inc count)))))                  ;Pass rest of the tree and increment count

(defn tail-length2
  ([tree]   (tail-length2 tree 0))
  ([tree count]
   (if-not (seq tree) count
   (recur (rest tree) (inc count)))))
;------------------------------------------------------------


(defn tail-tree-average1[tree]
  (/ (tail-sum1 (flatten tree)) (tail-length1 (flatten tree))))

(defn tail-tree-average2 [tree]
  (/ (tail-sum2 (flatten tree)) (tail-length2 (flatten tree))))


;--- TO DO LIST ---
;Create a function that gets the sum and the count in 1 (then divides them) (SEND HELP)
;Find a tail recursive way for sum-tree and count-tree
;Use recur with the tail functions. (test times)
;Create a method that uses the both of the tail recursive methods
;------------------


;Why doesnt this work?
(defn nested-average-v3 [tree]
  (cond
    (number? tree)  tree
    (not (seq  tree))  0
    :else
    (/ (reduce + (map nested-average-v3 tree)) (count (map nested-average-v3 tree)))))

;Create helper function to check against all the values
;pass in a tree, pass in a tree and the "count", check the tree, add the valeus then divide by the "count"

(defn tree-average
  ([tree] (tree-average tree 0))
  ([tree n]
   (cond
     (number? tree) tree
     ;(number? tree)
     ;Checks against tree
     (not (seq  tree))  0
     ;(nil? tree) 0
     ;((not (coll? tree)) inc n) ;This checks if number, if true inc n
     :else
     (/ (+(tree-average (first tree))
          (tree-average (rest tree))) 2)))) ;Otherwise add the first to the tree to the last of the tree
