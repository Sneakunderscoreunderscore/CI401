package ci401.breakout;

// An object in the game, represented as a rectangle, with a position,
// a size, a colour and a direction of movement.

// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import the JavaFX Color class
import javafx.scene.paint.Color;

public class BatObj extends BaseObj
{
    public boolean direction = true;

    public BatObj( int x, int y, int w, int h, Color c )
    {
        super(x, y, w, h, c);
    }

    // move in x axis
    public void moveX()
    {
        if (direction==true){
            topX += 5;
        }
        else{
            topX-= 5;
        }

        // stop bat from going of the screen
        if (topX<0){
            topX=0;
        } 
        if (topX>1000-width){
            topX=1000-width;
        }
    }

}
