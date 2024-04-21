import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
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
        mediaPlayer.play();

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
        perspectiveCamera.setTranslateZ(-2500);
        perspectiveCamera.getTransforms().addAll(new Rotate(-40, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS)
                ,new Rotate(0, Rotate.Z_AXIS));

       return  perspectiveCamera;
    }

    private Group empireStateBuilding(){
        ObjModelImporter importe = new ObjModelImporter();
        try {
            importe.read(this.getClass().getResource("/13941_Empire_State_Building_v1_l1.obj"));
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
        group.setTranslateY(-500);
        group.setTranslateX(-1000);
        group.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        return group;
    }

    private Pane create3DRoot(){
        Pane root3D = new Pane();
       /* root3D.setPrefHeight(height *1.1);
        root3D.setPrefWidth(width);*/

        StlMeshImporter importer = new StlMeshImporter();

        ObjModelImporter importe1 = new ObjModelImporter();

        try {
            importer.read(this.getClass().getResource("/building.stl"));
            importe1.read(this.getClass().getResource("/townhome/10091_townhome_V1_L1.obj"));
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
        //new MeshView(mesh)[] ;
        MeshView[] meshViews1 = importe1.getImport();
        //meshViews[0] = mess;

        mess.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        //Ground
        Box groundBox = new Box(width*10,height*10,10);
        groundBox.setLayoutY(20);
        groundBox.setLayoutX(width/2);
        groundBox.setTranslateZ(500);

        Box oceanBox = new Box(width*2,height*10,10);
        oceanBox.setLayoutY(0);
        oceanBox.setLayoutX(width*5);
        oceanBox.setTranslateZ(500);

        Box oceanBox1 = new Box(width*2,height*10,10);
        oceanBox1.setLayoutY(0);
        oceanBox1.setLayoutX(-width*5);
        oceanBox1.setTranslateZ(500);

        Box oceanBoxN = new Box(width*2,height*18,10);
        oceanBoxN.setLayoutY(0);
        oceanBoxN.setLayoutX(0);
        oceanBoxN.setTranslateZ(5300);

 /*       Box skyBox = new Box(width*25,height*8,10);
        skyBox.setLayoutY(-3000);
        skyBox.setLayoutX(width/2);
        skyBox.setTranslateZ(6000);

        Box skyBox1 = new Box(width*8.5,height*8,10);
        skyBox1.setLayoutY(-3000);
        skyBox1.setLayoutX(width*6);
        skyBox1.setTranslateZ(500);

        Box skyBox2 = new Box(width*8.5,height*8,10);
        skyBox2.setLayoutY(-3000);
        skyBox2.setLayoutX(-width*6);
        skyBox2.setTranslateZ(500);*/

        //StopLight
        Pane trafficLight1 = new Pane();
        TrafficLightCreation trafficLight = new TrafficLightCreation();
        trafficLight1.getChildren().add(trafficLight.getTrafficLight());
        trafficLight1.setLayoutY(-300);
        trafficLight1.setLayoutX(-250);
        trafficLight1.setTranslateZ(0);

        //Set the traffic light to red
        trafficLight.setRed();

        //Ground Material
        PhongMaterial groundMaterial = new PhongMaterial();
        groundMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/asphalt.jpg"));
        groundMaterial.setSpecularColor(Color.GRAY);
        groundMaterial.setDiffuseMap(imageHelper.getImage("./images/asphalt.jpg"));
        groundBox.setMaterial(groundMaterial);

        PhongMaterial oceanMaterial = new PhongMaterial();
        oceanMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/ocean.jpg"));
        oceanMaterial.setSpecularColor(Color.GRAY);
        oceanMaterial.setDiffuseMap(imageHelper.getImage("./images/ocean.jpg"));
        oceanBox.setMaterial(oceanMaterial);
        oceanBox1.setMaterial(oceanMaterial);
        oceanBoxN.setMaterial(oceanMaterial);

    /*    PhongMaterial skyMaterial = new PhongMaterial();
        skyMaterial.setSelfIlluminationMap(imageHelper.getImage("./images/sky.jpg"));
        skyMaterial.setSpecularColor(Color.GRAY);
        skyMaterial.setDiffuseMap(imageHelper.getImage("./images/sky.jpg"));
        skyBox.setMaterial(skyMaterial);
        skyBox1.setMaterial(skyMaterial);
        skyBox2.setMaterial(skyMaterial);*/


        Group group1 = new Group();


        group1.getChildren().addAll(meshViews1);
        group1.setScaleX(.25);
        group1.setScaleY(.25);
        group1.setScaleZ(.25);

        group1.setTranslateZ(1000);
        group1.setTranslateY(-5);
        group1.setTranslateX(1000);
        group1.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

        testing.createRoot(clickCount);

        Pane streetScene = testing.getRoot();

        Pane tempPane = new Pane();

        streetScene.getChildren().add(tempPane);

        streetScene.setTranslateX(-800);
        streetScene.setTranslateZ(2200);



        streetScene.setScaleX(5);
        streetScene.setScaleZ(5);

        menuPane = menuPane(tempPane);

        //Add to root

        root3D.getChildren().addAll(streetScene, trafficLight1, mess, empireStateBuilding(), group1, oceanBox, oceanBox1,
                oceanBoxN);

        groundBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        streetScene.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));

     /*   skyBox1.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));
        skyBox2.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS),new Rotate(-90, Rotate.Y_AXIS),new Rotate(0, Rotate.Z_AXIS));
*/
        oceanBox.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        oceanBox1.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(0, Rotate.Z_AXIS));
        oceanBoxN.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS),new Rotate(0, Rotate.Y_AXIS),
                new Rotate(90, Rotate.Z_AXIS));

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