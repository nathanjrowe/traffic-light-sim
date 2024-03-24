import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Traffic extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        TrafficLightCreation trafficLight = new TrafficLightCreation();
        pane.getChildren().add(trafficLight.getTrafficLight());

        TrafficScene trafficScene = new TrafficScene();

        Scene scene = new Scene(pane, 800, 400);
        scene.setFill(Color.GRAY);


        primaryStage.setTitle("Traffic Light");
        primaryStage.setScene(trafficScene.Traffic());
        primaryStage.show();
    }
}
