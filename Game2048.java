/**
* The Game2048 class.
*
* This class represents a 2048 game.
* It contains all the logics need to 
* enforce the rules of the game.
*/

import java.lang.Math;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Game2048 {
// =========================================================================
// Constants and variables required by other classes
// Do not change the names nor delete them
// You may modify the value of the constants

   public static final int NUM_ROW = 4;
   public static final int NUM_COLUMN = 4;
   
   // The number user must reach to win.  It should be a power of 2
   public static final int WINNING_NUMBER = 2048;
   
   public static final int LEFT 	= 0;
   public static final int DOWN	= 1;
   public static final int RIGHT = 2;
   public static final int UP 	= 3;

   private Game2048GUI gui;

// ======================================================================

//=== *** Your "global" constants & variables can be added starting here *** ===//

   //Declaring global variables such as grid, score and, empty value placeholder
   public static int[][] grid = new int[NUM_ROW][NUM_COLUMN];
   public static int score = 0;
   public static final int EMPTY = -1;

   //Declaring a variable specific to this class for storing the play again button usage
   private boolean gameReset = false;
/**
 * Constructs Game2048 object.
 *
 * @param gameGUI	The GUI object that will be used by this class.
 */   
   public Game2048(Game2048GUI gameGUI) {
      gui = gameGUI;
   }

// ============================================================================================================= //
//                                 Game Initialization == newGame(), move()                                      //
// ============================================================================================================= //

   //Method which is called at the beginning of the game and if game is rest, or restarted to start a new game
   public void newGame(){
      //Resets grid and score
      score = 0;
      gui.setScore(score);
      gui.clearGrid();

      //Reset the array to empty
      for (int i = 0; i < NUM_ROW; i++) {
         for (int j = 0; j < NUM_COLUMN; j++) {
            grid[i][j] = EMPTY;
         }
      }

      //Call the randomBlock() method twice to create two starting blocks
      randomBlock();
      randomBlock();
   }

   //Method which is called when any arrow key is clicked to move the blocks
   public void move(int direction) {
      //Resets all the booleans to false such as if moved or if merged, also resets the gameReset boolean
      boolean moved = false;
      boolean merged = false;
      gameReset = false;

      //Only proceeds with moves if the game is not over, calls isGameOver to check if it is over, else it ends the game and asks to play again
      if (!isGameOver()) {

         /*
            Switch case structure to go through all the possible directions
            Calls the check[direction] methods for the called direction and checks if a move or merge is possible
            Calls move[direction] to move the grid in that direction and then stores a boolean depending on if a move occurs
            Calls merge[ROW||COL][direction] to call any possible merges and checks the boolean if any merge occurs
            Sets the merged boolean to true if a merge occurs
            Moves all the tiles up again
         */
         switch (direction) {
            case UP:
               if (checkUp()) {
                  moved = moveUp();
                  if (mergeColUp()) {
                     merged = true;
                     moveUp();
                  }
               }
            break;
            case DOWN:
               if (checkDown()) {
                  moved = moveDown();
                  if (mergeColDown()) {
                     merged = true;
                     moveDown();
                  }
               }
            break;
            case RIGHT:
               if (checkRight()) {
                  moved = moveRight();
                  if (mergeRowDown()) {
                     merged = true;
                     moveRight();
                  }
               }
            break;
            case LEFT:
               if (checkLeft()) {
                  moved = moveLeft();
                  if (mergeRowUp()) {
                     merged = true;
                     moveLeft();
                  }
               }
            break;
         }

         //If the play again button is clicked then it leaves the move method
         if (gameReset) {
            return;
         }

         //If a move or a merge is done then a random block is placed on the grid using randomBlock()
         if (moved || merged) {
            randomBlock();
            gui.displayGrid(grid);
         }

         //Checks if game is over after moves and if it is then it ends game and asks if they want to play again
         if (isGameOver()) {
            gui.showGameOver();
            if (gui.showPlayAgain()) {
               newGame();
            }
         }
      } else {
         gui.showGameOver();
         if (gui.showPlayAgain()) {
            newGame();
         }
      }
   }

// ============================================================================================================= //
//                      Movement Methods == moveUp(), moveDown(), moveLeft(), moveRight()                        //
// ============================================================================================================= //

   //Method which is called if the up arrow is clicked and moves all the tiles up
   public boolean moveUp() {

      //Before any move occurs it sets the moved boolean to false as no move has occured yet
      boolean moved = false;

      //Creates a temporary array which stores a whole column, so it must be the length of the amount of rows
      int[] temp = new int[NUM_ROW];

      //Declares a variable which stores the index for the temp array
      int index;

      //Calls checkUp() to check if a move or merge is possible first
      if (checkUp()) {

         //Loops through each column in the array
         for (int j = 0; j < NUM_COLUMN; j++) {

            //Sets index to 0 every time it goes to the next column
            index = 0;

            //Sets the temporary array to -1
            for (int k = 0; k < NUM_ROW; k++) {
               temp[k] = EMPTY;
            }

            //Goes through all the rows for the given column
            for (int i = 0; i < NUM_ROW; i++) {

               //If the tile is not empty it is stored in the temp array for the given index
               if (grid[i][j] != EMPTY) {
                  temp[index] = grid[i][j];

                  //Checks if any tiles were moved vertically in the move and sets the moved boolean to true if it did
                  if (i != index) {
                     moved = true;
                  }

                  /*
                     Increases index to the next place in the temp array only for the non -1 values
                     This stores the grid column as a 2D array without any spaces in between the tiles
                  */
                  index++;
               }
            }

            //Sets the given column to the temp array with the moved tiles
            for (int i = 0; i < NUM_ROW; i++) {
               grid[i][j] = temp[i];
            }
         }
      }

      //Returns if any tiles were moved up
      return moved;
   }

   //Method which is called if the down arrow is clicked and moves all the tiles down
   public boolean moveDown() {

      //Before any move occurs it sets the moved boolean to false as no move has occured yet
      boolean moved = false;

      //Creates a temporary array which stores a whole column, so it must be the length of the amount of rows
      int[] temp = new int[NUM_ROW];

      //Declares a variable which stores the index for the temp array
      int index;

      //Calls checkDown() to check if a move or merge is possible first
      if (checkDown()) {

         //Loops through each column in the array
         for (int j = 0; j < NUM_COLUMN; j++) {

            //Sets index to bottom every time it goes to the next column
            index = NUM_ROW - 1;

            //Sets the temporary array to -1
            for (int k = 0; k < NUM_ROW; k++) {
               temp[k] = EMPTY;
            }

            //Goes through all the rows from bottom to top for the given column
            for (int i = NUM_ROW - 1; i >= 0; i--) {

               //If the tile is not empty it is stored in the temp array for the given index
               if (grid[i][j] != EMPTY) {
                  temp[index] = grid[i][j];

                  //Checks if any tiles were moved vertically in the move and sets the moved boolean to true if it did
                  if (i != index) {
                     moved = true;
                  }

               /*
                  Decreases index to the next place in the temp array only for the non -1 values
                  This stores the grid column as a 2D array without any spaces in between the tiles
               */
                  index--;
               }
            }

            //Sets the given column to the temp array with the moved tiles
            for (int i = 0; i < NUM_ROW; i++) {
               grid[i][j] = temp[i];
            }
         }
      }

      //Returns if any tiles were moved down
      return moved;
   }

   //Method which is called if the right arrow is clicked and moves all the tiles right
   public boolean moveRight() {

      //Before any move occurs it sets the moved boolean to false as no move has occured yet
      boolean moved = false;

      //Creates a temporary array which stores a whole row, so it must be the length of the amount of columns
      int[] temp = new int[NUM_COLUMN];

      //Declares a variable which stores the index for the temp array
      int index;

      //Calls checkRight() to check if a move or merge is possible first
      if (checkRight()) {

         //Loops through each row in the array
         for (int i = 0; i < NUM_ROW; i++) {

            //Sets index to rightmost every time it goes to the next row
            index = NUM_COLUMN - 1;

            //Sets the temporary array to -1
            for (int k = 0; k < NUM_COLUMN; k++) {
               temp[k] = EMPTY;
            }

            //Goes through all the columns from right to left for the given row
            for (int j = NUM_COLUMN - 1; j >= 0; j--) {

               //If the tile is not empty it is stored in the temp array for the given index
               if (grid[i][j] != EMPTY) {
                  temp[index] = grid[i][j];

                  //Checks if any tiles were moved horizontally in the move and sets the moved boolean to true if it did
                  if (j != index) {
                     moved = true;
                  }

               /*
                  Decreases index to the next place in the temp array only for the non -1 values
                  This stores the grid row as a 2D array without any spaces in between the tiles
               */
                  index--;
               }
            }

            //Sets the given row to the temp array with the moved tiles
            for (int j = 0; j < NUM_COLUMN; j++) {
               grid[i][j] = temp[j];
            }
         }
      }

      //Returns if any tiles were moved right
      return moved;
   }

   //Method which is called if the left arrow is clicked and moves all the tiles left
   public boolean moveLeft() {

      //Before any move occurs it sets the moved boolean to false as no move has occured yet
      boolean moved = false;

      //Creates a temporary array which stores a whole row, so it must be the length of the amount of columns
      int[] temp = new int[NUM_COLUMN];

      //Declares a variable which stores the index for the temp array
      int index;

      //Calls checkLeft() to check if a move or merge is possible first
      if (checkLeft()) {

         //Loops through each row in the array
         for (int i = 0; i < NUM_ROW; i++) {

            //Sets index to 0 every time it goes to the next row
            index = 0;

            //Sets the temporary array to -1
            for (int k = 0; k < NUM_COLUMN; k++) {
               temp[k] = EMPTY;
            }

            //Goes through all the columns for the given row
            for (int j = 0; j < NUM_COLUMN; j++) {

               //If the tile is not empty it is stored in the temp array for the given index
               if (grid[i][j] != EMPTY) {
                  temp[index] = grid[i][j];

                  //Checks if any tiles were moved horizontally in the move and sets the moved boolean to true if it did
                  if (j != index) {
                     moved = true;
                  }

               /*
                  Increases index to the next place in the temp array only for the non -1 values
                  This stores the grid row as a 2D array without any spaces in between the tiles
               */
                  index++;
               }
            }

            //Sets the given row to the temp array with the moved tiles
            for (int j = 0; j < NUM_COLUMN; j++) {
               grid[i][j] = temp[j];
            }
         }
      }

      //Returns if any tiles were moved left
      return moved;
   }

// ============================================================================================================= //
//              Check Move Possibility Methods == checkUp(), checkDown(), checkLeft(), checkRight()              //
// ============================================================================================================= //

   //Method which is called every time we want to check if any move or merge is possible upwards
   public boolean checkUp() {

      //Loops over the whole grid
      for (int j = 0; j < NUM_COLUMN; j++) {
         for (int i = 1; i < NUM_ROW; i++) {

            //If the tile is not empty it checks if the tile above it is empty or the same tile
            if (grid[i][j] != EMPTY) {
               if (grid[i - 1][j] == EMPTY || grid[i - 1][j] == grid[i][j]) {

                  //Returns true as a merge of move would be possible
                  return true;
               }
            }
         }
      }

      //If no move or merge was detected in the whole grid it returns a false
      return false;
   }

   //Method which is called every time we want to check if any move or merge is possible downwards
   public boolean checkDown() {

      //Loops over the whole grid
      for (int j = 0; j < NUM_COLUMN; j++) {
         for (int i = NUM_ROW - 2; i >= 0; i--) {

            //If the tile is not empty it checks if the tile below it is empty or the same tile
            if (grid[i][j] != EMPTY) {
               if (grid[i + 1][j] == EMPTY || grid[i + 1][j] == grid[i][j]) {

                  //Returns true as a merge of move would be possible
                  return true;
               }
            }
         }
      }

      //If no move or merge was detected in the whole grid it returns a false
      return false;
   }

   //Method which is called every time we want to check if any move or merge is possible to the right
   public boolean checkRight() {

      //Loops over the whole grid
      for (int i = 0; i < NUM_ROW; i++) {
         for (int j = NUM_COLUMN - 2; j >= 0; j--) {

            //If the tile is not empty it checks if the tile to the right is empty or the same tile
            if (grid[i][j] != EMPTY) {
               if (grid[i][j + 1] == EMPTY || grid[i][j + 1] == grid[i][j]) {

                  //Returns true as a merge of move would be possible
                  return true;
               }
            }
         }
      }

      //If no move or merge was detected in the whole grid it returns a false
      return false;
   }

   //Method which is called every time we want to check if any move or merge is possible to the left
   public boolean checkLeft() {

      //Loops over the whole grid
      for (int i = 0; i < NUM_ROW; i++) {
         for (int j = 1; j < NUM_COLUMN; j++) {

            //If the tile is not empty it checks if the tile to the left is empty or the same tile
            if (grid[i][j] != EMPTY) {
               if (grid[i][j - 1] == EMPTY || grid[i][j - 1] == grid[i][j]) {

                  //Returns true as a merge of move would be possible
                  return true;
               }
            }
         }
      }

      //If no move or merge was detected in the whole grid it returns a false
      return false;
   }

// ============================================================================================================= //
//                  Merge Methods == mergeColUp(), mergeColDown(), mergeRowUp(), mergeRowDown()                  //
// ============================================================================================================= //

   //Method which is called to merge all the tiles up when moveUp() is called
   public boolean mergeColUp() {

      //Sets the merged boolean to false as no merge has occurred yet
      boolean merged = false;

      //Loops over every column
      for (int j = 0; j < NUM_COLUMN; j++) {

         //Creates a array of booleans for each column to store if a merge has occurred in the given row
         boolean[] mergedCol = new boolean[NUM_ROW];

         //Loops through each row except for the last one as we compare from the top to the bottom using i+1
         for (int i = 0; i < NUM_ROW - 1; i++) {

            /*
               Checks if the tile is not empty and if the tile above is the same
               It also checks if a merge has already occurred on that tile or on the tile above to limit it to one merge per move
            */
            if (grid[i][j] != EMPTY && grid[i][j] == grid[i + 1][j] && !mergedCol[i] && !mergedCol[i + 1]) {

               //Sets the grid above the the sum of the two tiles and the lower one to empty
               grid[i][j] = grid[i][j] + grid[i + 1][j];
               grid[i + 1][j] = EMPTY;

               //A merge has occurred in that row, so it stores it in the array and in the merged boolean
               mergedCol[i] = true;
               merged = true;

               //Calls scoreTracker() to update the score with the newly merged tile
               scoreTracker(grid[i][j]);
            }
         }
      }

      //Returns if a merge occurred in the grid
      return merged;
   }
   //Method which is called to merge all the tiles down when moveDown() is called
   public boolean mergeColDown() {

      //Sets the merged boolean to false as no merge has occurred yet
      boolean merged = false;

      //Loops over every column
      for (int j = 0; j < NUM_COLUMN; j++) {

         //Creates an array of booleans for each column to store if a merge has occurred in the given row
         boolean[] mergedCol = new boolean[NUM_ROW];

         //Loops through each row from bottom to top as we compare from the bottom using i-1
         for (int i = NUM_ROW - 1; i > 0; i--) {

         /*
            Checks if the tile is not empty and if the tile below is the same
            It also checks if a merge has already occurred on that tile or on the tile below to limit it to one merge per move
         */
            if (grid[i][j] != EMPTY && grid[i][j] == grid[i - 1][j] && !mergedCol[i] && !mergedCol[i - 1]) {

               //Sets the grid below to the sum of the two tiles and the upper one to empty
               grid[i][j] = grid[i][j] + grid[i - 1][j];
               grid[i - 1][j] = EMPTY;

               //A merge has occurred in that row, so it stores it in the array and in the merged boolean
               mergedCol[i] = true;
               merged = true;

               //Calls scoreTracker() to update the score with the newly merged tile
               scoreTracker(grid[i][j]);
            }
         }
      }

      //Returns if a merge occurred in the grid
      return merged;
   }

   //Method which is called to merge all the tiles to the left when moveLeft() is called
   public boolean mergeRowUp() {

      //Sets the merged boolean to false as no merge has occurred yet
      boolean merged = false;

      //Loops over every row
      for (int i = 0; i < NUM_ROW; i++) {

         //Creates an array of booleans for each row to store if a merge has occurred in the given column
         boolean[] mergedRow = new boolean[NUM_COLUMN];

         //Loops through each column from left to right as we compare from the left using j+1
         for (int j = 0; j < NUM_COLUMN - 1; j++) {

         /*
            Checks if the tile is not empty and if the tile to the right is the same
            It also checks if a merge has already occurred on that tile or on the tile to the right to limit it to one merge per move
         */
            if (grid[i][j] != EMPTY && grid[i][j] == grid[i][j + 1] && !mergedRow[j] && !mergedRow[j + 1]) {

               //Sets the grid to the left to the sum of the two tiles and the right one to empty
               grid[i][j] = grid[i][j] + grid[i][j + 1];
               grid[i][j + 1] = EMPTY;

               //A merge has occurred in that column, so it stores it in the array and in the merged boolean
               mergedRow[j] = true;
               merged = true;

               //Calls scoreTracker() to update the score with the newly merged tile
               scoreTracker(grid[i][j]);
            }
         }
      }

      //Returns if a merge occurred in the grid
      return merged;
   }

   //Method which is called to merge all the tiles to the right when moveRight() is called
   public boolean mergeRowDown() {

      //Sets the merged boolean to false as no merge has occurred yet
      boolean merged = false;

      //Loops over every row
      for (int i = 0; i < NUM_ROW; i++) {

         //Creates an array of booleans for each row to store if a merge has occurred in the given column
         boolean[] mergedRow = new boolean[NUM_COLUMN];

         //Loops through each column from right to left as we compare from the right using j-1
         for (int j = NUM_COLUMN - 1; j > 0; j--) {

         /*
            Checks if the tile is not empty and if the tile to the left is the same
            It also checks if a merge has already occurred on that tile or on the tile to the left to limit it to one merge per move
         */
            if (grid[i][j] != EMPTY && grid[i][j] == grid[i][j - 1] && !mergedRow[j] && !mergedRow[j - 1]) {

               //Sets the grid to the right to the sum of the two tiles and the left one to empty
               grid[i][j] = grid[i][j] + grid[i][j - 1];
               grid[i][j - 1] = EMPTY;

               //A merge has occurred in that column, so it stores it in the array and in the merged boolean
               mergedRow[j] = true;
               merged = true;

               //Calls scoreTracker() to update the score with the newly merged tile
               scoreTracker(grid[i][j]);
            }
         }
      }

      //Returns if a merge occurred in the grid
      return merged;
   }

// ============================================================================================================= //
//              Game State & Utility Methods == randomBlock(), isGridFull(), isGameOver(), scoreTracker()        //
// ============================================================================================================= //

   //Method that is called every time a merge occurs to update the score
   public void scoreTracker(int sum){

      /*
         First checks if the merge creates the winning number 2048, else updates the score using the given input and sets score
         If it is found then shows the win screen and asks to play again
         If the play again button is clicked sets gameReset boolean to true and calls newGame()
      */
      if (sum == WINNING_NUMBER){
         gui.displayGrid(grid);
         gui.showGameWon();
         if (gui.showPlayAgain()){
            gameReset = true;
            newGame();
         }
      } else {
         score += sum;
         gui.setScore(score);
      }
   }

   //Method which is called when a new random block is needed
   public void randomBlock(){

      //Only runs if the grid is not full
      if (!isGridFull()) {

         //Declares variables for the random row and column
         int randCol;
         int randRow;

         //Creates instance of Random() to use randomized numbers
         Random rand = new Random();

         //Do While loop to come up with randomized tile within the grid and loops if the random tile is not empty
         do {
            randCol = rand.nextInt(NUM_COLUMN);
            randRow = rand.nextInt(NUM_ROW);
         } while (grid[randRow][randCol] != EMPTY);

         /*
            Controls a 10% to 90% chance creation of 2 and 4 by using random
            If the random number between 0 to less than 10 is 0 that is a 10% chance of occurring therefore creating a 4
            If the random number is anything else between 1-9 that is a 90% chance of occurring therefore creating a 2
         */
         if (rand.nextInt(10) == 0) {
            grid[randRow][randCol] = 4;
         } else {
            grid[randRow][randCol] = 2;
         }

         //Displays randomly created slot with a short delay
         gui.displaySlot(randRow, randCol, grid[randRow][randCol], 100);
      }
   }

   //Method which is called to check if the grid is full
   public boolean isGridFull(){

      //Declare variable which stores slots with values other than -1
      int fullSlots = 0;

      //Loops through each value in the grid and if it is a value other than -1 it increases fullSlots count
      for (int i = 0; i < NUM_ROW; i++) {
         for (int j = 0; j < NUM_COLUMN; j++) {
            if (grid[i][j] != EMPTY) {
               fullSlots++;
            }
         }
      }

      //If all 16 slots are full returns true that the grid is full else returns that it is not full yet
      if (fullSlots == 16) {
         return true;
      } else {
         return false;
      }
   }

   //Method which is called to check if the game is over
   public boolean isGameOver(){

      //First calls isGridFull() to check if the grid is full else it returns that the game is not over
      if (isGridFull()) {

         //Checks if any move or merge is possible in any direction and returns that the game is not over if it is possible
         //Else returns false if no move or merge is possible
         if (checkUp() || checkDown() || checkLeft() || checkRight()) {
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

// ============================================================================================================= //
//                      Game File Management Methods == saveToFile(), loadFromFile()                             //
// ============================================================================================================= //

   //Method that is called when the save game button is clicked to save game to a file
   public boolean saveToFile(String filename){

      //try and catch structure to create a new file and write to it
      try {
         //Create an instance of BufferedWriter to write to a file
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));

         //First writes the score at the top and goes to next line
         out.write(score + "\n");

         //Goes through every row and column in the grid and outputs it line by line
         for (int i = 0; i < NUM_ROW; i++) {
            for (int j = 0; j < NUM_COLUMN; j++) {
               out.write(grid[i][j] + "\n");
            }
         }

         //Closes file
         out.close();

         //Returns true if file was saved correctly
         return true;
      } catch (IOException iox){
         //Returns false if an error occurred with creating the file
         return false;
      }
   }

   //Method that is called when the load game button is clicked to load a game from a saved file
   public boolean loadFromFile(String filename){

      //try and catch structure to access a saved file
      try {
         //Create an instance of BufferedReader to read file
         BufferedReader in = new BufferedReader(new FileReader(filename));

         //Reads first line as score and sets it to the score
         score = Integer.parseInt(in.readLine());
         gui.setScore(score);

         //Sets the winTileFound boolean to false before checking if the winning tile is already present
         boolean winTileFound = false;

         //Goes line by line and sets the grid to the numbers in the file
         //Checks if the winning tile is already present
         for (int i = 0; i < NUM_ROW; i++) {
            for (int j = 0; j < NUM_COLUMN; j++) {
               grid[i][j] = Integer.parseInt(in.readLine());
               if (grid[i][j] == WINNING_NUMBER) {
                  winTileFound = true;
               }
            }
         }

         //Closes file and displays grid
         in.close();
         gui.displayGrid(grid);

         //If the winning tile is present it displays that you have already won and if you want to play again
         if (winTileFound) {
            gui.showGameWon();
            if (gui.showPlayAgain()) {
               newGame();
            }
         }

         //Returns true for if the file was loaded correctly
         return true;
      } catch (IOException iox) {
         //Returns false if an error occurred with reaching the file
         return false;
      }
   }

}