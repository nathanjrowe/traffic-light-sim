import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.util.List;

public class Testing extends Application {

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

        root.setOnMouseClicked(event -> {
            double temp[] = reverseCoords(event.getX(), event.getY(), imageW, imageH);
            System.out.println("X Position: " + temp[0] + " Y Position: " + temp[1]);
        });

        root.getChildren().add(mapPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Viewer");
        primaryStage.show();
    }

    public Pane drawPossibilities(List<Integer[]> Path) {
        Pane pane = new Pane();

        for (Integer[] singlePath : Path){
            //              Line(startX, startY, endX, endY);
            Line line = new Line(singlePath[0], singlePath[1], singlePath[2], singlePath[3]);
            pane.getChildren().add(line);
        }

        return pane;
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
}
