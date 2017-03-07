(defn nested-average-denest-v1 [tree]                            ;define function takes sequence as argument
  (/ (reduce + (flatten tree)) (count (flatten tree))))          ;divide the total of the flattened tree by the number of elements in the flattened tree

                                                                 ;Test 1 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                                 ;Expected = 18
                                                                 ;Test 2 - (nested-average-denest-v1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                                 ;Expected = 15.375
                                                                 ;Breaks with nil
;----------------------------------------------------------------
(defn nested-average-denest-v2[tree]                             ;define function takes tree (nested list)
  (let[denest (flatten tree)                                     ;removes brackets and returns all elements in a single (flat)sequence
       nest-count (count denest)                                 ;counts all elements in sequence
       sum-values (reduce + denest)]                             ;recursively sums all the values in sequence
    (float (/ sum-values nest-count))))

                                                                 ;Test 1 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                                 ;Expected = 18
                                                                 ;Test 2 - (nested-average-denest-v2 '(10 ((30 1) 20) (8 (5 ()) 9) 40))
                                                                 ;Expected = 123/8(15.375)
                                                                 ;Breaks with nil
;----------------------------------------------------------------

;Tail recursion for the sum -------------------------------------
(defn tail-sum1                                                  ;These only work with a flattened list, which will be needed to be passed as an argument
  ([tree] (tail-sum1 tree 0))                                    ;Get the tree, recall the method with the tree and the total passed in. Starting from 0
  ([tree total]
    (if-not (seq tree) total                                     ;If not a seq pass through total
      (tail-sum1 (rest tree) (+ (first tree) total)))))          ;Pass rest of tree then add the first of the tree to the total

(defn tail-sum2
  ([tree] (tail-sum2 tree 0))
  ([tree total]
    (if-not (seq tree) total
      (recur (rest tree) (+ (first tree) total)))))
;----------------------------------------------------------------

;Tail recursion for the length ----------------------------------
(defn tail-length1
  ([tree]   (tail-length1 tree 0))                               ;Get the tree, recall the method with the tree and the count passed in. Starting from 0
  ([tree count]
    (if-not (seq tree) count                                     ;If not a seq pass through count
      (tail-length1 (rest tree) (inc count)))))                  ;Pass rest of the tree and increment count

(defn tail-length2
  ([tree]   (tail-length2 tree 0))
  ([tree count]
    (if-not (seq tree) count
      (recur (rest tree) (inc count)))))
;-----------------------------------------------------------------

(defn tail-tree-average1 [tree]                                  ;Average function time of 5 :=> 0.1012684 msecs and 0.0976762 msecs
  (/ (tail-sum1 (flatten tree)) (tail-length1 (flatten tree))))
                                                                 ;Test 1 - (tail-tree-average1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                                 ;Expected = 18
                                                                 ;Test 2 - (tail-tree-average1 '(10 ((30 1) 20) (8 (5 ()) 9) 40))
                                                                 ;Expected = 123/8
                                                                 ;Breaks with nil

(defn tail-tree-average2 [tree]                                  ;Average function time of 5 :=>  0.925446 msecs and 0.1116664 msecs
  (/ (tail-sum2 (flatten tree)) (tail-length2 (flatten tree))))
                                                                 ;Test 1 - (tail-tree-average1 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
                                                                 ;Expected = 18
                                                                 ;Test 2 - (tail-tree-average1 '(10 ((30 1) 20) (8 (5 ()) 9) 40))
                                                                 ;Expected = 123/8
                                                                 ;Breaks with nil

;Head recursive methods------------------------------------------
(defn head-sum [tree]
  (cond
    (number? tree)  tree                                         ;Check if value is a number, then passes the tree again
    (not (seq  tree))  0                                         ;If value is empty add 0
    :else
    (+ (head-sum (first tree)) (head-sum (rest tree)))))         ;Otherwise add the first to the tree to the last of the tree


(defn head-length [tree]
  (cond
    (nil? tree) 0
    (not (coll? tree)) 1                                         ;Returns true if x implements a persistent collection eg (coll '()) = true but (coll 4) = false.Checks that its not one of them, so it means its a number. It will then add 1
    (empty? tree) 0                                              ;If the pulled element is empty add 0
    :else
    (+ (head-length (first tree)) (head-length (rest tree)))))   ;Add the first node of tree, (current branch). Recur through the rest of the tree


(defn head-tree-average [tree]                                   ;Average function time of 5 :=> 0.1228226 msecs and 0.080399 msecs
  (/ (head-sum [tree]) (head-length [tree])))                    ;Uses the already created functions to find the average

;----------------------------------------------------------------

;--- TO DO LIST ---
;Create a function that gets the sum and the count in 1 (then divides them) (SEND HELP)
;!!!!! NEEDS TO CHECK AGAINST NIL !!!!!!
;------------------

;The failures
(defn nested-average-v3 [tree]
  (cond
    (number? tree)  tree
    (empty?  tree)  0
    :else
    (/ (reduce + (mapcat tree)) (count (mapcat tree)))))


;Create helper function to check against all the values
;pass in a tree, pass in a tree and the "count", check the tree, add the valeus then divide by the "count"
(defn tree-average
  ([tree] (tree-average tree 0))
  ([tree n]
    (cond
      (number? tree) tree
      (not (seq  tree))  0
      :else
      (+(tree-average (first tree)) (tree-average (rest tree)) (inc n))))) ;Otherwise add the first to the tree to the last of the tree
                                                                           ;This currently only adds n on so total is 195. When recuring through in incs n even if there is an empty list