import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.*;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TrafficScene {

    private ImageHelper imageHelper = new ImageHelper();
    private Testing testing = new Testing();
    private List<Vehicle> vehicleCollidables = new ArrayList<>();
    private AtomicInteger clickCount = new AtomicInteger(0);
    private int counter = 0;
    private SubScene subScene;
    private Pane root = new Pane();
    private Pane menuPane = new Pane();
    int width = 1200;
    int height = 800;
    public Scene Traffic(){

        Media backgroundMusic = new Media(new File("./resources/Music/cityTraffic.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //mediaPlayer.play();

        createSubScene();

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
        //scene.setFill(skyColors(scene, currentTimeT));

        return scene;
    }
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
    private Pane create3DRoot(){
        Pane root3D = new Pane();

        //StopLight
        SystemController systemController = new SystemController();
        //Add lights to the 3D scene
        systemController.addLights(root3D);

        //Flag to denote if the scene is 3D or not
        testing.set3DFlag();
        testing.createRoot(clickCount);

        //Add to root pane here. Make any 3d Models as a function that returns a group then add.
        //Commented out some models to keep the load times down when testing
        root3D.getChildren().addAll(streetScene(),runWay(), oceanBlock(), empireStateBuilding(), building2(),car(), trees(),airport(),
                shoppingMall(), apartment());

        return root3D;
    }

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
        Text currentTimeT = new Text("Current Sim Time: 10:40 AM");
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
    private Text spawnVehiclesBTN(Pane tempPane){
        Text spawnTrafficT = new Text("Spawn Traffic");
        spawnTrafficT.setFill(Color.WHITE);

        spawnTrafficT.setOnMouseClicked(event -> {
            testing.addVehiclesUntilCount(vehicleCollidables.size(), tempPane, vehicleCollidables);
            //streetScene.getChildren().add(tempPane);
        });
        return  spawnTrafficT;
    }
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

    /*private Group water(){
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
        group3.setScaleX(10);
        group3.setScaleY(10);
        group3.setScaleZ(10);

        //group.setTranslateY(1000);
        group3.setTranslateZ(800);
        group3.setTranslateY(50);
        group3.setTranslateX(-2000);
        group3.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group3;
    }

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
    private LinearGradient skyColors(Scene scene, Text currentTimeT){
        //region Sky Colors
        Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.rgb(150,200,225,1))};
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


        final long[] startingTime1 = {System.currentTimeMillis()};
        final long[] startingTime = {System.currentTimeMillis()};
        AnimationTimer timer = new AnimationTimer() {
            @Override
        public void handle(long l) {
            if ((l - startingTime1[0]) / 1000000 > 5000) {
                switch (counter){
                    case 0: scene.setFill(lg2); currentTimeT.textProperty().set("Current Sim Time: 5:00am"); counter++; break;
                    case 1: scene.setFill(lg3); currentTimeT.textProperty().set("Current Sim Time: 7:00 AM"); counter++;break;
                    case 2: scene.setFill(lg4); currentTimeT.textProperty().set("Current Sim Time: 9:00 AM");counter++;break;
                    case 3: scene.setFill(lg5); currentTimeT.textProperty().set("Current Sim Time: 11:00 AM");counter++;break;
                    case 4: scene.setFill(lg6); currentTimeT.textProperty().set("Current Sim Time: 1:00 PM");counter++;break;
                    case 5: scene.setFill(lg7); currentTimeT.textProperty().set("Current Sim Time: 3:00 PM");counter++;break;
                    case 6: scene.setFill(lg8); currentTimeT.textProperty().set("Current Sim Time: 5:00 PM");counter++;break;
                    case 7: scene.setFill(lg9); currentTimeT.textProperty().set("Current Sim Time: 7:00 PM");counter++;break;
                    case 8: scene.setFill(lg10); currentTimeT.textProperty().set("Current Sim Time: 9:00 PM");counter++;break;
                    case 9: scene.setFill(lg11); currentTimeT.textProperty().set("Current Sim Time: 11:00 PM");counter++;break;
                    case 10: scene.setFill(lg12); currentTimeT.textProperty().set("Current Sim Time: 1:00 AM");counter++;break;
                    case 11: scene.setFill(lg13); currentTimeT.textProperty().set("Current Time: 3:00am"); counter = 0;break;
                }

                startingTime1[0] = l;
            }
        }
        };
        timer.handle(5000);
        timer.start();
        return lg1;
    }
}