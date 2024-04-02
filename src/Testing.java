import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Testing extends Application {

    private final Boolean DEBUG = false;

    private double[] polygonPoints = new double[100];
    private List<Double> polygonTemp = new ArrayList<>();
    private List<double[]> polygonTempArray = new ArrayList<>();
    private int count = 0;


    private List<double[]> allPossiblePaths = new ArrayList<>(); //Does not include Starting Paths
    private List<double[]> startingPaths = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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

        polygonTempArray.add(new double[4]);

        root.setOnMouseClicked(event -> {
            double[] temp = {event.getX(), event.getY()};
            polygonTemp.add(temp[0]);
            polygonTemp.add(temp[1]);
            if (DEBUG) System.out.println("X Position: " + temp[0] + " Y Position: " + temp[1]);
            int var1 = 0;
            if (polygonTemp.size() == 4) {
                for (Double value : polygonTemp) {
                    polygonPoints[count] = value;
                    polygonTempArray.get(0)[var1] = value;
                    count++;
                    var1++;
                }
                tempPane.getChildren().add(drawPossibilities(polygonTempArray));
                polygonTempArray.clear();
                polygonTemp.clear();
                polygonTempArray.add(new double[4]);
                System.out.println(DoubleArrayToString(polygonPoints));
                var1 = 0;
            }
        });

        root.getChildren().add(tempPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    public Line drawPossibilities(List<double[]> Path) {
        Line line = new Line();

        for (double[] singlePath : Path){
            //              Line(startX, startY, endX, endY);
            if (DEBUG) System.out.println("starX: " + singlePath[0] + " startY: " + singlePath[1] +
                    " endX: " + singlePath[2] + " endY: " + singlePath[3]);
            line = new Line(singlePath[0], singlePath[1], singlePath[2], singlePath[3]);
            if (DEBUG) System.out.println("starX: " + line.getStartX() + " startY: " + line.getStartY() +
                    " endX: " + line.getEndX() + " endY: " + line.getEndY());
        }
        System.out.println("Attempting to draw the line");

        return line;
    }

    public double[] reverseCoords(double mouseX, double mouseY, double imageWidth, double imageHeight) {
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

    private String DoubleArrayToString(double[] array){
        String temp = "[ ";
        int i = 0;
        for (double value : array){
            if (value != 0.0) {
                if (i == 0 ){
                    temp += "[" + value + ", ";
                }
                if (i > 0 && i < 3){
                    temp += value + ", ";
                }
                i++;
                if (i == 4){
                    temp += value + "] ";
                    i = 0;
                }
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

        double[] currentPath = startingPathList.remove(random.nextInt(startingPathList.size()));
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
