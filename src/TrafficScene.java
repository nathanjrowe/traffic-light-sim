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

        //Root Pane
        BorderPane root = new BorderPane();
        root.setPrefHeight(height *1.1);
        root.setPrefWidth(width);

        //Ground
        Box groundBox = new Box(width*8,height*14,10);
        groundBox.setLayoutY(150);
        groundBox.setLayoutX(width/2);
        groundBox.setTranslateZ(0);

        //Ground Material
        PhongMaterial groundMaterial = new PhongMaterial();
        groundMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/map.png"));
        groundMaterial.setDiffuseMap(imageHelper.getImage("./images/map.png"));
        groundBox.setMaterial(groundMaterial);

        //Add to root
        root.getChildren().add(groundBox);

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
        perspectiveCamera.setLayoutY(-50);
        perspectiveCamera.setLayoutX(0);
        perspectiveCamera.setTranslateZ(-1500);

        //Camera controls (Zoom in/out, move camera)
        Camera fpsCamera = new Camera();
        fpsCamera.loadControlsForScene(scene, subScene, cameraPane);

        //Rotate the groundbox
        groundBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        return scene;
    }
}
