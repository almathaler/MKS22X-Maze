import java.util.*;
import java.io.*;
public class Maze{
    private int[][] moves = {{-1, 0}, {1, 0}, {0, 1}, {0,-1}};
    private char[][] maze;
    private boolean animate;//false by default
    //private int[][] startLocation; //i will set this in constructor, hope it's fine
    public static void main(String[] args){
      try{
        makeRandomMazeFile(args[0]);
        Maze example = new Maze(args[0]);
        System.out.println(example);
      }catch (FileNotFoundException e){
        System.out.println("file not found");
      }catch (UnsupportedEncodingException e){
        System.out.println("UTF-8 not there");
      }
      /*
      try{
        Maze example = new Maze(args[0]);
        System.out.println(example);
        example.setAnimate(Boolean.parseBoolean(args[1]));
        System.out.println(example.solve());

      }catch (FileNotFoundException e){
        System.out.println("That file doesn't exist!");
      }
      */
    }
    /*Constructor loads a maze text file, and sets animate to false by default.

      1. The file contains a rectangular ascii maze, made with the following 4 characters:
      '#' - Walls - locations that cannot be moved onto
      ' ' - Empty Space - locations that can be moved onto
      'E' - the location of the goal (exactly 1 per file)
      'S' - the location of the start(exactly 1 per file)

      2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!

      3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:
         throw a FileNotFoundException or IllegalStateException
    */
    public Maze(String filename) throws FileNotFoundException{
        animate = false;
        //set dimensions of maze
        File f = new File(filename);
        Scanner inf1 = new Scanner(f);
        int numRows = 0;
        String scanForCols = "";
        while (inf1.hasNextLine()){
          numRows++;
          scanForCols = inf1.nextLine();
        }
        int numCols = scanForCols.length();
        maze = new char[numRows][numCols];
        inf1.close();
        Scanner inf = new Scanner(f);
        //reset scanner to fill up the char[][]
        int row = 0;
        int countE = 0;
        int countS = 0;
        while (inf.hasNextLine()){
          String toAdd = inf.nextLine();
          if (!toAdd.equals("") && !toAdd.equals("\n") && !toAdd.equals(" ") && !toAdd.equals("\t")){
            char[] curArray = toAdd.toCharArray();
            for (char i : curArray){
              //System.out.println("testing [" + i + "] for E or S");
              if (i == 'E'){
                countE++;
              }
              if (i == 'S'){
                countS++;
              }
            }
            maze[row] = curArray;
          }
          row++;
        }
        if (countE != 1 || countS != 1){
          throw new IllegalStateException("Inappropriate amount of E and S!");
        }
    }
    public static void makeRandomMazeFile(String filename) throws FileNotFoundException, UnsupportedEncodingException{ //throws a filenot found if the input filename can't be created, like if it's file.abcdefg
      //first make the Maze
      Random rnd = new Random();
      int rows = rnd.nextInt(30) + 5;
      int cols = rnd.nextInt(30) + 5; //want at least 5 rows
      int ePosR = rnd.nextInt(rows); //row of e
      int ePosC = rnd.nextInt(cols); //col
      int sPosR = rnd.nextInt(rows);
      if (sPosR == ePosR){
        sPosR = (sPosR + sPosR/2) % rows;
      }
      int sPosC = rnd.nextInt(cols);
      if (sPosC == ePosC){
        sPosC = (sPosC + sPosC/2) % cols;
      }
      char[][] maze = new char[rows][cols];
      for (int i = 0; i<maze.length; i++){
        for (int k = 0; k<maze[0].length; k++){
          maze[i][k] = '#';
        }
      }
      maze[ePosR][ePosC] = 'E';
      maze[sPosR][sPosC] = 'S';
      //then write out to a file specified by user
      PrintWriter writer = new PrintWriter(filename, "UTF-8");
      for (int r = 0; r<maze.length; r++){
        String toWrite = new String(maze[r]);
        writer.println(toWrite);
      }
      writer.close();

    }

    private void wait(int millis){
         try {
             Thread.sleep(millis);
         }
         catch (InterruptedException e) {
         }
     }

    public void setAnimate(boolean b){
        animate = b;
    }

    public void clearTerminal(){
        //erase terminal, go to top left of screen.
        System.out.println("\033[2J\033[1;1H");
    }





   /*Return the string that represents the maze.
     It should look like the text file with some characters replaced.
    */
    public String toString(){
      String toReturn = "";
      for (int row = 0; row<maze.length; row++){
        for (int col = 0; col<maze[0].length; col++){
          toReturn += maze[row][col];
        }
        toReturn += "\n";
      }
      return toReturn;
    }


    /*Wrapper Solve Function returns the helper function
      Note the helper function has the same name, but different parameters.
      Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.
    */
    public int solve(){
            //find the location of the S.
            //erase the S
            //and start solving at the location of the s.
            for (int r = 0; r<maze.length; r++){
              for (int c = 0; c<maze[0].length; c++){
                if (maze[r][c] == 'S'){
                  return solve(r, c, 0);
                }
              }
            }
            //return solve(???,???);
            return -1; //should never get here, if there is no S but constructor takes care of that.
    }

    /*
      Recursive Solve function:

      A solved maze has a path marked with '@' from S to E.

      Returns the number of @ symbols from S to E when the maze is solved,
      Returns -1 when the maze has no solution.

      Postcondition:
        The S is replaced with '@' but the 'E' is not.
        All visited spots that were not part of the solution are changed to '.'
        All visited spots that are part of the solution are changed to '@'
    */
    private int solve(int row, int col, int numAts){ //you can add more parameters since this is private
        //automatic animation! You are welcome.
        if(animate){
            clearTerminal();
            System.out.println(this);
            wait(140);
        }
        //COMPLETE SOLVE
        if (maze[row][col] == 'E'){ //if you're at the end
          return numAts; //don't change its value; return # of @s
        }
        maze[row][col] = '@'; //if you're not at the end, put down an @
        for (int[] move : moves){ //for all 4 moves
          try{
            int newR = row + move[0]; //make these new moves
            int newC = col + move[1];
            if (maze[newR][newC] == ' ' || maze[newR][newC] == 'E'){ //see if you can add to these spaces (so if everything around a move is a . or a @, u hit a state space dead end)
              int toReturn = solve(newR, newC, numAts+1); //also bc it's try/catch, if this is an invalid space for loop will j continue
              if (toReturn != -1){ //anyways set an int variable to the value of the solve from the next space
                return toReturn; //if this int is greater than -1 (so it works), return the value
              }
            }
          }
          catch (IndexOutOfBoundsException e){
            //don't do anything continue w for loop
          }
        }
        maze[row][col] = '.'; //if the recursion returns -1, change the @ to .
        return -1; //failed
    }

}
