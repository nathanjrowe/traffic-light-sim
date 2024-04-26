import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

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
    private final HashMap<Integer, Pane> pedestrianLights = new HashMap<>();
    //HashMap to store the collision boxes
    //Key stores the location of the light(N, S, E, W)
    private final HashMap<String, List<CollisionBox>> collisionBoxes = new HashMap<>(){{
        put("N", new ArrayList<>());
        put("S", new ArrayList<>());
        put("E", new ArrayList<>());
        put("W", new ArrayList<>());
    }};
    private CollisionBox intersectionBox = null;
    //Store a list of vehicles in the intersection
    private List<Vehicle> vehicles = new ArrayList<>();
    //Vars to store the time in seconds for the lights
    private final int cycleTime = 120;
    private final int yellow = 6;
    private int minGreen = 15;
    private int greenTime = minGreen;
    private int maxGreen = 30;
    private int vehicleCount = 0;
    private Vehicle currentVehicle = null;
    //Enum to control the cycle changes
    private enum direction {NS, EW};
    
    //Constructor
    //Takes a list of coordinates for the lights
    //lightCoord[0]: location of the light(N, S, E, W)
    //lightCoord[1]: x coordinate
    //lightCoord[2]: y coordinate
    //See SystemController for the list of coordinates
    public LightController(List<Object[]> lightCoords, List<Object[]> collisionCoords) {
        //Create the traffic lights at the intersection
        for(Object[] coord : lightCoords){
            createTrafficLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
        //Create the collision boxes at the intersection
        for(Object[] coord : collisionCoords){
            createCollisionBox((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
    }

    /**
     * Create a traffic light
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

    //Create a collision box
    protected void createCollisionBox(String location, double x, double y){
        CollisionBox collisionBox = new CollisionBox(x, y, 6, 6, this);
        collisionBox.setState(CollisionBox.State.STOP);
        //Add collision box to the list of collision boxes for the location
        collisionBoxes.get(location).add(collisionBox);
    }

    //Create the intersection box
    protected void createIntersectionBox(double x, double y, int width, int height){
        intersectionBox = new CollisionBox(x, y, width, height, this);
    }

    /**
     * Creates a pedestrian light, Not implemented
     * @param x position
     * @param y position
     * @return pane of pedestrian light
     */
    private Pane createPedestrianLight(double x, double y){
        Pane pedestrianLight = new Pane();
        PedestrianLightCreation pedestrianLightCreation = new PedestrianLightCreation();
        pedestrianLight.getChildren().add(pedestrianLightCreation.getPedestrianLight());
        pedestrianLight.setLayoutY(0);
        pedestrianLight.setLayoutX(0);
        pedestrianLight.setTranslateZ(0);
        pedestrianLights.put(pedestrianLightCreation.getId(), pedestrianLight);
        return pedestrianLight;
    }

    /**
     * Method to add the lights to the root pane
     * @param root
     */
    public void addLights(Pane root) {
        for(Pane trafficLight : trafficLights.values()){
            root.getChildren().add(trafficLight);
        }
        for(Pane pedestrianLight : pedestrianLights.values()){
            root.getChildren().add(pedestrianLight);
        }
        for(List<CollisionBox> collisionBox : collisionBoxes.values()){
            for(CollisionBox box : collisionBox){
                root.getChildren().add(box);
            }
        }
        if(intersectionBox != null) {
            root.getChildren().add(intersectionBox);
        }
    }

    public List<CollisionBox> getCollisionBoxes(){
        List<CollisionBox> boxes = new ArrayList<>();
        for(List<CollisionBox> collisionBox : collisionBoxes.values()){
            boxes.addAll(collisionBox);
        }
        return boxes;
    }
    //Set the maximum green time for the lights
    public void setMaxGreen(int time) {
        this.maxGreen = time;
    }

    //Increment the vehicle count if a vehicle passes through the intersection box
    public void incrementVehicleCount(List<Vehicle> allVehicles) {
        if(intersectionBox != null) {
            for (Vehicle vehicle : allVehicles) {
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
                        System.out.println("Vehicle Count: " + vehicleCount);
                        vehicles.add(vehicle);
                    }
                }
            }
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

    //Animation cycle for the lights
    //Uses an animation timer to change light colors at the intersection
    //Counts time in milliseconds
    public void startCycle() {
        AnimationTimer timer = new AnimationTimer() {
            private direction dir = direction.NS;
            private int time = cycleTime;
            private int yellowTime = yellow;
            private int pedestrianTime = 0;
            private int pedestrianCycle = 0;
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
                    //Change the light color for perpendicular lights
                    //Identified by the location of the light
                    //North and South lights
                    if(time <= 0){
                       
                        //Reset the time
                        time = cycleTime;
                        //Clean up the vehicle list
                        for (Vehicle vehicle : vehicles) {
                        if (!intersectionBox.getBoundsInParent().intersects(vehicle.getBoundsInGrandparent(vehicle.returnCarShape()))) {
                            vehicles.remove(vehicle);
                        }
                    }
                        
                    }
                    //Change the light color for the current direction
                    if (greenTime > 0) {
                        if(dir == direction.NS){
                            changeLightState("N", "green");
                            changeLightState("S", "green");
                            //Change the state of the collision boxes
                            for(CollisionBox box : collisionBoxes.get("N")){
                                box.setState(CollisionBox.State.GO);
                            }
                            for(CollisionBox box : collisionBoxes.get("S")){
                                box.setState(CollisionBox.State.GO);
                            }
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "green");
                            changeLightState("W", "green");
                            //Change the state of the collision boxes
                            for(CollisionBox box : collisionBoxes.get("E")){
                                box.setState(CollisionBox.State.GO);
                            }
                            for(CollisionBox box : collisionBoxes.get("W")){
                                box.setState(CollisionBox.State.GO);
                            }
                        }
                        greenTime--;
                    } else if (yellowTime > 0) {
                        if(dir == direction.NS){
                            changeLightState("N", "yellow");
                            changeLightState("S", "yellow");
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "yellow");
                            changeLightState("W", "yellow");
                        }
                        yellowTime--;
                    } else {
                        if(dir == direction.NS){
                            changeLightState("N", "red");
                            changeLightState("S", "red");
                            //Change the state of the collision boxes
                            for(CollisionBox box : collisionBoxes.get("N")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : collisionBoxes.get("S")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            //Transition to the next direction
                            dir = direction.EW;
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "red");
                            changeLightState("W", "red");
                            //Change the state of the collision boxes
                            for(CollisionBox box : collisionBoxes.get("E")){
                                box.setState(CollisionBox.State.STOP);
                            }
                            for(CollisionBox box : collisionBoxes.get("W")){
                                box.setState(CollisionBox.State.STOP);
                            }
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