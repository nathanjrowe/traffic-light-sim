import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Testing extends Application {
    private final Boolean DEBUG = false;
    private final Boolean getCoordinates = true;
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

        AtomicInteger clickCount = new AtomicInteger();
        root.setOnMouseClicked(event -> {
            if (getCoordinates) {
                double[] temp = {event.getX(), event.getY()};
                System.out.println("X Position: " + temp[0] + ", Y Position: " + temp[1] + ", Dot Number: " + clickCount.get());
                Circle circle = new Circle(event.getX(), event.getY(),2);
                circle.setFill(Color.RED);
                Text dotCount = new Text(event.getX() + 3, event.getY(), String.valueOf(clickCount.get()));
                dotCount.setFill(Color.RED);
                clickCount.getAndIncrement();
                tempPane.getChildren().addAll(circle, dotCount);
            }
            else {
                for (int j = 0; j < 10; j++) {
                    Vehicle vehicle = new Vehicle(tempPane, vehicleCollidables);
                    vehicle.startAnimation();
                    vehicleCollidables.add(vehicle);
                }
            }
        });

        startCollisionTimer();

        root.getChildren().add(tempPane);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    private void startCollisionTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkCollisions();
            }
        };
        timer.start();
    }

    private void checkCollisions() {
        for (int i = 0; i < vehicleCollidables.size(); i++) {
            for (int j = i + 1; j < vehicleCollidables.size(); j++) {
                Vehicle v1 = vehicleCollidables.get(i);
                Vehicle v2 = vehicleCollidables.get(j);
                //System.out.println(v2.returnCarShape().getBoundsInParent());
                if ((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth() > 0)
                        && !v1.returnCollided() && !v2.returnCollided()) {
                    System.out.println((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth()));
                    System.out.println((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent()));
                    //System.out.println("Collision Detected");
                    //v1.setCollided(true);
                    v2.setCollided(true);
                    //v1.stopVehicle();
                    v2.stopVehicle();
                } else if ((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth() <= 0)
                        && !v1.returnCollided() && v2.returnCollided()) {
                    //System.out.println("Collision Over");
                    v2.setCollided(false);
                    v2.restartVehicle();
                }
//                else {
//                    if (v1.returnCollided()){
//                        v1.setCollided(false);
//                        v1.restartVehicle();
//                    }
//                    else if (v2.returnCollided()){
//                        v2.setCollided(false);
//                        v2.restartVehicle();
//                    }
//                }
            }
        }
    }

    private void checkCollision(Vehicle src, Vehicle other){
        if(Shape.intersect(src.returnCarShape(), other.returnCarShape()).getBoundsInLocal().getWidth() > -1){
            System.out.println("Collision Detected");
        }else if(Shape.intersect(src.returnCarShape(), other.returnCarShape()).getBoundsInLocal().getWidth() <= 0){
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
