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

public class Person {

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

    public Person(Pane tempPane, List<Person> collidablePerson){
        initializeArrays();
        createPath();
        initializeCarShape();
        initializePathTransition(tempPane, collidablePerson);
        this.collided = false;
    }

    private void initializeCarShape() {
        carShape = new Circle(4);
        carShape.setFill(Color.GREEN);
        //Set initial angle based on the first segment
        if (!temp.isEmpty()) {
            double[] firstSegment = temp.get(0);
            carShape.setRotate(calculateAngle(firstSegment[0], firstSegment[1], firstSegment[2], firstSegment[3]));
        }
    }

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
