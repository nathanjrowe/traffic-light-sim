import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Testing extends Application {
    private final Boolean DEBUG = false;
    private List<Vehicle> vehicleCollidables = new ArrayList<>();

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
            for (int j = 0; j < 10; j++) {
                Vehicle vehicle = new Vehicle(tempPane);
                vehicle.startAnimation();
            }
        });

        root.getChildren().add(tempPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    private void checkCollision(Vehicle src, Vehicle other){
        if(Shape.intersect(src.carShape(), other.carShape()).getBoundsInLocal().getWidth() > -1){
            System.out.println("Collision Detected");
        }else if(Shape.intersect(src.carShape(), other.carShape()).getBoundsInLocal().getWidth() <= 0){
            //System.out.println("Collision Over");
        }
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


}
