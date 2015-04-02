//Max Huddleston
//CSC 542 
//Assignment 1
//10 February 2015
package aimaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BasicStroke;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class GMaze extends JPanel 
{
    static final int WALL_LEFT = 1;
    static final int WALL_RIGHT= 2;
    static final int WALL_UP   = 4;
    static final int WALL_DOWN = 8;
    
    protected int   m_nrRows;
    protected int   m_nrCols;
    protected int[] m_cells;
    protected int[] m_path;

    public void readMaze(String fname)
    {
	int r, c, id, nrLines;
	String msg;
   
	try
	{
	    Scanner sc = new Scanner(new File(fname));
	    
	    m_nrRows = sc.nextInt();
	    m_nrCols = sc.nextInt();
	    m_cells = new int[m_nrRows * m_nrCols];
	    for(int i = m_nrRows * m_nrCols - 1; i >= 0; --i)
		m_cells[i] = 0;

	    nrLines = sc.nextInt();
	    
	    for(int j = 0; j < nrLines; ++j)
	    {
		r  = sc.nextInt();
		c  = sc.nextInt();
		id = r * m_nrCols + c;
		msg= sc.next();
		for(int i = 0; i < msg.length(); ++i)
		    if(msg.charAt(i) == 'L')
			m_cells[id] |= WALL_LEFT;
		    else if(msg.charAt(i) == 'R')
			m_cells[id] |= WALL_RIGHT;
		    else if(msg.charAt(i) == 'U')
			m_cells[id] |= WALL_UP;
		    else if(msg.charAt(i) == 'D')
			m_cells[id] |= WALL_DOWN;
	    }
	}
	catch(FileNotFoundException e)
	{
            System.out.println("Can't find maze file. "+e.getMessage());
	}
    }
    
    public void readPath(String fname)
    {
	int r, c, id, nrLines;
	String msg;
   
	try
	{
	    Scanner sc = new Scanner(new File(fname));
	    
	    nrLines = sc.nextInt();
	    m_path = new int[nrLines];
	    
	    System.out.println("reading " + nrLines + " lines");
	    
	    for(int j = 0; j < nrLines; ++j)
	    {
		r  = sc.nextInt();
		c  = sc.nextInt();
		m_path[j] = r * m_nrCols + c;
	    }
	}
	catch(FileNotFoundException e)
	{
            System.out.println("Can't find path file. "+e.getMessage());
	}
    }
    
    protected int getOffset()
    {
	return 5;
    }
      
    protected int getCellWidth()
    {
	return (int) ((getWidth() - 2.0 * getOffset()) / m_nrCols);
    }

    protected int getCellHeight()
    {
	return (int) ((getHeight() - 2.0 * getOffset()) / m_nrRows);
    }
    
    protected void drawMaze(Graphics2D g)
    {
	if(m_cells == null)
	    return;
	
	int w  = getCellWidth();
	int h  = getCellHeight();
	
	g.setColor(Color.BLUE);	
	
	for(int i = 0; i < m_nrRows * m_nrCols; ++i)
	{
	    int r = i / m_nrCols;
	    int c = i % m_nrCols;
	    
	    if((m_cells[i] & WALL_LEFT) != 0)
		g.drawLine(getOffset() + w * c, getOffset() + r * h, getOffset() + w * c, getOffset() + (r + 1) * h);
	    if((m_cells[i] & WALL_RIGHT) != 0)
		g.drawLine(getOffset() + w * (c+1), getOffset() + r * h, getOffset() + w * (c+1), getOffset() + (r + 1) * h);
	    if((m_cells[i] & WALL_UP) != 0)
		g.drawLine(getOffset() + w * c, getOffset() + r * h, getOffset() + w * (c+1), getOffset() + r * h);
	    if((m_cells[i] & WALL_DOWN) != 0)
		g.drawLine(getOffset() + w * c, getOffset() + (r+1) * h, getOffset() + w * (c+1), getOffset() + (r+1) * h);
	}
    }
    
    protected void drawPath(Graphics2D g)
    {
	if(m_path == null)
	    return;
	
	int w  = getCellWidth();
	int h  = getCellHeight();
	
	g.setColor(Color.RED);	
	
	for(int i = 1; i < m_path.length; ++i)
	{
	    int rp = m_path[i - 1] / m_nrCols;
	    int cp = m_path[i - 1] % m_nrCols;
	    int r  = m_path[i] / m_nrCols;
	    int c  = m_path[i] % m_nrCols;
	    
	    g.drawLine(getOffset() + (int) ((cp + 0.5) * w), 
		       getOffset() + (int) ((rp + 0.5) * h), 
		       getOffset() + (int) ((c + 0.5) * w), 
		       getOffset() + (int) ((r + 0.5) * h));
	}
    }

    public void paint(Graphics g) 
    {
	Graphics2D g2 = (Graphics2D)g;
	g2.setStroke(new BasicStroke(3.0f));
	drawMaze(g2);
	drawPath(g2);
    }

    public static void main(String[] args) throws IOException , FileNotFoundException
    {      
        ////Write Maze Function////
        int numCells;
        int nrCols, nrRows;
        int rstart, cstart, rend, cend;
        String method, mazeFile, pathFile;
        //**change your filepath here**//
        String filepath = new String("C:/Users/Max/Documents/AIMaze/src/aimaze/");
        
        Scanner input = new Scanner(System.in); //input = keyboard
        System.out.println("Enter nrRows nrCols maze.txt");
        nrRows = input.nextInt();
        nrCols = input.nextInt();
        mazeFile = input.next();
        System.out.println("Enter method rstart cstart rend cend path.txt");
        method = input.next();
        rstart = input.nextInt();
        cstart = input.nextInt();
        rend = input.nextInt();
        cend = input.nextInt();        
        pathFile = input.next();
        
        //example input
        //3 3 maze.txt [ENTER]
        //dfs 0 0 2 2 path.txt [ENTER]
        
        numCells = nrCols*nrRows;
        
        ////randomly generate maze
        int cellWallNum; //number of walls for each cell
        char cellWallSide = 'L'; //initialize
        char[] cellWalls = {'L', 'R', 'U', 'D'}; //which sides will be generated 
        boolean[] visitedCellWalls = {false, false, false, false}; //don't use same wall twice
        
        //for random amount from 1 to 3
        Random randomChar = new Random();//for random wall generation
        int randomCellWall;
        String[] wallSides = new String[numCells+1]; //allocate enough indexes for every cell
        String temp = ""; //initialize temp index of wall side array
        
        for(int i = 0; i<numCells; i++) {
            //System.out.println("Cell# "+i); 
            //**Uncomment below for original random creation algorithm**//
            //cellWallNum = randomNum.nextInt(3)+1;//generates random num of cell walls from 1 to 3
            ////maze is too hard to solve if more than 1 wall per cell////
            cellWallNum = 1; //comment this out for original alogorithm
            //System.out.println("#cell walls: "+cellWallNum);
            
            for(int j = 0; j<cellWallNum; j++) //pick any amount/combination of L, R, U or D
            {   //one side for each wall
                randomCellWall = randomChar.nextInt(4); //generate random index  for cell wallside array 0-3 

                while(visitedCellWalls[randomCellWall] == true) { //if generated before 
                    randomCellWall = randomChar.nextInt(4);   //try again            
                }            

                cellWallSide = cellWalls[randomCellWall]; //set cell wall side to random side
                visitedCellWalls[randomCellWall] = true;  //set index of side array to visited
                temp += cellWallSide;
            } //end random cell wall generator
            
            wallSides[i] = temp; //store array of wall sides for each cell
            //System.out.println("cell walls: "+temp); //test
            temp = ""; //reset temp
            
            
            for(int k = 0; k < 4; k++) {
                visitedCellWalls[k] = false; //reset visited array
            }
        } //end random maze generator
        
        //initialize 2 arrays
        int row[] = new int[nrRows+1];
        int col[] = new int[nrCols+1];
        //fill with arbitrary values
        for(int i = 0; i < nrRows; i++)
            row[i] = i;
        for(int i = 0; i < nrCols; i++)
            col[i] = i;
        
        //initialize 2D char array to hold wall values
        char cells[][] = new char[nrRows+1][nrCols+1];
        
        //write random maze to file
        FileWriter fw = new FileWriter("C:/Users/Max/Documents/AIMaze/src/aimaze/maze.txt", false); //true to append
        PrintWriter pw = new PrintWriter(fw);
        int x = 0,y = 0,ctr = 0; //initialze counters
        
        //System.out.print(nrRows+" "+nrCols+"\n"+numCells+"\n");
        pw.print(nrRows+" "+nrCols+"\n"+numCells+"\n");
        
        while(ctr < numCells) {
            pw.print(row[x]+" "+col[y]+" "+wallSides[ctr]+"\n");
            //System.out.print(row[x]+" "+col[y]+" "+wallSides[ctr]+"\n");
            cells[x][y] = wallSides[ctr].charAt(0); 
            //System.out.println(x+","+y+": "+cells[x][y]);
            
            y++; //increase column index
            if(y > nrCols - 1){  //if column ctr is bigger than column index
                x++; //increase row index
                y = 0; //reset column
            } //end if
            
            ctr++;
        }//end while
        
        pw.close(); //close file
        
        ////End Write Maze Function
        
        
        ////Write Path
        FileWriter pfw = new FileWriter("C:/Users/Max/Documents/AIMaze/src/aimaze/path.txt", false);
        PrintWriter pathW = new PrintWriter(pfw);
        
        //set starting point
        int rowIndex = rstart;
        int colIndex = cstart;
        //initialize 2D boolean array of visited vells
        boolean[][] visitedCells = new boolean[nrRows+1][nrCols+1];
        for(int i = 0; i < nrRows; i++) {
            for(int j = 0; j<nrCols; j++)
                visitedCells[i][j] = false;
        }
        
        visitedCells[rstart][cstart] = true; //starting position = visited

        //2 stacks for DFS: 1 for inital storage one to reverse pop out of
        Stack pathStack = new Stack();
        Stack pathStack2 = new Stack();
        
        //Queue for BFS
        Queue<String> pathQ = new LinkedList<String>();
        
        boolean truth = true;
        int pathCtr = 0;
        /////////////DFS///////////////////
        if(method.equals("dfs")) {
        
        pathStack.push(rstart); //push starting point in stack
        pathStack.push(cstart);    
            
        try{
        
        while(truth) //while current cell != goal
        {
            //DFS priority check D, R, U, L in order (up and down)
            if(cells[rowIndex][colIndex] != 'D' & //current cell wall != D 
               rowIndex != nrRows-1 & //not at edge of maze     
               cells[rowIndex+1][colIndex] != 'U' & //cell below wall != U
               visitedCells[rowIndex+1][colIndex] != true)  //if below isn't visited
            {
                rowIndex++; //get ready to go down one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathStack.push(rowIndex); //record coordinates of path in stack
                pathStack.push(colIndex); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test   
            }
            
            else if(cells[rowIndex][colIndex] != 'R' & //current cell wall != R 
               colIndex != nrCols-1 & //not at edge of maze     
               cells[rowIndex][colIndex+1] != 'L' & //cell wall to left  != L
               visitedCells[rowIndex][colIndex+1] != true)  //if right cell isn't visited
            {
                colIndex++; //get ready to go right one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathStack.push(rowIndex); //record coordinates of path in stack
                pathStack.push(colIndex); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
            else if(cells[rowIndex][colIndex] != 'U' & //current cell wall != Up 
               rowIndex != 0 & //not at edge of maze     
               cells[rowIndex-1][colIndex] != 'D' & //cell wall above  != Down
               visitedCells[rowIndex-1][colIndex] != true)  //if above cell isn't visited
            {
                rowIndex--; //get ready to go up one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathStack.push(rowIndex); //record coordinates of path in stack
                pathStack.push(colIndex); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
            else if(cells[rowIndex][colIndex] != 'L' & //current cell wall != L
              colIndex != 0 & //not at edge of maze
               cells[rowIndex][colIndex-1] != 'R' & //cell wall to left  != R
               visitedCells[rowIndex][colIndex-1] != true)  //if left cell isn't visited
            {
                colIndex--; //get ready to go left one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathStack.push(rowIndex); //record coordinates of path in stack
                pathStack.push(colIndex); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
            else {
                System.out.println("Got stuck - Try again");
                break;
            }
            
            System.out.println(colIndex); //simulate popping
            System.out.println(rowIndex);
            
            if(rowIndex == rend & colIndex == cend)
                truth = false;
           
            pathCtr++; //get number of lines needed to write to path file
        } //end while traverse path
        }//end try
        
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Array Index out of bounds: "+e.getMessage()+" try again");
        }
        
        ////write path to file
        pathW.print(pathCtr+1+"\n"); //include starting point
        for(int i = 0; i < pathCtr+1; i++) //while loop iterations * 2
        {
            pathStack2.push(pathStack.pop());
            pathStack2.push(pathStack.pop());
            pathW.print(pathStack2.pop()+" ");
            pathW.print(pathStack2.pop()+"\n");
        }
       
        pathW.close();    
        ////end write to path file 
        
        } //end if method = dfs
        
        ///////////BFS///////////////////

        else if (method.equals("bfs")) {      
        
        pathQ.add(Integer.toString(rstart));
        pathQ.add(Integer.toString(cstart));
            
        try{
        
        while(truth) //while current cell != goal
        {
            //BFS priority check R, D, L, U in order (side to side)
           if(cells[rowIndex][colIndex] != 'R' & //current cell wall != R 
               colIndex != nrCols-1 & //not at edge of maze     
               cells[rowIndex][colIndex+1] != 'L' & //cell wall to left  != L
               visitedCells[rowIndex][colIndex+1] != true)  //if right cell isn't visited
            {
                colIndex++; //get ready to go right one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathQ.add(Integer.toString(rowIndex)); //record coordinates of path in Queue
                pathQ.add(Integer.toString(colIndex));
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
           else if(cells[rowIndex][colIndex] != 'D' & //current cell wall != D 
               rowIndex != nrRows-1 & //not at edge of maze     
               cells[rowIndex+1][colIndex] != 'U' & //cell below wall != U
               visitedCells[rowIndex+1][colIndex] != true)  //if below isn't visited
            {
                rowIndex++; //get ready to go down one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathQ.add(Integer.toString(rowIndex)); //record coordinates of path in Queue
                pathQ.add(Integer.toString(colIndex)); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test   
            }
            
            else if(cells[rowIndex][colIndex] != 'L' & //current cell wall != L
              colIndex != 0 & //not at edge of maze
               cells[rowIndex][colIndex-1] != 'R' & //cell wall to left  != R
               visitedCells[rowIndex][colIndex-1] != true)  //if left cell isn't visited
            {
                colIndex--; //get ready to go left one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathQ.add(Integer.toString(rowIndex)); //record coordinates of path in Queue
                pathQ.add(Integer.toString(colIndex)); 
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
             else if(cells[rowIndex][colIndex] != 'U' & //current cell wall != Up 
               rowIndex != 0 & //not at edge of maze     
               cells[rowIndex-1][colIndex] != 'D' & //cell wall above  != Down
               visitedCells[rowIndex-1][colIndex] != true)  //if above cell isn't visited
            {
                rowIndex--; //get ready to go up one
                visitedCells[rowIndex][colIndex] = true; //mark cell as visited
                pathQ.add(Integer.toString(rowIndex)); //record coordinates of path in Queue
                pathQ.add(Integer.toString(colIndex));
                System.out.println("r:"+rowIndex+" c:"+colIndex); //test
            }
            
            else {
                System.out.println("Got stuck - Try again");
                break;
            }
            
            //System.out.println(colIndex); //simulate popping
            //System.out.println(rowIndex);
            
            if(rowIndex == rend & colIndex == cend)
                truth = false;
           
            pathCtr++; //get number of lines needed to write to path file
        } //end while traverse path
        }//end try
        
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Array Index out of bounds: "+e.getMessage()+" try again");
        }
        
        ////write path to file
        pathW.print(pathCtr+1+"\n"); //include starting point
        for(int i = 0; i < pathCtr+1; i++) //while loop iterations * 2
        {
            pathW.print(pathQ.remove()+" ");
            pathW.print(pathQ.remove()+"\n");
        }
       
        pathW.close();    
        ////end write to path file 
            
        } //end if method = bfs
        
        ////////////////A*/////////////////////
        
        
        
        ////end A*
        
        else {
            System.out.println("Enter valid method");
        }
        
        ////end write path functions
        
           
        
        if(rowIndex == rend & colIndex == cend) {
            System.out.println("At goal!");
        }
        else
            System.out.println("Failure - try again");
        
        
        ////////////////////////////////
        
	GMaze gMaze = new GMaze();
	
	if(args.length > 0) {
	    gMaze.readMaze(filepath+mazeFile);

        }
	if(args.length > 1)
	    gMaze.readPath(filepath+pathFile);
	

	JFrame f = new JFrame();
	f.getContentPane().add(gMaze);
	f.setSize(600, 600);
	f.setVisible(true);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } //end main
} //end GMAze
