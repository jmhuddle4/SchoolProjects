Max Huddleston
ID: 2422320
email: 20huddleston@cardinalmail.cua.edu

CSC 542
Assignment 2
24 March 2015

Artificially Intelligent Tic Tac Toe by Max Huddleston

Run on visual studio

OR

Compile on linux:
gcc TTT.cpp
./a.out 3 3 3 AI human 3


Notes
The game board works very well and take input dimensions of up to 40x40
The game knows how to detect a winner on an n X n size board or a n x m size board
Human vs Human works very well
AI vs Human on 3x3 never loses
AI vs Human on nxn with n to win never loses
AI vs human on nxn with nrToWin<n doesn't win very oftern
AI uses an evaluation function which to pick its next move.
Evaluation function evaluates the board and returns a score
Not all elements of evaluation function were implemented
Pseudo code for Alpha-Beta search is written but not implemented
