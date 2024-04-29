import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Pane;

//TODO: Receive and send data to the light controllers
/**
 * Controller to communicate to individual light controllers
 */
public class SystemController {
    /**
     * Store the coordinates of the lights for each intersection
     * Pass to each light controller the coordinate of the lights
     * Object[] = {String direction, int x, int y}
     */
    private final HashMap<Integer, ArrayList<Object[]>> lightCoords = new HashMap<>(){{
        put(1, new ArrayList<Object[]>(){{
            add(new Object[]{"W", 256, 108});
            add(new Object[]{"N", 316, 93});
            add(new Object[]{"E", 337, 126});
            add(new Object[]{"S", 280, 140});
        }});
        put(2, new ArrayList<Object[]>(){{
            add(new Object[]{"W", 594, 108});
            add(new Object[]{"N", 652, 92});
            add(new Object[]{"E", 675, 127});
            add(new Object[]{"S", 618, 141});
        }});
        put(3, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 314, 342});
            add(new Object[]{"E", 340, 429});
            add(new Object[]{"S", 279, 459});
            add(new Object[]{"W", 255, 375});
        }});
        put(4, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 652, 342});
            add(new Object[]{"E", 675, 429});
            add(new Object[]{"S", 615, 460});
            add(new Object[]{"W", 592, 374});
        }});
        put(5, new ArrayList<Object[]>(){{
            add(new Object[]{"S", 278, 692});
            add(new Object[]{"N", 314, 643});
            add(new Object[]{"W", 257, 659});
            add(new Object[]{"E", 337, 677});
        }});
        put(6, new ArrayList<Object[]>(){{
            add(new Object[]{"S", 617, 691});
            add(new Object[]{"N", 652, 643});
            add(new Object[]{"W", 595, 658});
            add(new Object[]{"E", 674, 676});
        }});
        //Coordinates for lights at bus intersections
        put(7, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 309, 640});
            add(new Object[]{"S", 275, 688});
        }});
        put(8, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 648, 640});
            add(new Object[]{"S", 614, 688});
        }});
    }};

    //HashMap to store the collision box coordinates for each intersection
    //Object[] = {String associated light, double xCenter, double yCenter}
    private final HashMap<Integer, ArrayList<Object[]>> lightCollisionCoords = new HashMap<>(){{
        put(1, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 322, 155});
            add(new Object[]{"N", 302, 156});
            add(new Object[]{"S", 264, 86});
            add(new Object[]{"S", 284, 86});
            add(new Object[]{"E", 242, 130});
            add(new Object[]{"W", 342, 111});
        }});
        put(2, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 636, 156});
            add(new Object[]{"N", 656, 156});
            add(new Object[]{"S", 622, 86});
            add(new Object[]{"S", 602, 86});
            add(new Object[]{"E", 576, 130});
            add(new Object[]{"W", 686, 111});
        }});
        put(3, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 299, 467});
            add(new Object[]{"N", 319, 467});
            add(new Object[]{"S", 268, 328});
            add(new Object[]{"S", 284, 328});
            add(new Object[]{"E", 234, 408});
            add(new Object[]{"E", 234, 428});
            add(new Object[]{"E", 234, 448});
            add(new Object[]{"W", 345, 355});
            add(new Object[]{"W", 345, 375});
            add(new Object[]{"W", 345, 395});
        }});
        put(4, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 641, 469});
            add(new Object[]{"N", 661, 469});
            add(new Object[]{"S", 602, 330});
            add(new Object[]{"S", 622, 330});
            add(new Object[]{"E", 570, 410});
            add(new Object[]{"E", 570, 425});
            add(new Object[]{"E", 570, 445});
            add(new Object[]{"W", 682, 355});
            add(new Object[]{"W", 682, 375});
            add(new Object[]{"W", 682, 395});
        }});
        put(5, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 300, 699});
            add(new Object[]{"N", 320, 699});
            add(new Object[]{"S", 264, 630});
            add(new Object[]{"S", 284, 630});
            add(new Object[]{"E", 237, 674});
            add(new Object[]{"W", 346, 653});
        }});
        put(6, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 638, 699});
            add(new Object[]{"N", 658, 699});
            add(new Object[]{"S", 600, 630});
            add(new Object[]{"S", 620, 630});
            add(new Object[]{"E", 575, 674});
            add(new Object[]{"W", 685, 653});
        }});
        //Busses
        put(7, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 322, 589});
            add(new Object[]{"N", 302, 589});
            add(new Object[]{"S", 284, 528});
            add(new Object[]{"S", 264, 528});
            add(new Object[]{"B", 250, 548});
            add(new Object[]{"B", 30, 568});
        }});
        put(8, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 636, 589});
            add(new Object[]{"N", 656, 589});
            add(new Object[]{"S", 602, 528});
            add(new Object[]{"S", 622, 528});
            add(new Object[]{"B", 602, 548});
            add(new Object[]{"B", 372, 568});
        }});
    }};
    //HashMap to store the pedestrian light coordinates for each intersection
    //Object[] = {String associated light, double xCenter, double yCenter}
    private final HashMap<Integer, ArrayList<Object[]>> pedestrianLights = new HashMap<>(){{
        put(1, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 322, 155});
            add(new Object[]{"S", 264, 86});
            add(new Object[]{"E", 242, 130});
            add(new Object[]{"W", 342, 111});
        }});
        put(2, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 636, 156});
            add(new Object[]{"S", 622, 86});
            add(new Object[]{"E", 576, 130});
            add(new Object[]{"W", 686, 111});
        }});
        put(3, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 299, 467});
            add(new Object[]{"S", 268, 328});
            add(new Object[]{"E", 234, 408});
            add(new Object[]{"W", 345, 355});
        }});
        put(4, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 641, 469});
            add(new Object[]{"S", 602, 330});
            add(new Object[]{"E", 570, 410});
            add(new Object[]{"W", 682, 355});
        }});
        put(5, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 300, 699});
            add(new Object[]{"S", 264, 630});
            add(new Object[]{"E", 237, 674});
            add(new Object[]{"W", 346, 653});
        }});
        put(6, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 638, 699});
            add(new Object[]{"S", 600, 630});
            add(new Object[]{"E", 575, 674});
            add(new Object[]{"W", 685, 653});
        }});
        //Pedestrian lights for busses
        put(7, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 322, 589});
            add(new Object[]{"S", 284, 528});
        }});
        put(8, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 636, 589});
            add(new Object[]{"S", 602, 528});
        }});
    }};
    //HashMap to store the pedestrian light collision box coordinates for each intersection
    //Object[] = {String associated light, double xCenter, double yCenter, double width, double height}
    private final HashMap<Integer, ArrayList<Object[]>> pedCollisionCoords = new HashMap<>(){{
        put(1, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 254, 97, 76, 1});
            //add(new Object[]{"N", 330, 97});
            add(new Object[]{"S", 257, 150, 76, 1});
            //add(new Object[]{"S", 328, 150});
            add(new Object[]{"E", 340, 106, 1, 30});
            //add(new Object[]{"E", 340, 136});
            add(new Object[]{"W", 241, 106, 1, 30});
            //add(new Object[]{"W", 241, 139});
        }});
        put(2, new ArrayList<Object[]>(){{
            //add(new Object[]{"N", 667, 92});
            add(new Object[]{"N", 595, 92, 76, 1});
            //add(new Object[]{"S", 667, 150});
            add(new Object[]{"S", 595, 150, 76, 1});
            add(new Object[]{"E", 678, 106, 1, 30});
            //add(new Object[]{"E", 678, 136});
            add(new Object[]{"W", 586, 106, 1, 30});
            //add(new Object[]{"W", 586, 139});
        }});
        put(3, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 255, 340, 70, 1});
           // add(new Object[]{"N", 325, 340});
            add(new Object[]{"S", 254, 465, 70, 1});
            //add(new Object[]{"S", 330, 465});
            add(new Object[]{"E", 337, 353, 1, 103});
            //add(new Object[]{"E", 337, 459});
            add(new Object[]{"W", 241, 353, 1, 103});
            //add(new Object[]{"W", 241, 454});
        }});
        put(4, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 595, 340, 76, 1});
            //add(new Object[]{"N", 666, 340});
            //add(new Object[]{"S", 666, 465});
            add(new Object[]{"S", 595, 465, 76, 1});
            add(new Object[]{"E", 679, 350, 1, 109});
            //add(new Object[]{"E", 679, 459});
            add(new Object[]{"W", 583, 350, 1, 109});
            //add(new Object[]{"W", 583, 459});
        }});
        put(5, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 254, 638, 76, 1});
            //add(new Object[]{"N", 330, 638});
            add(new Object[]{"S", 257, 690, 76, 1});
            //add(new Object[]{"S", 328, 690});
            add(new Object[]{"E", 340, 645, 1, 37});
            //add(new Object[]{"E", 340, 682});
            add(new Object[]{"W", 241, 645, 1, 37});
            //add(new Object[]{"W", 241, 682});
        }});
        put(6, new ArrayList<Object[]>(){{
            //add(new Object[]{"N", 667, 638});
            add(new Object[]{"N", 595, 638, 76, 1});
            //add(new Object[]{"S", 667, 690});
            add(new Object[]{"S", 595, 690, 76, 1});
            add(new Object[]{"E", 678, 645, 1, 37});
            //add(new Object[]{"E", 678, 682});
            add(new Object[]{"W", 586, 645, 1, 37});
            //add(new Object[]{"W", 586, 682});
        }});
        //Pedestrian lights for busses
        put(7, new ArrayList<Object[]>(){{
            add(new Object[]{"N", 254, 530, 76, 1});
            //add(new Object[]{"N", 330, 530});
            add(new Object[]{"S", 257, 590, 76, 1});
            //add(new Object[]{"S", 328, 590});
            add(new Object[]{"E", 340, 539, 1, 39});
            //add(new Object[]{"E", 340, 578});
            add(new Object[]{"W", 241, 539, 1, 39});
            //add(new Object[]{"W", 241, 578});
        }});
        put(8, new ArrayList<Object[]>(){{
            //add(new Object[]{"N", 667, 530});
            add(new Object[]{"N", 595, 530, 76, 1});
            //add(new Object[]{"S", 667, 590});
            add(new Object[]{"S", 595, 590, 76, 1});
            add(new Object[]{"E", 678, 539, 1, 39});
            //add(new Object[]{"E", 678, 578});
            add(new Object[]{"W", 586, 539, 1, 39});
            //add(new Object[]{"W", 586, 578});
        }});
    }};
    //HashMap to store the light controllers
    private final HashMap<Integer, LightController> lightControllers = new HashMap<>();
    //Set final value for the number of intersections
    private final int INTERSECTIONS = 6;

    /**
     * Constructor
     */
    public SystemController() {
        spawnLights();
        intersectCollisionBoxes();
    }

    /**
     * Create the lights for each intersection
     */
    private void spawnLights() {
        for (int i = 1; i <= INTERSECTIONS; i++) {
            lightControllers.put(i, new LightController(LightController.lightType.STANDARD, lightCoords.get(i), lightCollisionCoords.get(i), pedestrianLights.get(i), pedCollisionCoords.get(i)));
        }
        //Create the bus lights, last two elements in the HashMap
        lightControllers.put(7, new LightController(LightController.lightType.BUS, lightCoords.get(7), lightCollisionCoords.get(7), pedestrianLights.get(7), pedCollisionCoords.get(7)));
        lightControllers.put(8, new LightController(LightController.lightType.BUS, lightCoords.get(8), lightCollisionCoords.get(8), pedestrianLights.get(8), pedCollisionCoords.get(8)));
    }

    /**
     * Add lights to the scene
     * @param root
     */
    public void addLights(Pane root) {
        if (root != null) {
            for (LightController lightController : lightControllers.values()) {
                lightController.addLights(root);
                lightController.startCycle();
            }
        }
    }

    private void intersectCollisionBoxes() {
        //Create a collision box based on the key of the light controller
        lightControllers.get(1).createIntersectionBox(265, 107, 60, 30);
        lightControllers.get(2).createIntersectionBox(603, 107, 60, 30);
        lightControllers.get(3).createIntersectionBox(263, 358, 60, 90);
        lightControllers.get(4).createIntersectionBox(603, 358, 60, 90);
        lightControllers.get(5).createIntersectionBox(262, 652, 60, 30);
        lightControllers.get(6).createIntersectionBox(601, 652, 60, 30);
    }

    //Check for collisions at intersections
    public void checkVehicleCrossing(List<Vehicle3D> vehicles) {
        for (LightController lightController : lightControllers.values()) {
            lightController.incrementVehicleCount(vehicles);
        }
    }

    //Check for collisions with pedestrians
    public void checkPedestrianCrossing(List<Person> pedestrians) {
        for (LightController lightController : lightControllers.values()) {
            lightController.checkPedestrianCollision(pedestrians);
        }
    }

    //Check for collisions with busses
    public void checkBusCrossing(List<Bus> busses) {
        for (LightController lightController : lightControllers.values()) {
            lightController.checkBusCollision(busses);
        }
    }
    //Get the light collision boxes
    public ArrayList<CollisionBox> getLightCollisionBoxes() {
        ArrayList<CollisionBox> collisionBoxes = new ArrayList<>();
        for (LightController lightController : lightControllers.values()) {
            collisionBoxes.addAll(lightController.getLightCollisionBoxes());
        }
        return collisionBoxes;
    }

    //Get the pedestrian collision boxes
    public ArrayList<CollisionBox> getPedestrianCollisionBoxes() {
        ArrayList<CollisionBox> collisionBoxes = new ArrayList<>();
        for (LightController lightController : lightControllers.values()) {
            collisionBoxes.addAll(lightController.getPedCollisionBoxes());
        }
        return collisionBoxes;
    }
}
