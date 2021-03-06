*Assignment 2*

1. [1
point]
Write
a
Scheme
function
myappend
that
takes
two
lists
and
returns
the
result
of
appending
the
second
list
to
the
first.
For
example:
(myappend '(1 2 3 4) '(5 6 7 8))
should
return
'(1 2 3 4 5 6 7 8).
Do
not
use
the
built-­‐in
append
function.
You
my
only
use
car,
cdr,
cons,
if
and
null?.
2. [1
point]
Write
a
Scheme
function
fibonnaci
that
returns
the
n-­‐th
Fibonacci
number.
Recall
that
Fibonacci
numbers
can
be
defined
recursively
as
follows:
the
1st
and
2nd
Fibonacci
numbers
are
both
1.
The
k-­‐th
Fibonacci
number
for
k
>
2
is
the
sum
of
the
(k-­‐1)-­‐th
and
(k-­‐2)-­‐th
Fibonacci
number.
Use
two
recursive
calls
for
the
general
case.
Example:
(fibonacci 10)
should
return
55.
3. [1
point]
Write
a
Scheme
function
fibtail
that
implements
a
tail-­‐recursive
version
of
the
previous
function
that
runs
in
O(n)
time
.
Hint:
you
can
write
a
helper
function
fibhelp
that
takes
as
many
arguments
as
you
need.
Then,
fibtail
simply
calls
this
helper
function
with
the
initial
argument
values
necessary.
Example:
(fibtail 50)
should
return
12586269025.
4. [1
point]
Write
a
Scheme
function
to
return
a
list
containing
all
elements
of
a
given
list
that
satisfy
a
given
predicate
(i.e.,
function
returning
a
boolean
value).
For
example:
(filter (lambda (x) (< x 5)) '(3 9 5 8 2 4 7))
should
return
'(3 2 4).
5. [1
points]
Using
the
above
function,
write
a
function
that
implements
quick
sort.
Recall
that
quick
sort
takes
a
pivot
element
in
the
list
(you
may
use
the
first
element
as
the
pivot),
and
splits
the
list
into
two
lists:
one
containing
the
elements
that
are
lesser
or
equal
to
the
pivot
and
one
containing
those
that
are
greater.
Both
of
these
lists
are
then
sorted
recursively.



*Asignment 3*

[1
point]
Write
a
Prolog
predicate
mysubset(S1, S2)
which
is
true
if
set
S1
is
a
subset
of
(included
in)
set
S2.
For
example:
?- mysubset([1,3,2], [4,2,1,3,5]).
true
2. [1
point]
Write
a
Prolog
predicate
mysetequal(S1, S2)
which
is
true
if
the
sets
S1
and
S2
are
equal.
For
example:
?- mysetequal([1,3,2], [1,2,3]).
true
Hint:
You
may
invoke
mysubset
and
use
the
fact
that
two
sets
are
equal
if
and
only
if
they
are
each
a
subset
of
the
other.
3. [1
point]
Write
a
Prolog
predicate
myunion(S1, S2, U)
where
set
U
is
the
union
of
the
sets
S1
and
S2.
There
should
be
no
repeated
elements
in
U.
For
example:
?- myunion([1,2,3],[1,2,4], U).
U = [3, 1, 2, 4].
4. [1
point]
Write
a
Prolog
predicate
myintersection(S1, S2, I)
where
set
I
is
the
intersection
of
the
sets
S1
and
S2.
For
example:
?- myintersection([1,2,3],[1,2,4], I).
I = [1, 2].
5. [1
point]
Write
a
Prolog
predicate
mysetdifference(S1, S2, D)
where
D
is
the
set
difference
of
the
sets
S1
and
S2;
i.e.
S1
–
S2
,
sometimes
also
denoted
S1\S2.
These
are
the
elements
of
S1
that
are
not
elements
of
S2.
For
example:
?- mysetdifference([1,2,3], [1,2,4], D).
D = [3].
