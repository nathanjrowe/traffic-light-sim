import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * This class creates a traffic light with three circles and a rectangle.
 * Added methds to set the color of the circles and get the pane of which the traffic light is created.
 */
public class TrafficLight {

    public enum LightColor {
        RED, YELLOW, GREEN, GREENLEFT, GREENRIGHT
    }

    private final GridPane pane;
    private final Circle redCircle;
    private final Circle yellowCircle;
    private final Circle greenCircle;
    private final ImageView leftTurnArrowImageView;
    private final ImageView rightTurnArrowImageView;
    private LightColor lightColor;
    //Create a pseudo-random ID for the traffic light
    private final int id = (int) (Math.random() * 1000);
    /**
     * Constructor that creates a traffic light with three circles and a rectangle.
     */
    public TrafficLight() {
        pane = new GridPane();
        pane.setStyle("-fx-background-color: black;");
        pane.setMaxSize(20, 50);

        redCircle = new Circle(5);
        redCircle.setFill(null);
        redCircle.setStroke(Color.WHITE);
        GridPane.setConstraints(redCircle, 0, 2);

        yellowCircle = new Circle(5);
        yellowCircle.setFill(null);
        yellowCircle.setStroke(Color.WHITE);
        GridPane.setConstraints(yellowCircle, 0, 1);

        greenCircle = new Circle(5);
        greenCircle.setFill(null);
        greenCircle.setStroke(Color.WHITE);
        GridPane.setConstraints(greenCircle, 0, 0);

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


        pane.getChildren().addAll(redCircle, yellowCircle, greenCircle);
        pane.setUserData(this);
        this.setRed();
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

    public int getId() {
        return id;
    }

}
