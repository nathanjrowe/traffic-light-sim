import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TrafficCarCreation {

    private final ImageView imageView;

    public TrafficCarCreation() {

        //new Image(new FileInputStream("/car.png"));
        Image image = null;
        try {
            image = new Image(new FileInputStream("./images/car.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        imageView = new ImageView(image);

    }

    public ImageView getTrafficCar() {
        return imageView;
    }
}
