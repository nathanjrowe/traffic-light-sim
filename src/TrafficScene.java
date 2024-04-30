import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Animation;//unused
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.binding.When;//unused
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.effect.Light;//unused
import javafx.scene.effect.Lighting;//unused
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;//unused
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Traffic Scene class renders 3D space and models for 2d implementation of
 * Traffic Light Controller, Pedestrian Light Controller, Bus Light Controller
 */

public class TrafficScene {

    /**
     * Instantiate volatile variables
     */

    private ImageHelper imageHelper = new ImageHelper();
    private Testing testing = new Testing();
    private List<Vehicle> vehicleCollidables = new ArrayList<>();//unused
    private List<Vehicle3D> vehicleCollidables3D = new ArrayList<>();
    private List<Bus3D> busCollidables3D = new ArrayList<>();
    private List<Person3D> personCollidablese3D = new ArrayList<>();
    private List<Person> personCollidables = new ArrayList<>();//unused
    private AtomicInteger clickCount = new AtomicInteger(0);
    private int counter = 0;
    private SubScene subScene;
    private Pane root = new Pane();
    private Pane root3D = new Pane();
    private Pane menuPane = new Pane();
    Text currentTimeT = new Text("Current Sim Time: 10:40 AM");
    private static Text dataText = new Text();

    // Define window size
    int width = 1200;
    int height = 800;
    boolean stop = false;
    SystemController systemController = new SystemController();

    /**
     * Acts as the main function of the GUI scene.
     * @return Scene
     */
    public Scene Traffic(){

        // Create media call to mp3 to generate aesthetic city sounds
        Media backgroundMusic = new Media(new File("./resources/Music/cityTraffic.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //mediaPlayer.play();

        testing.startCollisionTimer3D(vehicleCollidables3D);
        createSubScene(); // Container for different components of scene

        //Root Pane
        root.setPrefHeight(height-20);
        root.setPrefWidth(width);

        root.getChildren().addAll(subScene, menuPane);

        //Set scene
        Scene scene = new Scene(root);

        //Camera controls (Zoom in/out, move camera)
        Camera fpsCamera = new Camera();
        fpsCamera.loadControlsForScene(scene, subScene, new Group());


        //Set the scene background color
        scene.setFill(skyColors(scene));

        return scene;
    }

    /**
     * sets street scene image
     * @return pane
     */
    private Pane streetScene(){
        //Root pane for the cars
        Pane streetScene = testing.getRoot();
        Pane tempPane = new Pane();

        streetScene.getChildren().addAll(tempPane, cityMap());

        streetScene.setTranslateX(-800);
        streetScene.setTranslateZ(2200);

        streetScene.setScaleX(3.75);
        streetScene.setScaleZ(3.75);

        menuPane = menuPane(tempPane);

        //Rotate the streetScene for the cars
        streetScene.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return streetScene;
    }

    /**
     * sets pane objects
     * @return returns pane of objects
     */
    private Pane create3DRoot(){
        root3D = new Pane();

        //StopLight

        //Add lights to the 3D scene
        //systemController.addLights(root3D);

        //Flag to denote if the scene is 3D or not
        testing.set3DFlag();
        testing.createRoot(clickCount);

        //Add to root pane here. Make any 3d Models as a function that returns a group then add.
        //Commented out some models to keep the load times down when testing
        root3D.getChildren().addAll(streetScene(),runWay(), empireStateBuilding(),townHome(), building2(), trees(),
                airport(),shoppingMall(), apartment(), runWayParking(), runWayParking2(), city(), city1(), city2(),
                city3(),city4(), city5(), city6(), trees1(), trees2(), trees3(), townHome1(), townHome2(), car());

        Group joeGroup = joe();
        Path path1 = createJoePath();
        root3D.getChildren().addAll(joeGroup, path1);
        PathTransition pathTransition1 = createPathTransition(joeGroup, path1);
        pathTransition1.play();

        //Airplane 1
        Group airplaneGroup1 = airplane(1);
        Path path2 = createAirplanePath1();
        root3D.getChildren().addAll(airplaneGroup1, path2);
        PathTransition pathTransition2 = createPathTransition(airplaneGroup1, path2);
        pathTransition2.play();

        //Airplane 2
        Group airplaneGroup2 = airplane(2);
        Path path3 = createAirplanePath2();
        root3D.getChildren().addAll(airplaneGroup2, path3);
        PathTransition pathTransition3 = createPathTransition(airplaneGroup2, path3);
        pathTransition3.play();

        return root3D;
    }

    private Path createAirplanePath1() {
        Path path = new Path();
        path.getElements().add(new MoveTo(-10500, -3000));
        path.getElements().add(new LineTo(-1500, -50));
        path.getElements().add(new LineTo(1500, -50));
        path.getElements().add(new LineTo(10500, -3000));
        return path;
    }

    private Path createAirplanePath2() {
        Path path = new Path();
        path.getElements().add(new MoveTo(10500, -3000));
        path.getElements().add(new LineTo(1500, -50));
        path.getElements().add(new LineTo(-1500, -50));
        path.getElements().add(new LineTo(-10500, -3000));
        return path;
    }

    private Path createJoePath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(-12500, -3300));
        path.getElements().add(new LineTo(-3000, -350));
        path.getElements().add(new LineTo(0, -350));
        path.getElements().add(new LineTo(9000, -3300));
        return path;
    }

    private PathTransition createPathTransition(Group group, Path path) {
        PathTransition pathTransition = new PathTransition(Duration.seconds(10), path, group);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        return pathTransition;
    }

    private ImageView createFollowingImage(String imagePath) {
        InputStream is = getClass().getResourceAsStream(imagePath);
        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        return imageView;
    }


    /**
     * For layering 2D and 3D streen scene and objects
     * @return subscene to for layering 2D and 3D street scenes and objects
     */
    private SubScene createSubScene(){
        SubScene subScene = new SubScene(create3DRoot(),width*1.1, height*1.1, true,
                SceneAntialiasing.BALANCED);
        subScene.setCamera(mainCamera());
        subScene.setTranslateZ(-300);
        subScene.setTranslateX(-100);
        subScene.setTranslateY(-100);
        subScene.setDepthTest(DepthTest.ENABLE);
        this.subScene = subScene;
        return subScene;
    }

    /**
     * Constructs camera for scene
     * @return camera object
     */
    private PerspectiveCamera mainCamera(){
        //Scene camera (what makes it 3D)
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFieldOfView(75);
        perspectiveCamera.setRotate(0);
        perspectiveCamera.minWidth(height);
        perspectiveCamera.minWidth(width);

        //Camera Rotation
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

        perspectiveCamera.getTransforms().addAll(xRotate,yRotate,zRotate);

        //The view distance
        perspectiveCamera.setFarClip(15500);

        //Sets the camera properties
        perspectiveCamera.setLayoutY(-1550);
        perspectiveCamera.setLayoutX(0);
        perspectiveCamera.setTranslateZ(-1000);
        perspectiveCamera.getTransforms().addAll(new Rotate(-30, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS)
                ,new Rotate(0, Rotate.Z_AXIS));

        return  perspectiveCamera;
    }

    /**
     * Creates temp menu pane
     * @param tempPane
     * @return menu
     */
    private Pane menuPane(Pane tempPane){
        //Menu
        StackPane menuPane = new StackPane();

        //Background
        Rectangle startRec = new Rectangle();
        startRec.setFill(Color.BLACK);
        startRec.setOpacity(.45);
        startRec.setWidth(width);
        startRec.setHeight(100);

        //Label label = new Label("2D");
        //Pane for buttons
        Pane controlPane = new Pane();
        controlPane.getChildren().add(spawnVehiclesBTN(tempPane));
        controlPane.setPrefHeight(200);
        controlPane.setPrefWidth(200);

        Text labels = new Text("Traffic: Moderate");
        labels.setFill(Color.WHITE);
        controlPane.getChildren().add(labels);
        //Current Simulation Time
        currentTimeT = new Text("Current Sim Time: 10:40 AM");
        currentTimeT.setFill(Color.WHITE);

        Text currentTrafficT = new Text("Traffic: Moderate");
        currentTrafficT.setFill(Color.WHITE);

        Image uiT = imageHelper.getImage("./images/logo.png");
        ImageView uiTitle = new ImageView(uiT);
        uiTitle.setScaleX(.5);
        uiTitle.setScaleY(.5);
        //uiTitle.setFill(Color.WHITE);

        menuPane.getChildren().addAll(startRec, spawnVehiclesBTN(tempPane), uiTitle, currentTimeT,
                currentTrafficT, dataText);//, testButton);
        menuPane.setMargin(uiTitle, new Insets(-20,0,0,-950));
        menuPane.setMargin(currentTimeT, new Insets(-50,0,0,950));
        menuPane.setMargin(currentTrafficT, new Insets(0,0,0,895));
        menuPane.setMargin(dataText, new Insets(0,0,0,600));
        menuPane.setMargin(spawnVehiclesBTN(tempPane), new Insets(50,0,0,-800));

        return menuPane;
    }
    public static void setData(String data){
        dataText.setText(data);
        dataText.setFill(Color.WHITE);
    }

    /**
     * Creates traffic and adds to scene
     * @param tempPane
     * @return text of completed spawn traffic
     */
    private Text spawnVehiclesBTN(Pane tempPane){
        Text spawnTrafficT = new Text("Spawn Traffic");
        spawnTrafficT.setFill(Color.WHITE);

        spawnTrafficT.setTranslateX(-200);
        spawnTrafficT.setOnMouseClicked(event -> {
            if (!stop) {
                testing.addVehicles3D(vehicleCollidables3D.size(), tempPane, vehicleCollidables3D);
                testing.addBuses3D(busCollidables3D.size(), tempPane, busCollidables3D);
                testing.addPeople3D(personCollidablese3D.size(), tempPane, personCollidablese3D);
                //streetScene.getChildren().add(tempPane);
                stop = true;
            }
            else {
                testing.stopVehicles(personCollidablese3D);
            }
        });
        return  spawnTrafficT;
    }

    /**
     * Airplane creation, position, scaling
     * @return groups
     */
    private Group airplane(int airplaneNumber){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/vehicleModels/11803_Airplane_v1_l1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.25);
        group3.setScaleY(.25);
        group3.setScaleZ(.25);

        //group.setTranslateY(1000);
        if (airplaneNumber % 2 == 1) {
            group3.setTranslateZ(5100);
            group3.setTranslateY(-120);
            group3.setTranslateX(200);
            group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS), new Rotate(0, Rotate.Y_AXIS),
                    new Rotate(0, Rotate.Z_AXIS));
        }
        else {
            group3.setTranslateZ(5800);
            group3.setTranslateY(-120);
            group3.setTranslateX(200);
            group3.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(180, Rotate.Y_AXIS),
                    new Rotate(0, Rotate.Z_AXIS));
        }

        return group3;
    }

    private Group joe() {
        Group group3 = new Group();
        group3.setTranslateZ(4500);
        group3.setTranslateY(-120);
        group3.setTranslateX(200);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        ImageView imageView = createFollowingImage("joe.png");
        imageView.setScaleX(5);
        imageView.setScaleY(5);

        group3.getChildren().add(imageView);

        return group3;
    }

    /**
     * Empire state building creation, position, scaling
     * @return group
     */
    private Group empireStateBuilding(){
        ObjModelImporter importe = new ObjModelImporter();
        try {
            importe.read(this.getClass().getResource("/empireState/13941_Empire_State_Building_v1_l1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        Group group = new Group();


        group.getChildren().addAll(meshViews);
        group.setScaleX(.05);
        group.setScaleY(.05);
        group.setScaleZ(.05);

        //group.setTranslateY(1000);
        group.setTranslateZ(11500);
        group.setTranslateY(-580);
        group.setTranslateX(-650);
        group.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group;
    }

    /**
     * Grouped 3D tree models creation, placement, scale
     * @return
     */
    private Group trees(){
        ObjModelImporter importe = new ObjModelImporter();
        ObjModelImporter importes = new ObjModelImporter();

        try {
            importe.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importes.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        MeshView[] meshViews1 = importes.getImport();

        Group group = new Group();

        Group group0 = new Group();

        Group group1 = new Group();

        group.getChildren().addAll(meshViews);
        group1.getChildren().addAll(meshViews1);

        group.setScaleX(50);
        group.setScaleY(50);
        group.setScaleZ(50);

        group1.setScaleX(50);
        group1.setScaleY(50);
        group1.setScaleZ(50);


        group1.setTranslateZ(250);
        group1.setTranslateY(-5);
        group1.setTranslateX(200);


        group.setTranslateZ(250);
        group.setTranslateY(-5);
        group.setTranslateX(-500);


        //group.setTranslateY(1000);
        group0.getChildren().addAll(group, group1);//, group2, group3, group4, group5, group6, group7);
        group0.setTranslateZ(300);
        group0.setTranslateY(-70);
        group0.setTranslateX(-505);

        group0.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group1.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group0;
    }

    /**
     * Grouped 3D tree models creation, placement, scale
     * @return
     */
    private Group trees1(){
        ObjModelImporter importe = new ObjModelImporter();
        ObjModelImporter importes = new ObjModelImporter();

        try {
            importe.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importes.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        MeshView[] meshViews1 = importes.getImport();

        Group group = new Group();

        Group group0 = new Group();

        Group group1 = new Group();

        group.getChildren().addAll(meshViews);
        group1.getChildren().addAll(meshViews1);

        group.setScaleX(50);
        group.setScaleY(50);
        group.setScaleZ(50);

        group1.setScaleX(50);
        group1.setScaleY(50);
        group1.setScaleZ(50);


        group1.setTranslateZ(250);
        group1.setTranslateY(-5);
        group1.setTranslateX(200);


        group.setTranslateZ(250);
        group.setTranslateY(-5);
        group.setTranslateX(-500);


        //group.setTranslateY(1000);
        group0.getChildren().addAll(group, group1);//, group2, group3, group4, group5, group6, group7);
        group0.setTranslateZ(-100);
        group0.setTranslateY(-70);
        group0.setTranslateX(-505);

        group0.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group1.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group0;
    }
    /**
     * Grouped 3D tree models creation, placement, scale
     * @return
     */
    private Group trees2(){
        ObjModelImporter importe = new ObjModelImporter();
        ObjModelImporter importes = new ObjModelImporter();

        try {
            importe.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importes.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        MeshView[] meshViews1 = importes.getImport();

        Group group = new Group();

        Group group0 = new Group();

        Group group1 = new Group();

        group.getChildren().addAll(meshViews);
        group1.getChildren().addAll(meshViews1);

        group.setScaleX(50);
        group.setScaleY(50);
        group.setScaleZ(50);

        group1.setScaleX(50);
        group1.setScaleY(50);
        group1.setScaleZ(50);


        group1.setTranslateZ(250);
        group1.setTranslateY(-5);
        group1.setTranslateX(200);


        group.setTranslateZ(250);
        group.setTranslateY(-5);
        group.setTranslateX(-500);


        //group.setTranslateY(1000);
        group0.getChildren().addAll(group, group1);//, group2, group3, group4, group5, group6, group7);
        group0.setTranslateZ(2075);
        group0.setTranslateY(-70);
        group0.setTranslateX(-1905);

        group0.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group1.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group0;
    }
    /**
     * Grouped 3D tree models creation, placement, scale
     * @return
     */
    private Group trees3(){
        ObjModelImporter importe = new ObjModelImporter();
        ObjModelImporter importes = new ObjModelImporter();
        ObjModelImporter importe1 = new ObjModelImporter();
        ObjModelImporter importe2 = new ObjModelImporter();
        ObjModelImporter importe3 = new ObjModelImporter();
        ObjModelImporter importe4 = new ObjModelImporter();

        try {
            importe.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importes.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importe1.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importe2.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importe3.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
            importe4.read(this.getClass().getResource("/treesDetailed/Lemon Trees With Fruits.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        MeshView[] meshViews1 = importes.getImport();
        MeshView[] meshViews2 = importe1.getImport();
        MeshView[] meshViews3 = importe2.getImport();
        MeshView[] meshViews4 = importe3.getImport();
        MeshView[] meshViews5 = importe4.getImport();

        Group group = new Group();

        Group group0 = new Group();

        Group group1 = new Group();

        Group group2 = new Group();

        Group group3 = new Group();

        Group group4 = new Group();

        Group group5 = new Group();

        group.getChildren().addAll(meshViews);
        group1.getChildren().addAll(meshViews1);
        group2.getChildren().addAll(meshViews2);
        group3.getChildren().addAll(meshViews3);
        group4.getChildren().addAll(meshViews4);
        group5.getChildren().addAll(meshViews5);

        group.setScaleX(50);
        group.setScaleY(50);
        group.setScaleZ(50);

        group1.setScaleX(50);
        group1.setScaleY(50);
        group1.setScaleZ(50);


        group1.setTranslateZ(325);
        group1.setTranslateY(-200);
        group1.setTranslateX(-500);


        group.setTranslateZ(325);
        group.setTranslateY(-50);
        group.setTranslateX(-500);

        group2.setScaleX(50);
        group2.setScaleY(50);
        group2.setScaleZ(50);

        group3.setScaleX(50);
        group3.setScaleY(50);
        group3.setScaleZ(50);


        group3.setTranslateZ(325);
        group3.setTranslateY(-50);
        group3.setTranslateX(-900);


        group2.setTranslateZ(325);
        group2.setTranslateY(-50);
        group2.setTranslateX(-400);

        group4.setScaleX(50);
        group4.setScaleY(50);
        group4.setScaleZ(50);

        group5.setScaleX(50);
        group5.setScaleY(50);
        group5.setScaleZ(50);

        group5.setTranslateZ(325);
        group5.setTranslateY(-200);
        group5.setTranslateX(-1100);


        group4.setTranslateZ(325);
        group4.setTranslateY(-200);
        group4.setTranslateX(-800);



        //group.setTranslateY(1000);
        group0.getChildren().addAll(group, group1, group2, group3,group4, group5);// group6, group7);
        group0.setTranslateZ(2075);
        group0.setTranslateY(-70);
        group0.setTranslateX(1905);

        group0.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group1.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group2.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group3.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group4.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        group5.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return group0;
    }

    /**
     * Airport 3D model creation, placement, scale
     * @return
     */
    private Group airport(){
        ObjModelImporter importe = new ObjModelImporter();
        try {
            importe.read(this.getClass().getResource("/airport/3d-model.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViews = importe.getImport();
        Group group = new Group();


        group.getChildren().addAll(meshViews);
        group.setScaleX(.125);
        group.setScaleY(.125);
        group.setScaleZ(.125);



        //group.setTranslateY(1000);
        group.setTranslateZ(3450);
        group.setTranslateY(1550);
        group.setTranslateX(900);
        group.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-87.5, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        group.getChildren().add(airportTarmac());
        return group;
    }

    /**
     * Building 3D model creation, placement, scale
     * @return
     */
    private Group building2(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/apartmentBuilding/Apartment Building_01_obj.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.6);
        group3.setScaleY(.6);
        group3.setScaleZ(.55);

        //group.setTranslateY(1000);
        group3.setTranslateZ(1250);
        group3.setTranslateY(200);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }
    /**
     * Water mesh import, creation, placement, scale
     * @return
     */
    /*
    private Group water(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/water/uploads_files_2723489_POOL.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.75);
        group3.setScaleY(.75);
        group3.setScaleZ(.75);

        //group.setTranslateY(1000);
        group3.setTranslateZ(1250);
        group3.setTranslateY(120);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }*/

    /**
     * Apartment building 3D creation placement
     * @return
     */
    private Group apartment(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/apartment1/3d-model.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(1.05);
        group3.setScaleY(.15);
        group3.setScaleZ(.15);

        //group.setTranslateY(1000);
        group3.setTranslateZ(-90);
        group3.setTranslateY(1375);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

    /**
     * Shopping mall 3D model creation, placement and scale
     * @return
     */
    private Group shoppingMall(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/shoppingMall/3d-model.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.15);
        group3.setScaleY(.15);
        group3.setScaleZ(.15);

        //group.setTranslateY(1000);
        group3.setTranslateZ(1250);
        group3.setTranslateY(350);
        group3.setTranslateX(1600);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(170, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

    /**
     * Cars 3D models grouped creation, placement, scale
     * @return
     */
    private Group car(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/vehicleModels/NormalCar2.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(20);
        group3.setScaleY(20);
        group3.setScaleZ(20);

        //group.setTranslateY(1000);
        group3.setTranslateZ(1500);
        group3.setTranslateY(-10);
        group3.setTranslateX(410);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

    /**
     * Town Home 3D model creation, placement, scale
     * @return
     */
    private Group townHome(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/townhome/10091_townhome_V1_L1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.45);
        group3.setScaleY(.15);
        group3.setScaleZ(.15);

        //group.setTranslateY(1000);
        group3.setTranslateZ(810);
        group3.setTranslateY(40);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }
    /**
     * Town Home 3D model creation, placement, scale
     * @return
     */
    private Group townHome1(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/townhome/10091_townhome_V1_L1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.75);
        group3.setScaleY(.15);
        group3.setScaleZ(.14);

        //group.setTranslateY(1000);
        group3.setTranslateZ(380);
        group3.setTranslateY(40);
        group3.setTranslateX(1000);
        group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(180, Rotate.Z_AXIS));
        return group3;
    }
    /**
     * Town Home 3D model creation, placement, scale
     * @return
     */
    private Group townHome2(){
        ObjModelImporter importes = new ObjModelImporter();
        try {
            importes.read(this.getClass().getResource("/townhome/10091_townhome_V1_L1.obj"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MeshView[] meshViewss = importes.getImport();
        Group group3 = new Group();


        group3.getChildren().addAll(meshViewss);
        group3.setScaleX(.75);
        group3.setScaleY(.15);
        group3.setScaleZ(.16);

        //group.setTranslateY(1000);
        group3.setTranslateZ(800);
        group3.setTranslateY(40);
        group3.setTranslateX(1000);
        group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

    /**
     * Default ocean mesh and material
     * @return
     */
    private Box oceanBlock(){//unused
        Box oceanBox = new Box(width*2,height*10,10);
        oceanBox.setLayoutY(0);
        oceanBox.setLayoutX(-width*3.25);
        oceanBox.setTranslateZ(500);

        PhongMaterial oceanMaterial = new PhongMaterial();
        oceanMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/ocean.jpg"));
        oceanMaterial.setSpecularColor(Color.GRAY);
        oceanMaterial.setDiffuseMap(imageHelper.getImage("./images/ocean.jpg"));
        oceanBox.setMaterial(oceanMaterial);
        oceanBox.setMaterial(oceanMaterial);

        oceanBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return oceanBox;
    }
    private Box airportTarmac(){
        Box tarmac = new Box(width*15 -100,height*25,10);
        tarmac.setLayoutY(-50);
        //tarmac.setTranslateX(1500);
        //tarmac.setTranslateZ(1000);

        PhongMaterial tarmacMaterial = new PhongMaterial();
        tarmacMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/airportTarmac.png"));
        tarmacMaterial.setSpecularColor(Color.GRAY);
        tarmacMaterial.setDiffuseMap(imageHelper.getImage("./images/airportTarmac.png"));
        tarmac.setMaterial(tarmacMaterial);
        tarmac.setMaterial(tarmacMaterial);

        tarmac.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(-2, Rotate.Z_AXIS));

        return tarmac;
    }
    private Box cityMap(){
        Box cityBox = new Box(width,height - 50,10);
        cityBox.setTranslateY(-40);
        //oceanBox.setLayoutX(500);
        cityBox.setTranslateZ(10);

        PhongMaterial mapMaterial = new PhongMaterial();
        mapMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/trafficMap3d.png"));
        mapMaterial.setSpecularColor(Color.GRAY);
        mapMaterial.setDiffuseMap(imageHelper.getImage("./images/trafficMap3d.png"));
        mapMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/trafficMap3dLight.png"));
        cityBox.setMaterial(mapMaterial);

        cityBox.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return cityBox;
    }

    /**
     * Texture to place for airport model
     * @return
     */
    private Group runWay(){
        //Simple runway for airplanes
        Box runWay = new Box(width*1.25,height*14,10);
        runWay.setLayoutY(0);
        runWay.setTranslateX(-500);
        runWay.setTranslateZ(5300);


        PhongMaterial runway = new PhongMaterial();
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/runway.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/runwayLight.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(90, Rotate.Z_AXIS));

        Group runwayBalls = new Group();

        int x = 4000;
        for(int i = 0; i < 46; i++){
            Sphere sphere = new Sphere(20);
            sphere.setLayoutY(0);
            sphere.setTranslateX(x);
            sphere.setTranslateZ(5300);

            PhongMaterial runwayBallW = new PhongMaterial();
            runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/white.png"));
            sphere.setMaterial(runwayBallW);

            runwayBalls.getChildren().add(sphere);
            x-= 200;
        }

        x = 4000;
        for(int i = 0; i < 46; i++){
            Sphere sphere = new Sphere(20);
            sphere.setLayoutY(0);
            sphere.setTranslateX(x);
            sphere.setTranslateZ(4600);

            PhongMaterial runwayBallW = new PhongMaterial();
            runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/red.png"));
            sphere.setMaterial(runwayBallW);

            runwayBalls.getChildren().add(sphere);
            x-= 200;
        }

        x = 4000;
        for(int i = 0; i < 46; i++){
            Sphere sphere = new Sphere(20);
            sphere.setLayoutY(0);
            sphere.setTranslateX(x);
            sphere.setTranslateZ(6000);

            PhongMaterial runwayBallW = new PhongMaterial();
            runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/red.png"));
            sphere.setMaterial(runwayBallW);

            runwayBalls.getChildren().add(sphere);
            x-= 200;
        }

       /* x = 4400;
        int y = 4620;
        for(int j = 0; j< 4; j++) {

            y = 4620;
            for (int i = 0; i < 20; i++) {

                Sphere sphere = new Sphere(20);
                sphere.setLayoutY(0);
                sphere.setTranslateX(x);
                sphere.setTranslateZ(y);

                PhongMaterial runwayBallW = new PhongMaterial();
                runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/orange.png"));
                sphere.setMaterial(runwayBallW);

                runwayBalls.getChildren().add(sphere);
                y += 70;
            }
            x += 200;

        }

        x = -5450;
        for(int j = 0; j< 4; j++) {

            y = 4620;
            for (int i = 0; i < 20; i++) {

                Sphere sphere = new Sphere(20);
                sphere.setLayoutY(0);
                sphere.setTranslateX(x);
                sphere.setTranslateZ(y);

                PhongMaterial runwayBallW = new PhongMaterial();
                runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/orange.png"));
                sphere.setMaterial(runwayBallW);

                runwayBalls.getChildren().add(sphere);
                y += 70;
            }
            x -= 200;

        }

        x = 4100;
        y = 4620;
        for(int i = 0; i < 20; i++){
            Sphere sphere = new Sphere(20);
            sphere.setLayoutY(0);
            sphere.setTranslateX(x);
            sphere.setTranslateZ(y);

            PhongMaterial runwayBallW = new PhongMaterial();
            runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/green.png"));
            sphere.setMaterial(runwayBallW);

            runwayBalls.getChildren().add(sphere);
            y+= 70;
        }

        x = -5150;
        y = 4620;
        for(int i = 0; i < 20; i++){
            Sphere sphere = new Sphere(20);
            sphere.setLayoutY(0);
            sphere.setTranslateX(x);
            sphere.setTranslateZ(y);

            PhongMaterial runwayBallW = new PhongMaterial();
            runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/green.png"));
            sphere.setMaterial(runwayBallW);

            runwayBalls.getChildren().add(sphere);
            y+= 70;
        }*/
 /*       Sphere sphere = new Sphere(20);
        sphere.setLayoutY(0);
        sphere.setTranslateX(-500);
        sphere.setTranslateZ(5300);

        Sphere sphere1 = new Sphere(20);
        sphere1.setLayoutY(0);
        sphere1.setTranslateX(-600);
        sphere1.setTranslateZ(5300);

        PhongMaterial runwayBallW = new PhongMaterial();
        runwayBallW.setSelfIlluminationMap(imageHelper.getImage("./images/white.png"));
        sphere.setMaterial(runwayBallW);
        sphere1.setMaterial(runwayBallW);*/
        runWay.setMaterial(runway);

        Group runwayGroup = new Group(runWay, runwayBalls);
        return runwayGroup;
    }

    /**
     * Texture to place for airport model
     * @return
     */
    private Box runWayParking(){
        //Simple runway for airplanes
        Box runWay = new Box(width*1,height*2.75,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-1350);
        runWay.setTranslateZ(4000);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/parking.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/parking.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/parkingLight.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(90, Rotate.Z_AXIS));

        return runWay;
    }

    private Box runWayParking2(){
        //Simple runway for airplanes
        Box runWay = new Box(width,height*2.75,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-1350);
        runWay.setTranslateZ(2800);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/parking.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/parking.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/parkingLight.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(90, Rotate.Z_AXIS));

        return runWay;
    }

    private Box city(){
        //Simple runway for airplanes
        Box runWay = new Box(width*3,height*3.25,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-4200);
        runWay.setTranslateZ(3300);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/newyork.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/newyork.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/newyorkLight.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }

    private Box city1(){
        //Simple runway for airplanes
        Box runWay = new Box(width*3,height*3.14,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-4200);
        runWay.setTranslateZ(750);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/city1.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }
    private Box city2(){
        //Simple runway for airplanes
        Box runWay = new Box(width*3,height*3.25,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-4200);
        runWay.setTranslateZ(-1800);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }
    private Box city3(){
        //Simple runway for airplanes
        Box runWay = new Box(width*3,height*3.25,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(-600);
        runWay.setTranslateZ(-1800);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }
    private Box city4(){
        //Simple runway for airplanes
        Box runWay = new Box(width*3,height*3.25,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(3000);
        runWay.setTranslateZ(-1800);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/cirty2.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/cirty2Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }
    private Box city5(){
        //Simple runway for airplanes
        Box runWay = new Box(width*2.3,height*3,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(3400);
        runWay.setTranslateZ(700);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/city1.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }
    private Box city6(){
        //Simple runway for airplanes
        Box runWay = new Box(width*2.3,height*3,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(3400);
        runWay.setTranslateZ(3100);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/city1.png"));
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/city1Light.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        return runWay;
    }

    /**
     * Creates skybox for scene
     * @param scene
     * @return
     */
    private LinearGradient skyColors(Scene scene){
        //region Sky Colors
        final int[] red = {75};
        final int[] green = {160};
        final int[] blue = {254};
        final int[] white = {254};

        final boolean[] redFlag = {true};
        final boolean[] greenFlag = {true};
        final boolean[] blueFlag = {true};

        //White to blue
        //rgba(255, 230, 229, 1.0)

        //Blue transition
        //rgba(76, 138, 255, 1.0)
        //rgba(1, 8, 24, 1.0)


        final long[] startingTime1 = {System.currentTimeMillis()};
        final long[] startingTime = {System.currentTimeMillis()};
        final Stop[][] stops12 = {new Stop[]{}};
        final LinearGradient[] lg1 = {new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops12[0])};


        PointLight sunLight = new PointLight(Color.rgb(255, 255, 230));
        PointLight moonLight = new PointLight(Color.rgb(15, 15, 10, .25));

        sunLight.setScaleX(5000);
        sunLight.setScaleY(5000);
        sunLight.setScaleZ(5000);
        sunLight.setTranslateX(-1500);
        sunLight.setTranslateY(-1000);

        moonLight.setScaleX(5000);
        moonLight.setScaleY(5000);
        moonLight.setScaleZ(5000);
        moonLight.setTranslateX(-1500);
        moonLight.setTranslateY(-1000);
        /*sunLight.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));*/

        Path path = new Path();
        path.getElements().add(new MoveTo(6000, 0));
        path.getElements().add(new LineTo(3000, -8000));
        path.getElements().add(new LineTo(-1000, -10000));
        path.getElements().add(new LineTo(-5000, -8000));
        path.getElements().add(new LineTo(-10000, 0));
        path.getElements().add(new LineTo(-5000, 8000));
        path.getElements().add(new LineTo(3000, 6000));
        path.getElements().add(new LineTo(6000, 0));

        Path path2 = new Path();
        path2.getElements().add(new MoveTo(-10000, 0));
        path2.getElements().add(new LineTo(-5000, 8000));
        path2.getElements().add(new LineTo(-1000, 8000));
        path2.getElements().add(new LineTo(6000, 0));
        path2.getElements().add(new LineTo(-1000, -10000));
        path2.getElements().add(new LineTo(-3000, -8000));
        path2.getElements().add(new LineTo(-10000, 0));


        Group sunGroup = new Group(sunLight);
        Group moonGroup = new Group(moonLight);
        Sphere sun = new Sphere(500);

        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(imageHelper.getImage("./images/sun.png"));
        sunMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/sun.png"));
        sun.setMaterial(sunMaterial);

        Sphere moon = new Sphere(300);

        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(imageHelper.getImage("./images/moon.png"));
        moonMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/moon.png"));
        moon.setMaterial(moonMaterial);

        sunGroup.getChildren().addAll(sun);
        sunGroup.setTranslateX(-2000);
        sunGroup.setTranslateY(-4000);
        sunGroup.setTranslateZ(500);

        moonGroup.getChildren().addAll(moon);
        moonGroup.setTranslateX(-2000);
        moonGroup.setTranslateY(-4000);
        moonGroup.setTranslateZ(500);

        root3D.getChildren().addAll(sunGroup, path, moonGroup, path2);

        PathTransition pathTransition = createPathTransition(sunGroup, path);
        pathTransition.setDuration(Duration.seconds(8.25));
        pathTransition.play();

        PathTransition pathTransition2 = createPathTransition(moonGroup, path2);
        pathTransition2.setDuration(Duration.seconds(8.25));
        pathTransition2.play();

        /**
         * Day/Night Cycle
         */
        AnimationTimer timer = new AnimationTimer() {
            @Override
        public void handle(long l) {
            if ((l - startingTime1[0]) / 1000000 > 50) {
                if(red[0] > 1 && red[0] <= 100 && redFlag[0] == true){
                   red[0]--;
                   if(green[0] >= 2) {
                       green[0] -= 2;
                   }
                   blue[0]-= 3;
                   white[0] -= 3;
                }
                else {
                    if(red[0] >= 1 && red[0] <= 76) {
                        redFlag[0] = false;
                        red[0]++;
                        green[0]+= 2;
                        if (blue[0] < 254) {
                            blue[0]+=3;
                            white[0] +=3;
                        }

                    }
                    else {
                        redFlag[0] = true;

                        System.out.println("True red" + red[0] + " green" + green[0] + " blue" + blue[0]);
                    }
                }



                stops12[0] = new Stop[] { new Stop(0,Color.rgb(red[0], green[0],blue[0],1)),
                        new Stop(1, Color.rgb(white[0],white[0],white[0]))};
                lg1[0] = new LinearGradient(1, 1, 1, 0, true,
                        CycleMethod.NO_CYCLE, stops12[0]);

                switch (counter){
                    case 0: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 5:00am"); counter++; break;
                    case 1: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 7:00 AM"); counter++;break;
                    case 2: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 9:00 AM");counter++;break;
                    case 3: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 11:00 AM");counter++;break;
                    case 4: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 1:00 PM");counter++;break;
                    case 5: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 3:00 PM");counter++;break;
                    case 6: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 5:00 PM");counter++;break;
                    case 7: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 7:00 PM");counter++;break;
                    case 8: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 9:00 PM");counter++;break;
                    case 9: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 11:00 PM");counter++;break;
                    case 10: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Sim Time: 1:00 AM");counter++;break;
                    case 11: scene.setFill(lg1[0]); currentTimeT.textProperty().set("Current Time: 3:00am"); counter = 0;break;
                }


                startingTime1[0] = l;
            }
        }
        };
        timer.handle(5000);
        timer.start();
        return lg1[0];
    }
}