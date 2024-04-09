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

public class Testing extends Application {
    //[startX, startY, endX, endY];
    private final double[][] INITIALPATHS = {
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

    private final double[][] RESTOFPATHS = {
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
            {626,412,1185,412}
            //{608,412,1185,412}

    };

    private final Boolean DEBUG = false;

    private List<Double> polygonPoints = new ArrayList<>();
    private List<Double> polygonTemp = new ArrayList<>();

    private List<double[]> allPossiblePaths = new ArrayList<>(); //Does not include Starting Paths
    private int allPathSize;
    private List<double[]> startingPaths = new ArrayList<>();

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        for (double[] array : INITIALPATHS){
            if (DEBUG) System.out.println("starX: " + array[0] + " startY: " + array[1] +
                    " endX: " + array[2] + " endY: " + array[3]);
            startingPaths.add(array);
        }
        for (double[] array : RESTOFPATHS){
            if (DEBUG) System.out.println("starX: " + array[0] + " startY: " + array[1] +
                    " endX: " + array[2] + " endY: " + array[3]);
            allPossiblePaths.add(array);
        }

        allPathSize = allPossiblePaths.size();

        StackPane root = new StackPane();

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

            for (int j = 0; j < 10; j++) {
                List<double[]> temp = generateRandomPath(allPossiblePaths, startingPaths);
                Path path = new Path();
                path.getElements().add(new MoveTo(temp.get(0)[0], temp.get(0)[1]));
                for (int i = 0; i < temp.size(); i++) {
                    double[] point = temp.get(i);
                    path.getElements().add(new LineTo(point[2], point[3]));
                }
                path.setOpacity(0);
                Circle car = new Circle(4);
                car.setFill(Color.GREEN);
                tempPane.getChildren().addAll(path, car);
                PathTransition pt = new PathTransition(Duration.seconds(2), path, car);
                pt.setDelay(Duration.seconds(0.2));
                pt.setCycleCount(1);
                pt.setInterpolator(Interpolator.LINEAR);
                pt.play();
            }
        });

        root.getChildren().add(tempPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    public Line arrayToLine(double[] array) {
        Line line = new Line(array[0], array[1], array[2], array[3]);
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(5.0);

        if (DEBUG) System.out.println("startX: " + array[0] + " startY: " + array[1] +
                " endX: " + array[2] + " endY: " + array[3]);
        if (DEBUG) System.out.println("starX: " + line.getStartX() + " startY: " + line.getStartY() +
                " endX: " + line.getEndX() + " endY: " + line.getEndY());
        if (DEBUG) System.out.println("Attempting to draw the line");

        return line;
    }
    public Line drawPossibilities(List<Double> Path) {
        Line line = new Line(Path.get(0), Path.get(1), Path.get(2), Path.get(3));

        if (DEBUG) System.out.println("starX: " + Path.get(0) + " startY: " + Path.get(1) +
                " endX: " + Path.get(2) + " endY: " + Path.get(3));
        if (DEBUG) System.out.println("starX: " + line.getStartX() + " startY: " + line.getStartY() +
                " endX: " + line.getEndX() + " endY: " + line.getEndY());
        if (DEBUG) System.out.println("Attempting to draw the line");

        return line;
    }

    public double[] reverseCords(double mouseX, double mouseY, double imageWidth, double imageHeight) {
        double originalX = mouseX * (imageWidth / 1200);
        double originalY = mouseY * (imageHeight / 800);

        return new double[]{originalX, originalY};
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

    public Pane resizePane(Pane sourcePane, double maxWidth, double maxHeight) {
        double scaleX = maxWidth / sourcePane.getPrefWidth();
        double scaleY = maxHeight / sourcePane.getPrefHeight();
        double scaleToFit = Math.min(scaleX, scaleY);

        sourcePane.setScaleX(scaleToFit);
        sourcePane.setScaleY(scaleToFit);

        Pane pane = new Pane();
        pane.getChildren().add(sourcePane);

        sourcePane.layoutXProperty().bind(pane.widthProperty().subtract(sourcePane.prefWidthProperty()).divide(2).multiply(scaleToFit));
        sourcePane.layoutYProperty().bind(pane.heightProperty().subtract(sourcePane.prefHeightProperty()).divide(2).multiply(scaleToFit));

        return pane;
    }

    private String arrayListToString(List<Double> array){
        String temp = "[";

        for (int i = 0; i < array.size(); i++){
            if (i % 4 == 0) {
                temp += "[";
            }
            temp += array.get(i);
            if (i % 4 == 3) {
                temp += "]";
                if (i < array.size() - 1) {
                    temp += ", ";
                }
            } else {
                temp += ", ";
            }
        }
        temp += "]";
        return temp;
    }

    private boolean pathConnects(double[] path1, double[] path2) {
        //Path1 endX == Path2 startX && Path1 endY == Path2 startY
        return path1[2] == path2[0] && path1[3] == path2[1];
    }

    private List<double[]> generateRandomPath(List<double[]> allPathsList, List<double[]> startingPathList) {
        Random random = new Random();

        List<double[]> path = new ArrayList<>();
        List<double[]> availablePaths = new ArrayList<>(allPathsList);
        List<double[]> startingTemp = new ArrayList<>(startingPathList);

        if (DEBUG) System.out.println(startingTemp.size());
        double[] currentPath = startingTemp.remove(random.nextInt(startingTemp.size()));
        path.add(currentPath);

        boolean pathFinished = false;
        while (!pathFinished) {
            pathFinished = true;
            List<double[]> potentialPaths = new ArrayList<>();
            for (double[] nextPath : availablePaths) {
//                if (pathConnects(currentPath, nextPath)) {
//                    path.add(nextPath);
//                    availableSegments.remove(nextPath);
//                    currentPath = nextPath;
//                    pathFinished = false;
//                    break;
//                }
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

}
