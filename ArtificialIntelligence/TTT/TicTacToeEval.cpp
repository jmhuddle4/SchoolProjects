//Max Huddleston
//CSC 542 Assignment 2
//24 March 2015
//last edited: 3/24 @ 1121a
#include <iostream>
#include <cstdlib>
#include <string>
using namespace std;

//global variables
int nrRows, nrCols, nrToWin, depth;
string player1Type, player2Type;
const int terminalSize = 40;
char board[terminalSize][terminalSize]; //windows terminal supports up tp 40x40
int cnt = 1; //move counter
int AIrow;
int AIcol;
int evalScore = -1; //initialize evaluation score
bool game = true;

void printBoard(int height, int width) {
	for(int i = 1; i <= height; i++) {
		for(int j = 1; j <= width; j++) {

			cout<<board[i][j];

			if (j < width) {
				cout<<"|";
			}
			else if (j == width && i < height) {
				cout<<endl;
				for(int k = 0; k < width*2; k++) {
					cout<<"_";
				}
				cout<<endl;
			}
			else if (j == width && i == height) {
				cout<<endl;
			}

			else {
				cout<<"board error";
			}
		} //end for width
	} //end for height
}

int checkWin(char selectChar, int selectRow, int selectCol, int needToWin) {
	char tempChar =  selectChar;
	int tempWin = needToWin;
	
	////check a row
	int nrRowsToWinCtr = 0;

	for(int i = 1; i <= nrCols; i++) { //for number of columns in selected ow
		if(board[selectRow][i] != tempChar) { //check each column's char
			if(i == nrCols) //if at last column and still no success...
				break;
		}
		else if(i <= (nrCols + 2 - tempWin)) //only check for nrToWin if there is room
		 {
			for(int j = 0; j < tempWin; j++) { //check nrToWin amount in a row
			    if (board[selectRow][i+j] == tempChar) { //resume at correct index
			        nrRowsToWinCtr++; //increase nrToWin counter every time the same char is next to eachother
			    }
			    else {
			        //not enough room for nrToWin
			        nrRowsToWinCtr = 0; //reset nrToWin counter
			        break; //if not nrToWin amount in a row, go to main loop again
			    }
			}
			if(nrRowsToWinCtr == tempWin) { //correct nrToWin in a row player wins
			    return 1; //won by row
			}	    
		}
	} //end check row


	////check a column
	int nrColsToWinCtr = 0;

	for(int i = 1; i <= nrRows; i++) { //for number of rows in a selected column
		if(board[i][selectCol] != tempChar) { //check each column's char
			if(i == nrRows) //if at last row and still no success...
				break;      
		}
		else if(i <= (nrRows + 2 - tempWin)) //only check for nrToWin if there is room
		 {
			for(int j = 0; j < tempWin; j++) { //check nrToWin amount in a row
			    if (board[i+j][selectCol] == tempChar) { //resume at correct index
			        nrColsToWinCtr++; //increase nrToWin counter every time the same char is next to eachother
			    }
			    else {
			        //not enough room for nrToWin
			        nrColsToWinCtr = 0; //reset nrToWin counter
			        break; //if not nrToWin amount in a row, go to main loop again
			    }
			}
			if(nrColsToWinCtr == tempWin) { //correct nrToWin in a row player wins
			    return 2; //won by column
			}			    
		}
	} //end check column


	////check diagonol 1 
	char tempD1Array[36];
	int nrD1ToWinCtr = 0;
    int d1Ctr = 0;
    //find distance to edge of board
    while (((selectRow - d1Ctr) > 1) && ((selectCol - d1Ctr) > 1)) { //will stop after finds closest edge
            d1Ctr++; //increase index
        }
    
    for(int j = 0; j < nrRows; j++) {
        tempD1Array[j] = board[(selectRow - d1Ctr) + j][(selectCol - d1Ctr) + j]; //store in array in order
    }
    
    //check diagonol 1 by checking tmpArray
	for(int i = 0; i <= nrRows; i++) { //diagonol can only be as long as the amount of rows
		if(tempD1Array[i] != tempChar) { //check each column's char
			if(i == nrRows) //if not equal, then loop again
				break;		//if at last row and still no success then break
		}
		else if(i <= (nrRows + 2 - tempWin)) { //only check for nrToWin if there is room
			for(int j = 0; j < tempWin; j++) { //check nrToWin amount in a row
			    if (tempD1Array[i+j] == tempChar) { //resume at correct index
			        nrD1ToWinCtr++; //increase nrToWin counter every time the same char is next to eachother
			    }
			    else {				  //if not equal to tempChar
			        nrD1ToWinCtr = 0; //reset nrToWin counter
			        break; //if not nrToWin amount in a row, go to main loop again
			    }
			}
			if(nrD1ToWinCtr == tempWin) {  //if correct nrToWin in a row player wins
			    return 3; //won by diag 1
			}
		} //end else if
	}//end check diagonol 1*/


	//check diagonol 2
	char tempD2Array[36];
	int nrD2ToWinCtr = 0;
    int d2Ctr = 0;
    
    while (((selectRow - d2Ctr) > 1) && ((selectCol + d2Ctr) < nrCols + 1)) {
            d2Ctr++; //increase index
        }
    
    for(int j = 0; j < nrRows; j++) {
        tempD2Array[j] = board[(selectRow - d2Ctr) + j][(selectCol + d2Ctr) - j];
    }
    
    //check diagonol 2 by checking tmpArray
	for(int i = 0; i < nrRows; i++) { //diagonol can only be as long as the amount of rows
		if(tempD2Array[i] != tempChar) { //check each column's char
			if(i == nrRows) //if at last row and still no success...
				break;
		}
		else if(i <= (nrRows + 2 - tempWin)) { //only check for nrToWin if there is room
			for(int j = 0; j < tempWin; j++) { //check nrToWin amount in a row
			    if (tempD2Array[i+j] == tempChar) { //resume at correct index
			        nrD2ToWinCtr++; //increase nrToWin counter every time the same char is next to eachother
			    }
			    else {
			        nrD2ToWinCtr = 0; //reset nrToWin counter
			        break; //if not nrToWin amount in a row, go to main loop again
			    }
			}
			if(nrD2ToWinCtr == tempWin) {  //correct nrToWin in a row player wins
			    return 4; //won by diag 2
			}
		} //end else if
	}//end check diagonol 2*/
		
	//if #of moves = #of spaces and no one has won - draw by default	
	if(cnt == nrRows*nrCols) {
		cout<<"It's a draw\n"<<endl;
		return 5;
	}
	
	return 0; //else return 0

} //end checkWin

int evalWinRows(char selChar, int selRow, int numToWin) { //selCol = 1 dummy var
	
	////for every element in every row			
	for(int k = 1; k <= nrCols; k++) { //only loop if evaluate has selCol = 1
		int selCharCtr = 0;
		int blankCtr = 0;

		//cout<<"eW board[selRow][k] = board["<<selRow<<"]["<<k<<"] = "<<board[selRow][k] <<endl;
		//cout<<"selRow: "<<selRow<<endl;
			if((board[selRow][k] == selChar) && k <= (nrCols + 2 - numToWin)) { //if elemet = char or blank space AND there's enough room
				//reset every iteration
				selCharCtr = 0;
				blankCtr = 0;
				
				for(int c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
					if(board[selRow][k+c] == selChar) {  
						selCharCtr++; 
					}
					else if(board[selRow][k+c] == 'E') {
						blankCtr++;
						AIrow = selRow;
						AIcol = k+c;
						//cout<<"evalWin line 209 k: "<<k<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
					}
				}

				if((selCharCtr == numToWin-2) && blankCtr == 1) {
					//cout<<" k: "<<k<<"  computer can win in this row with selChar preceding\n";
					//...report computer can win in this row
					return 1;
				}
				//else {
					//cout<<" k: "<<k<<"  cant win in this row with selChar preceding\n";
					// loop again
				//}
	
		} //end if E

		else if((board[selRow][k] == 'E') && k <= (nrCols + 2 - numToWin)) { //if = blankspace
			//reset every iteration
			selCharCtr = 0;
				
			for(int c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
				if(board[selRow][k+c] == selChar) {  
					selCharCtr++; 
				}
			}

			if(selCharCtr == numToWin-1) {
				//cout<<" k: "<<k<<"  computer can win in this row with E preceding\n";
				AIrow = selRow;
				AIcol = k;
				//cout<<"evalWin line 239 k: "<<k<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
				//...report computer can win in this row
				return 1;
			}
			//else {
				//cout<<" k: "<<k<<"  cant win in this row with E preceding\n";
				//loop again
			//}
		} //end if = selChar 

		if (k == nrCols)
			return 0;

	}//end for every element in selected row

	return 0; //if no success return 0
} //end evalWinRows


int evalWinCols(char selChar, int selCol, int numToWin) {

	////for every element in selected column			
	for(int k = 1; k <= nrRows; k++) { //only loop if evaluate had selRow = 1
		int selCharCtr = 0;
		int blankCtr = 0;
		//selCol = 1; //reset selecet column
		//cout<<"eW board[selRow][k] = board["<<selRow<<"]["<<k<<"] = "<<board[selRow][k] <<endl;
		//cout<<"selCol: "<<selCol;
			if((board[k][selCol] == selChar) && k <= (nrRows + 2 - numToWin)) { //if elemet = char or blank space AND there's enough room
				//reset every iteration
				selCharCtr = 0;
				blankCtr = 0;
				
				for(int c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
					if(board[k+c][selCol] == selChar) {  
						selCharCtr++; 
					}
					else if(board[k+c][selCol] == 'E') {
						blankCtr++;
						AIrow = k+c;
						AIcol = selCol;
						//cout<<"evalWin line 274 k: "<<k<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
					}
				}

				if((selCharCtr == numToWin-2) && blankCtr == 1) {
					//cout<<" k: "<<k<<"  computer can win in this column with selChar preceding\n";
					//...report computer can win in this row
					return 1;
				}
				//else {
					//cout<<" k: "<<k<<"  cant win in this column with selChar preceding\n";
					//cout<<" k: "<<k<<"  selCharCtr: "<<selCharCtr<<" blankCtr: "<<blankCtr<<"\n";
					// loop again
				//}
	
		} //end if E

		else if((board[k][selCol] == 'E') && k <= (nrRows + 2 - numToWin)) { //if = blankspace
			//reset every iteration
			selCharCtr = 0;
				
			for(int c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
				if(board[k+c][selCol] == selChar) {  
					selCharCtr++; 
				}
			}

			if(selCharCtr == numToWin-1) {
				//cout<<" k: "<<k<<"  computer can win in this column with E preceding\n";
				AIrow = k;
				AIcol = selCol;
				//cout<<"evalWin line 304 k: "<<k<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
				//...report computer can win in this row
				return 1;
			}
			//else {
				//cout<<" k: "<<k<<"  cant win in this column with E preceding\n";
				//loop again
			//}
		} //end if = selChar 

		if (k == nrCols)
			return 0;

	} //end for every element in selected column

return 0;
} //end evalWinCols



int evalWinDiag(char selChar, int numToWin) {
	char tempArray[terminalSize];
	int bound = nrCols - nrToWin+1; //last row and column where diagonal could start
	int numDiags = (bound*2)-1;
	//cout<<"nrToWin: "<<nrToWin<<endl;
	int j = 1; int k = 1;
	//cout<<"bound: "<<bound<<endl;
	int index = bound-1; 
	//cout<<"index: "<<index<<endl;
	int diagLimit = nrToWin;
	//cout<<"diagLimit: "<<diagLimit<<endl;
	//cout<<"numDiags: "<<numDiags<<endl;

	//cout<<"eval diag 1\n";
	//store each diag1 to temparray
	for(int i = 0; i < numDiags; i++) {
		//cout<<"i: "<<i<<"\n";

		while(j <= diagLimit) {
			tempArray[j] = board[j][j+index];
			//cout<<"temparray["<<j<<"]: "<<tempArray[j]<<endl;
			/////eval evalWinDiags////
			int selCharCtr = 0;
			int blankCtr = 0;
			int c;

				if((board[j][j+index] == selChar)) { //if elemet = char or blank space AND there's enough room
					//reset every iteration
					selCharCtr = 0;
					blankCtr = 0;
				
					for(c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
						if(board[j+c][j+index+c] == selChar) {  
							//cout<<"432 board[j+c][j+index+c]: "<<board[j+c][j+index+c]<<endl;
							selCharCtr++; 
						}
						else if(board[j+c][j+index+c] == 'E') {
							blankCtr++;
							AIrow = j+c;
							AIcol = j+index+c;
							//cout<<"evalDiagsWin line 439 k: "<<k<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
						}
					}

					if((selCharCtr == numToWin-2) && blankCtr == 1) {
						//cout<<"444 j: "<<j<<"  computer can win in this diag with selChar preceding\n";
						//...report computer can win in this row
						return 1;
					}
					else {
						//cout<<"449 j: "<<j<<"  cant win in this diag with selChar preceding\n";
						// loop again
					}
	
				} //end if E

				else if((board[j][j+index] == 'E')) { //if = blankspace
					//reset every iteration
					selCharCtr = 0;
				
					for(c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
						if(board[j+c][j+index+c] == selChar) {  
							selCharCtr++; 
						}
					}

					if(selCharCtr == numToWin-1) {
						//cout<<" j: "<<j<<"  computer can win in this diag with E preceding\n";
						AIrow = j+c;
						AIcol = j+index;
						//cout<<"evalDiagsWin line 469 j: "<<j<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
						//...report computer can win in this row
						return 1;
					}
					else {
						//cout<<" j: "<<j<<"  cant win in this diag with E preceding\n";
						//loop again
					}
				} //end if = selChar 

				////end evalWimDiags
				j++;
			} //end while j < diagLimit
			index--; //column index increases while row index increases
			j=1; //reset
			if(diagLimit < nrCols) { //top of middle diag
				diagLimit++;
			}
			else if(diagLimit == nrCols) { //below middle diag
				k++;
				j = k;
			}

		//cout<<endl;

	} //end for #diags in diag1


	//for #diags in diag2
	//cout<<"eval diag 2\n";
	j = 1; k = 1; //reset
	index = bound+1; //reset
	diagLimit = nrToWin; //reset
	//cout<<"index: "<<index<<endl;

	//store each diag2 to temparray
	for(int i = 0; i < numDiags; i++) {
		//cout<<"i: "<<i<<"\n";

		while(j <= diagLimit) {
			tempArray[j] = board[j][index-j];
			//cout<<"temparray["<<j<<"]: "<<tempArray[j]<<endl;
			////evalDiagsWin////
			int selCharCtr = 0;
			int blankCtr = 0;
			int c;

				if((board[j][index-j] == selChar)) { //if elemet = char or blank space AND there's enough room
					//reset every iteration
					selCharCtr = 0;
					blankCtr = 0;
				
					for(c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
						if(board[j+c][index-j+c] == selChar) {  
							//cout<<"527 board[j+c][index-j+c]: "<<board[j+c][index-j+c]<<endl;
							selCharCtr++; 
						}
						else if(board[j+c][index-j+c] == 'E') {
							blankCtr++;
							AIrow = j+c;
							AIcol = index-j+c;
							//cout<<"evalDiagsWin line 533 j: "<<j<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
						}
					}

					if((selCharCtr == numToWin-2) && blankCtr == 1) {
						//cout<<"539 j: "<<j<<"  computer can win in this diag with selChar preceding\n";
						//...report computer can win in this row
						return 1;
					}
					//else {
						//cout<<"449 j: "<<j<<"  cant win in this diag with selChar preceding\n";
						// loop again
					//}
	
				} //end if selChar

				else if((board[j][index-j] == 'E')) { //if = blankspace
					//reset every iteration
					selCharCtr = 0;
				
					for(c = 1; c <= numToWin-1; c++) { //and next numToWin-1 chars = blank or selChar...
						if(board[j+c][index-j+c] == selChar) {  
							selCharCtr++; 
						}
					}

					if(selCharCtr == numToWin-1) {
						//cout<<" j: "<<j<<"  computer can win in this diag with E preceding\n";
						AIrow = j;
						AIcol = index-j;
						//cout<<"evalDiagsWin line 564 j: "<<j<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
						//...report computer can win in this row
						return 1;
					}
					//else {
						//cout<<"569 j: "<<j<<"  cant win in this diag with E preceding\n";
						//loop again
					//}
				} //end if = E

			////end evalDiagsWin////
			j++;
		}
		index++;
		j=1; //reset
		if(diagLimit < nrCols) { //top of middle diag
			diagLimit++;
		}
		else if(diagLimit == nrCols) { //below middle diag
			k++;
			j = k;
		}

		//for(int k = 1; k < 6; k++) 
			//cout<<"temparray["<<k<<"] = "<<tempArray[k]<<"\n";

		//cout<<endl;

	} //end for each # diag2


return 0;
} //end evalWinDiags


int evalCenter() {
	int centerRow;
	int centerCol;

	if(nrRows%2 == 1) {//if odd 
		centerRow = (nrRows/2) + 1; //round up
		AIrow = centerRow;
	}
	else if(nrRows%2 == 0) { //if even
		centerRow = nrRows/2;
		AIrow = centerRow;
	}
	if(nrCols%2 == 1) { //if odd
		centerCol = (nrCols/2) + 1; //round up
		AIcol = centerCol;
	}
	else if(nrCols%2 == 0) { //if even
		centerCol = nrCols/2; 
		AIcol = centerCol;
	}

	//cout<<"evalCenter line 345 "<<" AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
	
	if(board[centerRow][centerCol]=='E')
		return 1;
	else
		return 0; //if no success return 0
}//end evalCenter


int evalEmptyRows(char selChar, int selRow, int numToWin) {
	//for every element in selected row	
	int blankCtr = 0; //reset every iteration
	for(int k = 1; k <= nrCols; k++) { 
		//cout<<"eES board[selRow][k] = board["<<selRow<<"]["<<k<<"] = "<<board[selRow][k] <<endl;
		if(board[selRow][k] == 'E') { //if element = blank space
			blankCtr++;
		}	
	} //end for		

	if(blankCtr == nrCols) { //if every spot in row is blank
		//cout<<"this row is empty\n"; //report row is empty
		if(nrRows%2 == 1) {//if odd 
			AIcol = (nrRows/2) + 1; //round up
			AIrow = selRow;
		}
		else if(nrRows%2 == 0) { //if even
			AIcol = nrRows/2;
			AIrow = selRow;
		}
		return 1; //if success return 1
	}

	//cout<<"evalEmptyRow line 383  AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;
		
return 0;//if no success return 0		
}//end evalEmptyRows

	 



int evalEmptyCols(char selChar, int selCol, int numToWin) {
	//for every element in selected row	
	int blankCtr = 0; //reset every iteration
	for(int k = 1; k <= nrRows; k++) { 
		
		if(board[k][selCol] == 'E') { //if element = blank space
			blankCtr++; //increase blank counter
		}	
	} //end for		

	if(blankCtr == nrRows) { //if every spot in column is blank
		//cout<<"this column is empty\n"; //report row is empty
		if(nrRows%2 == 1) {//if odd 
			AIcol = selCol; //round up
			AIrow = (nrRows/2) + 1;
		}
		else if(nrRows%2 == 0) { //if even
			AIcol = selCol;
			AIrow = nrRows/2;
		}
		return 1; //if success return 1
	}

	//cout<<"evalEmptyCol line 416  AIrow: "<<AIrow<<" AIcol: "<<AIcol<<endl;

	return 0; //if no success return 0
}//end evalEmptyCols


int evaluate(char selChar, int numToWin) {
	/*EVALUATION FUNCTION 
	Evaluate goodness of the board
	Listed in order of priority and assigned a score
	1) CPU can win:						8pts
	2) CPU can block opp:				7pts
	3) CPU can fork:					6pts
	4) CPU can block opp fork:			5pts
	5) CPU can go in center:			4pts
	6) CPU can go to opposite corner:	3pts
	7) CPU can go to next empty corner: 2pts
	8) CPU can go to empty side:		1pt
	9) CPU goes to next open space:		0pts
	**SOURCE = www.demo.ksankaran.com
	*/

	if(cnt == nrRows*nrCols) {
		cout<<"STOP\n";
		game = false;
	}
	
	//evaluate win
	//check rows
	//cout<<"EVALWIN - CHECK ROWS\n";
	for(int i = 1; i <= nrCols; i++) {
		if (evalWinRows(selChar, i, numToWin) == 1) {
			evalScore = 8;
			return evalScore;
		}
	}
	//cout<<"EVALWIN - CHECK COLS\n";
	//check columns
	for(int i = 1; i <= nrRows; i++) {
		if (evalWinCols(selChar, i, numToWin) == 1) {
			evalScore = 8;
			return evalScore;
		}
	}

	//check diags for nxn size board
	if(nrCols == nrRows) { 
		//cout<<"\nEVALWIN - CHECK DIAGS\n";
		if(evalWinDiag(selChar, numToWin) == 1) {
			evalScore = 8;
			return evalScore;
		}
	}

	//check diags for nxm size board


	//evaluate block
	char oppChar;
	if(selChar == 'o')
		oppChar = 'x';
	else if(selChar == 'x')
		oppChar = 'o';

	//cout<<"EVALBLOCK - CHECK ROWS\n";
	//check rows
	for(int i = 1; i <= nrRows; i++) {
		if (evalWinRows(oppChar, i, numToWin) == 1) { //CPU can block
			evalScore = 7;
			return evalScore;
		}
	}
	//cout<<"EVALBLOCK - CHECK COLS\n";
	//check columns
	for(int i = 1; i <= nrRows; i++) {
		if (evalWinCols(oppChar, i, numToWin) == 1) { //CPU can block
			evalScore = 7;
			return evalScore;
		}
	}

	//cout<<"EVALCENTER\n";
	//check center
	if(evalCenter()==1) {
		evalScore = 5;
		return evalScore;
	}

	//evaluate empty side
	//cout<<"EVALEMPTYROWS\n";
	//check rows
	for(int i = 1; i <= nrRows; i++) {
		if (evalEmptyRows(selChar, i, numToWin) == 1) {
			evalScore = 1;
			return evalScore;
		}
	}
	//cout<<"EVALEMPTYCOLS\n";
	//check columns
	for(int i = 1; i <= nrRows; i++) {
		if (evalEmptyCols(selChar, i, numToWin) == 1) {
			evalScore = 1;
			return evalScore;
		}
	}

	evalScore = 0;
	return evalScore; //if nothing else go to next spot available

} //end evaluate


void AIturn(char selChar, int& selRow, int& selCol, int numToWin) {

	bool breakFlag = true;
	cout<<"------- AI turn evaluatin now ------\n";
	evaluate(selChar, numToWin);
	cout<<"------- AI turn done evaluating --------\n";

	if(evalScore == 8) {
		selRow = AIrow;
		selCol = AIcol;
	}
	else if(evalScore == 7) {
		selRow = AIrow;
		selCol = AIcol;
	}
	else if(evalScore == 1) {
		selRow = AIrow;
		selCol = AIrow;
	}
	
	else if(evalScore == 0){ //next available
		for(int i = 1; i <= nrRows && breakFlag; i++) {
			for(int j = 1; j <= nrCols && breakFlag; j++) {
				if(board[i][j] == 'E') {
					selRow = i;
					selCol = j;
					cout<<"nextAvail line 541 Irow: "<<i<<" Jcol: "<<j<<endl;
					breakFlag = false;
				}
			}
		}
	}
}


/*Alpha-Beta Pruning
AlphaBeta(state, depth, alpha, beta, maxPlayer) {
	int state; //number of moves left
	//int play; //check score if play move at board[i][j]
	int alpha;
	int beta;
	int val;
	bool maxPlayer;
	bool breakFlag1 = true;
	bool breakFlag2 = true;
    if (cnt == nrRows*nrCols || state == -1):
        return evaluate(selChar, nrToWin);
    if (maxPlayer) {
        //for each possible play from state
		val = -999999;
		for(int i = 0; i < nrRows && breakFlag1; i++) {
			for(int j = 0; j< nrCols && breakFlag1; j++) {
				val = max(alpha, AlphaBeta(play, depth-1, Alpha, Beta, !(maxPlayer));
				alpha = max(alpha, val);
				if (Beta >= alpha):
					breakFlag1 = false;
			}
		}
	} //end if maxPlayer
    return val;
    else { //opponent
    //for each possible play from state
		val = 999999;
		for(int i = 0; i < nrRows && breakFlag2; i++) {
			for(int j = 0; j< nrCols && breakFlag2; j++) {
				val = min(alpha, AlphaBeta(play, depth-1, Alpha, Beta, !(maxPlayer));
				beta = min(alpha, val);
				if (beta <= alpha):
					breakFlag2 = false;
			}
		}
	} //end if opponent
    return val;
End Alpha-Beta Pruning*/


/*
*Main function*
*uses while loop to check validty of user input
*initializes board with empty characters
*prints the initialized board
*initializes variables needed for main function
*uses a while loop until game is finished
*in the while loop:
	*determines who's turn it is
	*what type of player they are
	*depening on type of player, it gets user input or input from AIturn function
	*checks to make sure player input is valid
	*checks board to see if a player won/or if draw
	*changes player turn
*exits
*/

int main() {
	//get user input
	bool userInput = true;
	int validtyCtr = 0;

	while(userInput) {
		cout << "Enter nrCols nrRows nrToWin player1Type player2Type depth"<<endl;
		cin >> nrRows>>nrCols>>nrToWin>>player1Type>>player2Type>>depth;

		if(nrToWin > nrRows || nrToWin > nrCols) {
			cout<<"nrToWin cannot be larger than nrCols or nrRows"<<endl;
		}
		else if(nrToWin <= nrRows && nrToWin <= nrCols) {
			validtyCtr++;
		}

		if(nrCols > terminalSize || nrRows > terminalSize) {
			cout<<"nrRows or nrCols cannot be large than terminal size"<<endl;
		}
		else if(nrCols <= terminalSize && nrRows <= terminalSize) {
			validtyCtr++;
		}

		if(((player1Type.compare("AI") == 0) || 
			(player1Type.compare("human") == 0)) &&
			((player2Type.compare("AI") == 0) ||
			(player2Type.compare("human") == 0))) {
			 
			validtyCtr++;
		}
		//else
			//cout<<"Player type must be 'AI' or 'human'"<<endl;

		if(depth > nrRows*nrCols) {
			cout<<"depth must be less than # of spaces on board"<<endl;
		}
		else if(depth <= nrRows*nrCols) {
			validtyCtr++;
		}

		if(validtyCtr == 4)
			userInput = false;
		else {
			cout<<"Make sure Player type is 'AI' or 'human'"<<endl;
			validtyCtr = 0; //reset
		}

		//loop again until everything is valid
	} //end while user input


	//fill board with empty characters (blank spaces)
	for(int i = 1; i <= nrCols; i++) {
		for(int j = 1; j <= nrRows; j++) {
			board[i][j] = 'E';
		}
	}

	printBoard(nrCols, nrRows);

	//initialize
	int inRow = 0, inCol = 0; 
	bool o_turn = true; //o goes first
	bool breakFlag = true;
	char tempChar;
	int decision = 0;

	////////////////////////GAME//////////////////////////////////////
	while (game) { //while still spaces left

		if(o_turn) 
			tempChar = 'o'; //player 1
		else if(!o_turn)
			tempChar = 'x'; //player 2

		if((player1Type.compare("AI") == 0) && o_turn) { //if p1 is AI and it's his turn
			AIturn(tempChar, inRow, inCol, nrToWin); //get input from AI function
			inRow = AIrow;
			inCol = AIcol;
		}
		else if((player1Type.compare("human") == 0) && o_turn){ //if p1 is human and its their turn 
			cout<<"Enter row number and column number"<<endl;
			cin>>inRow>>inCol;
		}

		if((player2Type.compare("AI") == 0) && !o_turn) {
			AIturn(tempChar, inRow, inCol, nrToWin); //get input from AI function
			inRow = AIrow;
			inCol = AIcol;
		}
		else if((player2Type.compare("human") == 0) && !o_turn){ //human
			cout<<"Enter row number and column number"<<endl;
			cin>>inRow>>inCol;
		}

		//debug
		//cout<<"============Evaluating agian=============\n";
		//evaluate(tempChar, nrToWin);
		//cout<<"=========End Evaluating agian=============\n";
		cout<<"evalScore = "<<evalScore<<endl;
		//cout<<"AIrow: "<<AIrow<<" AIcol: "<<AIcol<<" inRow: "<<inRow<<" inCol: "<<inCol<<endl;


		if (inRow < 1 || inRow > nrCols || inCol < 1 || inCol > nrRows) {
			cout<<"\nInvalid input, try again\n";
			//break;////////////////////////////////////////////////////////////////////////////////////TEST
			cnt--; //move counter does not increase 
			if(o_turn)			//and player does not change b/c
				o_turn = false; //this player change code is at end of main too
			else if(!o_turn)
				o_turn = true;
		}
		else if(board[inRow][inCol] == 'x' || board[inRow][inCol] == 'o') {
			//prevent AI from doing infinite loop
			if((player1Type.compare("AI") == 0) && o_turn) { 
				cout<<"P1 going to next avail\n";
				for(int i = 1; i <= nrRows && breakFlag; i++) {
					for(int j = 1; j <= nrCols && breakFlag; j++) {
						if(board[i][j] == 'E') {
							board[i][j] = tempChar;
							breakFlag = false;
						}
					}
				}
			}
			else if((player2Type.compare("AI") == 0) && !o_turn) { 
				cout<<"P2 going to next avail\n";
				for(int i = 1; i <= nrRows && breakFlag; i++) {
					for(int j = 1; j <= nrCols && breakFlag; j++) {
						if(board[i][j] == 'E') {
							board[i][j] = tempChar;
							breakFlag = false;
						}
					}
				}
			}
			//end prevent AI from foing infinite loop
			//if human
			else if (player1Type.compare("human") == 0) {
				cout<<"Hey "<<tempChar<<" you can't go here  inRow: "<<inRow<<" inCol: "<<inCol<<"\n";
				cnt--; //move counter does not increase 
				if(o_turn)          //and player does not change
					o_turn = false; 
				else if(!o_turn)
					o_turn = true;
			}
			else if (player2Type.compare("human") == 0){
				cout<<"Hey "<<tempChar<<" you can't go here  inRow: "<<inRow<<" inCol: "<<inCol<<"\n";
				cnt--; //move counter does not increase 
				if(o_turn)          //and player does not change
					o_turn = false; 
				else if(!o_turn)
					o_turn = true;
			}
			else {
				cout<<"board position was replaced\n";
				board[inRow][inCol] = tempChar;
			}
		} //end if board position is taken

		else { //if not taken
			board[inRow][inCol] = tempChar; //input selection
			//cout<<"Not taken going here: "<<inRow<<" "<<inCol<<endl;
		}

		decision = checkWin(tempChar, inRow, inCol, nrToWin);

		if (decision == 1 || decision == 2 || decision == 3 || decision == 4) {
			printBoard(nrCols, nrRows);
			cout <<" "<<tempChar<<" has won the game!\n";
			break;
		}
		else if (decision == 5) {
			printBoard(nrCols, nrRows);
			cout<<"It's a draw\n";
			break;
		}

		
		if(o_turn)
			o_turn = false;
		else if(!o_turn)
			o_turn = true;

		cnt++; //# of moves
		//cout<<endl;
		printBoard(nrCols, nrRows); //reprint after every input
	}

	cout << "Enter 0 to terminate.\n";
	char terminate;
	cin >> terminate;
	if(terminate == '0')
		exit;

	return 0;

}
