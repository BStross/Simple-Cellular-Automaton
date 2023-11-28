import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class AutomatonTest
{
   private Random numGen = new Random();
   
   private static final int UPPER_BOUND = 100;
   private static final int GRID_SIZE = 49;
   private static final int NUM_GENERATIONS = 1000;
   private static final int NEIGHBORHOOD_SIZE = 3;
   private static final int RANDOM_AMOUNT = 90;
   
   private int[][] grid = new int[GRID_SIZE][GRID_SIZE];  
   
   // fills grid with entries in accordance with the results of the random number generator
   private void populateGrid() 
   {
      int[][] fillGrid = new int[GRID_SIZE][GRID_SIZE]; 
      
      int randomNum = numGen.nextInt(UPPER_BOUND); 
      
      for (int i = 0; i < fillGrid.length; i++)
      {
         for (int j = 0; j < fillGrid.length; j++)
         {
            randomNum = numGen.nextInt(UPPER_BOUND);
            if (randomNum > RANDOM_AMOUNT)
            {
               fillGrid[i][j] = 1;
            }  
            else
            {
               fillGrid[i][j] = 0;
            } 
         }
      }
      
      this.grid = fillGrid;
   }
   
   private void populateGridStatic()
   {
      int[][] fillGrid = new int[GRID_SIZE][GRID_SIZE]; 

      fillGrid[GRID_SIZE / 2 - 1][GRID_SIZE / 2 - 1] = 1;   // center
      
      fillGrid[GRID_SIZE / 2 - 2][GRID_SIZE / 2 - 2] = 1;   // top left
      fillGrid[GRID_SIZE / 2 - 3][GRID_SIZE / 2 - 3] = 1; 
      
      fillGrid[GRID_SIZE / 2 - 2][GRID_SIZE / 2] = 1;       // top right
      fillGrid[GRID_SIZE / 2 - 3][GRID_SIZE / 2 + 1] = 1;
      
      fillGrid[GRID_SIZE / 2][GRID_SIZE / 2] = 1;           // bottom left
      fillGrid[GRID_SIZE / 2 + 1][GRID_SIZE / 2 + 1] = 1;
      
      fillGrid[GRID_SIZE / 2][GRID_SIZE / 2 - 2] = 1;       // bottom right
      fillGrid[GRID_SIZE / 2 + 1][GRID_SIZE / 2 - 3] = 1;
      
      this.grid = fillGrid;
   }
   
   // Starting grid with fixed entries
   private void populateGridFixed()
   {
      int[][] fillGrid = new int[GRID_SIZE][GRID_SIZE]; 

      for (int i = 0; i < fillGrid.length; i++)
      {
         for (int j = 0; j < fillGrid.length; j++)
         {
            if ( i == j )
            {
               fillGrid[i][j] = 1;
            }
            else if (j == GRID_SIZE - 1 - i)
            {
               fillGrid[i][j] = 1;
            }
            else if ( ((j % GRID_SIZE) % 2 == 0) && i == GRID_SIZE / 2)
            {
               fillGrid[i][j] = 1;
            }
            else if ( ((i % GRID_SIZE) % 2 == 0) && j == GRID_SIZE / 2 )
            {
               fillGrid[i][j] = 1;
            }
         }
      }
      
      this.grid = fillGrid;
   }
   
   // Meat of the class; changes values in grid in accordance with rules
   public void updateGrid() 
   {
      int[][] oldGrid = this.grid;
      int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
      
      for (int i = 0; i < newGrid.length; i++)
      {
         for (int j = 0; j < newGrid.length; j++)
         {
            int[][] neighborhood = getNeighborhood(oldGrid, i, j);            
            int liveNeighbors = getLiveNeighbors(neighborhood);
            newGrid[i][j] = applyRules(oldGrid, liveNeighbors, i, j);
         }
      }
      
      this.grid = Arrays.copyOf(newGrid, newGrid.length);
   }
   
   // Transforms grid
   private int applyRules(int[][] inputGrid, int liveNeighbors, int i, int j) // applies the rules
   {
      if (inputGrid[i][j] == 1) 
      {
          if (liveNeighbors < 2) 
          {
              return 0; // underpopulation
          } 
          else if (liveNeighbors == 2 || liveNeighbors == 3) 
          {
              return 1; // survival
          } 
          else if (liveNeighbors > 3) 
          {
              return 0; // overpopulation
          }
      } 
      else 
      {
          if (liveNeighbors >= 3 ) 
          {
              return 1; // reproduction
          } 
          else 
          {
              return 0; // no change
          }
      }
      return 0;
   }
   
   // creates neighborhood
   private int[][] getNeighborhood(int[][] inputGrid, int i, int j) 
   {
      int[][] neighborhood = new int[NEIGHBORHOOD_SIZE][];
      
      for (int k = 0; k < NEIGHBORHOOD_SIZE; k++)
      {
         int[] neighborhoodRow = new int[NEIGHBORHOOD_SIZE];
         
         for (int l = 0; l < NEIGHBORHOOD_SIZE; l++)
         {
            neighborhoodRow[l] = 
               inputGrid[ Math.floorMod(i + (k - 1), GRID_SIZE) ][ Math.floorMod( j + (l - 1), GRID_SIZE) ]; 
         }
         
         neighborhood[k] = neighborhoodRow;
      }
      return neighborhood;
   }
   
   // determines number of living neighbors
   private int getLiveNeighbors(int[][] neighborhood) 
   {
      int liveNeighbors = 0;
      
      for (int i = 0; i <  NEIGHBORHOOD_SIZE; i++)
      {
         liveNeighbors += Arrays.stream(neighborhood[i]).sum();
      }
      
      return liveNeighbors;
   }
   
   // For debugging in the console
   public void displayGrid()
   {
      for (int i = 0; i < this.grid.length; i++)
      {
         for (int j = 0; j < this.grid.length; j++)
         {
            if (this.grid[i][j] == 0)
            {
               System.out.print(" # ");
            }
            else
            {
               System.out.print(" - ");
            }
         }
         System.out.println();
      }
      System.out.println();
   }

   public static int getGenerations()
   {
      return NUM_GENERATIONS;
   }
   
   public static int getGridSize()
   {
      return GRID_SIZE;
   }
   
   public int[][] getGrid() // returns grid for testing
   {
      return this.grid;
   }
   
   public AutomatonTest() // constructor
   {
      populateGridFixed();
   }
}