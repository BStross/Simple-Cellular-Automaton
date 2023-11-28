// Colorful 2D cellular automaton 
// GUI made with the help of ChatGPT

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;

public class AutomatonGUI extends Application {
    private static final int CELL_SIZE = 10;
    private static final int GRID_SIZE = AutomatonTest.getGridSize();
    private static final int NUM_GENERATIONS = AutomatonTest.getGenerations();
     
    private AutomatonTest automaton;

    public static void main(String[] args) {
        launch(args);
    }

    // Basically the application window
    @Override
    public void start(Stage primaryStage) {  
        automaton = new AutomatonTest();

        primaryStage.setTitle("Automaton");
        GridPane gridPane = createGridPane();

        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateGridPeriodically(gridPane);
    }

    // Places rectangles in accordance with grid entries
    private GridPane createGridPane() {      
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle rectangle = new Rectangle(CELL_SIZE, CELL_SIZE, Color.WHITE);
                gridPane.add(rectangle, col, row);
            }
        }

        return gridPane;
    }

    // Separate thread that runs the simulation while the application window is open.
    private void updateGridPeriodically(GridPane gridPane) {   
        Thread updateThread = new Thread(() -> {
            for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
                final int generationCapture = generation;
                int[][] grid = automaton.getGrid();

                for (int row = 0; row < GRID_SIZE; row++) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        Rectangle rectangle = (Rectangle) gridPane.getChildren().get(row * GRID_SIZE + col);
                        if (grid[row][col] == 0) {
                            Platform.runLater(() -> rectangle.setFill(Color.WHITE));
                        } else {
                           //rectangle.setFill(Color.BLACK);
                           Platform.runLater(() -> complexColoring(rectangle));
                        }
                    }
                }

                automaton.updateGrid();
                // uncomment to create snapshots and save to input path
                //Platform.runLater(() -> createSnapshot(gridPane, generationCapture)); 

                try {
                    Thread.sleep(250); // Delay between generations
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        updateThread.setDaemon(true);
        updateThread.start();
    }
    
    // Colors cells randomly
    public Rectangle complexColoring(Rectangle r) 
    {
        Random colorNumGen = new Random();
        Random whichColor = new Random();
        
        int colorSelection = whichColor.nextInt(9);
        double opacity = colorNumGen.nextDouble() % .5;
        
        if (colorSelection >= 4)
        {
            opacity = whichColor.nextDouble() % .5;
            // pink
            r.setFill(Color.rgb(255, 209, 220, opacity + 0.49));
        }  else
        {
            opacity = whichColor.nextDouble() % .5;
            // old blue
            r.setFill(Color.rgb(167, 199, 231, opacity + 0.49));
        }
        
        return r;
    }
    
    public void createSnapshot(GridPane g, int generation) // for printing a snapshot to a designated folder
    {
        WritableImage image = g.snapshot(new SnapshotParameters(), null);
        
        File file = new File("C:\\Users\\Brad\\Pictures\\Automata\\capture" + generation + ".png");
        
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}
