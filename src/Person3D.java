import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * 3D implementation of pedestrian object
 */
public class Person3D {

    private static final double[][] PATHS = {
            {333,247,333,500}, {194, 467, 383, 467}, {206, 345, 384, 345},
            {583, 238, 583, 501}, {681, 501, 681, 242}, {532, 153, 751, 153},
            {719, 91, 543, 91}, {108, 100, 360, 100}, {121, 591, 463, 591},
            {868, 529, 516, 529}, {244, 62, 244, 246}
    };

    private List<double[]> startingPaths = new ArrayList<>();
    private List<double[]> temp = new ArrayList<>();
    private Path path;
    private double distance;
    private double seconds;
    private Boolean collided;
    private PathTransition pathTransition;
    private Shape carShape;

    /**
     * Constructor
     * @param tempPane
     * @param collidablePerson
     */
    public Person3D(Pane tempPane, List<Person> collidablePerson){
        initializeArrays();
        createPath();
        initializeCarShape();
        initializePathTransition(tempPane, collidablePerson);
        this.collided = false;
    }

    /**
     * Initialize 3D pedestrian object
     */
    private void initializeCarShape() {
        carShape = new Circle(4);
        carShape.setFill(Color.GREEN);
        //Set initial angle based on the first segment
        if (!temp.isEmpty()) {
            double[] firstSegment = temp.get(0);
            carShape.setRotate(calculateAngle(firstSegment[0], firstSegment[1], firstSegment[2], firstSegment[3]));
        }
    }

    /**
     * Initialize path transitions
     * @param tempPane
     * @param collidablePerson
     */
    private void initializePathTransition(Pane tempPane, List<Person> collidablePerson) {
        tempPane.getChildren().addAll(path,carShape);
        if (path != null && carShape != null) {
            pathTransition = new PathTransition(Duration.millis(seconds*1000), path, carShape);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.setCycleCount(1);

            pathTransition.setOnFinished(event -> {
                tempPane.getChildren().removeAll(path,carShape);
                collidablePerson.remove(this);
            });
        }
    }

    /**
     * Starts 3D pedestrian path transition animation
     */
    protected void startAnimation() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }
    /**
     * Stops 3D pedestrian path transition animation
     */
    protected void stopVehicle() {
        if (pathTransition != null) {
            pathTransition.pause();
        }
    }
    /**
     * Restarts 3D pedestrian path transition animation
     */
    protected void restartVehicle() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    /**
     * Initialize paths array
     */
    private void initializeArrays(){
        for (double[] array : PATHS){
            startingPaths.add(array);
        }
    }

    /**
     * Creates 3D pedestrian paths
     */
    protected void createPath(){
        Random random = new Random();
        int tempInt = random.nextInt(PATHS.length);
        System.out.println("Choosing Path number: " + tempInt);
        temp.add(startingPaths.get(tempInt));
        path = new Path();
        path.getElements().add(new MoveTo(temp.get(0)[0], temp.get(0)[1]));
        double startX = temp.get(0)[0];
        double startY = temp.get(0)[1];
        for (int i = 0; i < temp.size(); i++) {
            double[] point = temp.get(i);
            path.getElements().add(new LineTo(point[2], point[3]));
            double endX = point[2];
            double endY = point[3];
            distance += abs((endX - startX)) + abs((endY - startY));
            startX = point[2];
            startY = point[3];
        }
        //This is where you edit the Speed
        seconds = distance / 50;
        path.setOpacity(0);
    }

    /**
     * Updates 3D pedestrian object to face the right direction
     * by calculating the angles of paths
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    protected double calculateAngle(double startX, double startY, double endX, double endY) {
        double angle = Math.toDegrees(Math.atan2(endX - startX, endY - startY));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

    /**
     * Updates the 3D pedestrian collided boolean
     * @param bool
     */
    protected void setCollided(boolean bool){
        collided = bool;
    }

    /**
     * Returns the 3D pedestrian path
     * @return
     */
    protected Path returnPath(){
        return path;
    }

    /**
     * returns seconds for path transition
     * @return
     */
    protected double returnSeconds(){
        return seconds;
    }

    /**
     * returns path array
     * @return
     */
    protected List<double[]> returnPathArray(){
        return temp;
    }

    /**
     * returns 3D pedestrian shape
     * @return
     */
    protected Shape returnCarShape() {
        return carShape;
    }

    /**
     * returns 3D pedestrians collided boolean
     * @return
     */
    protected boolean returnCollided(){
        return collided;
    }

}
