package ci401.breakout;

// The View class creates and manages the GUI for the application.
// It doesn't know anything about the game itself, it just displays
// the current state of the Model, and handles user input

// We import lots of JavaFX libraries (we may not use them all, but it
// saves us having to thinkabout them if we add new code)
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View implements EventHandler<KeyEvent>
{ 
    // variables for components of the user interface
    public int width;       // width of window
    public int height;      // height of window

    // user interface objects
    public Pane pane;       // basic layout pane
    public Canvas canvas;   // canvas to draw game on
    public Label infoText;  // info at top of screen
    public Label shopText;  // shop at the bottom of teh screen

    // The other parts of the model-view-controller setup
    public Controller controller;
    public Model model;

    public BatObj   bat;            // The bat
    public BallObj   ball;           // The ball
    public BrickObj[] bricks;         // The bricks
    public int       score =  0;     // The score
    public int shopbat;
    public int shopball;
   
    // constructor method - we get told the width and height of the window
    public View(int w, int h)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
    }

    // start is called from the Main class, to start the GUI up
    
    public void start(Stage window) 
    {
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the text at the top - this
        // is a label which sits 'in front of' the canvas.
        
        // Note that it is important to create control objects (Pane, Label,Canvas etc) 
        // here not in the constructor (or as initialisations to instance variables),
        // to make sure everything is initialised in the right order
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box for the score - a label which we position in front of
        // the canvas (by adding it to the pane after the canvas)
        infoText = new Label("BreakOut: Score = " + score);
        infoText.setTranslateX(20);  // these commands setthe position of the text box
        infoText.setTranslateY(10);  // (measuring from the top left corner)
        pane.getChildren().add(infoText);  // add label to the pane

        shopText = new Label("Shop - [1] bat upgrade" + shopbat + "    [2] extra ball" + shopball);
        shopText.setTranslateX(200);  // these commands set the position of the text box
        shopText.setTranslateY(10);  // (measuring from the top left corner)
        pane.getChildren().add(shopText);  // add label to the pane

        // Make a new JavaFX Scene, containing the complete GUI
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // Add an event handler for key presses. By using 'this' (which means 'this 
        // view object itself') we tell JavaFX to call the 'handle' method (below)
        // whenever a key is pressed
        scene.setOnKeyPressed(this);

        // put the scene in the window and display it
        window.setScene(scene);
        window.show();
    }

    // Event handler for key presses - it just passes the event to the controller
    public void handle(KeyEvent event)
    {
        // send the event to the controller
        controller.userKeyInteraction( event );
    }
    
    // drawing the game image
    public void drawPicture()
    {
        synchronized ( model ) 
        {
            // get the 'paint brush' to pdraw on the canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // clear the whole canvas to white
            gc.setFill( Color.WHITE );
            gc.fillRect( 0, 0, width, height );
            
            // draw the bat and ball
            displayBall( gc, ball );   // Display the Ball
            displayBat( gc, bat  );   // Display the Bat

            // Display the bricks that make up the game
            for (BrickObj brick: bricks) {
                if (brick.visible) {
                    displayBrick(gc, brick);
                }
            }  
            
            // update the score
            infoText.setText("BreakOut: Score = " + score);
            shopText.setText("Shop - [1] bat size upgrade: " + shopbat + "[2] bat hit multiplier: " + shopball);
        }
    }

    // Display game objects
    public void displayBall( GraphicsContext gc, BallObj go ) 
    {
        gc.setFill( go.colour );
        gc.fillOval( go.topX, go.topY, go.width, go.height );
    }

    public void displayBat( GraphicsContext gc, BatObj go ) 
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    public void displayBrick( GraphicsContext gc, BrickObj go ) 
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    // This is how the Model talks to the View
    // This method gets called BY THE MODEL, whenever the model changes
    // It has to do whatever is required to update the GUI to show the new game position
    public void update()
    {
        // Get from the model the ball, bat, bricks & score
        ball    = model.getBall();              // Ball
        bricks  = model.getBricks();            // Bricks
        bat     = model.getBat();               // Bat
        score   = model.getScore();             // Score
        shopbat = model.getshopbat();
        shopball = model.getshopball();
        //Debug.trace("Update");
        drawPicture();                     // Re draw game
    }
}
