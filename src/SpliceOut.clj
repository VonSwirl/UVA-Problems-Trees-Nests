(ns SpliceOut)

(defn splice-out1 [x y lis]                                 ;  Function takes in 3 arguments (2 indexes, 1 list)
  (take y (drop x lis)))                                    ;  drop elements until x, take elements till y

; Test1 - (splice-out1 2 3 '(a b c d e f))
; Expected (c d)

; Test 2 - (splice-out1 6 3 '(a b c d e f))
; Expected ()

; Test 3 - (splice-out1 3 5 '(a b c d e f))
; Expected (d e f)

(defn splice-out2 [x y lis]
  (if (<= x y)
    (cons (nth lis x) (splice-out2 (inc x) y lis))
    ()))

; Test1 - (splice-out2 0 3 '(a b c d e f))
; Expected (a b c d)

; Test2 - (splice-out2 5 3 '(a b c d e f))
; Expected ()

; Test 3 - (splice-out2 3 5 '(a b c d e f))
; Expected (d e f)

(defn addx [x]
  (+ x 5))




