import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;


public class Testing extends Application {
    private final Boolean DEBUG = false;
    private List<Vehicle> vehicleCollidables = new ArrayList<>();
    private StackPane root = new StackPane();

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        createRoot();
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    private void checkCollision(Vehicle src, Vehicle other){
        if(Shape.intersect(src.carShape(), other.carShape()).getBoundsInLocal().getWidth() > -1){
            System.out.println("Collision Detected");
        }else if(Shape.intersect(src.carShape(), other.carShape()).getBoundsInLocal().getWidth() <= 0){
            //System.out.println("Collision Over");
        }
    }

    private double[] findClosestSegmentBasedOnPosition(double xPosition, double yPosition, List<double[]> segments) {
        //Initialize variables
        double[] closestSegment = null;
        double minDistance = Double.MAX_VALUE;
        //Loop through checking for closest segment from its path segments
        for (double[] segment : segments) {
            //Finding which segment is closest
            double distance = pointToSegmentDistance(xPosition, yPosition, segment[0], segment[1], segment[2], segment[3]);
            if (distance < minDistance) {
                minDistance = distance;
                closestSegment = segment;
            }
        }
        //return closest segment
        return closestSegment;
    }

    private double pointToSegmentDistance(double pointX, double pointY, double startX, double startY, double endX, double endY) {
        double changeX = endX - startX;
        double changeY = endY - startY;
        //Check if the same
        if (changeX == 0 && changeY == 0) {
            changeX = pointX - startX;
            changeY = pointY - startY;
            return Math.sqrt(changeX * changeX + changeY * changeY);
        }

        double projectionOnSegment = ((pointX - startX) * changeX + (pointY - startY) * changeY) /
                (changeX * changeX + changeY * changeY);
        //confirm it is on the line
        projectionOnSegment = Math.max(0, Math.min(1, projectionOnSegment));
        //Calculate the closest pixel to where it is at on the line
        double closestX = startX + projectionOnSegment * changeX;
        double closestY = startY + projectionOnSegment * changeY;
        //Adjust the position of where it is to segment
        changeX = pointX - closestX;
        changeY = pointY - closestY;
        //return distance from segment
        return Math.sqrt(changeX * changeX + changeY * changeY);
    }

    private double calculateAngle(double startX, double startY, double endX, double endY) {
        double angle = Math.toDegrees(Math.atan2(endX - startX, endY - startY));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

    public Pane resizeImage(ImageView imageView, double maxWidth, double maxHeight) {
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(maxWidth);
        imageView.setFitHeight(maxHeight);

        Pane pane = new Pane();
        pane.getChildren().add(imageView);

        imageView.layoutXProperty().bind(pane.widthProperty().subtract(imageView.fitWidthProperty()).divide(2));
        imageView.layoutYProperty().bind(pane.heightProperty().subtract(imageView.fitHeightProperty()).divide(2));

        return pane;
    }

    public void createRoot(){
        ImageHelper imageHelper = new ImageHelper();
        Image map = imageHelper.getImage("./images/Updated 2 of 460 Traffic Map-2.png");
        ImageView fullMap = new ImageView(map);
        double imageW = map.getWidth();
        double imageH = map.getHeight();
        System.out.println("Original Image Width: " + imageW + " Original Image Height: " + imageH);

        Pane mapPane = resizeImage(fullMap, 1200, 800);
        root.getChildren().add(mapPane);

        Pane tempPane = new Pane();
        root.setOnMouseClicked(event -> {
            spawnVehicles(tempPane);
        });

        root.getChildren().add(tempPane);

    }

    public void spawnVehicles(Pane tempPane){
        for (int j = 0; j < 10; j++) {
            //Add vehicle class in
            Vehicle vehicle = new Vehicle();
            Shape car = vehicle.carShape();
            List<double[]> temp = vehicle.returnPathArray();
            Path path = vehicle.returnPath();
            double seconds = vehicle.returnSeconds();
            vehicleCollidables.add(vehicle);
            System.out.println(vehicleCollidables.size());

            //Initialize the Pane and Path Transition
            tempPane.getChildren().addAll(path, car);
            PathTransition pt = new PathTransition(Duration.seconds(seconds), path, car);
            pt.setDelay(Duration.seconds(0.2));
            pt.setCycleCount(1);
            pt.setInterpolator(Interpolator.LINEAR);
            pt.play();

            //Car Rotation Code
            double[] firstSegment = temp.get(0);
            car.setRotate(calculateAngle(firstSegment[0], firstSegment[1], firstSegment[2], firstSegment[3]));
            pt.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                //get car position
                double xPosition = car.getLayoutX() + car.getTranslateX();
                double yPosition = car.getLayoutY() + car.getTranslateY();
                //Now we are going to find which segment it is currently on
                double[] currentSegment = findClosestSegmentBasedOnPosition(xPosition, yPosition, temp);
                //Calculate angle based upon the current segment
                if (currentSegment != null) {
                    double angle = calculateAngle(currentSegment[0], currentSegment[1], currentSegment[2], currentSegment[3]);
                    car.setRotate(angle);
                }

            });
            pt.setOnFinished(event1 -> {
                tempPane.getChildren().removeAll(path, car);
            });
        }
    }
    public Pane getRoot(){
        return root;
    }


}
