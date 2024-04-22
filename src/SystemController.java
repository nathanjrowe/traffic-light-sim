import java.util.ArrayList;
import java.util.HashMap;

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

    //HashMap to store the light controllers
    private final HashMap<Integer, LightController> lightControllers = new HashMap<>();
    //Set final value for the number of intersections
    private final int INTERSECTIONS = 6;

    //Constructor
    public SystemController() {
        spawnLights();
    }

    //Create the lights for each intersection
    private void spawnLights() {
        for (int i = 1; i <= INTERSECTIONS; i++) {
            lightControllers.put(i, new LightController(lightCoords.get(i)));
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
}
