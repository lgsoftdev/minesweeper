package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Helper {
    public static boolean isOutOfBounds(int heightIndex, int widthIndex, int arrayHeight, int arrayWidth){
        if(heightIndex >= 0 && heightIndex < arrayHeight){
            if(widthIndex >= 0 && widthIndex < arrayWidth){
                return false;
            }
        }
        return true;
    }

    public static boolean isAnInteger(String value){
        try{
            int anInt = Integer.parseInt(value);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static Integer[] convertStringToIntegerArray(String xy) {
        String[] strArr = xy.split(",");
        Integer[] intArr = new Integer[strArr.length];
        for(int i = 0; i < intArr.length; i++){
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        return intArr;
    } 

    public static int getIntegerInput(Scanner sc, String fieldName, int max){
        System.out.println("Please enter the " + fieldName + " (max " + max + ").");
        String input = sc.nextLine();
        if(!Helper.isAnInteger(input) || Integer.parseInt(input) > max){
            System.out.println("Invalid " + fieldName + ".");
            return -1;
        }
        return Integer.parseInt(input);
    }

    public static boolean doCoordinatesExist(ArrayList<Integer[]> coordinatesArray, int x, int y){
        Integer[] arrToCompare = { x, y };
        ArrayList<Integer[]> filtered = new ArrayList<Integer[]>();
        if(coordinatesArray.size() > 0) filtered = coordinatesArray.stream().filter(xy -> Arrays.equals(arrToCompare, xy)).collect(Collectors.toCollection(ArrayList::new));
        if (filtered.size() > 0) return true;
        return false;
    }
}
