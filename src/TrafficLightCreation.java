import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * This class creates a traffic light with three circles and a rectangle.
 * Added methds to set the color of the circles and get the pane of which the traffic light is created.
 */
public class TrafficLightCreation {

    private final Pane pane;
    private final Rectangle rectangle;
    private final Circle redCircle;
    private final Circle yellowCircle;
    private final Circle greenCircle;

    /**
     * Constructor that creates a traffic light with three circles and a rectangle.
     */
    public TrafficLightCreation() {
        pane = new Pane();
        rectangle = new Rectangle(300, 50, 100, 220);
        rectangle.setStroke(Color.WHITE);

        redCircle = new Circle(350, 90, 25);
        redCircle.setFill(null);
        redCircle.setStroke(Color.WHITE);

        yellowCircle = new Circle(350, 160, 25);
        yellowCircle.setFill(null);
        yellowCircle.setStroke(Color.WHITE);

        greenCircle = new Circle(350, 230, 25);
        greenCircle.setFill(null);
        greenCircle.setStroke(Color.WHITE);

        pane.getChildren().addAll(rectangle, redCircle, yellowCircle, greenCircle);
    }

    /**
     * Method to get the pane of which the traffic light is created.
     * @return pane
     */
    public Pane getTrafficLight() {
        return pane;
    }

    /**
     * Method to set the color of the red circle to red and the other circles to null.
     */
    public void setRed(){
        redCircle.setFill(Color.RED);
        yellowCircle.setFill(null);
        greenCircle.setFill(null);
    }

    /**
     * Method to set the color of the yellow circle to yellow and the other circles to null.
     */
    public void setYellow(){
        redCircle.setFill(null);
        yellowCircle.setFill(Color.YELLOW);
        greenCircle.setFill(null);
    }

    /**
     * Method to set the color of the green circle to green and the other circles to null.
     */
    public void setGreen(){
        redCircle.setFill(null);
        yellowCircle.setFill(null);
        greenCircle.setFill(Color.GREEN);
    }

}
