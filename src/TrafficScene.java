import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.binding.When;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
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
    private List<Vehicle> vehicleCollidables = new ArrayList<>();
    private List<Bus3D> busCollidables3D = new ArrayList<>();
    private List<Person> personCollidables = new ArrayList<>();
    private AtomicInteger clickCount = new AtomicInteger(0);
    private int counter = 0;
    private SubScene subScene;
    private Pane root = new Pane();
    private Pane root3D = new Pane();
    private Pane menuPane = new Pane();
    Text currentTimeT = new Text("Current Sim Time: 10:40 AM");

    // Define window size
    int width = 1200;
    int height = 800;

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

        testing.startCollisionTimer();
        createSubScene(); // Container for different components of scene

        //Root Pane
        root.setPrefHeight(height *1.1);
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

        streetScene.getChildren().add(tempPane);

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
        SystemController systemController = new SystemController();
        //Add lights to the 3D scene
        //systemController.addLights(root3D);

        //Flag to denote if the scene is 3D or not
        testing.set3DFlag();
        testing.createRoot(clickCount);

        //Add to root pane here. Make any 3d Models as a function that returns a group then add.
        //Commented out some models to keep the load times down when testing
        root3D.getChildren().addAll(streetScene());//,runWay(), oceanBlock(), empireStateBuilding(),townHome(), building2(),car(), trees(),airport(),
                //shoppingMall(), apartment());

        Group joeGroup = joe();
        Path path1 = createJoePath();
        root3D.getChildren().addAll(joeGroup, path1);
        PathTransition pathTransition1 = createPathTransition(joeGroup, path1);
        pathTransition1.play();

        Group airplaneGroup = airplane();
        Path path2 = createAirplanePath();
        root3D.getChildren().addAll(airplaneGroup, path2);
        PathTransition pathTransition2 = createPathTransition(airplaneGroup, path2);
        pathTransition2.play();

        return root3D;
    }

    private Path createAirplanePath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(-3500, -1000));
        path.getElements().add(new LineTo(-1500, -50));
        path.getElements().add(new LineTo(1500, -50));
        path.getElements().add(new LineTo(3500, -1000));
        return path;
    }

    private Path createJoePath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(-4500, -1300));
        path.getElements().add(new LineTo(-2500, -350));
        path.getElements().add(new LineTo(500, -350));
        path.getElements().add(new LineTo(2500, -1300));
        return path;
    }

    private PathTransition createPathTransition(Group group, Path path) {
        PathTransition pathTransition = new PathTransition(Duration.seconds(5), path, group);
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
        perspectiveCamera.getTransforms().addAll(new Rotate(-40, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS)
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
        Pane buttonPane = new Pane();

        //Current Simulation Time
        currentTimeT = new Text("Current Sim Time: 10:40 AM");
        currentTimeT.setFill(Color.WHITE);

        Text currentTrafficT = new Text("Traffic: Moderate");
        currentTrafficT.setFill(Color.WHITE);

        Image uiT = imageHelper.getImage("./images/logo.png");
        ImageView uiTitle = new ImageView(uiT);
        uiTitle.setScaleX(.75);
        uiTitle.setScaleY(.75);
        //uiTitle.setFill(Color.WHITE);

        menuPane.getChildren().addAll(startRec,buttonPane, spawnVehiclesBTN(tempPane), uiTitle, currentTimeT, currentTrafficT);//, testButton);
        menuPane.setMargin(uiTitle, new Insets(-20,0,0,-775));
        menuPane.setMargin(currentTimeT, new Insets(-50,0,0,950));
        menuPane.setMargin(currentTrafficT, new Insets(0,0,0,895));

        return menuPane;
    }

    /**
     * Creates traffic and adds to scene
     * @param tempPane
     * @return text of completed spawn traffic
     */
    private Text spawnVehiclesBTN(Pane tempPane){
        Text spawnTrafficT = new Text("Spawn Traffic");
        spawnTrafficT.setFill(Color.WHITE);

        spawnTrafficT.setOnMouseClicked(event -> {
            testing.addVehiclesUntilCount(vehicleCollidables.size(), tempPane, vehicleCollidables);
            testing.addBuses3D(busCollidables3D.size(), tempPane, busCollidables3D);
            testing.addPeople(personCollidables.size(), tempPane, personCollidables);
            //streetScene.getChildren().add(tempPane);
        });
        return  spawnTrafficT;
    }

    /**
     * Airplane creation, position, scaling
     * @return groups
     */
    private Group airplane(){
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
        group3.setTranslateZ(4500);
        group3.setTranslateY(-120);
        group3.setTranslateX(200);
        group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

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
        group.setScaleX(.06);
        group.setScaleY(.06);
        group.setScaleZ(.06);

        //group.setTranslateY(1000);
        group.setTranslateZ(11300);
        group.setTranslateY(-700);
        group.setTranslateX(-575);
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
        ObjModelImporter importe1 = new ObjModelImporter();
        ObjModelImporter importe2 = new ObjModelImporter();
        ObjModelImporter importe3 = new ObjModelImporter();
        ObjModelImporter importe4 = new ObjModelImporter();

        ObjModelImporter importe5 = new ObjModelImporter();
        ObjModelImporter importe6 = new ObjModelImporter();

        try {
            importe.read(this.getClass().getResource("/trees/CommonTree_1.obj"));
            importes.read(this.getClass().getResource("/trees/CommonTree_2.obj"));
            importe1.read(this.getClass().getResource("/trees/CommonTree_3.obj"));
            importe2.read(this.getClass().getResource("/trees/CommonTree_4.obj"));
            importe3.read(this.getClass().getResource("/trees/Bush_1.obj"));
            importe4.read(this.getClass().getResource("/trees/Bush_2.obj"));

            importe5.read(this.getClass().getResource("/trees/Grass.obj"));
            importe6.read(this.getClass().getResource("/trees/Grass_2.obj"));
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

        MeshView[] meshViews6 = importe5.getImport();
        MeshView[] meshViews7 = importe6.getImport();

        Group group = new Group();

        Group group0 = new Group();

        Group group1 = new Group();

        Group group2 = new Group();

        Group group3 = new Group();

        Group group4 = new Group();

        Group group5 = new Group();

        Group group6 = new Group();

        Group group7 = new Group();

        group.getChildren().addAll(meshViews);
        group1.getChildren().addAll(meshViews1);
        group2.getChildren().addAll(meshViews2);
        group3.getChildren().addAll(meshViews3);

        group4.getChildren().addAll(meshViews4);
        group5.getChildren().addAll(meshViews5);

        group6.getChildren().addAll(meshViews6);
        group7.getChildren().addAll(meshViews7);

        group.setScaleX(50);
        group.setScaleY(50);
        group.setScaleZ(50);

        group1.setScaleX(50);
        group1.setScaleY(50);
        group1.setScaleZ(50);

        group2.setScaleX(50);
        group2.setScaleY(50);
        group2.setScaleZ(50);

        group3.setScaleX(50);
        group3.setScaleY(50);
        group3.setScaleZ(50);

        group4.setScaleX(50);
        group4.setScaleY(50);
        group4.setScaleZ(50);

        group5.setScaleX(50);
        group5.setScaleY(50);
        group5.setScaleZ(50);

        group6.setScaleX(50);
        group6.setScaleY(50);
        group6.setScaleZ(50);

        group7.setScaleX(50);
        group7.setScaleY(50);
        group7.setScaleZ(50);

        group1.setTranslateZ(0);
        group1.setTranslateY(0);
        group1.setTranslateX(150);

        group2.setTranslateZ(0);
        group2.setTranslateY(0);
        group2.setTranslateX(-80);

        group3.setTranslateZ(0);
        group3.setTranslateY(0);
        group3.setTranslateX(-300);

        group.setTranslateZ(0);
        group.setTranslateY(0);
        group.setTranslateX(-550);

        group4.setTranslateZ(0);
        group4.setTranslateY(35);
        group4.setTranslateX(-200);

        group5.setTranslateZ(0);
        group5.setTranslateY(35);
        group5.setTranslateX(-400);

        group6.setTranslateZ(-100);
        group6.setTranslateY(35);
        group6.setTranslateX(-400);

        group7.setTranslateZ(100);
        group7.setTranslateY(35);
        group7.setTranslateX(-400);


        //group.setTranslateY(1000);
        group0.getChildren().addAll(group, group1, group2, group3, group4, group5, group6, group7);
        group0.setTranslateZ(300);
        group0.setTranslateY(-70);
        group0.setTranslateX(-505);
        group0.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
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
        group.setScaleX(.15);
        group.setScaleY(.15);
        group.setScaleZ(.15);

        //group.setTranslateY(1000);
        group.setTranslateZ(3400);
        group.setTranslateY(1500);
        group.setTranslateX(575);
        group.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-87.5, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
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
        group3.setScaleY(.25);
        group3.setScaleZ(.25);

        //group.setTranslateY(1000);
        group3.setTranslateZ(-70);
        group3.setTranslateY(1200);
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
        group3.setScaleX(.25);
        group3.setScaleY(.15);
        group3.setScaleZ(.15);

        //group.setTranslateY(1000);
        group3.setTranslateZ(1250);
        group3.setTranslateY(350);
        group3.setTranslateX(1000);
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
        group3.setScaleX(10);
        group3.setScaleY(10);
        group3.setScaleZ(10);

        //group.setTranslateY(1000);
        group3.setTranslateZ(50);
        group3.setTranslateY(-120);
        group3.setTranslateX(200);
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
        group3.setScaleX(.15);
        group3.setScaleY(.15);
        group3.setScaleZ(.15);

        //group.setTranslateY(1000);
        group3.setTranslateZ(800);
        group3.setTranslateY(40);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

    /**
     * Default ocean mesh and material
     * @return
     */
    private Box oceanBlock(){
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

    /**
     * Texture to place for airport model
     * @return
     */
    private Box runWay(){
        //Simple runway for airplanes
        Box runWay = new Box(width*1.5,height*10,10);
        runWay.setLayoutY(0);
        runWay.setLayoutX(0);
        runWay.setTranslateZ(4300);


        PhongMaterial runway = new PhongMaterial();
        runway.setSelfIlluminationMap(imageHelper.getImage("./images/runway.png"));
        runway.setSpecularColor(Color.GRAY);
        runway.setDiffuseMap(imageHelper.getImage("./images/runway.png"));
        runWay.setMaterial(runway);


        runWay.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(90, Rotate.Z_AXIS));

        return runWay;
    }

    /**
     * Creates skybox for scene
     * @param scene
     * @return
     */
    private LinearGradient skyColors(Scene scene){
        //region Sky Colors
       /* Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.rgb(150,200,225,1))};
        LinearGradient lg1 = new LinearGradient(1, 1, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        Stop[] stops1 = new Stop[] { new Stop(0, Color.rgb(50,50,75)),
                new Stop(1, Color.rgb(100,100,125,1))};
        LinearGradient lg2 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops1);
        BackgroundFill backgroundFill1 = new BackgroundFill(lg2,new CornerRadii(1),new Insets(0));

        Stop[] stops2 = new Stop[] { new Stop(0, Color.rgb(25,175,225)),
                new Stop(1, Color.rgb(25,125,175,1))};
        LinearGradient lg3 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops2);
        BackgroundFill backgroundFill2 = new BackgroundFill(lg3,new CornerRadii(1),new Insets(0));

        Stop[] stops3 = new Stop[] { new Stop(0, Color.WHITE),
                new Stop(1, Color.rgb(150,200,225,1))};
        LinearGradient lg4 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops3);
        BackgroundFill backgroundFill3 = new BackgroundFill(lg4,new CornerRadii(1),new Insets(0));

        Stop[] stops4 = new Stop[] { new Stop(0, Color.rgb(205,190,190)),
                new Stop(1, Color.rgb(125,150,175,1))};
        LinearGradient lg5 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops4);
        BackgroundFill backgroundFill4 = new BackgroundFill(lg5,new CornerRadii(1),new Insets(0));

        Stop[] stops5 = new Stop[] { new Stop(0, Color.rgb(125,100,100)),
                new Stop(1, Color.rgb(225,100,75,1))};
        LinearGradient lg6 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops5);
        BackgroundFill backgroundFill5 = new BackgroundFill(lg6,new CornerRadii(1),new Insets(0));

        Stop[] stops6 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1,Color.rgb(50,50,75,1))};
        LinearGradient lg7 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops6);
        BackgroundFill backgroundFill6 = new BackgroundFill(lg7,new CornerRadii(1),new Insets(0));

        Stop[] stops7 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1, Color.rgb(25,25,50,1))};
        LinearGradient lg8 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops7);
        BackgroundFill backgroundFill7 = new BackgroundFill(lg8,new CornerRadii(1),new Insets(0));

        Stop[] stops8 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1, Color.rgb(25,25,65,1))};
        LinearGradient lg9 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops8);
        BackgroundFill backgroundFill8 = new BackgroundFill(lg9,new CornerRadii(1),new Insets(0));
        Background backgroundss8 = new Background(backgroundFill8);

        Stop[] stops9 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1, Color.rgb(25,25,75,1))};
        LinearGradient lg10 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops9);
        BackgroundFill backgroundFill9 = new BackgroundFill(lg10,new CornerRadii(1),new Insets(0));

        Stop[] stops10 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1, Color.rgb(25,25,65,1))};
        LinearGradient lg11 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops10);
        BackgroundFill backgroundFill10 = new BackgroundFill(lg11,new CornerRadii(1),new Insets(0));

        Stop[] stops11 = new Stop[] { new Stop(0, Color.BLACK),
                new Stop(1, Color.rgb(25,25,50,1))};
        LinearGradient lg12 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops11);
        BackgroundFill backgroundFill11 = new BackgroundFill(lg12,new CornerRadii(1),new Insets(0));
        Background backgroundss11 = new Background(backgroundFill11);

        Stop[] stops12 = new Stop[] { new Stop(0, Color.BLACK), new Stop(1,Color.rgb(25,25,35,1))};
        LinearGradient lg13 = new LinearGradient(1, 1, 1, 0, true,
                CycleMethod.NO_CYCLE, stops12);
        BackgroundFill backgroundFill12 = new BackgroundFill(lg13,new CornerRadii(1),new Insets(0));
        //endregion
*/

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


      /*  Light.Distant sunLight = new Light.Distant(-135.0, 500, Color.RED);
        Lighting lighting = new Lighting(sunLight);

        lighting.setSurfaceScale(5.0);*/

        PointLight sunLight = new PointLight(Color.WHITE);
        //sunLight.setFalloff(10);

        sunLight.setScaleX(5000);
        sunLight.setScaleY(5000);
        sunLight.setScaleZ(5000);
        sunLight.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        Path path = new Path();
        path.getElements().add(new MoveTo(6000, 0));
        path.getElements().add(new LineTo(3000, -4000));
        path.getElements().add(new LineTo(-3000, -4000));
        path.getElements().add(new LineTo(-6000, 0));
        path.getElements().add(new LineTo(-3000, 4000));
        path.getElements().add(new LineTo(3000, 4000));
        path.getElements().add(new LineTo(6000, 0));


        Group lightGroup = new Group(sunLight);
        Sphere sphere = new Sphere(150);
        lightGroup.getChildren().add(sphere);
        lightGroup.setTranslateX(-2000);
        lightGroup.setTranslateY(-4000);
        lightGroup.setTranslateZ(500);

        root3D.getChildren().addAll(lightGroup, path);

        PathTransition pathTransition2 = createPathTransition(lightGroup, path);
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