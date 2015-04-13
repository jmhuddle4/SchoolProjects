%Max Huddleston
%2422320
%CSC 370
%Assignment 3

%Question 1
mySubset([ ],_). %initiate mySubset
mySubset([H|T],List) :- %List is unified to head
			%and tail of a list if
    member(H,List),     %Head is member of list and
    mySubset(T,List).	%the rest of the tail
%?- mySubset([1,2,3], [1,2,3,4]).


%Question 2
mySetEqual(A,B):- %Make sure sets are subsets of eachother
    mySubset(A,B),%If S1 is subset of S2 and
    mySubset(B,A).%S2 is subset of S1
		  %then they are equal
%?- mySetEqual([1,2,3], [1,2,3])


%Question 3
%Combines sets but only inlcudes all unique values no duplicates
myUnion([A|B],C,D) :- %unify set 1 if head of set 1
    member(A,C),      %is in set 2
    myUnion(B,C,D).
myUnion([A|B],C,[A|D]) :- %
    \+ member(A,C),       %tail of set 1 is
    union(B,C,D).    %added to set 2
myUnion([],C,C).     %if a set is empty then the union
                     %is the non empty set
%?- myUnion([1,2,3], [1,4,5], U).
%U = [1,2,3,4,5]


%Question 4
%Checks to see which unique elements the sets share
myIntersection([A|B],I,[A|C]) :- %takes in two sets who's head is the same and
	                         %tails are different, unifies if
	member(A,I),	         %head is member and
	myIntersection(B,I,C).   %both tails share a value
myIntersection([A|B],I,C) :-     %does not unify if
	\+ member(A,I),		 %sets do not share values
	myIntersection(B,I,C).   %
myIntersection([],I,[]).  %Empty sets intersect
%?- myIntersection([1,2,3],[1,4,5],I).
%I = [1]


%Question 5
%Substract set 2 from set 1
mySetDifference([], _, []) :- !. %If sets are empty, cut off because
				 %the answer is the empty set
mySetDifference([A|C], B, D) :-  %unifies if head of set 1 is
        memberchk(A, B), !,	 %member of set 2 and cuts off and
        mySetDifference(C, B, D).%removes that value from Difference set
mySetDifference([A|B], C, [A|D]) :-%the differnece is the values that are in
        mySetDifference(B, C, D).  %set 1 but not in set 2
%?- mySetDifference([1,2,3,4],[1,2,5,6],D).
%D = [3,4]

