import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class TrafficScene {

    private ImageHelper imageHelper = new ImageHelper();
    public Scene Traffic(){
        int width = 1280;
        int height = 720;

        BorderPane window = new BorderPane();
        window.setPrefHeight(height *1.1);
        window.setPrefWidth(width);

        Box groundBox = new Box(width*8,height*14,10);
        groundBox.setLayoutY(150);
        groundBox.setLayoutX(width/2);
        groundBox.setTranslateZ(0);

        PhongMaterial groundMaterial = new PhongMaterial();
        groundMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/view.png"));
        groundMaterial.setDiffuseMap(imageHelper.getImage("./images/view.png"));
        groundBox.setMaterial(groundMaterial);


        window.getChildren().add(groundBox);

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFieldOfView(60);
        perspectiveCamera.setRotate(0);

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

        perspectiveCamera.getTransforms().addAll(xRotate,yRotate,zRotate);
        perspectiveCamera.setFarClip(10500);

        //Creates the camera and positions all the labels
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


        Group group = new Group();
        group.getChildren().addAll(window, cameraPane);
        Scene scene = new Scene(group,width, height, true, SceneAntialiasing.BALANCED);
        scene.setCamera(perspectiveCamera);
        scene.setFill(Color.GRAY);

        //Sets the camera properties
        perspectiveCamera.setLayoutY(-50);
        perspectiveCamera.setLayoutX(0);
        perspectiveCamera.setTranslateZ(-1500);

        Camera fpsCamera = new Camera();
        fpsCamera.loadControlsForScene(scene, subScene, cameraPane);

        groundBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));


        return scene;
    }
}
