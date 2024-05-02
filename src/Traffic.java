import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

        primaryStage.setTitle("Traffic Light");
        primaryStage.setScene(trafficScene.Traffic());

        primaryStage.show();
    }
}
