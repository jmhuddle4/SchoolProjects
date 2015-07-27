;robot-domain

(define (domain robot-domain)
  
  (:constants robot)

  (:predicates (Object ?x) 
	       (Room ?x) 
	       (RobotNotHoldingObj) 
	       (AtRoom ?x ?y)
	       (RobotHoldingObj ?x) 
	       (Connected ?x ?y))

  (:action pickup
	   :parameters (?x ?location) 
	   :precondition (and (Object ?x) 
			 	(Room ?location)
			 	(not (= ?x robot)) 
			 	(RobotNotHoldingObj) 
			 	(AtRoom ?x ?location)
			 	(AtRoom robot ?location))

	   :effect (and (RobotHoldingObj ?x)
			(not (RobotNotHoldingObj))))

  (:action release
	   :parameters (?x)
	   :precondition (and (Object ?x)
			 (	not (= ?x robot)) 
				(RobotHoldingObj ?x))

	   :effect (and (RobotNotHoldingObj)
			(not (RobotHoldingObj ?x))))

  (:action MoveWithObj
	   :parameters (?from ?to)
	   :precondition (and (Room ?from) (Room ?to)
			 	(not (= ?from ?to)) (AtRoom robot ?from)
				(Connected ?from ?to))

	   :effect (and (AtRoom robot ?to)
			(not (AtRoom robot ?from))
			(forall (?x)
				(when (and (RobotHoldingObj ?x) (Object ?x))
				(and (AtRoom ?x ?to) 
				(not (AtRoom ?x ?from))))))))

;  (:action MoveWithoutObj
;	   :parameters (?from ?to)
;	   :precondition (and (Room ?from) (Room ?to)
;			 	(not (= ?from ?to)) 
;				(AtRoom robot ?from)
;				(Connected ?from ?to)
;				(RobotNotHoldingObj ?x))
;
;	   :effect (and (AtRoom robot ?to)
;			(not (AtRoom robot ?from))
;			(forall (?x)
;				(when (and (RobotHoldingObj ?x) (Object ?x))
;				(and (AtRoom ?x ?to) 
;				(not (AtRoom ?x ?from))))))))
;
