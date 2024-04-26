import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Pane;

//Controller to communicate to individual light controllers
//TODO: Receive and send data to the light controllers
public class SystemController {
    
    //Store the coordinates of the lights for each intersection
    //Pass to each light controller the coordinate of the lights
    //Object[] = {String direction, int x, int y}
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
    }};

    //HashMap to store the collision box coordinates for each intersection
    //Object[] = {String associated light, double xCenter, double yCenter}
    private final HashMap<Integer, ArrayList<Object[]>> collisionCoords = new HashMap<>(){{
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

    }};
    //HashMap to store the light controllers
    private final HashMap<Integer, LightController> lightControllers = new HashMap<>();
    //Set final value for the number of intersections
    private final int INTERSECTIONS = 6;

    //Constructor
    public SystemController() {
        spawnLights();
        intersectCollisionBoxes();
    }

    //Create the lights for each intersection
    private void spawnLights() {
        for (int i = 1; i <= INTERSECTIONS; i++) {
            lightControllers.put(i, new LightController(lightCoords.get(i), collisionCoords.get(i)));
        }
    }

    //Add lights to the scene
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
    public void checkVehicleCrossing(List<Vehicle> vehicles) {
        for (LightController lightController : lightControllers.values()) {
            lightController.incrementVehicleCount(vehicles);
        }
    }

    //Get the light collision boxes
    public ArrayList<CollisionBox> getCollisionBoxes() {
        ArrayList<CollisionBox> collisionBoxes = new ArrayList<>();
        for (LightController lightController : lightControllers.values()) {
            collisionBoxes.addAll(lightController.getCollisionBoxes());
        }
        return collisionBoxes;
    }
}
