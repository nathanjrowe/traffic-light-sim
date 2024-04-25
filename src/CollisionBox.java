import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CollisionBox extends Rectangle{
    
    //Enum to store the state of the collision box
    public static enum State{
        STOP, GO, LEFT, RIGHT
    }
    //Constructor for a simple rectangle
    public CollisionBox(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.RED);
        this.setStrokeWidth(2);
    }

    
    //Set the state of the collision box
    public void setState(State state){
        this.setUserData(state);
    }
    //Get the state of the collision box
    public State getState(){
        return this.getUserData() == null ? State.STOP : (State)this.getUserData();
    }
}
