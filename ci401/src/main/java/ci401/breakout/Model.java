package ci401.breakout;

// The model represents all the actual content and functionality of the game
// For Breakout, it manages all the game objects that the View needs
// (the bat, ball, bricks, and the score), provides methods to allow the Controller
// to move the bat (and a couple of other functions - change the speed or stop 
// the game), and runs a background process (a 'thread') that moves the ball 
// every 20 milliseconds and checks for collisions 

import javafx.scene.paint.*;
import javafx.application.Platform;
import java.util.Random;

public class Model 
{
    // First,a collection of useful values for calculating sizes and layouts etc.

    public int B              = 6;      // Border round the edge of the panel
    public int M              = 40;     // Height of menu bar space at the top

    public int BALL_SIZE      = 30;     // Ball side
    public int BRICK_WIDTH    = 50;     // Brick size
    public int BRICK_HEIGHT   = 30;

    public int BAT_MOVE       = 5;      // Distance to move bat on each keypress
    public int BALL_MOVE      = 3;      // Units to move the ball on each step

    public int HIT_BRICK      = 50;     // Score for hitting a brick
    public int HIT_MULTI     = 1;       // current score multiplier
    public int MAX_MULTI     = 1;       // multiplier for hitting bat
 
    public int BrickNum;                // The number of bricks
    public Random RandomGen = new Random();         // a random number generator
    public BrickObj upgrade;            //  a brick randomly selected to get upgraded
    public static Color[] colours = {Color.GRAY,Color.AQUAMARINE,Color.AQUA,Color.AZURE,Color.RED};

    // The other parts of the model-view-controller setup
    View view;
    Controller controller;

    // The game 'model' - these represent the state of the game
    // and are used by the View to display it
    public BallObj ball;                // The ball
    public BrickObj[] bricks;           // The bricks
    public BatObj bat;                  // The bat
    public int score = 0;               // The score

    // variables that control the game 
    public String gameState = "running";// Set to "finished" to end the game
    public boolean fast = false;        // Set true to make the ball go faster

    // shop values
    public int shopball = 500;
    public int shopbat = 200;

    // initialisation parameters for the model
    public int width;                   // Width of game
    public int height;                  // Height of game

    // CONSTRUCTOR - needs to know how big the window will be
    public Model( int w, int h )
    {
        Debug.trace("Model::<constructor>");  
        width = w; 
        height = h;
    }

    // ##################
    // Animating the game   
    // ##################

    // Start the animation thread
    public void startGame()
    {
        initialiseGame();                           // set the initial game state
        Thread t = new Thread( this::runGame );     // create a thread running the runGame method
        t.setDaemon(true);                          // Tell system this thread can die when it finishes
        t.start();                                  // Start the thread running
    }   
    
    // Initialise the game - reset the score and create the game objects 
    public void initialiseGame()
    {       
        score = 0;
        ball   = new BallObj(width/2, height/2, BALL_SIZE, Color.RED );
        bat    = new BatObj(width/2, height - BRICK_HEIGHT*3/2, BRICK_WIDTH*3, BRICK_HEIGHT/4, Color.GRAY);
        bricks = new BrickObj[0];
        int WALL_TOP = 100;                     // how far down the screen the wall starts
        int NUM_BRICKS = width/BRICK_WIDTH;     // how many bricks fit on screen
        bricks = new BrickObj[NUM_BRICKS];       // make an array big enough for all the bricks
        for (int i=0; i < NUM_BRICKS; i++) {
            BrickObj brick = new BrickObj(BRICK_WIDTH*i, WALL_TOP, BRICK_WIDTH, BRICK_HEIGHT, Color.BLUE);
            bricks[i] = brick;      // add this brick to the list of bricks
            BrickNum++;
        }
    }

    
    // The main animation loop
    public void runGame()
    {
        try
        {
            Debug.trace("Model::runGame: Game starting"); 
            // set game true - game will stop if it is set to "finished"
            setGameState("running");
            while (!getGameState().equals("finished"))
            {
                updateGame();                        // update the game state
                modelChanged();                      // Model changed - refresh screen
                Thread.sleep( getFast() ? 10 : 20 ); // wait a few milliseconds
            }
            Debug.trace("Model::runGame: Game finished"); 
        } catch (Exception e) 
        { 
            Debug.error("Model::runAsSeparateThread error: " + e.getMessage() );
        }
    }
  
    // updating the game - this happens about 50 times a second to give the impression of movement
    public synchronized void updateGame()
    {
        //
        // Ball logic
        //
        
        // move the ball one step
        ball.moveX(BALL_MOVE);                      
        ball.moveY(BALL_MOVE);

        // get the current ball possition (top left corner)
        int x = ball.topX;  
        int y = ball.topY;

        // Deal with possible edge of board hit
        if (x >= width - B - BALL_SIZE)  ball.changeDirectionX();
        if (x <= 0 + B)  ball.changeDirectionX();
        if (y >= height - B - BALL_SIZE){
            ball.changeDirectionY(); // Bottom
            HIT_MULTI=1;
        }
        if (y <= 0 + M)  ball.changeDirectionY();

        // check whether ball has hit a (visible) brick and destroy (hide) a brick if it is
        boolean hit = false;
        for (BrickObj brick: bricks) {
            if (brick.visible && (ball.IFrames==0) && brick.hitBy(ball)) {
                hit = true;
                brick.visible = false;      // set the brick invisible
                addToScore( HIT_BRICK * HIT_MULTI);    // add to score for hitting a brick
                BrickNum -= 1;
                Debug.trace("Model::updateGame: bricks left:"+BrickNum);
                if (BrickNum == 0)          // if no bricks are left, restart
                {
                    resetGame();
                }
            }
        }    

        if (hit) {
            ball.changeDirectionY();
        }
        
        // check whether ball has hit the bat
        if ( ball.hitBy(bat) ) {
            ball.changeDirectionY();

        }

        //
        // bat logic
        //

        moveBat();

    }

    // when all the bricks are destroyed, reset them and double 1 bricks value
    public void resetGame()
    {
        Debug.trace("Model::resetGame: reset started");
        ball.IFrames=60;
        for (BrickObj brick: bricks)        // make all bricks visible
        {
            brick.visible = true;
            BrickNum++;
        // double the value of a random brick
        bricks[RandomGen.nextInt(BrickNum)].LevelUp();
        }
    }

    // This is how the Model talks to the View
    // Whenever the Model changes, this method calls the update method in
    // the View. It needs to run in the JavaFX event thread, and Platform.runLater 
    // is a utility that makes sure this happens even if called from the
    // runGame thread
    public synchronized void modelChanged()
    {
        Platform.runLater(view::update);
    }
    
    
    // Methods for accessing and updating values
    // these are all synchronized so that the can be called by the main thread 
    // or the animation thread safely
    
    // Change game state - set to "running" or "finished"
    public synchronized void setGameState(String value)
    {  
        gameState = value;
    }
    
    // Return game running state
    public synchronized String getGameState()
    {  
        return gameState;
    }

    // Change game speed - false is normal speed, true is fast
    public synchronized void setFast(Boolean value)
    {  
        fast = value;
    }
    
    // Return game speed - false is normal speed, true is fast
    public synchronized Boolean getFast()
    {  
        return(fast);
    }

    // Return bat object
    public synchronized BatObj getBat()
    {
        return(bat);
    }
    
    // return ball object
    public synchronized BallObj getBall()
    {
        return(ball);
    }
    
    // return bricks
    public synchronized BrickObj[] getBricks()
    {
        return(bricks);
    }
    
    // return score
    public synchronized int getScore()
    {
        return(score);
    }
    
     // update the score
    public synchronized void addToScore(int n)    
    {
        score += n;        
    }

     // update the score
    public synchronized void setBatDirection(Boolean n)    
    {
        bat.direction = n;        
    }
    
    // move the bat one step - -1 is left, +1 is right
    public synchronized void moveBat()
    {        
        bat.moveX();
    }

    // ##################
    // Shop functions 
    // ##################

    public synchronized int getshopbat()
    {
        return(shopbat);
    }

    public synchronized int getshopball()
    {
        return(shopball);
    }

    public synchronized void buybat()    
    {
        if (score>shopbat){
            bat.width += 5;
            shopbat = (int) (shopbat*1.7);  
        }  
    }

    public synchronized void buyball()    
    {
        if (score>shopball){
            MAX_MULTI +=0.2;
            shopball = (int) (shopball*1.4);  
        } 
    }
}   
    