import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.image.Image;//unused
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * 3D implementation of vehicle object
 * INITIALPATHS is the list of viable paths used to guide the busses.
 */
public class Vehicle3D {
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
            {289,10,289,108},{289,10,289,127},{269,10,269,108},

            //Biasing the Randomness towards main road spawns
            {1185,357,663,357},{1185,357,643,357},
            {1185,374,663,374},
            {1185,394,663,394},{1185,394,626,394},{1185,394,608,394},
            {13,412,305,412}, {13,412,324,412}, {13,412,269,412},
            {13,428,269,428},
            {13,445,269,445}, {13,445,289,445},
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

    private final List<CollisionBox> collisionBoxes = new ArrayList<>();
    private List<double[]> allPossiblePaths = new ArrayList<>(); //Does not include Starting Paths
    private int allPathSize;//unused
    private List<double[]> startingPaths = new ArrayList<>();
    private double distance;
    private double seconds;
    private List<double[]> temp;
    private Path path;
    private Boolean collided;
    private PathTransition pathTransition;
    private Shape carShape;
    private Shape frontSensor;
    ImageHelper imageHelper = new ImageHelper();
    private Pane carGroup;//unused
    private CollisionBox collidedBox = null;
    private Vehicle3D collidedVehicle = null;
    private Boolean stoppedAtLight = false;
    private Group cars = new Group();
    private List<String> directions = new ArrayList<>();
    private double[] tempSegment;
    private double[] previousSegment;
    private long timeInIntersection = 0;
    private long timeAtStopLight = 0;
    /**
     * Constructor
     * @param tempPane
     * @param collidableVehicles
     */
    public Vehicle3D(Pane tempPane, List<Vehicle3D> collidableVehicles, List<CollisionBox> collisionBoxes) {
        initializeArrays();
        createPath();
        car();
        initializePathTransition(tempPane, collidableVehicles);
        this.collided = false;
        this.collisionBoxes.addAll(collisionBoxes);
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
     * As we've seen with the differences between Bus.java and Bus3D.java, and
     * Person.java and Person3D.java, Vehicle.java and Vehicle3D.java have a
     * different initialization where the 3D version requires its models to be
     * loaded and headlights created.
     *
     * @return
     */
    private Group car(){
        carShape = new Rectangle(15, 8);
        frontSensor = new Rectangle(8,8);
        carShape.setFill(Color.GREEN);
        frontSensor.setFill(Color.RED);
        frontSensor.setTranslateX(3.5);
        frontSensor.setTranslateY(11.5);
        carShape.setRotate(90);

        ObjModelImporter importes = new ObjModelImporter();
        String[] vehicles = new String[]{
                "/vehicleModels/NormalCar1.obj",
                "/vehicleModels/NormalCar2.obj",
                "/vehicleModels/SportsCar.obj",
                "/vehicleModels/SportsCar2.obj",
                "/vehicleModels/SUV.obj",
                "/vehicleModels/Taxi.obj",
                "/vehicleModels/Cop.obj"};
        Random random = new Random();

        String carChoice = vehicles[random.nextInt(7)];
        try {
            importes.read(this.getClass().getResource(carChoice));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();

        Sphere headLightL = new Sphere(.15);
        Sphere headLightR = new Sphere(.15);
        Sphere tailLightL = new Sphere(.15);
        Sphere tailLightR = new Sphere(.15);

        PhongMaterial headLightMat = new PhongMaterial();
        headLightMat.setSelfIlluminationMap(imageHelper.getImage("./images/white.png"));

        PhongMaterial tailLightMat = new PhongMaterial();
        tailLightMat.setSelfIlluminationMap(imageHelper.getImage("./images/red.png"));

        headLightL.setMaterial(headLightMat);
        headLightR.setMaterial(headLightMat);

        tailLightL.setMaterial(tailLightMat);
        tailLightR.setMaterial(tailLightMat);

        if(carChoice.equals("/vehicleModels/NormalCar2.obj")) {
            headLightL.setTranslateX(-.5);
            headLightL.setTranslateY(-.4);
            headLightL.setTranslateZ(-1.5);

            headLightR.setTranslateX(.5);
            headLightR.setTranslateY(-.4);
            headLightR.setTranslateZ(-1.5);

            tailLightL.setTranslateX(-.5);
            tailLightL.setTranslateY(-.4);
            tailLightL.setTranslateZ(1.45);

            tailLightR.setTranslateX(.5);
            tailLightR.setTranslateY(-.4);
            tailLightR.setTranslateZ(1.45);
        }
        else if(carChoice.equals("/vehicleModels/NormalCar1.obj")) {
            headLightL.setTranslateX(-.5);
            headLightL.setTranslateY(-.4);
            headLightL.setTranslateZ(-1.95);

            headLightR.setTranslateX(.5);
            headLightR.setTranslateY(-.4);
            headLightR.setTranslateZ(-1.95);

            tailLightL.setTranslateX(-.5);
            tailLightL.setTranslateY(-.4);
            tailLightL.setTranslateZ(1.9);

            tailLightR.setTranslateX(.5);
            tailLightR.setTranslateY(-.4);
            tailLightR.setTranslateZ(1.9);
        }

        else if(carChoice.equals("/vehicleModels/Taxi.obj") || carChoice.equals("/vehicleModels/SUV.obj")) {
            headLightL.setTranslateX(-.5);
            headLightL.setTranslateY(-.4);
            headLightL.setTranslateZ(-2.1);

            headLightR.setTranslateX(.5);
            headLightR.setTranslateY(-.4);
            headLightR.setTranslateZ(-2.1);

            tailLightL.setTranslateX(-.5);
            tailLightL.setTranslateY(-.4);
            tailLightL.setTranslateZ(1.9);

            tailLightR.setTranslateX(.5);
            tailLightR.setTranslateY(-.4);
            tailLightR.setTranslateZ(1.9);
        }

        else if(carChoice.equals("/vehicleModels/Cop.obj")) {
            headLightL.setTranslateX(-.5);
            headLightL.setTranslateY(-.4);
            headLightL.setTranslateZ(-1.7);

            headLightR.setTranslateX(.5);
            headLightR.setTranslateY(-.4);
            headLightR.setTranslateZ(-1.7);

            tailLightL.setTranslateX(-.5);
            tailLightL.setTranslateY(-.4);
            tailLightL.setTranslateZ(1.65);

            tailLightR.setTranslateX(.5);
            tailLightR.setTranslateY(-.4);
            tailLightR.setTranslateZ(1.65);
        }

        else if(carChoice.equals("/vehicleModels/SportsCar2.obj") || carChoice.equals("/vehicleModels/SportsCar.obj")) {
            headLightL.setTranslateX(-.5);
            headLightL.setTranslateY(-.4);
            headLightL.setTranslateZ(-1.75);

            headLightR.setTranslateX(.5);
            headLightR.setTranslateY(-.4);
            headLightR.setTranslateZ(-1.75);

            tailLightL.setTranslateX(-.5);
            tailLightL.setTranslateY(-.4);
            tailLightL.setTranslateZ(1.75);

            tailLightR.setTranslateX(.5);
            tailLightR.setTranslateY(-.4);
            tailLightR.setTranslateZ(1.75);

        }


        Group group3 = new Group();

        PointLight headLights = new PointLight(Color.WHITE);
        headLights.setScaleX(.01);
        headLights.setScaleY(.01);
        headLights.setScaleZ(.01);

        group3.getChildren().addAll(meshViewss);
        group3.getChildren().addAll(headLightL, headLightR, tailLightL, tailLightR);
        group3.setScaleX(5);
        group3.setScaleY(5);
        group3.setScaleZ(18);


        //group.setTranslateY(1000);
        group3.setTranslateZ(0);
        group3.setTranslateY(0);
        group3.setTranslateX(7);
        group3.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        cars.getChildren().addAll(group3, carShape, frontSensor);

        cars.prefWidth(8);
        cars.setTranslateY(-110);
        return cars;
    }
    /**
     * Runs vehicles on paths
     * This function utilizes tempPane and collidableVehicles, both obtained when the constructor
     * is first called for the vehicle.
     * 
     * It then adds the path and carShape values to the provided tempPane, and provided that both values exist
     * it will initialize the pathTransition with an event to remove the bus once it has completed the path once.
     * 
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
     * @param tempPane
     * @param collidableVehicles
     */
    private void initializePathTransition(Pane tempPane, List<Vehicle3D> collidableVehicles) {
        tempPane.getChildren().addAll(path,cars);
        if (path != null && cars != null) {
            pathTransition = new PathTransition(Duration.millis(seconds*1000), path, cars);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.setCycleCount(1);

            //Car Rotation Code
            pathTransition.currentTimeProperty().addListener((obs, old, current) -> {
                double xPosition = cars.getLayoutX() + cars.getTranslateX();
                double yPosition = cars.getLayoutY() + cars.getTranslateY();
                double[] currentSegment = findClosestSegmentBasedOnPosition(xPosition, yPosition, temp);

                if (tempSegment == null && !checkDirections(temp.get(0), currentSegment, previousSegment)){
                    tempSegment = currentSegment.clone();
                }
                if (tempSegment != null && checkDirections(currentSegment, tempSegment, previousSegment)){
                    previousSegment = tempSegment.clone();
                    tempSegment = currentSegment.clone();
                    if (directions.size() > 0) {
                        directions.remove(0);
                    }
                }
                if (currentSegment != null) {
                    double angle = calculateAngle(currentSegment[0], currentSegment[1], currentSegment[2], currentSegment[3]);
                    cars.setRotate(-angle);
                }
            });

            pathTransition.setOnFinished(event -> {
                tempPane.getChildren().removeAll(path,cars);
                collidableVehicles.remove(this);
            });
        }
    }

    /**
     * Removes the vehicle that was in an accident. Called by traffic scene since we need the streetscene
     * @param streetScene
     * @param collidableVehicles
     */
    public void removeVehicle(Pane streetScene,  List<Vehicle3D> collidableVehicles){

        try {
            streetScene.getChildren().removeAll(path, cars);
            collidableVehicles.removeIf(this::equals);
        }
        catch (Exception e){

        }

      /*  Iterator as = collidableVehicles.iterator();
        for (Iterator it = as; it.hasNext(); ) {
            Vehicle3D vehicles = (Vehicle3D) it.next();
            if (vehicles == vehicle3D) {
                it.remove();
            }
        }*/
    }

    private boolean checkDirections(double[] current, double[] held, double[] previous){
        if (held[0] != current[0] || held[1] != current[1] ||
                held[2] != current[2] || held[3] != current[3]){
            if (previous == null){
                return true;
            }
            if (held[0] != previous[0] || held[1] != previous[1] ||
                    held[2] != previous[2] || held[3] != previous[3]){
                return true;
            }
        }
        return false;
    }

    /**
     * A protected method that runs the path transition for a selected vehicle
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
     */
    protected void startAnimation() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }
    /**
     * A protected method that pauses the path transition for a selected vehicle
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
     */
    protected void stopVehicle() {
        if (pathTransition != null) {
            pathTransition.pause();
        }
    }
    /**
     * A protected method that restarts the path transition for a selected vehicle
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
     * 
     */
    protected void restartVehicle() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    /**
     * Creates paths for vehicles to follow
     * This function starts by randomly selecting a vaible path in the startingPaths array
     * it then initializes a path and sets the correct starting location and MoveTo element 
     * from startingPaths 0 and 1.
     * 
     * Looping through each element in the selected startingPath we create distance vectors
     * to represent the distance and direction of each step in the vehicles' overall path.
     * 
     * The vehicles' speed is also set here as a ratio of the distance traveled / 100
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
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

            //Direction List
            if (i < temp.size() - 1){
                double x3 = temp.get(i+1)[2];
                double y3 = temp.get(i+1)[3];
                //Enter from Top
                if (startX == endX && startY < endY){
                    if (endX < x3 && endY == y3){
                        directions.add("Left");
                    } else if (endX == x3 && endY < y3) {
                        directions.add("Straight");
                    } else if (endX > x3 && endY == y3) {
                        directions.add("Right");
                    }
                }
                //Enter from Left
                else if (startX < endX && startY == endY) {
                    if (endX == x3 && endY > y3){
                        directions.add("Left");
                    } else if (endX < x3 && endY == y3) {
                        directions.add("Straight");
                    } else if (endX == x3 && endY < y3) {
                        directions.add("Right");
                    }
                }
                //Enter from Bottom
                else if (startX == endX && startY > endY) {
                    if (endX > x3 && endY == y3){
                        directions.add("Left");
                    } else if (endX == x3 && endY > y3) {
                        directions.add("Straight");
                    } else if (endX < x3 && endY == y3) {
                        directions.add("Right");
                    }
                }
                //Enter from Right
                else if (startX > endX && startY == endY) {
                    if (endX == x3 && endY < y3){
                        directions.add("Left");
                    } else if (endX > x3 && endY == y3) {
                        directions.add("Straight");
                    } else if (endX == x3 && endY > y3) {
                        directions.add("Right");
                    }
                }
            }

            startX = point[2];
            startY = point[3];
        }
        //This is where you edit the Speed
        seconds = distance / 100;
        path.setOpacity(0);
    }

    private Path createNewPath(double x){
        Path path = new Path();

        path.getElements().add(new MoveTo(x, -3000));
        path.getElements().add(new LineTo(x, -50));
        path.getElements().add(new LineTo(-x, -50));
        path.getElements().add(new LineTo(-x, -3000));
        return path;
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
     * The vehicle paths are much more complex than the bus and pedestrian
     * paths so this is a helper function designed to generate a viable
     * path from the available nodes.
     * same as in Vehicle.java
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
     * calculateAngle is a helper function designed to calculate the angle of a vehicle
     * based on which path segment it is currently on.
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
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


    /*
     * This is a collision checker similar to the one in Person.java that stops and starts
     * vehicles based on wheter or not their collision boxes overlap.
     * same as in Vehicle.java
     */
        protected void checkCollision(List<Vehicle3D> vehicles) {
        //Get the bounds of the front sensor
        Bounds frontBoundsInGrandParent = getBoundsInGrandparent(frontSensor);
        //Check for collisions every frame
        //Print the bounds of the car
        if(collidedBox != null) {
            if(collidedBox.getState() == CollisionBox.State.GO &&
                    (returnCurrentDirection().matches("Straight")) || returnCurrentDirection().matches("Right")) {
                if(System.currentTimeMillis() - timeAtStopLight >= 2500 && stoppedAtLight) {
                    TrafficScene.setIllegalMove("Hey fuck", collidedBox.getX(), collidedBox.getY());
                }
                else {
                    this.collided = false;
                    this.stoppedAtLight = false;
                    this.restartVehicle();
                    this.collidedBox = null;
                }

            } else if (collidedBox.getState() == CollisionBox.State.LEFT && returnCurrentDirection().matches("Left")) {
                System.out.println("Left Turn Lane");
                if(System.currentTimeMillis() - timeAtStopLight >= 2500 && stoppedAtLight){
                    TrafficScene.setIllegalMove("Hey fuck", collidedBox.getX(), collidedBox.getY());
                }
                else {
                    this.collided = false;
                    this.stoppedAtLight = false;
                    this.restartVehicle();
                    this.collidedBox = null;
                }

            } else if (collidedBox.getState() == CollisionBox.State.RIGHT && returnCurrentDirection().matches("Right")) {
                System.out.println("Right Turn Lane");
                if(System.currentTimeMillis() - timeAtStopLight >= 2500 && stoppedAtLight){
                    TrafficScene.setIllegalMove("Hey fuck", collidedBox.getX(), collidedBox.getY());
                }
                else {
                    this.collided = false;
                    this.stoppedAtLight = false;
                    this.restartVehicle();
                    this.collidedBox = null;
                }

            }
        }
        for (CollisionBox collisionBox : collisionBoxes) {
            if (frontBoundsInGrandParent.intersects(collisionBox.getBoundsInParent())) {
                this.collidedBox = collisionBox;
                if(collisionBox.getState() == CollisionBox.State.STOP) {
                    this.collided = true;
                    this.stoppedAtLight = true;
                    this.stopVehicle();
                    this.setTimeAtStoplight(System.currentTimeMillis());
                    if(System.currentTimeMillis() - timeAtStopLight >= 2500 && stoppedAtLight){
                        TrafficScene.setIllegalMove("Hey move", collidedBox.getX(), collidedBox.getY());
                    }
                }
                break;
            }
        }
        //Check for collisions with other vehicles
        if(collidedVehicle != null) {
            //int s = cars.getChildren().indexOf(carShape);
            Bounds vehicleBoundsInGrandParent = getBoundsInGrandparent(
                    collidedVehicle.returnCarShape());
            if (!frontBoundsInGrandParent.intersects(vehicleBoundsInGrandParent) && stoppedAtLight) {
                collidedVehicle = null;
                collided = false;
                stoppedAtLight = false;
                restartVehicle();
            }
            if (!frontBoundsInGrandParent.intersects(vehicleBoundsInGrandParent) && !stoppedAtLight) {
                collidedVehicle = null;
                collided = false;
                restartVehicle();
            }
        }
        else {
            for (Vehicle3D vehicle : vehicles) {
                if (vehicle != this) {
                    Bounds vehicleBoundsInGrandParent = getBoundsInGrandparent(vehicle.returnCarShape());
                    if (frontBoundsInGrandParent.intersects(vehicleBoundsInGrandParent)) {
                        collidedVehicle = vehicle;
                        if(vehicle.returnStoppedAtLight()) {
                            collided = true;
                            stoppedAtLight = true;
                            //TrafficScene.setData("Stopped", 1);
                            stopVehicle();
                            //vehicle.setTimeAtStoplight(System.currentTimeMillis());
                        }
                        stopVehicle();
                        collided = true;
                      /*  if(System.currentTimeMillis() - timeatStopLight >= 500 ){
                            TrafficScene.setData("Hey jerk", 1);
                        }*/
                        break;
                    }
                }
            }
        }
    }

    /**
     * setCollided is a helper function used to set the collision value of the bus to the provided value.
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java
     * @param bool
     */
    protected void setCollided(boolean bool){
        collided = bool;
    }
    /**
     * The following are temporary helper functions used while debugging.
     * They are simple return functions that provide a bus' path, seconds, pathArr, carShape, and collided
     * values.
     * is the same as Bus.java, Bus3D.java, Person.java, and Person3D.java

     * @return
     */
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
    protected boolean returnStoppedAtLight() {
        return stoppedAtLight;
    }
    protected String returnCurrentDirection(){
        if (directions.size() > 0) {
            return directions.get(0);
        }
        else {
            return "None";
        }
    }

    /*
     * This getter is slightly more complex than the rest, it checks the 
     * grandparent node for a bounds box and returns the result.
     */
    protected Bounds getBoundsInGrandparent(Node node) {
        Bounds nodeInParent = node.localToParent(node.getBoundsInLocal());
        return node.getParent().localToParent(nodeInParent);
    }

    public long getTimeInIntersection(){
        return timeInIntersection;
    }

    public void setTimeInIntersection(long time){
        timeInIntersection = time;
    }

    public long getTimeatStopLight(){
        return timeAtStopLight;
    }

    public void setTimeAtStoplight(long time){
        timeAtStopLight = time;
    }
    
}
