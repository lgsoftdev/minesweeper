package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) { 
        System.out.println("Welcome to Minesweeper!");
        Scanner mainSC = new Scanner(System.in);
        int userAction = 0;
        boolean inputComplete = false;
        while(!inputComplete) {
            showUserSelection();
            String strSelected = mainSC.nextLine();
            if(Helper.isAnInteger(strSelected) ){
                Integer intSelected = Integer.parseInt(strSelected);
                switch(intSelected){
                    case 1:
                    case 2:
                    case 9:
                        userAction = intSelected;
                        inputComplete = true;
                        //Go to while
                        continue;
                    default:
                        //Run rest of the code to warn input is invalid.
                }
            }         
            System.out.println("The selection you entered is invalid.");
        }

        if(userAction == 1) {
            Tiles minesweeper = new Tiles();
            minesweeper.playGame(mainSC);
        } else if(userAction == 2){
            Integer[] dim = customiseTiles(mainSC);
            Tiles minesweeper = new Tiles(dim[0], dim[1], dim[2]);
            minesweeper.playGame(mainSC);
        } else {
            System.out.println("Goodbye!");
        }
        
        mainSC.close();
    }

    private static void showUserSelection(){ 
        System.out.println("Please select from the following:");
        System.out.println("1 -> Enter 1 to play 10x10 Tiles with 10 mines.");
        System.out.println("2 -> Enter 2 to customise Tile height, width and number of mines.");
        System.out.println("9 -> Enter 9 to exit.");
    }

    private static Integer[] customiseTiles(Scanner sc){
        final int maxDim = 30;
        boolean inputComplete = false;
        int intHeight = -1;
        int intWidth = -1;
        int intMines = -1;
        while(!inputComplete) {  
            if(intHeight == -1){
                intHeight = Helper.getIntegerInput(sc, "height", maxDim);
                if(intHeight == -1) continue;
            }
            if(intWidth == -1){
                intWidth = Helper.getIntegerInput(sc, "width", maxDim);
                if(intWidth == -1) continue;
            }
            int maxMines = (int) ((intWidth * intHeight) * .20);
            intMines = Helper.getIntegerInput(sc, "number of mines", maxMines);
            if(intMines == -1) continue;
            inputComplete = true;
        }
        return new Integer[]{intHeight, intWidth, intMines};
    }
}
