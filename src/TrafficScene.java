import javafx.animation.TranslateTransition;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class TrafficScene {

    private ImageHelper imageHelper = new ImageHelper();
    public Scene Traffic(){
        int width = 1280;
        int height = 720;

        //Root Pane
        BorderPane root = new BorderPane();
        root.setPrefHeight(height *1.1);
        root.setPrefWidth(width);

        //Ground
        Box groundBox = new Box(width*8,height*14,10);
        groundBox.setLayoutY(350);
        groundBox.setLayoutX(width/2);
        groundBox.setTranslateZ(0);

        //StopLight
        Pane trafficLight1 = new Pane();
        TrafficLightCreation trafficLight = new TrafficLightCreation();
        trafficLight1.getChildren().add(trafficLight.getTrafficLight());
        trafficLight1.setLayoutY(0);
        trafficLight1.setLayoutX(width/2);
        trafficLight1.setTranslateZ(0);

        //Set the traffic light to red
        trafficLight.setRed();

        //Ground Material
        PhongMaterial groundMaterial = new PhongMaterial();
        groundMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/roads.png"));
        groundMaterial.setSpecularColor(Color.GRAY);
        groundMaterial.setDiffuseMap(imageHelper.getImage("./images/roads.png"));
        groundBox.setMaterial(groundMaterial);

        //Car
        TrafficCarCreation car = new TrafficCarCreation();
        car.getTrafficCar().setFitWidth(50);
        car.getTrafficCar().setFitHeight(100);
        car.getTrafficCar().setLayoutY(330);
        car.getTrafficCar().setTranslateZ(200);
        car.getTrafficCar().getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(-90, Rotate.Z_AXIS));

        //Car Movement (REMOVE)
        TranslateTransition transition = new TranslateTransition(Duration.millis(2500),car.getTrafficCar());
        transition.setByX(2000);
        transition.setAutoReverse(true);
        transition.play();

        transition.setOnFinished(event -> {
            car.getTrafficCar().getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(-90, Rotate.Z_AXIS));
            TranslateTransition transition1 = new TranslateTransition(Duration.millis(2500),car.getTrafficCar());
            transition1.setByZ(-2000);
            transition1.setAutoReverse(true);
            transition1.play();

            transition1.setOnFinished(event1 -> {
                car.getTrafficCar().getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(90, Rotate.Z_AXIS));
                TranslateTransition transition2 = new TranslateTransition(Duration.millis(2500),car.getTrafficCar());
                transition2.setByX(-2000);
                transition2.setAutoReverse(true);
                transition2.play();
                transition2.setOnFinished(event2 -> {
                    transition.play();
                });
            });

        });
        //end Car Movement

        //Add to root
        root.getChildren().addAll(groundBox, trafficLight1, car.getTrafficCar());

        //Scene camera (what makes it 3D)
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFieldOfView(60);
        perspectiveCamera.setRotate(0);

        //Camera Rotation
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

        perspectiveCamera.getTransforms().addAll(xRotate,yRotate,zRotate);

        //The view distance
        perspectiveCamera.setFarClip(10500);

        //The movable camera
        PerspectiveCamera perspectiveCamera1 = new PerspectiveCamera();
        Group cameraPane = new Group();

        cameraPane.setTranslateZ(-1500);
        cameraPane.setTranslateY(-100);
        cameraPane.setTranslateX(0);
        cameraPane.getChildren().addAll(perspectiveCamera1);

        SubScene subScene = new SubScene(cameraPane, 300, 300, true, SceneAntialiasing.BALANCED);
        //subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(perspectiveCamera1);
        subScene.setTranslateZ(-300);
        subScene.setTranslateX(-100);
        subScene.setTranslateY(-100);
        //End Move Camera

        //Group for the scene and camera
        Group group = new Group();
        group.getChildren().addAll(root, cameraPane);

        //Set scene
        Scene scene = new Scene(group,width, height, true, SceneAntialiasing.BALANCED);
        scene.setCamera(perspectiveCamera);
        scene.setFill(Color.GRAY);

        //Sets the camera properties
        perspectiveCamera.setLayoutY(-1550);
        perspectiveCamera.setLayoutX(0);
        perspectiveCamera.setTranslateZ(-2500);
        perspectiveCamera.getTransforms().addAll(new Rotate(-40, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        //Camera controls (Zoom in/out, move camera)
        Camera fpsCamera = new Camera();
        fpsCamera.loadControlsForScene(scene, subScene, cameraPane);

        //Rotate the groundbox
        groundBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        return scene;
    }
}
