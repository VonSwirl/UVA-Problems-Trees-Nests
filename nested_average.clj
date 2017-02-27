;--------------TO-DO-----------------------------------------------------
;more tests e.g nils, emptys, symbols, types.
;run time tracing/ scalability
;more funtions, matcher defmatch lenght, every seq, covert bitwise right?
;reece formattin :)
;review feedback for box ticking.
;NEED helper funtion.
;--------------TO-DO-----------------------------------------------------





(defn nested-average-denest-v2 [tree]                   ;define function takes sequence as argument
  (/ (reduce + (flatten tree))(count (flatten tree))))  ;divide the total of the flattened tree by the number of elements in the flattened tree
                                                        ;Test 1 - (nested-average-denestV2 '(10 ((30 1) 20) (8 (5 (50 7)) 9) 40))
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
