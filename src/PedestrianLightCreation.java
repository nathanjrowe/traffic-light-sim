import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PedestrianLightCreation {

    public enum LightColor {
        RED, WALKING
    }

    private final Pane pane;
    private final Rectangle rectangle;
    private final ImageView redHandImageView;
    private final ImageView walkingImageView;
    private LightColor lightColor;

    public PedestrianLightCreation() {
        pane = new Pane();

        rectangle = new Rectangle(300, 50, 100, 100);
        rectangle.setStroke(Color.WHITE);
        rectangle.setFill(Color.BLACK);

        ImageHelper imageHelper = new ImageHelper();
        redHandImageView = new ImageView(imageHelper.getImage("./images/redHand.jpg"));
        redHandImageView.setFitWidth(100);
        redHandImageView.setFitHeight(100);
        redHandImageView.setX(350);
        redHandImageView.setY(50);

        walkingImageView = new ImageView(imageHelper.getImage("./images/walking.jpg"));
        walkingImageView.setFitWidth(100);
        walkingImageView.setFitHeight(100);
        walkingImageView.setX(300);
        walkingImageView.setY(50);

        lightColor = null;

        pane.getChildren().addAll(redHandImageView, walkingImageView);
    }

    public Pane getPedestrianLight() {
        return pane;
    }

    public void setRedHand() {
        redHandImageView.setVisible(true);
        walkingImageView.setVisible(false);
        lightColor = LightColor.RED;
    }

    public void setWalking() {
        redHandImageView.setVisible(false);
        walkingImageView.setVisible(true);
        lightColor = LightColor.WALKING;
    }

    public LightColor getLightColor() {
        return lightColor;
    }
}
