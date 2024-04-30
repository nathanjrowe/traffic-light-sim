import javafx.application.Application;
import javafx.scene.control.Button;//unused import
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;//unused import
import javafx.scene.shape.Rectangle;//unused import
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Traffic extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /*
     * The Traffic class is our main function that is used to intialize the demonstration.
     * 
     */
    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        TrafficScene trafficScene = new TrafficScene();

        Scene scene = new Scene(pane, 800, 400);
        scene.setFill(Color.GRAY);

        primaryStage.setTitle("Traffic Light");
        primaryStage.setScene(trafficScene.Traffic());

        primaryStage.show();
    }
}
