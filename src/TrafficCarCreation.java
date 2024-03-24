import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TrafficCarCreation {

    private final ImageView imageView;

    public TrafficCarCreation() {

        Image image = new Image("images/Screenshot 2024-03-23 at 9.49.30 PM.png");
        imageView = new ImageView(image);

    }

    public ImageView getTrafficCar() {
        return imageView;
    }
}
