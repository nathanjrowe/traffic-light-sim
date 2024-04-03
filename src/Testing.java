import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Testing extends Application {

    private final Boolean DEBUG = false;

    private List<Double> polygonPoints = new ArrayList<>();
    private List<Double> polygonTemp = new ArrayList<>();

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

        root.setOnMouseClicked(event -> {
            double[] temp = {event.getX(), event.getY()};
            polygonTemp.add(temp[0]);
            polygonTemp.add(temp[1]);
            polygonPoints.add(temp[0]);
            polygonPoints.add(temp[1]);
            if (DEBUG) System.out.println("X Position: " + temp[0] + " Y Position: " + temp[1]);
            if (polygonTemp.size() == 4) {
                tempPane.getChildren().add(drawPossibilities(polygonTemp));
                polygonTemp.clear();
                if (DEBUG) System.out.println(DoubleArrayToString(polygonPoints));
            }
        });

        root.getChildren().add(tempPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
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

    private String DoubleArrayToString(List<Double> array){
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
