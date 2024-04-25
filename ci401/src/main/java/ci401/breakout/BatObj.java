package ci401.breakout;

// An object in the game, represented as a rectangle, with a position,
// a size, a colour and a direction of movement.

// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import the JavaFX Color class
import javafx.scene.paint.Color;

public class BatObj extends BaseObj
{
    public int   dirX   = 1;            // Direction X (1, 0 or -1)

    public BatObj( int x, int y, int w, int h, Color c )
    {
        super(x, y, w, h, c);
    }

    // move in x axis
    public void moveX( int units )
    {
        topX += units * dirX;
    }

    // change direction of movement in x axis (-1, 0 or +1)
    public void changeDirectionX()
    {
        dirX = -dirX;
    }
}
