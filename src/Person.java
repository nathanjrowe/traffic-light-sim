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
 *  2D Pedestrian class object implementation
 */
public class Person {

    // PATHS is the list of viable paths used to guide the busses.
    private static final double[][] PATHS = {
            {333,247,333,500}, {194, 467, 383, 467}, {206, 345, 384, 345},
            {583, 238, 583, 501}, {681, 501, 681, 242}, {532, 153, 751, 153},
            {719, 91, 543, 91}, {108, 100, 360, 100}, {121, 591, 463, 591},
            {868, 529, 516, 529}, {244, 62, 244, 246}
    };

    private List<double[]> startingPaths = new ArrayList<>();
    private List<double[]> temp = new ArrayList<>();
    private List<CollisionBox> collidableBoxes = new ArrayList<>();
    private Path path;
    private double distance;
    private double seconds;
    private Boolean collided;
    private Boolean isCrossing = false;
    private PathTransition pathTransition;
    private Shape carShape;

    /**
     * Constructor
     * @param tempPane
     * @param collidablePerson
     */
    public Person(Pane tempPane, List<Person> collidablePerson, List<CollisionBox> collidableBoxes){
        initializeArrays();
        createPath();
        initializeCarShape();
        initializePathTransition(tempPane, collidablePerson);
        this.collided = false;
        this.collidableBoxes = collidableBoxes;
    }

   /**
     * Creates person shape
     * Each person is given a set radius here,
     * this function also references each path segment to determine
     * the orientation of each pedestrian as they travel.
     * 
     * same as in Bus.java and Bus3D.java
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
     * This function utilizes tempPane and collidablePerson, both obtained when the constructor
     * is first called for the pedestrian.
     * 
     * It then adds the path and person values to the proveded tempPane, and provided that both values exist
     * it will initialize the pathTransition with an event to remove the bus once it has completed the path once.
     * 
     * Initializes pedestrian path transition
     * 
     * is the same as Bus.java and Bus3D.java
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
     * A protected method that runs the path transition for a selected person
     * is the same as in Bus.java and Bus3D.java
     */
    protected void startAnimation() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    /**
     * A protected method that pauses the path transition for a selected person
     * is the same as in Bus.java and Bus3D.java
     */
    protected void stopVehicle() {
        if (pathTransition != null) {
            pathTransition.pause();
        }
    }

    /**
     * A protected method that restarts the path transition for a selected person
     * is the same as in Bus.java and Bus3D.java
     */
    protected void restartVehicle() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    /**
     * initializeArrays adds each viable path from PATHS into startingPaths
     * is the same as in Bus.java and Bus3D.java 
     */
    private void initializeArrays(){
        for (double[] array : PATHS){
            startingPaths.add(array);
        }
    }

    /**
     * Creates paths for people to follow
     * This function starts by randomly selecting a vaible path in the startingPaths array
     * it then initializes a path and sets the correct starting location and MoveTo element 
     * from startingPaths 0 and 1.
     * 
     * Looping through each element in the selected startingPath we create distance vectors
     * to represent the distance and direction of each step in the person's overall path.
     * 
     * The person's speed is also set here as a ratio of the distance traveled / 50
     * 
     * this is the same as in Bus.java and Bus3D.java
     */
    protected void createPath(){
        Random random = new Random();
        int tempInt = random.nextInt(PATHS.length);
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
     * * calculateAngle is a helper function designed to calculate
     * the angle of a pedestrian
     * based on which path segment it is currently on.
     * 
     * this is the same as in Bus.java and Bus3D.java
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
     * setCollided is a helper function used to set the collision
     * value of the person to the provided value.
     * this is the same as in Bus.java and Bus3D.java
     * @param bool
     */
    protected void setCollided(boolean bool){
        collided = bool;
    }

    /**
     * The following are temporary helper functions used while debugging.
     * They are simple return functions that provide various info about a
     * selected pedestrian's values.
     * 
     * @return
     */
    public boolean isCrossing() {
        return isCrossing;
    }
    public void setCrossing(boolean crossing) {
        this.isCrossing = crossing;
    }
    protected Path returnPath(){
        return path;
    }
    protected double returnSeconds(){
        return seconds;
    }
    protected List<double[]> returnPathArray(){
        return temp;
    }
    protected Shape returnCarShape() {
        return carShape;
    }
    protected boolean returnCollided(){
        return collided;
    }


    /*
     * Handle pedestrian collision with other objects
     */
    protected void checkCollision(){
        //System.out.println("Person Checking collison");
        for (CollisionBox box : collidableBoxes){
            //System.out.println("Collision Box state: " + box.getState());
            if (box.isColliding(carShape.getBoundsInParent())){
                if (box.getState() == CollisionBox.State.STOP && !this.isCrossing){
                    stopVehicle();
                }
                else {
                    restartVehicle();
                    this.isCrossing = true;
                }
            }
        }
    }
}
