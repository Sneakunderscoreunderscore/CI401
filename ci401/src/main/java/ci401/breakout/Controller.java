package ci401.breakout;

// The breakout controller converts key presses from the user (received by the View object)
// into commands for the game (in the Model object)

// we need to use on JavaFX class
import javafx.scene.input.KeyEvent;

public class Controller
{
  // instance variables for the two other components of the MVC model  
  public Model model;
  public View  view;
  
  // we don't really need a constructor method, but include one to print a 
  // debugging message if required
  public Controller()
  {
    Debug.trace("Controller::<constructor>");
  }
  
// #############
// Input handler
// #############

  public void userKeyInteraction(KeyEvent event )
  {
    // print a debugging message to show a key has been pressed
    Debug.trace("Controller::userKeyInteraction: keyCode = " + event.getCode() );
    
    switch ( event.getCode() )             
    {
      case LEFT:                     // Left Arrow - move bat left
        model.setBatDirection(false);  
        Debug.trace("Controller::moving left");        
        break;

      case RIGHT:                    // Right arrow - move bat right
        model.setBatDirection(true);   
        Debug.trace("Controller::moving right");             
        break;

      case DIGIT1:                  // 1 - buy bat upgrade
        model.buybat();
        break;

      case DIGIT2:                  // 1 - buy ball upgrade
        model.buyball();
        break;

      case SHIFT:                    // shift - toggle fast movement
        if (model.fast == false) {model.fast = true;}
        else {model.fast = false;}
        break;

      case ESCAPE :                  // escape - stop the game
        model.setGameState("finished");
        break;
    }
  }
}
