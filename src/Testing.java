import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class renders 2D space and images for implementation of
 * Traffic Light Controller, Pedestrian Light Controller, Bus Light Controller
 */
public class Testing extends Application {
    private final Boolean DEBUG = false;
    private final Boolean getCoordinates = false;
    private List<Vehicle> vehicleCollidables = new ArrayList<>();
    private List<Vehicle3D> vehicleCollidables3D = new ArrayList<>();
    private List<Bus> busCollidables = new ArrayList<>();
    private List<Bus3D> busCollidables3D = new ArrayList<>();
    private List<Person> personCollidables = new ArrayList<>();
    private List<Person3D> personCollidables3D = new ArrayList<>();
    private StackPane root = new StackPane();
    private Pane tempPane = new Pane();
    private AtomicInteger clickCount = new AtomicInteger(0);
    private boolean stopSpawning = false;
    private boolean currentlySpawning = false;
    private boolean flag3D = false;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        startCollisionTimer();
        createRoot(clickCount);

        Scene scene = new Scene(root, 1200, 800);
        root.setFocusTraversable(true);
        root.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Testing");
        primaryStage.show();
    }

    /**
     * Creates base view of TransitFlow simulation,
     * generating the default images and linking objects to scene
     */
    public void createRoot(AtomicInteger clickCount){
        ImageHelper imageHelper = new ImageHelper();
        Image map;
        if(!flag3D){
            map = imageHelper.getImage("./images/trafficMap2.png");
        }
        else{
            map = imageHelper.getImage("./images/trafficMap3d.png");
        }
        ImageView fullMap = new ImageView(map);
        double imageW = map.getWidth();
        double imageH = map.getHeight();
        System.out.println("Original Image Width: " + imageW + " Original Image Height: " + imageH);

        Pane mapPane = resizeImage(fullMap, 1200, 800);
        root.getChildren().add(mapPane);

        root.setOnMouseClicked(event -> {
            if (getCoordinates) {
                double[] temp = {event.getX(), event.getY()};
                System.out.println("X Position: " + temp[0] + ", Y Position: " + temp[1] + ", Dot Number: " + clickCount.get());
                Circle circle = new Circle(event.getX(), event.getY(),2);
                circle.setFill(Color.RED);
                Text dotCount = new Text(event.getX() + 3, event.getY(), String.valueOf(clickCount.get()));
                dotCount.setFill(Color.RED);
                clickCount.getAndIncrement();
                tempPane.getChildren().addAll(circle, dotCount);
            }
            else {
                if (!currentlySpawning) {
                    stopSpawning = false;
                    currentlySpawning = true;
                    addVehiclesUntilCount(vehicleCollidables.size(), tempPane, vehicleCollidables);
                    addBuses(busCollidables.size(), tempPane, busCollidables);
                    addPeople(personCollidables.size(), tempPane, personCollidables);
                }
                else {
                    System.out.println("Currently Spawning Bool: " + currentlySpawning);
                    System.out.println("Already Spawning");
                }
            }
        });
        root.setOnKeyPressed(event1 -> {
            if (event1.getCode() == KeyCode.SPACE) {
                if (!stopSpawning) {
                    stopSpawning = true;
                    currentlySpawning = false;
                } else {
                    currentlySpawning = false;
                }
            }
        });

        //Add lights to the map
        SystemController systemController = new SystemController();
        systemController.addLights(tempPane);
        root.getChildren().add(tempPane);
    }

    /**
     * Generates and adds cars to main scene in pane using count as an upper bound
     * @param count amount of vehicles in scene
     * @param tempPane create new temporary pane to place vehicles
     * @param vehicleCollidables vehicle object list to act on
     */
    public void addVehiclesUntilCount(int count, Pane tempPane, List<Vehicle> vehicleCollidables) {
        //System.out.println("Total Vehicles on Map: " + count);
        //System.out.println("Stop Spawning Boolean: " + stopSpawning);
        if (count >= 100 || stopSpawning) {
            return;
        }

        if(flag3D == false) {
            Vehicle vehicle = new Vehicle(tempPane, vehicleCollidables);

            vehicle.startAnimation();
            vehicleCollidables.add(vehicle);

            //Using a recursive method to guarantee that the pause actually occurs.
            PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(100));
            pause.setOnFinished(event1 -> {
                addVehiclesUntilCount(vehicleCollidables.size(), tempPane, vehicleCollidables);
            });
            pause.play();
        }
        else{
            Vehicle3D vehicle = new Vehicle3D(tempPane, vehicleCollidables3D);

            vehicle.startAnimation();
            vehicleCollidables3D.add(vehicle);

            //Using a recursive method to guarantee that the timeframe actually occurs.
            PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(100));
            pause.setOnFinished(event1 -> {
                addVehiclesUntilCount(vehicleCollidables3D.size(), tempPane, vehicleCollidables);
            });
            pause.play();
        }
    }

    /**
     * Generates and adds buses to main scene in pane using count as an upper bound
     * @param count upper bound on bus objects
     * @param tempPane pane to place bus objects
     * @param busCollidables bus objects themselves
     */
    public void addBuses(int count, Pane tempPane, List<Bus> busCollidables) {
        System.out.println("Total Buses on Map: " + count);
        if (count >= 10) {
            return;
        }

        Bus bus = new Bus(tempPane, busCollidables);

        bus.startAnimation();
        busCollidables.add(bus);

        //Using a recursive method to guarantee that the pause actually occurs.
        PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(1000));
        pause.setOnFinished(event1 -> {
            addBuses(busCollidables.size(), tempPane, busCollidables);
        });
        pause.play();
    }

    public void addBuses3D(int count, Pane tempPane, List<Bus3D> busCollidables) {
        System.out.println("Total Buses on Map: " + count);
        Bus3D bus = new Bus3D(tempPane, busCollidables3D);

        bus.startAnimation();
        busCollidables3D.add(bus);

        //Using a recursive method to guarantee that the pause actually occurs.
        PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(1000));
        pause.setOnFinished(event1 -> {
            addBuses3D(busCollidables3D.size(), tempPane, busCollidables3D);
        });
        pause.play();
    }

    /**
     * Generates and adds pedestrians to main scene in pane using count as an upper bound
     * @param count upper bound on pedestrian objects
     * @param tempPane pane to place pedestrian objects
     * @param personCollidables pedestrian objects themselves
     */
    public void addPeople(int count, Pane tempPane, List<Person> personCollidables) {
        System.out.println("Total Buses on Map: " + count);
        if (count >= 50) {
            return;
        }

        if(flag3D == false) {
            Person person = new Person(tempPane, personCollidables);

            person.startAnimation();
            personCollidables.add(person);

            //Using a recursive method to guarantee that the pause actually occurs.
            PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(300));
            pause.setOnFinished(event1 -> {
                addPeople(personCollidables.size(), tempPane, personCollidables);
            });
            pause.play();
        }
        else{
            Person3D person = new Person3D(tempPane, personCollidables);

            person.startAnimation();
            personCollidables3D.add(person);

            //Using a recursive method to guarantee that the pause actually occurs.
            PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(1000));
            pause.setOnFinished(event1 -> {
                addPeople(personCollidables3D.size(), tempPane, personCollidables);
            });
            pause.play();
        }
    }

    /**
     * Animation timer to check for collisions
     * (animation timers are a separate thread)
     */
    private void startCollisionTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkCollisions();
            }
        };
        timer.start();
    }

    /**
     * Checks for all vehicle collisions
     */
    private void checkCollisions() {
        for (int i = 0; i < vehicleCollidables.size(); i++) {
            for (int j = i + 1; j < vehicleCollidables.size(); j++) {
                Vehicle v1 = vehicleCollidables.get(i);
                Vehicle v2 = vehicleCollidables.get(j);
                //System.out.println(v2.returnCarShape().getBoundsInParent());
                if ((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth() > 0)
                        && !v1.returnCollided() && !v2.returnCollided()) {
                    //System.out.println((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth()));
                    //System.out.println((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getHeight()));
                    v2.setCollided(true);
                    v2.stopVehicle();
                } else if ((Shape.intersect(v1.returnCarShape(), v2.returnCarShape()).getBoundsInParent().getWidth() <= 0)
                        && !v1.returnCollided() && v2.returnCollided()) {
                    v2.setCollided(false);
                    v2.restartVehicle();
                }
//                else {
//                    if (v1.returnCollided()){
//                        v1.setCollided(false);
//                        v1.restartVehicle();
//                    }
//                    else if (v2.returnCollided()){
//                        v2.setCollided(false);
//                        v2.restartVehicle();
//                    }
//                }
            }
        }
    }

    /**
     * Check for each individual collision
     * @param src
     * @param other
     */
    private void checkCollision(Vehicle src, Vehicle other){
        if(Shape.intersect(src.returnCarShape(), other.returnCarShape()).getBoundsInLocal().getWidth() > -1){
            System.out.println("Collision Detected");
        }else if(Shape.intersect(src.returnCarShape(), other.returnCarShape()).getBoundsInLocal().getWidth() <= 0){
            //System.out.println("Collision Over");
        }
    }

    /**
     * Scales the street image for the pane
     * @param imageView
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public Pane resizeImage(ImageView imageView, double maxWidth, double maxHeight) {
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(maxWidth);
        imageView.setFitHeight(maxHeight);

        Pane pane = new Pane();
        pane.getChildren().add(imageView);

        imageView.layoutXProperty().bind(pane.widthProperty().subtract(imageView.fitWidthProperty()).divide(2));
        imageView.layoutYProperty().bind(pane.heightProperty().subtract(imageView.fitHeightProperty()).divide(2));

        return pane;
    }

    public Pane getRoot(){
        return root;
    }

    public boolean set3DFlag(){
        return flag3D = true;
    }


}
