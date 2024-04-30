import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.effect.Light;//unused import
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

/*
 * -------------------------------------
 * Class to control the traffic lights at an intersection
 * ToDo: Collision boxes to stop cars, pedestrian lights, arrows
 * -------------------------------------
 */
public class LightController {

    //HashMap to store the traffic lights
    //Key stores the location of the light(N, S, E, W)
    private final HashMap<String, Pane> trafficLights = new HashMap<>();

    //HashMap to store the pedestrian lights
    //Not implemented yet
    private final HashMap<String, Pane> pedestrianLights = new HashMap<>();
    //HashMap to store the lane collision boxes
    //Key stores the location of the light(N, S, E, W)
    private final HashMap<String, CollisionBox> laneCollisionBoxes = new HashMap<>();
    //HashMap to store the collision boxes
    //Key stores the location of the light(N, S, E, W)
    private final HashMap<String, List<CollisionBox>> lightCollisionBoxes = new HashMap<>(){{
        put("N", new ArrayList<>());
        put("S", new ArrayList<>());
        put("E", new ArrayList<>());
        put("W", new ArrayList<>());
        put("B", new ArrayList<>());
    }};

    //HashMap to store the collision boxes
    //Key stores the location of the light(N, S, E, W)
    private final HashMap<String, List<CollisionBox>> pedCollisionBoxes = new HashMap<>(){{
        put("N", new ArrayList<>());
        put("S", new ArrayList<>());
        put("E", new ArrayList<>());
        put("W", new ArrayList<>());
    }};

    private CollisionBox intersectionBox = null;
    
    //Store a list of vehicles in the intersection
    private CopyOnWriteArrayList<Vehicle3D> vehicles = new CopyOnWriteArrayList<>();
    
    //Vars to store the time in seconds for the lights
    private final int cycleTime = 120;
    private final int yellow = 6;
    //ID for the controller
    private final int id;
    private int minGreen = 15;
    private int greenTime = minGreen;
    private int maxGreen = 30;
    private int vehicleCount = 0;//currently unused
    private Vehicle currentVehicle = null;//currently unused
    private Boolean busApproaching = false;
    
    //Enum to control the cycle changes
    private enum direction {NS, EW};
    
    //Enum to mark the type of light
    protected enum lightType {STANDARD, BUS};
    private lightType type;
    
    //List of scheduled pedestrian light changes
    private List<String> pedLightChanges = new ArrayList<>();
    
    //Queue to store the pedestrians waiting to cross
    private List<Person3D> pedQueue = new ArrayList<>();
    
    //Lists to check if pedestrians are crossing
    private CopyOnWriteArrayList<Person3D> pedCrossingNorth = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Person3D> pedCrossingSouth = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Person3D> pedCrossingEast = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Person3D> pedCrossingWest = new CopyOnWriteArrayList<>();
    //Lists to check if left turn vehicles are in lanes
    private CopyOnWriteArrayList<Vehicle3D> trafficLaneNorth = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Vehicle3D> trafficLaneSouth = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Vehicle3D> trafficLaneEast = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Vehicle3D> trafficLaneWest = new CopyOnWriteArrayList<>();
    
    //Constructor
    //Takes a list of coordinates for the lights
    //lightCoord[0]: location of the light(N, S, E, W)
    //lightCoord[1]: x coordinate
    //lightCoord[2]: y coordinate
    //See SystemController for the list of coordinates
    public LightController(int id, lightType type, List<Object[]> lightCoords, List<Object[]> lightCollisionCoords, List<Object[]> pedestrianLightCoords, List<Object[]> pedestrianCollisionCoords, List<Object[]> laneCollisionCoords) {
        //Create the traffic lights at the intersection
        for(Object[] coord : lightCoords){
            createTrafficLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the collision boxes at the intersection
        for(Object[] coord : lightCollisionCoords){
            createLightCollisionBox((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the pedestrian lights at the intersection
        for(Object[] coord : pedestrianLightCoords){
            //createPedestrianLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the collision boxes for the pedestrian lights
        for(Object[] coord : pedestrianCollisionCoords){
            createPedCollisionBox((String)coord[0], (Integer)coord[1], (Integer)coord[2], (Integer)coord[3], (Integer)coord[4]);
        }
        for(Object[] coord : laneCollisionCoords){
            createLaneCollisionBoxes((String)coord[0], (Integer)coord[1], (Integer)coord[2], (Integer)coord[3], (Integer)coord[4]);
        }
        this.type = type;
        this.id = id;
    }

    public LightController(int id, lightType type, List<Object[]> lightCoords, List<Object[]> lightCollisionCoords, List<Object[]> pedestrianLightCoords, List<Object[]> pedestrianCollisionCoords) {
        //Create the traffic lights at the intersection
        for(Object[] coord : lightCoords){
            createTrafficLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the collision boxes at the intersection
        for(Object[] coord : lightCollisionCoords){
            createLightCollisionBox((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the pedestrian lights at the intersection
        for(Object[] coord : pedestrianLightCoords){
            //createPedestrianLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the collision boxes for the pedestrian lights
        for(Object[] coord : pedestrianCollisionCoords){
            createPedCollisionBox((String)coord[0], (Integer)coord[1], (Integer)coord[2], (Integer)coord[3], (Integer)coord[4]);
        }
        this.type = type;
        this.id = id;
    }

    /**
     * creates a traffic light at a given location with a layout determined by x and y
     * the light is initially constructed on its own pane, and is added to its given location
     * before being returned.
     * 
     * @param location
     * @param x position
     * @param y position
     * @return pane of trafic light
     */
    private Pane createTrafficLight(String location, Integer x, Integer y){
        //Create a pane to hold the traffic light
        Pane trafficLight = new Pane();

        //Create the traffic light object
        TrafficLight trafficLightData = new TrafficLight();
        trafficLight = trafficLightData.getTrafficLight();

        //Set the location of the light
        trafficLight.setLayoutY(y);
        trafficLight.setLayoutX(x);
        trafficLight.setTranslateZ(0);

        //Add the light to the hashmap
        trafficLights.put(location, trafficLight);
        return trafficLight;
    }


    /* Creates a collision box in the road for buses and cars.
     * 
     * This function is one of two that manages the collision boxes at each intersection.
     * These collision boxes will manage the flow of traffic by phisically stopping any
     * car/bus/pedestrian with a collision box.
     * 
     * The alternating state of these collision boxes, "GO" and "STOP" determine if they are active,
     * this is necessary to prevent cars/buses/pedestrians from getting stuck inside the intersection
     * when the light changes.
     */
    protected void createLightCollisionBox(String location, double x, double y){
        CollisionBox collisionBox;
        if(location == "B") {
            collisionBox = new CollisionBox(x, y, 300, 6, this);
            collisionBox.setState(CollisionBox.State.GO);
        }
        else {
            collisionBox = new CollisionBox(x, y, 6, 6, this);
            collisionBox.setState(CollisionBox.State.STOP);
        }
        
        //Add collision box to the list of collision boxes for the location
        lightCollisionBoxes.get(location).add(collisionBox);
    }

    /* Creates a collision box on the sidewalk for pedestrians. 
     *
     * As with the above "createLightCollisionBox" function this function is used to manage the flow of traffic
     * by phisically blocking one direction with a collision box.
     */
    protected void createPedCollisionBox(String location, double x, double y, int width, int height){
        CollisionBox collisionBox = new CollisionBox(x, y, width, height, this);
        collisionBox.setState(CollisionBox.State.STOP);
        //Add collision box to the list of collision boxes for the location
        pedCollisionBoxes.get(location).add(collisionBox);
    }


    //This method is currently unused...
    protected void createIntersectionBox(double x, double y, int width, int height){
        intersectionBox = new CollisionBox(x, y, width, height, this);
    }
    //Create a set of lane collision boxes
    protected void createLaneCollisionBoxes(String location, double x, double y, int width, int height){
        CollisionBox collisionBox = new CollisionBox(x, y, width, height, this);
        laneCollisionBoxes.put(location, collisionBox);
    }

    /**This method is currently unused...
     * 
     * Creates a pedestrian light, Not implemented
     * @param x position
     * @param y position
     * @return pane of pedestrian light
     */
    private Pane createPedestrianLight(String location, double x, double y){
        Pane pedestrianLight = new Pane();
        PedestrianLight pedestrianLightCreation = new PedestrianLight();
        pedestrianLight.getChildren().add(pedestrianLightCreation.getPedestrianLight());
        pedestrianLight.setLayoutY(0);
        pedestrianLight.setLayoutX(0);
        pedestrianLight.setTranslateZ(0);
        pedestrianLights.put(location, pedestrianLight);
        return pedestrianLight;
    }

    /**
     * addLights adds each traffic/pedestrian light to the main scene
     * it does this by looping through the list of lights and adding them to the root scene.
     * each traffic light required a bit of tweaking to get its scale and position correct.
     * 
     * The function also adds each correspponding traffic & pedestrian collision box
     * 
     * @param root
     */
    public void addLights(Pane root) {
        for(Pane trafficLight : trafficLights.values()){
            root.getChildren().add(trafficLight);

       /*     Pane trafficlightLeftSide = new Pane(trafficLight);

            root.getChildren().add(trafficlightLeftSide);
            //Light scale and position 3D
            trafficlightLeftSide.setTranslateZ(-150);
            trafficlightLeftSide.setScaleY(1.25);
            trafficlightLeftSide.setScaleX(1.25);*/

            trafficLight.setTranslateZ(-150);
            trafficLight.setScaleY(1.25);
            trafficLight.setScaleX(1.25);
            //Light rotations 3D
            trafficLight.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS),new Rotate(90, Rotate.Y_AXIS),
                    new Rotate(0, Rotate.Z_AXIS));
        }
        for(Pane pedestrianLight : pedestrianLights.values()){
            root.getChildren().add(pedestrianLight);
        }
        for(List<CollisionBox> collisionBox : lightCollisionBoxes.values()){
            for(CollisionBox box : collisionBox){
                root.getChildren().add(box);
            }
        }
        for(List<CollisionBox> collisionBox : pedCollisionBoxes.values()){
            for(CollisionBox box : collisionBox){
                root.getChildren().add(box);
            }
        }
        for(CollisionBox box : laneCollisionBoxes.values()){
            root.getChildren().add(box);
        }
        if(intersectionBox != null) {
            root.getChildren().add(intersectionBox);
        }
    }

    //Get the type of light, returns STANDARD or BUS
    public LightController.lightType getType() {
        return type;
    }
    
    //Get the id for the light controller
    public int getId() {
        return id;
    }

    //Get the bus collision boxes
    public List<CollisionBox> getBusCollisionBoxes() {
        return lightCollisionBoxes.get("B");
    }

    //Check collsions with bus collision boxes
    public void checkBusCollision(List<Bus3D> allBuses) {
        //Print the size of the list holding the boxes
      //  System.out.println("Number of collision boxes bus: " + lightCollisionBoxes.get("B").size());
        if(lightCollisionBoxes.get("B").size() == 0){
            return;
        }
        CollisionBox box1 = lightCollisionBoxes.get("B").get(0);
        CollisionBox box2 = lightCollisionBoxes.get("B").get(1);
        for (Bus3D bus : allBuses) {
            for (CollisionBox box : lightCollisionBoxes.get("B")) {
                if (box.isColliding(bus.returnCarShape().getBoundsInParent())) {
                    this.busApproaching = true;
                }
            }
            //Check if there is no bus in either lanes
            if(!box1.isColliding(bus.returnCarShape().getBoundsInParent()) && !box2.isColliding(bus.returnCarShape().getBoundsInParent())){
                this.busApproaching = false;

            }
        }
    }

    /*
     * gets the list of light collision boxes
     */
    public List<CollisionBox> getLightCollisionBoxes(){
        List<CollisionBox> boxes = new ArrayList<>();
        for(List<CollisionBox> collisionBox : lightCollisionBoxes.values()){
            boxes.addAll(collisionBox);
        }
        return boxes;
    }

    /*
     * gets the list of pedestrian collision boxes
     */
    public List<CollisionBox> getPedCollisionBoxes(){
        List<CollisionBox> boxes = new ArrayList<>();
        for(List<CollisionBox> collisionBox : pedCollisionBoxes.values()){
            boxes.addAll(collisionBox);
        }
        return boxes;
    }
    
    //Set the maximum green time for the lights
    public void setMaxGreen(int time) {
        this.maxGreen = time;
    }

    //Increment the vehicle count if a vehicle passes through the intersection box
    public void incrementVehicleCount(List<Vehicle3D> allVehicles) {
        if(intersectionBox != null) {
            for (Vehicle3D vehicle : allVehicles) {
                if (intersectionBox.getBoundsInParent().intersects(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))) {
                    if(!vehicles.contains(vehicle)) {
                        //Increment the green time up to the max green time
                        //Only increment time if there are lights in the green state
                        if(greenTime < maxGreen) {
                            if(getLightState("N") == TrafficLight.LightColor.GREEN ||
                             getLightState("S") == TrafficLight.LightColor.GREEN ||
                            getLightState("E") == TrafficLight.LightColor.GREEN || 
                            getLightState("W") == TrafficLight.LightColor.GREEN) {
                                greenTime++;
                            }
                        }
                        vehicleCount++;
                        //System.out.println("Vehicle Count: " + vehicleCount);
                        vehicles.add(vehicle);
                    }
                }
            }
        }
    }

    //Method to check pedestrian collision
    public void checkPedestrianCollision(List<Person3D> allPeople) {
        for (Person3D person : allPeople) {
            for (CollisionBox box : pedCollisionBoxes.get("N")) {
                if (box.isColliding(person.getBoundsInGrandparent(person.returnCarShape()))) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("N")) {
                        pedLightChanges.add("N");   
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingNorth.contains(person)) {
                        pedCrossingNorth.add(person);
                    }
                }
                
            }
            for (CollisionBox box : pedCollisionBoxes.get("S")) {
                if (box.isColliding(person.getBoundsInGrandparent(person.returnCarShape()))) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("S")) {
                        pedLightChanges.add("S");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingSouth.contains(person)) {
                        pedCrossingSouth.add(person);
                    }
                }
            }
            for (CollisionBox box : pedCollisionBoxes.get("E")) {
                if (box.isColliding(person.getBoundsInGrandparent(person.returnCarShape()))) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("E")) {
                        pedLightChanges.add("E");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingEast.contains(person)) {
                        pedCrossingEast.add(person);
                    }
                }
            }
            for (CollisionBox box : pedCollisionBoxes.get("W")) {
                if (box.isColliding(person.getBoundsInGrandparent(person.returnCarShape()))) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("W")) {
                        pedLightChanges.add("W");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingWest.contains(person)) {
                        pedCrossingWest.add(person);
                    }   
                }
            }
        }
        //Remove the Person3D from the list if they are no longer colliding
        for (Person3D person : pedCrossingNorth) {
            if (!pedCollisionBoxes.get("N").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingNorth.remove(person);
            }
        }
        for (Person3D person : pedCrossingSouth) {
            if (!pedCollisionBoxes.get("S").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingSouth.remove(person);
            }
        }
        for (Person3D person : pedCrossingEast) {
            if (!pedCollisionBoxes.get("E").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingEast.remove(person);
            }
        }
        for (Person3D person : pedCrossingWest) {
            if (!pedCollisionBoxes.get("W").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingWest.remove(person);
            }
        }

    }

    public void checkPedestrianCollision3D(List<Person3D> allPeople) {
        for (Person3D person : allPeople) {
            for (CollisionBox box : pedCollisionBoxes.get("N")) {
                if (box.isColliding(person.returnCarShape().getBoundsInParent())) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("N")) {
                        pedLightChanges.add("N");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingNorth.contains(person)) {
                        pedCrossingNorth.add(person);
                    }
                }

            }
            for (CollisionBox box : pedCollisionBoxes.get("S")) {
                if (box.isColliding(person.returnCarShape().getBoundsInParent())) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("S")) {
                        pedLightChanges.add("S");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingSouth.contains(person)) {
                        pedCrossingSouth.add(person);
                    }
                }
            }
            for (CollisionBox box : pedCollisionBoxes.get("E")) {
                if (box.isColliding(person.returnCarShape().getBoundsInParent())) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("E")) {
                        pedLightChanges.add("E");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingEast.contains(person)) {
                        pedCrossingEast.add(person);
                    }
                }
            }
            for (CollisionBox box : pedCollisionBoxes.get("W")) {
                if (box.isColliding(person.returnCarShape().getBoundsInParent())) {
                    if (box.getState() != CollisionBox.State.GO && !pedLightChanges.contains("W")) {
                        pedLightChanges.add("W");
                    }
                    if (box.getState() == CollisionBox.State.GO && !pedCrossingWest.contains(person)) {
                        pedCrossingWest.add(person);
                    }
                }
            }
        }
        //Remove the Person3D from the list if they are no longer colliding
        for (Person3D person : pedCrossingNorth) {
            if (!pedCollisionBoxes.get("N").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingNorth.remove(person);
            }
        }
        for (Person3D person : pedCrossingSouth) {
            if (!pedCollisionBoxes.get("S").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingSouth.remove(person);
            }
        }
        for (Person3D person : pedCrossingEast) {
            if (!pedCollisionBoxes.get("E").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingEast.remove(person);
            }
        }
        for (Person3D person : pedCrossingWest) {
            if (!pedCollisionBoxes.get("W").get(0).isColliding(person.returnCarShape().getBoundsInParent())) {
                pedCrossingWest.remove(person);
            }
        }

    }

    //Method to check for vehicle collisions with lane boxes
    //Determines right of way for vehicles
    public void checkLaneCollision(List<Vehicle3D> allVehicles) {
        //Add a vehicle to the lane list if it is in the lane
        for (Vehicle3D vehicle : allVehicles) {
            if(laneCollisionBoxes.get("N").isColliding(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))){
                trafficLaneNorth.add(vehicle);
            }
            if(laneCollisionBoxes.get("S").isColliding(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))){
                trafficLaneSouth.add(vehicle);
            }
            if(laneCollisionBoxes.get("E").isColliding(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))){
                trafficLaneEast.add(vehicle);
            }
            if(laneCollisionBoxes.get("W").isColliding(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))){
                trafficLaneWest.add(vehicle);
            }
        }
        //Check each lane for left turning vehicles
        //If a vehicle is in the lane, set the state of the opposing lane to stop
        /*
        for(Vehicle3D vehicle : trafficLaneNorth){
            if(vehicle.getTurnDirection() == Vehicle3D.TurnDirection.LEFT){
                laneCollisionBoxes.get("S").setState(CollisionBox.State.STOP);
            }
        }
        for(Vehicle3D vehicle : trafficLaneSouth){
            if(vehicle.getTurnDirection() == Vehicle3D.TurnDirection.LEFT){
                laneCollisionBoxes.get("N").setState(CollisionBox.State.STOP);
            }
        }
        for(Vehicle3D vehicle : trafficLaneEast){
            if(vehicle.getTurnDirection() == Vehicle3D.TurnDirection.LEFT){
                laneCollisionBoxes.get("W").setState(CollisionBox.State.STOP);
            }
        }
        for(Vehicle3D vehicle : trafficLaneWest){
            if(vehicle.getTurnDirection() == Vehicle3D.TurnDirection.LEFT){
                laneCollisionBoxes.get("E").setState(CollisionBox.State.STOP);
            }
        }
        */
    }

    //Method to activate the pedestrian lights
    public void changePedestrianLight(String location, PedestrianLight.LightColor state) {
        Pane pedestrianLight = pedestrianLights.get(location);
        //Get the light attached to the pane and change the color
        PedestrianLight pedestrianLightData = (PedestrianLight) pedestrianLight.getUserData();
        switch (state) {
            case RED:
                pedestrianLightData.setRedHand();
                break;
            case WALKING:
                pedestrianLightData.setWalking();
                break;
        }
    }

    //TODO: Add changes for arrows and pedestrian lights
    /**
     * Changes the state for a light at the intersection
     * @param location
     * @param color
     */
    private void changeLightState(String location, String color) {
        Pane trafficLight = trafficLights.get(location);
        //Get the light attached to the pane and change the color
        if(trafficLight == null){
            return;
        }
        TrafficLight trafficLightData = (TrafficLight) trafficLight.getUserData();
        switch (color) {
            case "red":
                trafficLightData.setRed();
                break;
            case "yellow":
                trafficLightData.setYellow();
                break;
            case "green":
                trafficLightData.setGreen();
                break;
            default:
                break;
        }
    }
    //Get the light state
    private TrafficLight.LightColor getLightState(String location) {
        Pane trafficLight = trafficLights.get(location);
        //Get the light attached to the pane and change the color
        TrafficLight trafficLightData = (TrafficLight) trafficLight.getUserData();
        return trafficLightData.getLightColor();
    }

    //Send vehicle count to the system controller
    public void sendData() {
        this.maxGreen = SystemController.updateMaxGreenTime(this.vehicleCount);
        this.vehicleCount = 0;
        TrafficScene.setData("Traffic Light: " + getId()+ "\nAdjusting Green Time: " + maxGreen);
    }

    //Animation cycle for the lights
    //Uses an animation timer to change light colors at the intersection
    //Counts time in milliseconds
    public void startCycle() {
        AnimationTimer timer = new AnimationTimer() {
            private direction dir = direction.NS;
            private int time = cycleTime;
            private int yellowTime = yellow;
            private Duration lastUpdate = Duration.of(0, ChronoUnit.NANOS);

            /**
             *
             * @param now
             *            The timestamp of the current frame given in nanoseconds. This
             *            value will be the same for all {@code AnimationTimers} called
             *            during one frame.
             */
            @Override
            public void handle(long now) {
                Duration nowDur = Duration.of(now, ChronoUnit.NANOS);
                if (nowDur.minus(lastUpdate).toMillis() >= 150) {
                    //Update last update time
                    lastUpdate = nowDur;

                    for (Vehicle3D vehicle : vehicles) {
                        if (!intersectionBox.getBoundsInParent().intersects(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))) {
                            vehicles.remove(vehicle);
                    }

                    //Update min green time to 30 seconds if the pedestrian light contains an item
                    if(pedLightChanges.size() > 0){
                        minGreen = 30;
                    }
                    else{
                        minGreen = 15;
                    }
                    //If bus light, set green time to 0 if a pedestrian light contains an item
                    
                    if(type == lightType.BUS){
                        //System.out.println(busApproaching);
                        if(busApproaching || pedLightChanges.size() > 0 || !pedCrossingNorth.isEmpty() || !pedCrossingSouth.isEmpty() || !pedCrossingEast.isEmpty() || !pedCrossingWest.isEmpty()){
                            greenTime = 0;
                            yellowTime = 0;
                        }
                    } 
                    //Change the light color for perpendicular lights
                    //Identified by the location of the light
                    //North and South lights
                    if(time <= 0){
                       
                        //Reset the time
                        time = cycleTime;
                        //Send traffic data to the system controller
                        sendData();
                    }
                        
                    }
                    //Change the light color for the current direction
                    if (greenTime > 0) {
                        if(dir == direction.NS && pedCrossingNorth.isEmpty() && pedCrossingSouth.isEmpty()){
                            changeLightState("N", "green");
                            changeLightState("S", "green");
                            //Change the state of the collision boxes
                            for(CollisionBox box : lightCollisionBoxes.get("N")){
                                box.setState(CollisionBox.State.GO);
                            }
                            for(CollisionBox box : lightCollisionBoxes.get("S")){
                                box.setState(CollisionBox.State.GO);
                            }
                            
                        }
                        else if(dir == direction.EW && pedCrossingEast.isEmpty() && pedCrossingWest.isEmpty()){
                            changeLightState("E", "green");
                            changeLightState("W", "green");
                            //Change the state of the collision boxes
                            for(CollisionBox box : lightCollisionBoxes.get("E")){
                                box.setState(CollisionBox.State.GO);
                            }
                            for(CollisionBox box : lightCollisionBoxes.get("W")){
                                box.setState(CollisionBox.State.GO);
                            }
                            
                        }
                        //Decrement the green time
                        greenTime--;
                        //Increment green time if bus lane and no pedestrians
                        if(type == lightType.BUS){
                            if(!busApproaching && pedLightChanges.size() == 0){
                                greenTime++;
                            }
                        }
                    } else if (yellowTime > 0) {
                        if(dir == direction.NS){
                            changeLightState("N", "yellow");
                            changeLightState("S", "yellow");
                            //Set the pedestrian collision boxes to stop
                            for(CollisionBox box : pedCollisionBoxes.get("E")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : pedCollisionBoxes.get("W")){
                                box.setState(CollisionBox.State.STOP);
                            }
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "yellow");
                            changeLightState("W", "yellow");
                            //Set the pedestrian collision boxes to stop
                            for(CollisionBox box : pedCollisionBoxes.get("N")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : pedCollisionBoxes.get("S")){
                                box.setState(CollisionBox.State.STOP);
                            }
                        }
                        yellowTime--;
                        //Red light logic
                    } else {
                        for(CollisionBox box : pedCollisionBoxes.get("E")){
                            box.setState(CollisionBox.State.STOP);
                        }
                        for(CollisionBox box : pedCollisionBoxes.get("W")){
                            box.setState(CollisionBox.State.STOP);
                        }
                        for(CollisionBox box : pedCollisionBoxes.get("N")){
                            box.setState(CollisionBox.State.STOP);
                        }
                        for(CollisionBox box : pedCollisionBoxes.get("S")){
                            box.setState(CollisionBox.State.STOP);
                        }
                        if(dir == direction.NS){
                            changeLightState("N", "red");
                            changeLightState("S", "red");
                            
                            //Change the state of the collision boxes
                            for(CollisionBox box : lightCollisionBoxes.get("N")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : lightCollisionBoxes.get("S")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            //Change the east and west pedestrian lights to walk if the pedestrian light contains an item
                            if(pedLightChanges.contains("S")){
                                //changePedestrianLight("S", PedestrianLight.LightColor.WALKING);
                                //Set the collision boxes to go
                                for(CollisionBox box : pedCollisionBoxes.get("S")){
                                    System.out.println("Pedestrian Light changing");
                                    box.setState(CollisionBox.State.GO);
                                }
                                //Remove the item from the list
                                pedLightChanges.remove("S");
                            }
                            if(pedLightChanges.contains("N")){
                               // changePedestrianLight("N", PedestrianLight.LightColor.WALKING);
                                //Set the collision boxes to go
                                for(CollisionBox box : pedCollisionBoxes.get("N")){
                                    box.setState(CollisionBox.State.GO);
                                }
                                //Remove the item from the list
                                pedLightChanges.remove("N");
                            }
                            
                            //Set East and West pedestrian lights to stop
                            //changePedestrianLight("E", PedestrianLight.LightColor.RED);
                            //changePedestrianLight("W", PedestrianLight.LightColor.RED);
                           
                            //Transition to the next direction
                            if(type == LightController.lightType.STANDARD){
                                dir = direction.EW;
                            }
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "red");
                            changeLightState("W", "red");
                            //Change the state of the collision boxes
                            for(CollisionBox box : lightCollisionBoxes.get("E")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : lightCollisionBoxes.get("W")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            //Change the east and west pedestrian lights to walk if the pedestrian light contains an item
                            if(pedLightChanges.contains("E")){
                                //changePedestrianLight("E", PedestrianLight.LightColor.WALKING);
                                //Set the collision boxes to go
                                for(CollisionBox box : pedCollisionBoxes.get("E")){
                                    box.setState(CollisionBox.State.GO);
                                }
                                //Remove the item from the list
                                pedLightChanges.remove("E");
                            }
                            if(pedLightChanges.contains("W")){
                               // changePedestrianLight("W", PedestrianLight.LightColor.WALKING);
                                //Set the collision boxes to go
                                for(CollisionBox box : pedCollisionBoxes.get("W")){
                                    box.setState(CollisionBox.State.GO);
                                }
                                //Remove the item from the list
                                pedLightChanges.remove("W");
                            }
                            
                            //Set North and South pedestrian lights to stop
                            //changePedestrianLight("N", PedestrianLight.LightColor.RED);
                            //changePedestrianLight("S", PedestrianLight.LightColor.RED);
                            
                            dir = direction.NS;
                        }
                        //Set the green time for the next direction
                        greenTime = minGreen;
                        yellowTime = yellow;
                    }
                    time--;

                }
            }
        };
        timer.start();
    }
}