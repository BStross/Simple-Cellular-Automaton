// This program is a simple 2 dimensional celluar automaton displayed with a GUI

public class AutomatonTestMain
{
   public static void main(String[] args)
   {
      AutomatonTest testBot = new AutomatonTest();
      
      for (int i = 0; i < testBot.getGenerations(); i++)
      {
         testBot.updateGrid();
      }
      
      testBot.displayGrid();
   }
}