import javafx.geometry.Bounds;
import javafx.scene.Node;//this import is unused
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CollisionBox extends Rectangle{
    
    //Enum to store the state of the collision box
    public static enum State{
        STOP, GO, LEFT, RIGHT
    }

    private Object parent = null;

    //Constructor for a simple rectangle
    public CollisionBox(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.RED);
        this.setStrokeWidth(2);
    }

    //Constructor for a simple collision box
    public CollisionBox(double x, double y, int width, int height, Object parent) {
        super(x, y, width, height);
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.RED);
        this.setStrokeWidth(2);
        this.parent = parent;
    }
  
    //Set the state of the collision box
    public void setState(State state){
        this.setUserData(state);
    }

    //Get the state of the collision box
    public State getState(){
        return this.getUserData() == null ? State.STOP : (State)this.getUserData();
    }

    //Get the parent class of the collision box
    public Object getParentClass(){
        return this.parent;
    }

    /*Check if a collision box is colliding with another object
    * this is acomplished by using the built-in intersects function to return
    * true if a collision box is overlapping with another
    */
    public boolean isColliding(Bounds box){
        return this.getBoundsInParent().intersects(box);
    }
}
