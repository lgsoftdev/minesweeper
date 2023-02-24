package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tiles {
    final int emptyTile = 0;
    final int aMine = -1;
    final int beginnerGridHeight = 5;
    final int beginnerGridWidth = 5;
    final int beginnerTotalMines = 2;
    final String boom = "BOOM";
    
    private int gridHeight;
    private int gridWidth;
    private int totalMines;

    private ArrayList<ArrayList<Integer>> squaresArr = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer[]> xyArr = new ArrayList<Integer[]>();

    private boolean gameCompleted;

    public Tiles(){
        //Default is 10x10 tile.
        setGridHeight(beginnerGridHeight);
        setGridWidth(beginnerGridWidth);
        setTotalMines(beginnerTotalMines);
        setGameCompleted(false);
        
        // 1. Initialise squares.
        initialiseSquareTiles();
        // 2. Plant mines.
        plantMines();
        // 3. Assign mine count on squares adjacent to the mines.
        countAdjacentMines();
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
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

    private void initialiseSquareTiles(){
        for(int i = 0; i < getGridHeight(); i++)  {
            squaresArr.add(new ArrayList<Integer>(Collections.nCopies(getGridWidth(), emptyTile)));
        }
    }

    private void plantMines(){
        Random rand = new Random();
        for(int i = 0; i < getTotalMines(); i++){
            boolean placed = false;
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

    //A square tile adjacent to a mine must have the count of the number of mines adjacent to it.
    private void countAdjacentMines() {
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
        if(!Helper.isOutOfBounds(heightIndex, widthIndex, getGridHeight(), getGridWidth())){
            if(squaresArr.get(heightIndex).get(widthIndex) == -1) return true;
        }
        return false;
    }

    private boolean isSquareTileEmpty(int heightIndex, int widthIndex){
        if(!Helper.isOutOfBounds(heightIndex, widthIndex, getGridHeight(), getGridWidth())){
            if(squaresArr.get(heightIndex).get(widthIndex) == emptyTile) return true;
        }
        return false;
    }

    //Increment the mine count by one in the specified cell.
    private void incrementMineCountInSquare(int heightIndex, int widthIndex){
        if(!Helper.isOutOfBounds(heightIndex, widthIndex, getGridHeight(), getGridWidth())){
            int currentMineCount = squaresArr.get(heightIndex).get(widthIndex);
            squaresArr.get(heightIndex).set(widthIndex, currentMineCount + 1);
        }
    }

    //Look into refactoring revealSquareTiles and displayTiles
    public void revealTiles(Integer[] explosionCoordinates){
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            System.out.println("");
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                String content = squaresArr.get(i).get(j) > 0 ? Integer.toString(squaresArr.get(i).get(j)) :  squaresArr.get(i).get(j) == -1 ? "[@]" : " ";
                if(explosionCoordinates.length > 0){
                    if(explosionCoordinates[0].equals(i) && explosionCoordinates[1].equals(j)) content = boom;
                }
                System.out.print(String.format("|%1$5s", content));
                if(j == squaresArr.get(i).size() - 1){
                    System.out.print("|");
                }
            }    
            System.out.println("");
        }
        printRowTopBorder();
    }

    // IN PROGRESS
    private void displayTiles(){
        boolean completed = true;
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            System.out.println("");
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                String content = getTileContent(i, j);
                if (content.equals("*") && !hasAMine(i, j)) completed = false;
                System.out.print(String.format("|%1$5s", content));
                if(j == squaresArr.get(i).size() - 1){
                    System.out.print("|");
                }
            }    
            System.out.println("");
        }
        printRowTopBorder();

        setGameCompleted(completed);
    }

    private String getTileContent(int x, int y){
        String value = revealTile(x, y) ? (squaresArr.get(x).get(y) == 0) ? " " : Integer.toString(squaresArr.get(x).get(y)) : "*";
        return value;
    }

    private boolean revealTile(int rowIndex, int columnIndex){
        Integer[] arrToCompare = { rowIndex, columnIndex };
        List<Integer[]> filtered = new ArrayList<Integer[]>();
        if(xyArr.size() > 0) filtered = xyArr.stream().filter(xy -> Arrays.equals(arrToCompare, xy)).collect(Collectors.toList());
        if (filtered.size() > 0) return true;
        return false;
    }

    private void printRowTopBorder(){
        for(int i = 0; i < getGridWidth(); i++)  {
            System.out.print("------");
            if(i == getGridWidth() - 1) System.out.print("-");
        }
    }

    //IN PROGRESS
    private boolean validateXY(String xy) {
        //1. Size when string is split must be 2.
        String[] strArr = xy.split(",");
        if(strArr.length != 2) return false;
        //2. x and y must be numbers.
        if(!Helper.isAnInteger(strArr[0])) return false;
        if(!Helper.isAnInteger(strArr[1])) return false;
        //3. Check that coordinates are not out of bounds.
        if(Helper.isOutOfBounds(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[0]), getGridHeight(), getGridWidth())) return false;
        return true;
    }

    private Integer[] convertStringToIntegerArray(String xy) {
        String[] strArr = xy.split(",");
        Integer[] intArr = new Integer[strArr.length];
        for(int i = 0; i < intArr.length; i++){
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        return intArr;
    }    

    public void playGame(){
        boolean quit = false;
        boolean exploded = false;
        String header = String.format("\nMinesweeper %dx%d Square Tiles with %d Mines", getGridHeight(), getGridWidth(), getTotalMines());
        System.out.println(header);
        
        Scanner sc = new Scanner(System.in);
        while (!quit && !exploded && !isGameCompleted()){
            revealTiles(new Integer[0]);
            displayTiles();
            if(!isGameCompleted()) {
                System.out.printf("\nPlease enter the coordinates of the tile you wish to open by entering the row number (0-9) and column number (0-9) separated by a comma (eg. 0,2).");
                System.out.println("\nPlease enter 'q' if you wish to quit the game.");
                String xy = sc.nextLine();
                if (xy.equalsIgnoreCase("q")) {
                    quit = true;
                }else{
                    if(validateXY(xy)) {
                        Integer[] coordinate = convertStringToIntegerArray(xy);
                        if(hasAMine(coordinate[0], coordinate[1])){
                            exploded = true;
                            revealTiles(coordinate);
                            System.out.println("\n" + boom + "! You LOSE!");
                        } else {
                            if(isSquareTileEmpty(coordinate[0], coordinate[1])){
                                AdjacentTilesSearch search = new AdjacentTilesSearch(squaresArr, coordinate[0], coordinate[1], 0, true);
                                ArrayList<Integer[]> coordinatesArr =  search.getMatchedTilesCoordinates();
                                if(coordinatesArr.size() > 0){
                                    coordinatesArr.stream().forEach(element -> {
                                        xyArr.add(convertStringToIntegerArray(String.format("%s,%s", element[0], element[1])));
                                    });
                                }
                            } else{
                                xyArr.add(coordinate);
                            }
                        }
                    }else{
                        System.out.println("The coordinates you entered were invalid.");
                    }
                }
            } else{
                revealTiles(new Integer[0]);
                System.out.println("\nCongratulations! You WIN!");
            }
        }
        sc.close();
        if(quit) System.out.println("\nSee you next time!");
    }
}
