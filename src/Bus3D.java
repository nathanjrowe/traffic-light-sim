import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class Bus3D {

    private static final double[][] PATHS = {
            {13,570,1185,570},{1185,552,13,552}
    };

    private List<double[]> startingPaths = new ArrayList<>();
    private List<double[]> temp = new ArrayList<>();
    private Path path;
    private double distance;
    private double seconds;
    private Boolean collided;
    private PathTransition pathTransition;
    private Shape carShape;
    private Group buses = new Group();

    public Bus3D(Pane tempPane, List<Bus> collidableBus){
        initializeArrays();
        createPath();
        initializeCarShape();
        initializePathTransition(tempPane, collidableBus);
        this.collided = false;
    }

    private void initializeCarShape() {
        bus();
        carShape = new Rectangle(8, 50);
        //Set initial angle based on the first segment
        if (!temp.isEmpty()) {
            double[] firstSegment = temp.get(0);
            carShape.setRotate(calculateAngle(firstSegment[0], firstSegment[1], firstSegment[2], firstSegment[3]));
        }
    }

    private Group bus(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/vehicleModels/School Bus.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();

        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(8);
        group3.setScaleY(8);
        group3.setScaleZ(35);

        //group.setTranslateY(1000);
        group3.setTranslateZ(-45);
        group3.setTranslateY(0);
        group3.setTranslateX(10);
        group3.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        buses = group3;
        buses.setTranslateY(-100);
        return group3;
    }

    private void initializePathTransition(Pane tempPane, List<Bus> collidableBus) {
        tempPane.getChildren().addAll(path,buses);
        if (path != null && buses != null) {
            pathTransition = new PathTransition(Duration.millis(seconds*1000), path, buses);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.setCycleCount(1);

            pathTransition.setOnFinished(event -> {
                tempPane.getChildren().removeAll(path,buses);
                collidableBus.remove(this);
            });
        }
    }

    protected void startAnimation() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    protected void stopVehicle() {
        if (pathTransition != null) {
            pathTransition.pause();
        }
    }

    protected void restartVehicle() {
        if (pathTransition != null) {
            pathTransition.play();
        }
    }

    private void initializeArrays(){
        for (double[] array : PATHS){
            startingPaths.add(array);
        }
    }

    protected void createPath(){
        Random random = new Random();
        int tempInt = random.nextInt(2);
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
        seconds = distance / 200;
        path.setOpacity(0);
    }

    protected double calculateAngle(double startX, double startY, double endX, double endY) {
        double angle = Math.toDegrees(Math.atan2(endX - startX, endY - startY));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

    protected void setCollided(boolean bool){
        collided = bool;
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

}

