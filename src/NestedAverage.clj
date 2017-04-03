(ns NestedAverage)

(defn tail-sum [tree]
  (number? tree) tree
  (not (seq? tree)) 0
  (recur (+ (rest tree) (first tree)))
  )

(defn sum
  ([tree] (sum tree 0))
  ([tree total]
   (if (empty? tree)
     total
     (sum (rest tree) (+ (first tree) total)))))
;--------------TO-DO-----------------------------------------------------
;more tests e.g nils, emptys, symbols, types.
;run time tracing/ scalability
;more funtions, matcher defmatch lenght, every seq, covert bitwise right?
;reece formattin :)
;review feedback for box ticking.
;NEED helper funtion.
;--------------TO-DO-----------------------------------------------------





(defn nested-average-denest-v2 [tree]                   ;define function takes sequence as argument
  (/ (reduce + (flatten tree))(count (flatten tree))))      ;divide the total of the flattened tree by the number of elements in the flattened tree
;Test 1 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                        ;Expected = 18
                                                        ;Test 2 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                        ;Expected = 15.375
;-------------------------------------------------------
(defn nested-average-denest-v1[tree]                    ;define function takes tree (nested list)
  (let[denest (flatten tree)                            ;removes brackets and returns all elements in a single (flat)sequence
     nest-count (count denest)                          ;counts all elements in sequence
       sum-values (reduce + denest)]                    ;recursively sums all the values in sequence

    (float (/ sum-values nest-count))
    )
  )
                                                       ;Test 1 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                       ;Expected = 18
                                                       ;Test 2 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 ()) 9) 40))
                                                       ;Expected = 123/8(15.375)
;-------------------------------------------------------

(defn sum-tree [tree]
  (cond
    (number? tree) tree                                     ;Check if pulled value is a number, then passes the tree again
    (not (seq tree)) 0                                      ;If pulled value is empty add 0
    :else
    (+ (sum-tree (first tree)) (sum-tree (rest tree)))      ;Otherwise add the first to the tree to the last of the tree
    ))

(defn count-tree [tree]
  (cond
    (nil? tree) 0
    (not (coll? tree)) 1                                    ;returns true if x implements a persistent collection eg (coll '()) = true but (coll 4) = false
    ;Checks that its not one of them, so it means its a number. It will then add 1
    (empty? tree) 0                                         ;If the pulled element is empty add 0
    :else
    (+ (count-tree (first tree)) (count-tree (rest tree))))) ;Add the first node of tree, (current branch)
;Recur through the rest of the tree

(defn length-tree [tree]
  (flatten tree)
  (if-not (seq tree)
    0
    (inc (length-tree (rest tree)))
    ))


(defn nested-average-v2 [tree]                              ;define function takes sequence as argument
  (/ (reduce + (flatten tree)) (count (flatten tree))))     ;divide the total of the flattened tree by the number of elements in the flattened tree

;Why doesnt this work?
(defn nested-average-v3 [tree]
  (cond
    (number? tree) tree
    (not (seq tree)) 0
    :else
    (/ (reduce + (map nested-average-v3 tree)) (count (map nested-average-v3 tree)))))


(defn method-tree-average [tree]
  (/ (sum-tree [tree]) (count-tree [tree])))                ;Uses the already created methods to find the average


;Create helper function to check against all the values
;pass in a tree, pass in a tree and the "count", check the tree, add the valeus then divide by the "count"

(defn tree-average
  ([tree] (tree-average tree 0))
  ([tree n]
   (cond
     (number? tree) tree
     ;(number? tree)
     ;Checks against tree
     (not (seq tree)) 0
     ;(nil? tree) 0
     ;((not (coll? tree)) inc n) ;This checks if number, if true inc n
     :else
     (/ (+ (tree-average (first tree)) (tree-average (rest tree))) 2)

     ;Otherwise add the first to the tree to the last of the tree
     )))

;--- TO DO LIST ---
;Create a function that gets the sum and the count in 1 (then divides them) (SEND HELP)
;Find a tail recursive way for sum-tree and count-tree
;Use recur with the tail functions. (test times)
;Create a method that uses the both of the tail recursive methods
;------------------