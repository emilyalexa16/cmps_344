(defun Isort (L) 
  (if (listp L) 
      (sorter L nil) 
       nil
  )
)

(defun sorter (L partial)
   (if (null L) 
        partial
        (sorter (rest L) (insert (first L) partial))
   ) 
)

(defun insert (n L)
   (if (null L)
       (list n)
       (place n nil L))
)

(defun place (n pre L)
   (cond
     ((null L)
        (append pre (list n))
     )
     ((and (listp n) (numberp (first L))) ; n is list, first L is number
         (if (<= (sum-list n) (first L))
              (append pre (list n) L)
              (place n (append pre (list (first L))) (rest L))
          )
     )
     ((and (numberp n) (listp (first L))) ; n is number, first L is list 
          (if (<= n (sum-list (first L)))
              (append pre (list n) L)
              (place n (append pre (list (first L))) (rest L))
           )  
     )
     ((and (listp n) (listp (first L))) ; n is list, first L is list 
          (if (<= (sum-list n) (sum-list (first L)))
              (append pre (list n) L)
              (place n (append pre (list (first L))) (rest L))
           )  
     )
     (T ; n is a number, first L is a number
         (if (<= n (first L))
            (append pre (list n) L)
            (place n (append pre (list (first L))) (rest L))
         )
     )   
  )
)

(defun sum-list (L)
    (cond
      ((null L) 
       0
      )
      ((listp (first L))
        (+ (sum-list (first L)) (sum-list(rest L)))
      )
      (T
        (+ (first L)(sum-list(rest L)))
      )
    )
)
