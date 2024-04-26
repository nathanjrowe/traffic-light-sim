import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.abs;
import javafx.geometry.Bounds;

public class Vehicle extends Node{

    //List of CollisionBoxes
    private final List<CollisionBox> collisionBoxes = new ArrayList<>();
    private static final double[][] INITIALPATHS = {
            /**Left Side Starting*/
            //Straight/Right First Lane, Right Second Lane, Left First Lane, Left Second Lane
            {13,127,269,127}, {13,127,289,127},{13,127,305,127},{13,127,324,127},
            //Left first lane, Left Second Lane, Straight
            {13,412,305,412}, {13,412,324,412}, {13,412,269,412},
            //Straight
            {13,428,269,428},
            //Straight/Right first lane, Right Second Lane
            {13,445,269,445}, {13,445,289,445},
            //Left first Lane, Left Second Lane, Straight/Right first lane, Right Second Lane
            {13,677,305,677},{13,677,324,677},{13,677,269,677}, {13,677,289,677},
            /**Bottom Side Starting**/
            //Left Turn, Straight Left Lane, Straight Right Lane/Right Turn
            {305,745,305,659},{305,745,305,677}, {324,745,324,677},
            //Left Turn, Straight Left Lane, Straight Right Lane/Right Turn
            {643,745,643,659},{643,745,643,677}, {663,745,663,677},
            /**Right Side Starting*/
            //Straight/Right First Lane, Right Second Lane, Left First Lane, Left Second Lane
            {1185,659,663,659},{1185,659,643,659},{1185,659,626,659},{1185,659,608,659},
            //Straight/Right first lane, Right second lane
            {1185,357,663,357},{1185,357,643,357},
            //Straight
            {1185,374,663,374},
            //Straight, Left first Lane, Left Second Lane
            {1185,394,663,394},{1185,394,626,394},{1185,394,608,394},
            //Straight/Right first lane, Right Second Lane, Left first lane, Left Second lane
            {1185,108,663,108},{1185,108,643,108},{1185,108,626,108},{1185,108,608,108},
            /**Top Side Starting*/
            //Straight Left Lane, Left Turn, Straight Right Lane/Right Turn
            {626,10,626,108},{626,10,626,127},{608,10,608,108},
            //Straight
            {466,10,466,127},
            //Straight
            //Potentially wrong
            {412,10,412,108},
            //Straight Left Lane, Left Turn, Straight/Right Turn
            {289,10,289,108},{289,10,289,127},{269,10,269,108}
    };

    private static final double[][] RESTOFPATHS = {
            {269,127,428,127},{428,127,428,10},{305,127,305,10},{324,127,324,10},{269,127,269,357},{289,127,289,357},
            {269,357,269,659},{289,357,289,659},{269,659,269,745},{289,659,289,745},{269,357,13,357},{269,108,269,374},
            {269,108,269,394},{289,108,289,412},{289,108,289,428},{289,108,289,445},{269,374,13,374},{269,394,13,394},
            {269,659,13,659},{269,108,13,108},{289,412,608,412},{289,428,608,428},{289,445,608,445},{289,357,289,677},
            {289,412,643,412},{289,412,663,412},{289,445,626,445},{608,445,608,659},{626,445,626,659},{626,445,626,677},
            {626,659,466,659},{466,659,466,745},{626,677,1185,677},{626,659,626,745},{608,659,608,745},{608,428,1185,428},
            {608,412,1185,412},{608,445,1185,445},{643,412,643,127},{643,412,643,108},{643,108,484,108},{484,108,484,10},
            {643,127,643,10},{663,412,663,127},{663,127,1185,127},{663,127,663,10},{269,412,608,412},{269,428,608,428},
            {269,445,608,445},{324,108,13,108},{663,108,484,108},{663,357,324,357},{663,374,324,374},{663,394,324,394},
            {324,357,13,357},{324,374,13,374},{324,394,13,394}, {663,357,305,357},{305,357,305,127},{324,357,324,127},
            {324,127,428,127},{466,127,608,127},{466,127,626,127},{466,127,643,127},{466,127,663,127},{608,127,608,357},
            {608,127,608,374},{608,127,608,394},{608,357,324,357},{608,357,305,357},{608,374,324,374},{608,394,324,394},
            {608,394,289,394},{608,394,269,394},{269,394,269,659},{289,394,289,659},{289,394,289,677},{289,677,289,745},
            {269,677,269,745},{608,108,484,108},{608,108,608,357},{608,108,608,374},{608,108,608,394},{626,108,626,357},
            {626,108,626,412},{626,108,626,428},{626,108,626,445},{626,357,626,659},{626,357,626,677},{663,677,1185,677},
            {663,677,663,445},{663,677,663,428},{663,677,663,412},{663,445,1185,445},{663,428,1185,428},{663,412,1185,412},
            {663,445,663,108},{643,677,643,445},{643,659,466,659},{643,445,643,127},{643,445,643,108},{643,677,643,394},
            {643,677,643,374},{643,677,643,357},{643,394,324,394},{643,394,289,394},{643,394,269,394},{643,374,324,374},
            {643,357,324,357},{643,357,305,357},{324,677,324,445},{324,445,324,127},{324,445,608,445},{324,445,626,445},
            {324,677,324,428},{324,428,608,428},{324,677,324,412},{324,412,608,412},{324,412,643,412},{324,412,663,412},
            {305,659,13,659},{305,677,305,445},{305,677,305,394},{305,677,305,374},{305,677,305,357},{305,445,305,127},
            {305,445,305,108},{305,394,13,394},{305,374,13,374},{305,357,13,357},{663,659,663,445},{663,659,663,428},
            {663,659,663,412},{663,659,466,659},{626,394,626,659},{626,394,626,677},{305,412,305,127},{305,412,305,108},
            {626,428,1185,428},{412,108,324,108},{412,108,289,108},{412,108,269,108},{626,127,1185,127},{305,108,13,108},
            {626,412,1185,412},{269,677,412,677},{412,677,412,745}
    };
    private List<double[]> allPossiblePaths = new ArrayList<>(); //Does not include Starting Paths
    private int allPathSize;
    private List<double[]> startingPaths = new ArrayList<>();
    private double distance;
    private double seconds;
    private List<double[]> temp;
    private Path path;
    private Boolean collided;
    private Boolean stoppedAtLight = false;
    private PathTransition pathTransition;
    private Shape carShape;
    private Shape frontSensor;
    private Pane carGroup;
    private CollisionBox collidedBox = null;
    private Vehicle collidedVehicle = null;

    /**
     * Constructor
     * @param tempPane
     * @param collidableVehicles
     */
    public Vehicle(Pane tempPane, List<Vehicle> collidableVehicles, List<CollisionBox> collisionBoxes) {
        initializeArrays();
        createPath();
        initializeCarShape();
        initializePathTransition(tempPane, collidableVehicles);
        this.collisionBoxes.addAll(collisionBoxes);
        this.collided = false;
    }

    /**
     * Creates car object box
     */
    private void initializeCarShape() {
        //Create the car shape
        carShape = new Rectangle(8, 15);
        frontSensor = new Rectangle(8, 8);
        frontSensor.setTranslateY(15);
        carGroup = new Pane();
        carGroup.getChildren().addAll(carShape, frontSensor);
        frontSensor.setFill(javafx.scene.paint.Color.TRANSPARENT);
        //Set initial angle based on the first segment
        if (!temp.isEmpty()) {
            double[] firstSegment = temp.get(0);
            carGroup.setRotate(calculateAngle(firstSegment[0], firstSegment[1], firstSegment[2], firstSegment[3]));
        }
    }

    /**
     * initializes path transition
     * @param tempPane
     * @param collidableVehicles
     */
    private void initializePathTransition(Pane tempPane, List<Vehicle> collidableVehicles) {
        tempPane.getChildren().addAll(path, carGroup);
        //Collect CollisionBox objects from the root pane
        
        //Print out the number of collision boxes
        //System.out.println("Number of Collision Boxes: " + collisionBoxes.size());
        if (path != null && carShape != null) {
            pathTransition = new PathTransition(Duration.millis(seconds*1000), path, carGroup);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.setCycleCount(1);

            //Car Rotation Code
            pathTransition.currentTimeProperty().addListener((obs, old, current) -> {
                double xPosition = carGroup.getLayoutX() + carGroup.getTranslateX();
                double yPosition = carGroup.getLayoutY() + carGroup.getTranslateY();
                double[] currentSegment = findClosestSegmentBasedOnPosition(xPosition, yPosition, temp);
                if (currentSegment != null) {
                    double angle = calculateAngle(currentSegment[0], currentSegment[1], currentSegment[2], currentSegment[3]);
                    carGroup.setRotate(-angle);
                }
            });
            pathTransition.setOnFinished(event -> {
                tempPane.getChildren().removeAll(path, carGroup);
                collidableVehicles.remove(this);
            });
        }
    }

    /**
     * Starts 3D vehicle path transition animation
     */
    protected void startAnimation() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }
    /**
     * Stops vehicle path transition animation
     */
    protected void stopVehicle() {
        if (pathTransition != null) {
            pathTransition.pause();
        }
    }
    /**
     * Restarts vehicle path transition animation
     */
    protected void restartVehicle() {
        if (pathTransition != null) {
            //Resume after delay
            pathTransition.play();
        }
    }

    /**
     * Initializes path array
     */
    private void initializeArrays(){
        for (double[] array : INITIALPATHS){
            startingPaths.add(array);
        }
        for (double[] array : RESTOFPATHS){
            allPossiblePaths.add(array);
        }
    }

    /**
     * Creates path
     */
    protected void createPath(){
        temp = generateRandomPath(allPossiblePaths, startingPaths);
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
        seconds = distance / 200;
        path.setOpacity(0);
    }

    /**
     * Connects paths based on segments
     * @param path1
     * @param path2
     * @return
     */
    private boolean pathConnects(double[] path1, double[] path2) {
        //Path1 endX == Path2 startX && Path1 endY == Path2 startY
        return path1[2] == path2[0] && path1[3] == path2[1];
    }

    /**
     * Generates path segments and adds to path
     * @param allPathsList
     * @param startingPathList
     * @return list of segments
     */
    private List<double[]> generateRandomPath(List<double[]> allPathsList, List<double[]> startingPathList) {
        Random random = new Random();

        List<double[]> path = new ArrayList<>();
        List<double[]> availablePaths = new ArrayList<>(allPathsList);
        List<double[]> startingTemp = new ArrayList<>(startingPathList);

        double[] currentPath = startingTemp.remove(random.nextInt(startingTemp.size()));
        path.add(currentPath);

        boolean pathFinished = false;
        while (!pathFinished) {
            pathFinished = true;
            List<double[]> potentialPaths = new ArrayList<>();
            for (double[] nextPath : availablePaths) {
                if (pathConnects(currentPath, nextPath)) {
                    potentialPaths.add(nextPath);
                    pathFinished = false;
                }
            }
            if (!pathFinished) {
                double[] chosenPath = potentialPaths.get(random.nextInt(potentialPaths.size())).clone();
                path.add(chosenPath);
                availablePaths.remove(chosenPath);
                currentPath = chosenPath;
            }
        }
        return path;
    }

    /**
     * Primary function to go through all segments to find the closest one to vehicle
     * @param xPosition
     * @param yPosition
     * @param segments
     * @return
     */
    protected double[] findClosestSegmentBasedOnPosition(double xPosition, double yPosition, List<double[]> segments) {
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

    /**
     * Helper function to update car angle based on current path segment of vehicle.
     * @param pointX
     * @param pointY
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    protected double pointToSegmentDistance(double pointX, double pointY, double startX, double startY, double endX, double endY) {
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

    /**
     * Updates vehicle to the right direction based on path segment
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
     * Updates vehicle object collide boolean
     * @param bool
     */
    protected void setCollided(boolean bool){
        collided = bool;
    }

    /**
     * Returns path
     * @return
     */
    protected Path returnPath(){
        return path;
    }

    /**
     * Returns second for animation
     * @return
     */
    protected double returnSeconds(){
        return seconds;
    }

    /**
     * Returns path array
     * @return
     */
    protected List<double[]> returnPathArray(){
        return temp;
    }

    /**
     * Returns car shape box
     * @return
     */
    protected Shape returnCarShape() {
        return carShape;
    }

    /**
     * Returns vehicle collidable boolean
     * @return
     */
    protected boolean returnCollided(){
        return collided;
    }

    protected boolean returnStoppedAtLight() {
        return stoppedAtLight;
    }

    protected Bounds getBoundsInGrandparent(Node node) {
        Bounds nodeInParent = node.localToParent(node.getBoundsInLocal());
        return node.getParent().localToParent(nodeInParent);
    }
    //Take a root pane to check for collisions
    protected void checkCollision(List<Vehicle> vehicles) {
        //Get the bounds of the front sensor
        Bounds frontBoundsInGrandParent = getBoundsInGrandparent(frontSensor);
        //Check for collisions every frame
        //Print the bounds of the car
        if(collidedBox != null) {
            if(collidedBox.getState() != CollisionBox.State.STOP) {
                this.collided = false;
                this.stoppedAtLight = false;
                this.restartVehicle();
                this.collidedBox = null;
            }
        }
        for (CollisionBox collisionBox : collisionBoxes) {
            //System.out.println(collisionBox.getBoundsInParent());
            if (frontBoundsInGrandParent.intersects(collisionBox.getBoundsInParent())) {
                this.collidedBox = collisionBox;
                if(collisionBox.getState() == CollisionBox.State.STOP) {
                    this.collided = true;
                    this.stoppedAtLight = true;
                    this.stopVehicle();
                }
                break;
            }
        }

        //Check for collisions with other vehicles
        if(collidedVehicle != null) {
            Bounds vehicleBoundsInGrandParent = getBoundsInGrandparent(collidedVehicle.returnCarShape());
            if (!frontBoundsInGrandParent.intersects(vehicleBoundsInGrandParent)) {
                collidedVehicle = null;
                collided = false;
                stoppedAtLight = false;
                restartVehicle();
            }
        }
        else {
        for (Vehicle vehicle : vehicles) {
            if (vehicle != this) {
                Bounds vehicleBoundsInGrandParent = getBoundsInGrandparent(vehicle.returnCarShape());
                    if (frontBoundsInGrandParent.intersects(vehicleBoundsInGrandParent)) {
                        collidedVehicle = vehicle;
                        if(vehicle.returnStoppedAtLight()) {
                            collided = true;
                            stoppedAtLight = true;
                            stopVehicle();
                        }
                        //End the loop
                        break;
                    }
            }
        }
    }
    }
}
