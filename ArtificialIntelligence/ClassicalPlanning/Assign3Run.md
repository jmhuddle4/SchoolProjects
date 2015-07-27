Max Huddleston
Brandon Hanger
CSC 542 Assignment 3
28 April 2015

*****BENCHMARK 1*****

Translating probBLOCKS-4-0.pddl -> makes output.sas file

max@ubuntu:~/Downloads/Downward/src$ translate/translate.py domain.pddl probBLOCKS-4-0.pddl
Parsing...
Parsing: [0.000s CPU, 0.002s wall-clock]
Normalizing task... [0.000s CPU, 0.000s wall-clock]
Instantiating...
Generating Datalog program... [0.000s CPU, 0.000s wall-clock]
Normalizing Datalog program...
Normalizing Datalog program: [0.010s CPU, 0.013s wall-clock]
Preparing model... [0.000s CPU, 0.001s wall-clock]
Generated 21 rules.
Computing model... [0.000s CPU, 0.004s wall-clock]
78 relevant atoms
52 auxiliary atoms
130 final queue length
206 total queue pushes
Completing instantiation... [0.000s CPU, 0.004s wall-clock]
Instantiating: [0.010s CPU, 0.027s wall-clock]
Computing fact groups...
Finding invariants...
10 initial candidates
Finding invariants: [0.010s CPU, 0.014s wall-clock]
Checking invariant weight... [0.000s CPU, 0.000s wall-clock]
Instantiating groups... [0.000s CPU, 0.000s wall-clock]
Collecting mutex groups... [0.000s CPU, 0.000s wall-clock]
Choosing groups...
5 uncovered facts
Choosing groups: [0.000s CPU, 0.001s wall-clock]
Building translation key... [0.000s CPU, 0.000s wall-clock]
Computing fact groups: [0.010s CPU, 0.016s wall-clock]
Building STRIPS to SAS dictionary... [0.000s CPU, 0.000s wall-clock]
Building dictionary for full mutex groups... [0.000s CPU, 0.000s wall-clock]
Building mutex information...
Building mutex information: [0.000s CPU, 0.000s wall-clock]
Translating task...
Processing axioms...
Simplifying axioms... [0.000s CPU, 0.000s wall-clock]
Processing axioms: [0.000s CPU, 0.001s wall-clock]
Translating task: [0.010s CPU, 0.004s wall-clock]
44 effect conditions simplified
0 implied preconditions added
Detecting unreachable propositions...
0 operators removed
8 propositions removed
Detecting unreachable propositions: [0.000s CPU, 0.001s wall-clock]
Translator variables: 9
Translator derived variables: 0
Translator facts: 30
Translator goal facts: 3
Translator mutex groups: 9
Translator total mutex groups size: 45
Translator operators: 32
Translator axioms: 0
Translator task size: 315
Translator peak memory: 37480 KB
Writing output... [0.000s CPU, 0.001s wall-clock]
Done! [0.030s CPU, 0.056s wall-clock]


Creating output file using output.sas

max@ubuntu:~/Downloads/Downward/src$ preprocess/preprocess < output.sas
Building causal graph...
The causal graph is not acyclic.
9 variables of 9 necessary
5 of 9 mutex groups necessary.
32 of 32 operators necessary.
0 of 0 axiom rules necessary.
Building domain transition graphs...
solveable in poly time 0
Building successor generator...
Preprocessor facts: 30
Preprocessor derived variables: 0
Preprocessor task size: 295
Writing output...
done


Using A* search lmcut method on output file

max@ubuntu:~/Downloads/Downward/src$ ./fast-downward.py output --search "astar(lmcut())"
INFO     Running search.
INFO     search input: output
INFO     search executable: /home/max/Downloads/Downward/src/search/downward-release
INFO     search arguments: ['--search', 'astar(lmcut())', '--internal-plan-file', 'sas_plan']
reading input... [t=0.00s]
Simplifying transitions... done!
done reading input! [t=0.00s]
building causal graph...done! [t=0.00s]
packing state variables...done! [t=0.00s]
Variables: 9
Facts: 30
Bytes per state: 4
done initalizing global data [t=0.00s]
Conducting best first search with reopening closed nodes, (real) bound = 2147483647
Initializing landmark cut heuristic...
f = 6 [1 evaluated, 0 expanded, t=0.00s, 3284 KB]
Best heuristic value: 6 [g=0, 1 evaluated, 0 expanded, t=0.00s, 3284 KB]
Best heuristic value: 5 [g=1, 3 evaluated, 1 expanded, t=0.00s, 3284 KB]
Best heuristic value: 4 [g=2, 6 evaluated, 2 expanded, t=0.00s, 3284 KB]
Best heuristic value: 3 [g=3, 9 evaluated, 3 expanded, t=0.00s, 3284 KB]
Best heuristic value: 2 [g=4, 11 evaluated, 4 expanded, t=0.00s, 3284 KB]
Best heuristic value: 1 [g=5, 13 evaluated, 5 expanded, t=0.00s, 3284 KB]
Best heuristic value: 0 [g=6, 14 evaluated, 6 expanded, t=0.00s, 3284 KB]
Solution found!
Actual search time: 0.00s [t=0.00s]
pick-up b (1)
stack b a (1)
pick-up c (1)
stack c b (1)
pick-up d (1)
stack d c (1)
Plan length: 6 step(s).
Plan cost: 6
Initial state h value: 6.
Expanded 7 state(s).
Reopened 0 state(s).
Evaluated 14 state(s).
Evaluations: 14
Generated 18 state(s).
Dead ends: 0 state(s).
Expanded until last jump: 0 state(s).
Reopened until last jump: 0 state(s).
Evaluated until last jump: 1 state(s).
Generated until last jump: 0 state(s).
Number of registered states: 14
Search time: 0.00s
Total time: 0.00s
Solution found.
Peak memory: 3284 KB


*****BENCHMARK 2*****

Translating probBLOCKS-5.0 to output.sas file

max@ubuntu:~/Downloads/Downward/src$ translate/translate.py domain.pddl probBLOCKS-5-0.pddl
Parsing...
Parsing: [0.000s CPU, 0.002s wall-clock]
Normalizing task... [0.000s CPU, 0.000s wall-clock]
Instantiating...
Generating Datalog program... [0.000s CPU, 0.000s wall-clock]
Normalizing Datalog program...
Normalizing Datalog program: [0.000s CPU, 0.002s wall-clock]
Preparing model... [0.010s CPU, 0.010s wall-clock]
Generated 21 rules.
Computing model... [0.000s CPU, 0.004s wall-clock]
112 relevant atoms
75 auxiliary atoms
187 final queue length
299 total queue pushes
Completing instantiation... [0.000s CPU, 0.007s wall-clock]
Instantiating: [0.020s CPU, 0.025s wall-clock]
Computing fact groups...
Finding invariants...
10 initial candidates
Finding invariants: [0.000s CPU, 0.015s wall-clock]
Checking invariant weight... [0.000s CPU, 0.000s wall-clock]
Instantiating groups... [0.010s CPU, 0.000s wall-clock]
Collecting mutex groups... [0.000s CPU, 0.000s wall-clock]
Choosing groups...
6 uncovered facts
Choosing groups: [0.000s CPU, 0.001s wall-clock]
Building translation key... [0.000s CPU, 0.000s wall-clock]
Computing fact groups: [0.010s CPU, 0.019s wall-clock]
Building STRIPS to SAS dictionary... [0.000s CPU, 0.000s wall-clock]
Building dictionary for full mutex groups... [0.000s CPU, 0.000s wall-clock]
Building mutex information...
Building mutex information: [0.000s CPU, 0.000s wall-clock]
Translating task...
Processing axioms...
Simplifying axioms... [0.000s CPU, 0.000s wall-clock]
Processing axioms: [0.000s CPU, 0.001s wall-clock]
Translating task: [0.000s CPU, 0.006s wall-clock]
70 effect conditions simplified
0 implied preconditions added
Detecting unreachable propositions...
0 operators removed
10 propositions removed
Detecting unreachable propositions: [0.000s CPU, 0.002s wall-clock]
Translator variables: 11
Translator derived variables: 0
Translator facts: 42
Translator goal facts: 4
Translator mutex groups: 11
Translator total mutex groups size: 66
Translator operators: 50
Translator axioms: 0
Translator task size: 483
Translator peak memory: 37480 KB
Writing output... [0.000s CPU, 0.002s wall-clock]
Done! [0.030s CPU, 0.063s wall-clock]


Creating output file for probBLOCKS-5-0

max@ubuntu:~/Downloads/Downward/src$ preprocess/preprocess < output.sas
Building causal graph...
The causal graph is not acyclic.
11 variables of 11 necessary
6 of 11 mutex groups necessary.
50 of 50 operators necessary.
0 of 0 axiom rules necessary.
Building domain transition graphs...
solveable in poly time 0
Building successor generator...
Preprocessor facts: 42
Preprocessor derived variables: 0
Preprocessor task size: 453
Writing output...
done


Preform A* search lmcut method on probBLOCKS-5-0 output file

max@ubuntu:~/Downloads/Downward/src$ ./fast-downward.py output --search "astar(lmcut())"
INFO     Running search.
INFO     search input: output
INFO     search executable: /home/max/Downloads/Downward/src/search/downward-release
INFO     search arguments: ['--search', 'astar(lmcut())', '--internal-plan-file', 'sas_plan']
reading input... [t=0.00s]
Simplifying transitions... done!
done reading input! [t=0.00s]
building causal graph...done! [t=0.00s]
packing state variables...done! [t=0.00s]
Variables: 11
Facts: 42
Bytes per state: 4
done initalizing global data [t=0.00s]
Conducting best first search with reopening closed nodes, (real) bound = 2147483647
Initializing landmark cut heuristic...
f = 8 [1 evaluated, 0 expanded, t=0.00s, 3280 KB]
Best heuristic value: 8 [g=0, 1 evaluated, 0 expanded, t=0.00s, 3280 KB]
Best heuristic value: 7 [g=1, 3 evaluated, 1 expanded, t=0.00s, 3280 KB]
f = 9 [4 evaluated, 2 expanded, t=0.00s, 3280 KB]
Best heuristic value: 6 [g=3, 7 evaluated, 5 expanded, t=0.00s, 3280 KB]
Best heuristic value: 5 [g=4, 9 evaluated, 6 expanded, t=0.00s, 3280 KB]
f = 10 [11 evaluated, 7 expanded, t=0.00s, 3280 KB]
f = 11 [17 evaluated, 10 expanded, t=0.00s, 3280 KB]
f = 12 [20 evaluated, 12 expanded, t=0.00s, 3280 KB]
Best heuristic value: 4 [g=8, 24 evaluated, 15 expanded, t=0.00s, 3280 KB]
Best heuristic value: 3 [g=9, 27 evaluated, 16 expanded, t=0.00s, 3280 KB]
Best heuristic value: 2 [g=10, 32 evaluated, 18 expanded, t=0.00s, 3280 KB]
Best heuristic value: 1 [g=11, 33 evaluated, 19 expanded, t=0.00s, 3280 KB]
Best heuristic value: 0 [g=12, 34 evaluated, 20 expanded, t=0.00s, 3280 KB]
Solution found!
Actual search time: 0.00s [t=0.00s]
unstack c e (1)
put-down c (1)
pick-up d (1)
stack d c (1)
unstack e b (1)
put-down e (1)
unstack b a (1)
stack b d (1)
pick-up e (1)
stack e b (1)
pick-up a (1)
stack a e (1)
Plan length: 12 step(s).
Plan cost: 12
Initial state h value: 8.
Expanded 21 state(s).
Reopened 0 state(s).
Evaluated 34 state(s).
Evaluations: 34
Generated 52 state(s).
Dead ends: 0 state(s).
Expanded until last jump: 12 state(s).
Reopened until last jump: 0 state(s).
Evaluated until last jump: 20 state(s).
Generated until last jump: 30 state(s).
Number of registered states: 34
Search time: 0.00s
Total time: 0.00s
Solution found.
Peak memory: 3280 KB


*****BENCHMARK 3*****

Translating prob10 from Movie domain -> makes output.sas file

max@ubuntu:~/Downloads/Downward/src$ translate/translate.py domain.pddl prob10.pddl
Parsing...
Parsing: [0.000s CPU, 0.003s wall-clock]
Normalizing task... [0.000s CPU, 0.000s wall-clock]
Instantiating...
Generating Datalog program... [0.000s CPU, 0.001s wall-clock]
Normalizing Datalog program...
Trivial rules: Converted to facts.
Normalizing Datalog program: [0.010s CPU, 0.002s wall-clock]
Preparing model... [0.000s CPU, 0.002s wall-clock]
Generated 16 rules.
Computing model... [0.000s CPU, 0.007s wall-clock]
291 relevant atoms
0 auxiliary atoms
291 final queue length
356 total queue pushes
Completing instantiation... [0.010s CPU, 0.002s wall-clock]
Instantiating: [0.020s CPU, 0.016s wall-clock]
Computing fact groups...
Finding invariants...
7 initial candidates
Finding invariants: [0.000s CPU, 0.001s wall-clock]
Checking invariant weight... [0.000s CPU, 0.000s wall-clock]
Instantiating groups... [0.000s CPU, 0.000s wall-clock]
Collecting mutex groups... [0.000s CPU, 0.000s wall-clock]
Choosing groups...
7 uncovered facts
Choosing groups: [0.000s CPU, 0.000s wall-clock]
Building translation key... [0.000s CPU, 0.000s wall-clock]
Computing fact groups: [0.000s CPU, 0.002s wall-clock]
Building STRIPS to SAS dictionary... [0.000s CPU, 0.000s wall-clock]
Building dictionary for full mutex groups... [0.000s CPU, 0.000s wall-clock]
Building mutex information...
Building mutex information: [0.000s CPU, 0.000s wall-clock]
Translating task...
Processing axioms...
Simplifying axioms... [0.000s CPU, 0.000s wall-clock]
Processing axioms: [0.000s CPU, 0.000s wall-clock]
Translating task: [0.000s CPU, 0.002s wall-clock]
1 effect conditions simplified
0 implied preconditions added
Detecting unreachable propositions...
0 operators removed
0 propositions removed
Detecting unreachable propositions: [0.000s CPU, 0.004s wall-clock]
Translator variables: 7
Translator derived variables: 0
Translator facts: 14
Translator goal facts: 7
Translator mutex groups: 0
Translator total mutex groups size: 0
Translator operators: 72
Translator axioms: 0
Translator task size: 173
Translator peak memory: 37480 KB
Writing output... [0.000s CPU, 0.001s wall-clock]
Done! [0.020s CPU, 0.033s wall-clock]


Preprocess prob10 > make output file

max@ubuntu:~/Downloads/Downward/src$ preprocess/preprocess < output.sas
Building causal graph...
The causal graph is acyclic.
7 variables of 7 necessary
0 of 0 mutex groups necessary.
72 of 72 operators necessary.
0 of 0 axiom rules necessary.
Building domain transition graphs...
solveable in poly time 0
Building successor generator...
Preprocessor facts: 14
Preprocessor derived variables: 0
Preprocessor task size: 173
Writing output...
done

Preform A* search with lmcut method on prob10 output file

max@ubuntu:~/Downloads/Downward/src$ ./fast-downward.py output --search "astar(lmcut())"
INFO     Running search.
INFO     search input: output
INFO     search executable: /home/max/Downloads/Downward/src/search/downward-release
INFO     search arguments: ['--search', 'astar(lmcut())', '--internal-plan-file', 'sas_plan']
reading input... [t=0.00s]
Simplifying transitions... done!
done reading input! [t=0.00s]
building causal graph...done! [t=0.00s]
packing state variables...done! [t=0.00s]
Variables: 7
Facts: 14
Bytes per state: 4
done initalizing global data [t=0.00s]
Conducting best first search with reopening closed nodes, (real) bound = 2147483647
Initializing landmark cut heuristic...
f = 7 [1 evaluated, 0 expanded, t=0.00s, 3148 KB]
Best heuristic value: 7 [g=0, 1 evaluated, 0 expanded, t=0.00s, 3148 KB]
Best heuristic value: 6 [g=1, 2 evaluated, 1 expanded, t=0.00s, 3148 KB]
Best heuristic value: 5 [g=2, 9 evaluated, 2 expanded, t=0.00s, 3148 KB]
Best heuristic value: 4 [g=3, 15 evaluated, 3 expanded, t=0.00s, 3148 KB]
Best heuristic value: 3 [g=4, 20 evaluated, 4 expanded, t=0.00s, 3148 KB]
Best heuristic value: 2 [g=5, 24 evaluated, 5 expanded, t=0.00s, 3148 KB]
Best heuristic value: 1 [g=6, 27 evaluated, 6 expanded, t=0.00s, 3148 KB]
Best heuristic value: 0 [g=7, 29 evaluated, 8 expanded, t=0.00s, 3148 KB]
Solution found!
Actual search time: 0.00s [t=0.00s]
get-cheese z1 (1)
get-chips c1 (1)
get-crackers k1 (1)
get-dip d1 (1)
get-pop p1 (1)
rewind-movie  (1)
reset-counter  (1)
Plan length: 7 step(s).
Plan cost: 7
Initial state h value: 7.
Expanded 9 state(s).
Reopened 0 state(s).
Evaluated 29 state(s).
Evaluations: 29
Generated 576 state(s).
Dead ends: 0 state(s).
Expanded until last jump: 0 state(s).
Reopened until last jump: 0 state(s).
Evaluated until last jump: 1 state(s).
Generated until last jump: 0 state(s).
Number of registered states: 29
Search time: 0.00s
Total time: 0.00s
Solution found.
Peak memory: 3148 KB
max@ubuntu:~/Downloads/Downward/src$ 


*****ROBOT BENCHMARK*****

Translate robot-domian and robot-prob into output.sas file

max@ubuntu:~/Downloads/Downward/src$ translate/translate.py robot-domain.pddl robot-prob.pddlParsing...
Warning: name clash between type and predicate 'object'.
Interpreting as predicate in conditions.
Parsing: [0.000s CPU, 0.002s wall-clock]
Normalizing task... [0.000s CPU, 0.000s wall-clock]
Instantiating...
Generating Datalog program... [0.000s CPU, 0.001s wall-clock]
Normalizing Datalog program...
Normalizing Datalog program: [0.000s CPU, 0.002s wall-clock]
Preparing model... [0.000s CPU, 0.001s wall-clock]
Generated 22 rules.
Computing model... [0.010s CPU, 0.001s wall-clock]
34 relevant atoms
34 auxiliary atoms
68 final queue length
76 total queue pushes
Completing instantiation... [0.000s CPU, 0.001s wall-clock]
Instantiating: [0.010s CPU, 0.009s wall-clock]
Computing fact groups...
Finding invariants...
6 initial candidates
Finding invariants: [0.000s CPU, 0.003s wall-clock]
Checking invariant weight... [0.000s CPU, 0.000s wall-clock]
Instantiating groups... [0.000s CPU, 0.000s wall-clock]
Collecting mutex groups... [0.000s CPU, 0.000s wall-clock]
Choosing groups...
4 uncovered facts
Choosing groups: [0.000s CPU, 0.000s wall-clock]
Building translation key... [0.000s CPU, 0.000s wall-clock]
Computing fact groups: [0.000s CPU, 0.004s wall-clock]
Building STRIPS to SAS dictionary... [0.000s CPU, 0.000s wall-clock]
Building dictionary for full mutex groups... [0.000s CPU, 0.000s wall-clock]
Building mutex information...
Building mutex information: [0.000s CPU, 0.000s wall-clock]
Translating task...
Processing axioms...
Simplifying axioms... [0.000s CPU, 0.000s wall-clock]
Processing axioms: [0.000s CPU, 0.000s wall-clock]
Translating task: [0.000s CPU, 0.001s wall-clock]
6 effect conditions simplified
0 implied preconditions added
Detecting unreachable propositions...
0 operators removed
2 propositions removed
Detecting unreachable propositions: [0.000s CPU, 0.001s wall-clock]
Translator variables: 5
Translator derived variables: 0
Translator facts: 10
Translator goal facts: 1
Translator mutex groups: 1
Translator total mutex groups size: 2
Translator operators: 5
Translator axioms: 0
Translator task size: 47
Translator peak memory: 37480 KB
Writing output... [0.000s CPU, 0.000s wall-clock]
Done! [0.010s CPU, 0.021s wall-clock]


Preprocess output.sas to output file

max@ubuntu:~/Downloads/Downward/src$ preprocess/preprocess < output.sasBuilding causal graph...
The causal graph is not acyclic.
5 variables of 5 necessary
0 of 1 mutex groups necessary.
5 of 5 operators necessary.
0 of 0 axiom rules necessary.
Building domain transition graphs...
solveable in poly time 0
Building successor generator...
Preprocessor facts: 10
Preprocessor derived variables: 0
Preprocessor task size: 45
Writing output...
done


Preform lazy greedy search on robot output file

max@ubuntu:~/Downloads/Downward/src$ ./fast-downward.py output --heuristic "hff=ff()" --heuristic "hcea=cea()" --search "lazy_greedy([hff, hcea], preferred=[hff, hcea])"
INFO     Running search.
INFO     search input: output
INFO     search executable: /home/max/Downloads/Downward/src/search/downward-release
INFO     search arguments: ['--heuristic', 'hff=ff()', '--heuristic', 'hcea=cea()', '--search', 'lazy_greedy([hff, hcea], preferred=[hff, hcea])', '--internal-plan-file', 'sas_plan']
reading input... [t=0.00s]
Simplifying transitions... done!
done reading input! [t=0.00s]
building causal graph...done! [t=0.00s]
packing state variables...done! [t=0.00s]
Variables: 5
Facts: 10
Bytes per state: 4
done initalizing global data [t=0.00s]
Conducting lazy best first search, (real) bound = 2147483647
Initializing FF heuristic...
Initializing additive heuristic...
Simplifying 11 unary operators... done! [11 unary operators]
Initializing context-enhanced additive heuristic...
Best heuristic value: 3/4 [g=0, 1 evaluated, 0 expanded, t=0.00s, 3148 KB]
Best heuristic value: 2/2 [g=1, 2 evaluated, 1 expanded, t=0.00s, 3148 KB]
Best heuristic value: 1/1 [g=2, 3 evaluated, 2 expanded, t=0.00s, 3148 KB]
Solution found!
Actual search time: 0.00s [t=0.00s]
movewithobj rm1 rm2 (1)
pickup obj1 rm2 (1)
movewithobj rm2 rm1 (1)
Plan length: 3 step(s).
Plan cost: 3
Initial state h value: 3/4.
Expanded 3 state(s).
Reopened 0 state(s).
Evaluated 4 state(s).
Evaluations: 8
Generated 5 state(s).
Dead ends: 0 state(s).
Search time: 0.00s
Total time: 0.00s
Solution found.
Peak memory: 3148 KB

