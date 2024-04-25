package ci401.breakout;

// A generic object, contains all the data used by all the objects
// used primarily to inherit the necessary variables

// import the JavaFX Color class
import javafx.scene.paint.Color;

public class BaseObj
{
    // state variables for a game object
    public boolean visible  = true;     // Can be seen on the screen (change to false when the brick gets hit)
    public int topX   = 0;              // Position - top left corner X
    public int topY   = 0;              // position - top left corner Y
    public int width  = 0;              // Width of object
    public int height = 0;              // Height of object
    public Color colour;                // Colour of object

    public BaseObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }
}
