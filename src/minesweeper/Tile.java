package minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//import java.util.Scanner;

public class Tile {
    final int aMine = -1;
    final int beginnerGridHeight = 10;
    final int beginnerGridWidth = 10;
    final int beginnerTotalMines = 10;
    private ArrayList<ArrayList<Integer>> squaresArr = new ArrayList<ArrayList<Integer>>();
    private int gridHeight;
    private int gridWidth;
    private int totalMines;

    private String gameTitle;

    public Tile(){
        //Default is 10x10 tile.
        setGridHeight(beginnerGridHeight);
        setGridWidth(beginnerGridWidth);
        setTotalMines(beginnerTotalMines);
        
        setGameTitle();

        // 1. Initialise squares.
        initialiseTileSquares();
        // 2. Plant mines.
        plantMines();
        // 3. Assign mine count on squares surrounding the mines.
        countSurroundingMines();
    }

    private int getGridHeight() {
        return gridHeight;
    }

    private void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    private int getGridWidth() {
        return gridWidth;
    }

    private void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }
 
    private int getTotalMines() {
        return totalMines;
    }

    private void setTotalMines(int totalMines) {
        this.totalMines = totalMines;
    }

    private String getGameTitle() {
        return gameTitle;
    }

    private void setGameTitle() {
        this.gameTitle = String.format("\nMinesweeper %dx%d Square Tiles with %d Mines", getGridHeight(), getGridWidth(), getTotalMines());
    }

    private void initialiseTileSquares(){
        for(int i = 0; i < getGridHeight(); i++)  {
            squaresArr.add(new ArrayList<Integer>(Collections.nCopies(getGridWidth(), 0)));
        }
    }

    private void plantMines(){
        Random rand = new Random();
        for(int i = 0; i < getTotalMines(); i++){
            Boolean placed = false;
            while(!placed){
                int heightIndex = rand.nextInt(getGridHeight());
                int widthIndex = rand.nextInt(getGridWidth());
                if(squaresArr.get(heightIndex).get(widthIndex) == 0){
                    squaresArr.get(heightIndex).set(widthIndex, aMine);
                    placed = true;
                }
            }
        }
    }

    private void countSurroundingMines() {
        for(int i = 0; i < squaresArr.size(); i++){
            for(int j = 0; j < squaresArr.get(i).size(); j++){
                //find the mines
                if(hasAMine(i, j)){
                    int rowAbove = i - 1;
                    int rowBelow = i + 1;
                    int columnLeft = j - 1;
                    int columnRight = j + 1;

                    //Check rows above the current square mine.
                    if(!hasAMine(rowAbove, columnLeft)) incrementMineCountInSquare(rowAbove, columnLeft);
                    if(!hasAMine(rowAbove, j)) incrementMineCountInSquare(rowAbove, j);
                    if(!hasAMine(rowAbove, columnRight)) incrementMineCountInSquare(rowAbove, columnRight);
                    //Check rows adjacent to the current square mine.
                    if(!hasAMine(i, columnLeft)) incrementMineCountInSquare(i, columnLeft);
                    if(!hasAMine(i, columnRight)) incrementMineCountInSquare(i, columnRight);
                    //Check rows below the current square mine.
                    if(!hasAMine(rowBelow, columnLeft)) incrementMineCountInSquare(rowBelow, columnLeft);
                    if(!hasAMine(rowBelow, j)) incrementMineCountInSquare(rowBelow, j);
                    if(!hasAMine(rowBelow, columnRight)) incrementMineCountInSquare(rowBelow, columnRight);
                }
                
            }
        }
    }

    private boolean hasAMine(int heightIndex, int widthIndex){
        try{
            if(squaresArr.get(heightIndex).get(widthIndex) == -1) return true;
        } catch(IndexOutOfBoundsException e){
            //ignore error
        }
        return false;
    }

    //Increment the mine count by one in the specified cell.
    private void incrementMineCountInSquare(int heightIndex, int widthIndex){
        try{
            int currentMineCount = squaresArr.get(heightIndex).get(widthIndex);
            squaresArr.get(heightIndex).set(widthIndex, currentMineCount + 1);
        } catch(IndexOutOfBoundsException e){
            //ignore error
        }
        
    }

    public void playGame(){
        System.out.println(getGameTitle());
        displayTiles();
        // Scanner sc = new Scanner(System.in);
        // System.out.printf("\nPlease select the tile you wish to open by entering the row number (1-10) and column number (1-10) separated by a comma.");
        // System.out.println("\nFor example, if you wish to open the tile in row 2, column 5, you would enter 2,5.");
        // String str = sc.nextLine();
        // System.out.println("you typed" +str);
        // sc.close();
    }

    // IN PROGRESS
    public void displayTiles(){
        for(int i = 0; i < squaresArr.size(); i++)  {
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                System.out.print("______");
            }
            System.out.println("");
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                if(j > 0 && j < squaresArr.get(i).size()) {
                    System.out.print(" | ");
                }
                System.out.print(String.format("%1$3s", squaresArr.get(i).get(j)));
            }
            System.out.println("");
        }
    }
}
