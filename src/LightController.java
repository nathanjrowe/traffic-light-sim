import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
    //Vars to store the time in seconds for the lights
    private final int cycleTime = 120;
    private final int yellow = 6;
    private int minGreen = 15;
    private int maxGreen = 80;
    //Enum to control the cycle changes
    private enum direction {NS, EW};
    
    //Constructor
    //Takes a list of coordinates for the lights
    //lightCoord[0]: location of the light(N, S, E, W)
    //lightCoord[1]: x coordinate
    //lightCoord[2]: y coordinate
    //See SystemController for the list of coordinates
    public LightController(List<Object[]> lightCoords) {
        //Create the traffic lights at the intersection
        for(Object[] coord : lightCoords){
            createTrafficLight((String)coord[0], (Integer)coord[1], (Integer)coord[2]);
        }
    }

    //Create a traffic light
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

    //Create a pedestrian light
    //Not implemented
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

    /*
     * Method to add the lights to the root pane
     */
    public void addLights(Pane root) {
        for(Pane trafficLight : trafficLights.values()){
            root.getChildren().add(trafficLight);
        }
        for(Pane pedestrianLight : pedestrianLights.values()){
            root.getChildren().add(pedestrianLight);
        }
    }

    //Set the maximum green time for the lights
    public void setMaxGreen(int time) {
        this.maxGreen = time;
    }

    //Changes the state for a light at the intersection
    //TODO: Add changes for arrows and pedestrian lights
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

    //Animation cycle for the lights
    //Uses an animation timer to change light colors at the intersection
    //Counts time in milliseconds
    public void startCycle() {
        AnimationTimer timer = new AnimationTimer() {
            private direction dir = direction.NS;
            private int time = cycleTime;
            private int greenTime = minGreen;
            private int yellowTime = yellow;
            private int pedestrianTime = 0;
            private int pedestrianCycle = 0;
            private Duration lastUpdate = Duration.of(0, ChronoUnit.NANOS);
            @Override
            public void handle(long now) {
                Duration nowDur = Duration.of(now, ChronoUnit.NANOS);
                if (nowDur.minus(lastUpdate).toMillis() >= 1000) {
                    //Update last update time
                    lastUpdate = nowDur;  
                    //Change the light color for perpendicular lights
                    //Identified by the location of the light
                    //North and South lights
                    if(time <= 0){
                       
                        //Reset the time
                        time = cycleTime;
                        
                    }
                    //Change the light color for the current direction
                    if (greenTime > 0) {
                        if(dir == direction.NS){
                            changeLightState("N", "green");
                            changeLightState("S", "green");
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "green");
                            changeLightState("W", "green");
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
                            //Transition to the next direction
                            dir = direction.EW;
                        }
                        else if(dir == direction.EW){
                            changeLightState("E", "red");
                            changeLightState("W", "red");
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