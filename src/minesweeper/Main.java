package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) { 
        System.out.println("Welcome to Minesweeper!");
        Scanner mainSC = new Scanner(System.in);
        int userAction = 0;
        boolean inputComplete = false;
        while(!inputComplete) {
            displayUserSelection();
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
                        //Run rest of the code entered is invalid warning.
                }
            }         
            System.out.println("The selection you entered is invalid.");
        }

        if(userAction == 1) {
            Tiles minesweeper = new Tiles();
            minesweeper.playGame();
        } else if(userAction == 2){
            Integer[] dim = customiseTiles(mainSC);
            Tiles minesweeper = new Tiles(dim[0], dim[1], dim[2]);
            minesweeper.playGame();
        } else {
            System.out.println("Goodbye!");
        }
        
        mainSC.close();
    }

    private static void displayUserSelection(){ 
        System.out.println("Please select from the following:");
        System.out.println("1 -> Enter 1 to play 10x10 Tiles with 10 mines.");
        System.out.println("2 -> Enter 2 to customise Tile height, width and number of mines.");
        System.out.println("9 -> Enter 9 to exit.");
    }

    private static Integer[] customiseTiles(Scanner sc){
        boolean inputComplete = false;
        int intWidth = 0;
        int intHeight = 0;
        int intMines = 0;
        while(!inputComplete) {  
            if(intHeight == 0){
                System.out.println("Please enter the height (max 30).");
                String height = sc.nextLine();
                if(!Helper.isAnInteger(height) || Integer.parseInt(height) > 30){
                    System.out.println("The height you entered is invalid.");
                    continue;
                }
                intHeight = Integer.parseInt(height);
            }
            if(intWidth == 0){
                System.out.println("Please enter the width (max 30).");
                String width = sc.nextLine();
                if(!Helper.isAnInteger(width) || Integer.parseInt(width) > 30){
                    System.out.println("The width you entered is invalid.");
                    continue;
                }
                intWidth = Integer.parseInt(width);
            }
            int maxMines = (int) ((intWidth * intHeight) * .20);
            System.out.printf("Please enter the number of mines (max %d).\n", maxMines);
            String mines = sc.nextLine();
            if(!Helper.isAnInteger(mines) || Integer.parseInt(mines) > maxMines){
                System.out.println("The number of mines you entered is invalid.");
                continue;
            }
            intMines = Integer.parseInt(mines);
            inputComplete = true;
        }
        return new Integer[]{intHeight, intWidth, intMines};
    }
}
