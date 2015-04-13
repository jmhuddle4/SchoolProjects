;scheme with Dr. Racket IDE

;Max Huddleston 2422320
;CSC 370 Assignment 2
;18 November 2014

#lang racket

;Problem 1
(define (myAppend listOne listTwo) ;two base cases
      (if (null? listOne) listTwo ;if listOne is empty return listTwo
          (if (null? listTwo) listOne ;if listTwo is empty return listOne
              (cons (car listOne) (cons (car listTwo) (myAppend (cdr listOne) (cdr listTwo)))) ;concatenate listOne and listTwo
              ;(cons (cons (car (listOne)) (cdr(listOne))) (cons (car (listTwo)) (cdr(listTwo))))
          )
      )
)
;> (myAppend '(a e h p) '(n w o e)) where a,e,h,p,n,w,o can be any amount of integers or numbers


;Problem 2
(define (fib n)
  (cond ;2 base cases because we need previous two numbers
    ((= n 0) 0) ;1st two numbers in sequence are 0 and 1
    ((= n 1) 1)
    (else (+ (fib (- n 2)) (fib (- n 1)))) ;add prevous two numbers in sequence
    )     
  )
;>(fib n) where n is any integer


;Problem 3 ;fibTail calls fibHelp to make the function excute in O(n) time
(define (fibTail n)
  (fibHelp 1 0 n)) ;if change b to 1, would return fib(n + 1)
 
(define (fibHelp a b iter) ;arguments are first two numbers of sequence and # of iterations
  (if (= iter 0) b ;if iterations are complete return value of b because you want the n-1 iteration
      (fibHelp (+ a b) a (- iter 1)) ;tailrecursive function  that take arguments where it adds the 
  )                                  ;previous two numbers, fib(n -1), and decreases the iteration
)


;Problem 4
(define (myFilter filt list) ;arguments are a filter predicate and list of your choice
  (if (null? list) 
    '() ;if the list is empty return an empty list
    (if (filt (car list))  ;elseif the head is true
      (cons (car list) (myFilter filt (cdr list)))  ;return the head
      (myFilter filt (cdr list)) ;elseif false return the result of filtering the rest of the list
    )
  )
)

;filters
(define (greaterThan n) (> n 5)) ;filters greater than 5, can change number
(define (lessThan n) (< n 5)) ;filters less than 5, can change number

;>(myFilter greaterThan '(2 3 4 5 6))


;Problem 5
(define (quickSort lis) ;list is already bound
  (if (null? lis) '() ;if input list is null return empty list
      (let* ;have to use let* to refer to previous binding of pivot
          ((pivot (car lis)) ;bind pivot to 1st element of list
            (tail (cdr lis)) ;bind tail to the rest of the list
            (lessThan  (myFilter (lambda (x) (< x pivot)) tail)) ;put everything less than pivot on left
            (greaterThan (myFilter (lambda (x) (> x pivot)) tail)) ;put everything greater than pivot on right
          ) ;couldn't use >= because it returns empty list
        (append (quickSort lessThan) (cons pivot (quickSort greaterThan))) ;append left and right side and the pivot into the list
      ) ;myAppend doesn't work properly
  )
)

;>(quickSort '(1 3 2 5 4 7 12 8))
