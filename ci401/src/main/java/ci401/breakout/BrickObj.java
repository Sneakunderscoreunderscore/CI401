package ci401.breakout;

// An object in the game, represented as a rectangle, with a position,
// a size, a colour and a direction of movement.

// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import the JavaFX Color class
import javafx.scene.paint.Color;

public class BrickObj extends BaseObj
{
    // state variables for a game object
    public int Value;

    public BrickObj( int x, int y, int w, int h, Color c,int v)
    {
        super(x, y, w, h, c);
        Value = v;
    }


    // collision detection (if hit by a ball)

    public boolean hitBy( BallObj obj )
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
