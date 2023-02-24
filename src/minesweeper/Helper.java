package minesweeper;

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
}
