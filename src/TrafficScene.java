
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.TranslateTransition;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class TrafficScene {

    private ImageHelper imageHelper = new ImageHelper();
    private Testing testing = new Testing();
    public Scene Traffic(){

        StlMeshImporter importer = new StlMeshImporter();
        ObjModelImporter importe = new ObjModelImporter();

        try {
            importer.read(this.getClass().getResource("/building.stl"));
            importe.read(this.getClass().getResource("/13941_Empire_State_Building_v1_l1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Mesh mesh = importer.getImport();
        MeshView mess = new MeshView(mesh);
        mess.setLayoutX(-500);
        //mess.setLayoutY(500);
        mess.setScaleX(50);
        mess.setScaleY(50);
        mess.setScaleZ(50);
        MeshView[] meshViews = importe.getImport();//new MeshView(mesh)[] ;
        //meshViews[0] = mess;

        mess.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        int width = 1200;
        int height = 800;


        //Root Pane
        BorderPane root = new BorderPane();
        root.setPrefHeight(height *1.1);
        root.setPrefWidth(width);

        BorderPane root3D = new BorderPane();
       /* root3D.setPrefHeight(height *1.1);
        root3D.setPrefWidth(width);*/

        //Ground
        Box groundBox = new Box(width*10,height*10,10);
        groundBox.setLayoutY(20);
        groundBox.setLayoutX(width/2);
        groundBox.setTranslateZ(500);

        Box skyBox = new Box(width*5,height*5,10);
        skyBox.setLayoutY(-350);
        skyBox.setLayoutX(width/2);
        skyBox.setTranslateZ(2500);

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
        groundMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/asphalt.jpg"));
        groundMaterial.setSpecularColor(Color.GRAY);
        groundMaterial.setDiffuseMap(imageHelper.getImage("./images/asphalt.jpg"));
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
        root3D.getChildren().addAll(trafficLight1, car.getTrafficCar());

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

        //StartPane
        StackPane menuPane = new StackPane();

        Rectangle startRec = new Rectangle();
        startRec.setFill(Color.RED);
        startRec.setOpacity(.25);
        startRec.setWidth(width);
        startRec.setHeight(100);

        //Label label = new Label("2D");
        Pane buttonPane = new Pane();
        Text tesr = new Text("This");
        //Button testButton = new Button();

        menuPane.getChildren().addAll(startRec,buttonPane, tesr);//, testButton);
        menuPane.setTranslateX(640);
        menuPane.setTranslateY(55);


        cameraPane.setTranslateZ(-1500);
        cameraPane.setTranslateY(-100);
        cameraPane.setTranslateX(0);
        cameraPane.getChildren().addAll(menuPane    );

        SubScene subScene = new SubScene(root3D,width*1.1, height*1.1, true, SceneAntialiasing.BALANCED);
        //subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(perspectiveCamera);
        subScene.setTranslateZ(-300);
        subScene.setTranslateX(-100);
        subScene.setTranslateY(-100);
        subScene.setDepthTest(DepthTest.ENABLE);
        //End Move Camera

        //Group for the scene and camera
        /*Group group = new Group();
        group.getChildren().addAll(root, cameraPane);*/

        testing.createRoot();
        /*Image map = imageHelper.getImage("./images/Updated 2 of 460 Traffic Map-2.png");
        ImageView fullMap = new ImageView(map);
        double imageW = map.getWidth();
        double imageH = map.getHeight();
        System.out.println("Original Image Width: " + imageW + " Original Image Height: " + imageH);*/

        Pane streetScene = testing.getRoot();//new Pane(fullMap);
 /*       streetScene.setLayoutY(350);
        streetScene.setLayoutX(width/2);
        streetScene.setTranslateZ(0);
        streetScene.setPrefHeight(height*14);
        streetScene.setPrefWidth(width*8);
        streetScene.setMinWidth(width*8);
        streetScene.setMinHeight(height*14);*/
      /*  streetScene.setTranslateX(-4000);
        streetScene.setTranslateY(300);*/
        //streetScene.setTranslateZ(1000);



        Pane tempPane = new Pane();
        tempPane.setTranslateX(-600);
        tempPane.setTranslateY(-400);

        tesr.setOnMouseClicked(event -> {
            testing.spawnVehicles(tempPane);
            //streetScene.getChildren().add(tempPane);
        });
        streetScene.getChildren().add(tempPane);

        Group group = new Group();

        group.getChildren().addAll(meshViews);
        group.setScaleX(.05);
        group.setScaleY(.05);
        group.setScaleZ(.05);

        //group.setTranslateY(1000);
        group.setTranslateZ(11500);
        group.setTranslateY(-500);
        group.setTranslateX(-1000);
        group.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        streetScene.setScaleX(7);
        streetScene.setScaleZ(7);
        root.getChildren().addAll(subScene, menuPane);
        root3D.getChildren().addAll(streetScene, mess, group, groundBox);
        //Set scene
        Scene scene = new Scene(root);

        //scene.setCamera(perspectiveCamera);
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
        streetScene.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));

        return scene;
    }
}
