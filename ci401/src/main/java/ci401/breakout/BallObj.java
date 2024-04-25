package ci401.breakout;

// An object in the game, represented as a rectangle, with a position,
// a size, a colour and a direction of movement.

// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import the JavaFX Color class
import javafx.scene.paint.Color;

public class BallObj extends BaseObj
{
    public int   dirX   = 1;            // Direction X (1, 0 or -1)
    public int   dirY   = 1;            // Direction Y (1, 0 or -1)

    // ball object inherits basic needs from 'BaseObj'
    public BallObj( int x, int y, int d, Color c)
    {
        super(x, y, d, d, c); // uses constructor of 'baseObj'
    }

    // move in x axis
    public void moveX( int units )
    {
        topX += units * dirX;
    }

    // move in y axis
    public void moveY( int units )
    {
        topY += units * dirY;
    }

    // change direction of movement in x axis (-1, 0 or +1)
    public void changeDirectionX()
    {
        dirX = -dirX;
    }

    // change direction of movement in y axis (-1, 0 or +1)
    public void changeDirectionY()
    {
        dirY = -dirY;
    }

    public boolean hitBy( BatObj obj )
    {
        boolean separate =  
            topX >= obj.topX+obj.width     ||    // '||' means 'or'
            topX+width <= obj.topX         ||
            topY >= obj.topY+obj.height    ||
            topY+height <= obj.topY ;
        
        // use ! to return the opposite result - hitBy is 'not separate')
        return(! separate);  
          
    }
}
