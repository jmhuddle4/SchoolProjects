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
