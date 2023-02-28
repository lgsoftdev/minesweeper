package minesweeper;

import java.util.ArrayList;
import java.util.Random;

public class RandomCoordinates {
    private ArrayList<Integer[]> coordinatesArray = new ArrayList<>();

    public RandomCoordinates(int gridHeight, int gridWidth, int numberOfXY){
        setCoordinatesArray(gridHeight, gridWidth, numberOfXY);
    }

    private void setCoordinatesArray(int gridHeight, int gridWidth, int numberOfXY) {
        Random rand = new Random();
        for(int i = 0; i < numberOfXY; i++){
            boolean placed = false;
            while(!placed){
                int heightIndex = rand.nextInt(gridHeight);
                int widthIndex = rand.nextInt(gridWidth);
                if(!Helper.doCoordinatesExist(coordinatesArray, heightIndex, widthIndex)){
                    coordinatesArray.add(new Integer[]{ heightIndex, widthIndex });
                    placed = true;
                }
            }
        }
    }

    public ArrayList<Integer[]> getCoordinatesArray() {
        return coordinatesArray;
    }

}
