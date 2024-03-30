import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * This class creates a traffic light with three circles and a rectangle.
 * Added methds to set the color of the circles and get the pane of which the traffic light is created.
 */
public class TrafficLightCreation {

    public enum LightColor {
        RED, YELLOW, GREEN, GREENLEFT, GREENRIGHT
    }

    private final Pane pane;
    private final Rectangle rectangle;
    private final Circle redCircle;
    private final Circle yellowCircle;
    private final Circle greenCircle;
    private final ImageView leftTurnArrowImageView;
    private final ImageView rightTurnArrowImageView;
    private LightColor lightColor;

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

        leftTurnArrowImageView = new ImageView();
        rightTurnArrowImageView = new ImageView();
        ImageHelper imageHelper = new ImageHelper();
        Image map = imageHelper.getImage("./images/Steady_Green_Arrow-removebg-preview.png");

        leftTurnArrowImageView.setImage(map);
        leftTurnArrowImageView.setFitWidth(50); // Match the diameter of the greenCircle
        leftTurnArrowImageView.setFitHeight(50);
        leftTurnArrowImageView.setX(325); // Position it over the greenCircle
        leftTurnArrowImageView.setY(205);

        map = imageHelper.getImage("./images/right_Steady_Green_Arrow-removebg-preview.png");
        rightTurnArrowImageView.setImage(map);
        rightTurnArrowImageView.setFitWidth(50); // Match the diameter of the greenCircle
        rightTurnArrowImageView.setFitHeight(50);
        rightTurnArrowImageView.setX(325); // Position it over the greenCircle
        rightTurnArrowImageView.setY(205);

        lightColor = null;

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
        lightColor = LightColor.RED;
        if(pane.getChildren().contains(leftTurnArrowImageView)){
            pane.getChildren().remove(leftTurnArrowImageView);
        }
        if (pane.getChildren().contains(rightTurnArrowImageView)) {
            pane.getChildren().remove(rightTurnArrowImageView);
        }
    }

    /**
     * Method to set the color of the yellow circle to yellow and the other circles to null.
     */
    public void setYellow(){
        redCircle.setFill(null);
        yellowCircle.setFill(Color.YELLOW);
        greenCircle.setFill(null);
        lightColor = LightColor.YELLOW;
        if(pane.getChildren().contains(leftTurnArrowImageView)){
            pane.getChildren().remove(leftTurnArrowImageView);
        }
        if (pane.getChildren().contains(rightTurnArrowImageView)) {
            pane.getChildren().remove(rightTurnArrowImageView);
        }
    }

    /**
     * Method to set the color of the green circle to green and the other circles to null.
     */
    public void setGreen(){
        redCircle.setFill(null);
        yellowCircle.setFill(null);
        greenCircle.setFill(Color.GREEN);
        lightColor = LightColor.GREEN;
        if(pane.getChildren().contains(leftTurnArrowImageView)){
            pane.getChildren().remove(leftTurnArrowImageView);
        }
        if (pane.getChildren().contains(rightTurnArrowImageView)) {
            pane.getChildren().remove(rightTurnArrowImageView);
        }
    }

    public void setGreenLeftTurnArrow(){
        redCircle.setFill(null);
        yellowCircle.setFill(null);
        greenCircle.setFill(null);

        // Add the ImageView to the pane if it's not already there
        if (!pane.getChildren().contains(leftTurnArrowImageView)) {
            pane.getChildren().add(leftTurnArrowImageView);
        }


        if (pane.getChildren().contains(rightTurnArrowImageView)) {
            pane.getChildren().remove(rightTurnArrowImageView);
        }


        lightColor = LightColor.GREENLEFT;
    }

    public void setGreenRightTurnArrow(){
        redCircle.setFill(null);
        yellowCircle.setFill(null);
        greenCircle.setFill(null);

        if(pane.getChildren().contains(leftTurnArrowImageView)){
            pane.getChildren().remove(leftTurnArrowImageView);
        }

        // Add the ImageView to the pane if it's not already there
        if (!pane.getChildren().contains(rightTurnArrowImageView)) {
            pane.getChildren().add(rightTurnArrowImageView);
        }

        lightColor = LightColor.GREENRIGHT;
    }

    public LightColor getLightColor() {
        return lightColor;
    }

}
