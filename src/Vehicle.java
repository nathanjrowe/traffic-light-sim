import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class Vehicle {
    private static final double[][] INITIALPATHS = {
            /**Left Side Starting*/
            //Straight/Right First Lane, Right Second Lane, Left First Lane, Left Second Lane
            {13,127,269,127}, {13,127,289,127},{13,127,305,127},{13,127,324,127},
            //Left first lane, Left Second Lane, Straight
            {13,412,305,412}, {13,412,324,412}, {13,412,269,412},
            //Straight
            {13,428,269,428},
            //Straight/Right first lane, Right Second Lane
            {13,445,269,445}, {13,445,289,445},
            //Left first Lane, Left Second Lane, Straight/Right first lane, Right Second Lane
            {13,677,305,677},{13,677,324,677},{13,677,269,677}, {13,677,289,677},
            /**Bottom Side Starting**/
            //Left Turn, Straight Left Lane, Straight Right Lane/Right Turn
            {305,745,305,659},{305,745,305,677}, {324,745,324,677},
            //Left Turn, Straight Left Lane, Straight Right Lane/Right Turn
            {643,745,643,659},{643,745,643,677}, {663,745,663,677},
            /**Right Side Starting*/
            //Straight/Right First Lane, Right Second Lane, Left First Lane, Left Second Lane
            {1185,659,663,659},{1185,659,643,659},{1185,659,626,659},{1185,659,608,659},
            //Straight/Right first lane, Right second lane
            {1185,357,663,357},{1185,357,643,357},
            //Straight
            {1185,374,663,374},
            //Straight, Left first Lane, Left Second Lane
            {1185,394,663,394},{1185,394,626,394},{1185,394,608,394},
            //Straight/Right first lane, Right Second Lane, Left first lane, Left Second lane
            {1185,108,663,108},{1185,108,643,108},{1185,108,626,108},{1185,108,608,108},
            /**Top Side Starting*/
            //Straight Left Lane, Left Turn, Straight Right Lane/Right Turn
            {626,10,626,108},{626,10,626,127},{608,10,608,108},
            //Straight
            {466,10,466,127},
            //Straight
            //Potentially wrong
            {412,10,412,108},
            //Straight Left Lane, Left Turn, Straight/Right Turn
            {289,10,289,108},{289,10,289,127},{269,10,269,108}
    };

    private static final double[][] RESTOFPATHS = {
            {269,127,428,127},{428,127,428,10},{305,127,305,10},{324,127,324,10},{269,127,269,357},{289,127,289,357},
            {269,357,269,659},{289,357,289,659},{269,659,269,745},{289,659,289,745},{269,357,13,357},{269,108,269,374},
            {269,108,269,394},{289,108,289,412},{289,108,289,428},{289,108,289,445},{269,374,13,374},{269,394,13,394},
            {269,659,13,659},{269,108,13,108},{289,412,608,412},{289,428,608,428},{289,445,608,445},{289,357,289,677},
            {289,412,643,412},{289,412,663,412},{289,445,626,445},{608,445,608,659},{626,445,626,659},{626,445,626,677},
            {626,659,466,659},{466,659,466,745},{626,677,1185,677},{626,659,626,745},{608,659,608,745},{608,428,1185,428},
            {608,412,1185,412},{608,445,1185,445},{643,412,643,127},{643,412,643,108},{643,108,484,108},{484,108,484,10},
            {643,127,643,10},{663,412,663,127},{663,127,1185,127},{663,127,663,10},{269,412,608,412},{269,428,608,428},
            {269,445,608,445},{324,108,13,108},{663,108,484,108},{663,357,324,357},{663,374,324,374},{663,394,324,394},
            {324,357,13,357},{324,374,13,374},{324,394,13,394}, {663,357,305,357},{305,357,305,127},{324,357,324,127},
            {324,127,428,127},{466,127,608,127},{466,127,626,127},{466,127,643,127},{466,127,663,127},{608,127,608,357},
            {608,127,608,374},{608,127,608,394},{608,357,324,357},{608,357,305,357},{608,374,324,374},{608,394,324,394},
            {608,394,289,394},{608,394,269,394},{269,394,269,659},{289,394,289,659},{289,394,289,677},{289,677,289,745},
            {269,677,269,745},{608,108,484,108},{608,108,608,357},{608,108,608,374},{608,108,608,394},{626,108,626,357},
            {626,108,626,412},{626,108,626,428},{626,108,626,445},{626,357,626,659},{626,357,626,677},{663,677,1185,677},
            {663,677,663,445},{663,677,663,428},{663,677,663,412},{663,445,1185,445},{663,428,1185,428},{663,412,1185,412},
            {663,445,663,108},{643,677,643,445},{643,659,466,659},{643,445,643,127},{643,445,643,108},{643,677,643,394},
            {643,677,643,374},{643,677,643,357},{643,394,324,394},{643,394,289,394},{643,394,269,394},{643,374,324,374},
            {643,357,324,357},{643,357,305,357},{324,677,324,445},{324,445,324,127},{324,445,608,445},{324,445,626,445},
            {324,677,324,428},{324,428,608,428},{324,677,324,412},{324,412,608,412},{324,412,643,412},{324,412,663,412},
            {305,659,13,659},{305,677,305,445},{305,677,305,394},{305,677,305,374},{305,677,305,357},{305,445,305,127},
            {305,445,305,108},{305,394,13,394},{305,374,13,374},{305,357,13,357},{663,659,663,445},{663,659,663,428},
            {663,659,663,412},{663,659,466,659},{626,394,626,659},{626,394,626,677},{305,412,305,127},{305,412,305,108},
            {626,428,1185,428},{412,108,324,108},{412,108,289,108},{412,108,269,108},{626,127,1185,127},{305,108,13,108},
            {626,412,1185,412}
    };
    private List<double[]> allPossiblePaths = new ArrayList<>(); //Does not include Starting Paths
    private int allPathSize;
    private List<double[]> startingPaths = new ArrayList<>();

    private double distance;
    private double seconds;
    private List<double[]> temp;
    private Path path;
    private Boolean collided;

    protected Vehicle(){
        for (double[] array : INITIALPATHS){
            startingPaths.add(array);
        }
        for (double[] array : RESTOFPATHS){
            allPossiblePaths.add(array);
        }
        allPathSize = allPossiblePaths.size();
        createPath();
        collided = false;
    }

    protected Shape carShape() {
        Shape shape = new Rectangle(8, 15);
        return shape;
    }

    protected void createPath(){
        temp = generateRandomPath(allPossiblePaths, startingPaths);
        path = new Path();
        path.getElements().add(new MoveTo(temp.get(0)[0], temp.get(0)[1]));
        double startX = temp.get(0)[0];
        double startY = temp.get(0)[1];
        for (int i = 0; i < temp.size(); i++) {
            double[] point = temp.get(i);
            path.getElements().add(new LineTo(point[2], point[3]));
            double endX = point[2];
            double endY = point[3];
            distance += abs((endX - startX)) + abs((endY - startY));
            startX = point[2];
            startY = point[3];
        }
        //This is where you edit the Speed
        seconds = distance / 200;
        path.setOpacity(0);
    }

    protected Path returnPath(){
        return path;
    }

    protected double returnSeconds(){
        return seconds;
    }

    protected List<double[]> returnPathArray(){
        return temp;
    }

    private boolean pathConnects(double[] path1, double[] path2) {
        //Path1 endX == Path2 startX && Path1 endY == Path2 startY
        return path1[2] == path2[0] && path1[3] == path2[1];
    }

    private List<double[]> generateRandomPath(List<double[]> allPathsList, List<double[]> startingPathList) {
        Random random = new Random();

        List<double[]> path = new ArrayList<>();
        List<double[]> availablePaths = new ArrayList<>(allPathsList);
        List<double[]> startingTemp = new ArrayList<>(startingPathList);

        double[] currentPath = startingTemp.remove(random.nextInt(startingTemp.size()));
        path.add(currentPath);

        boolean pathFinished = false;
        while (!pathFinished) {
            pathFinished = true;
            List<double[]> potentialPaths = new ArrayList<>();
            for (double[] nextPath : availablePaths) {
                if (pathConnects(currentPath, nextPath)) {
                    potentialPaths.add(nextPath);
                    pathFinished = false;
                }
            }
            if (!pathFinished) {
                double[] chosenPath = potentialPaths.get(random.nextInt(potentialPaths.size())).clone();
                path.add(chosenPath);
                availablePaths.remove(chosenPath);
                currentPath = chosenPath;
            }
        }
        return path;
    }

    protected void setCollided(boolean bool){
        collided = bool;
    }

    protected boolean returnCollided(){
        return collided;
    }
}
