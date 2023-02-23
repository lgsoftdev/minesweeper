package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tile {
    final int aMine = -1;
    final int beginnerGridHeight = 10;
    final int beginnerGridWidth = 10;
    final int beginnerTotalMines = 10;
    
    private int gridHeight;
    private int gridWidth;
    private int totalMines;
    
    private String gameTitle;

    private ArrayList<ArrayList<Integer>> squaresArr = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer[]> xyArr = new ArrayList<Integer[]>();

    public Tile(){
        //Default is 10x10 tile.
        setGridHeight(beginnerGridHeight);
        setGridWidth(beginnerGridWidth);
        setTotalMines(beginnerTotalMines);
        
        setGameTitle();

        // 1. Initialise squares.
        initialiseSquareTiles();
        // 2. Plant mines.
        plantMines();
        // 3. Assign mine count on squares adjacent to the mines.
        countAdjacentMines();
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

    private void initialiseSquareTiles(){
        for(int i = 0; i < getGridHeight(); i++)  {
            squaresArr.add(new ArrayList<Integer>(Collections.nCopies(getGridWidth(), 0)));
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
        if(!isOutOfBounds(heightIndex, widthIndex)){
            if(squaresArr.get(heightIndex).get(widthIndex) == -1) return true;
        }
        return false;
    }

    private boolean isSquareTileEmpty(int heightIndex, int widthIndex){
        if(!isOutOfBounds(heightIndex, widthIndex)){
            if(squaresArr.get(heightIndex).get(widthIndex) == 0) return true;
        }
        return false;
    }

    //Increment the mine count by one in the specified cell.
    private void incrementMineCountInSquare(int heightIndex, int widthIndex){
        if(!isOutOfBounds(heightIndex, widthIndex)){
            int currentMineCount = squaresArr.get(heightIndex).get(widthIndex);
            squaresArr.get(heightIndex).set(widthIndex, currentMineCount + 1);
        }
    }

    //A COPY. DO NOT DELETE.
    public void revealTiles(){
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            System.out.println("");
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                System.out.print(String.format("|%1$5s", squaresArr.get(i).get(j)));
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
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            System.out.println("");
            for(int j = 0; j < squaresArr.get(i).size(); j++)  {
                System.out.print(String.format("|%1$5s", revealTile(i, j) ? (squaresArr.get(i).get(j) == 0) ? " ": squaresArr.get(i).get(j) : "?"));
                if(j == squaresArr.get(i).size() - 1){
                    System.out.print("|");
                }
            }    
            System.out.println("");
        }
        printRowTopBorder();
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

    private boolean isOutOfBounds(int heightIndex, int widthIndex){
        if(heightIndex >= 0 && heightIndex < getGridHeight()){
            if(widthIndex >= 0 && widthIndex < getGridWidth()){
                return false;
            }
        }
        return true;
    }

    //IN PROGRESS
    private boolean validateXY(String xy) {
        //1. Size when string is split must be 2.
        //2. x and y must be numbers.
        //3. Check that coordinates are not out of bounds.
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

    private void addEmptySquarestoXYArray(int initialX, int initialY) {
        int x = initialX;
        int y; // very first square to check is the square on the right of the initial xy
        int initialHighestY;
        int initialLowestY;
        int currentHighestY;
        int currentLowestY;
        int focalY = initialY;
        boolean stop;

            //move right
            y = focalY;
            stop = false;
            while(!stop){
                y++;
                if(y < getGridWidth()){
                    if(squaresArr.get(x).get(y) != aMine) {
                        xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                        if(squaresArr.get(x).get(y) > 0) {
                            stop = true;
                        }
                    } 
                } else {
                    stop = true;
                }
            }
            initialHighestY = y - 1;
            currentHighestY = initialHighestY;

            //move left
            y = focalY;
            stop = false;
            while(!stop){
                y--;
                if (y >= 0) {
                    if(squaresArr.get(x).get(y) != aMine) {
                        xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                        if(squaresArr.get(x).get(y) > 0) {
                            stop = true;
                        }
                    } 
                } else {
                    stop = true;
                }
            }
            initialLowestY = y + 1;
            currentLowestY = initialLowestY;


        //Check from row after initialX until last row.
        x = initialX + 1;
        focalY = -1;
        while(x < getGridHeight()){
            for(int i = currentLowestY; i <= currentHighestY; i++){
                xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, i)));
                if(focalY == -1 && isSquareTileEmpty(x, i)){                  
                    focalY = i;
                }
            }

            if(focalY > -1){
                //move right
                y = focalY;
                stop = false;
                while(!stop){
                    y++;
                    if(y < getGridWidth()){
                        if(squaresArr.get(x).get(y) != aMine) {
                            xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                            if(squaresArr.get(x).get(y) > 0) {
                                stop = true;
                            }
                        } 
                    } else {
                        stop = true;
                    }
                }
                currentHighestY = y - 1;

                //move left
                y = focalY;
                stop = false;
                while(!stop){
                    y--;
                    if (y >= 0) {
                        if(squaresArr.get(x).get(y) != aMine) {
                            xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                            if(squaresArr.get(x).get(y) > 0) {
                                stop = true;
                            }
                        } 
                    } else {
                        stop = true;
                    }
                }
                currentLowestY =  y + 1;

                x++;
            }else{
                x = getGridHeight();
            }
        }

        //Check from row before initialX until first row.
        x = initialX - 1;
        currentLowestY = initialLowestY;
        currentHighestY = initialHighestY;
        focalY = -1;
        while(x >= 0){
            for(int i = currentLowestY; i <= currentHighestY; i++){
                xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, i)));
                if(focalY == -1 && isSquareTileEmpty(x, i)){
                    focalY = i;
                }
            }

            if (focalY > -1){
                //move right
                y = focalY;
                stop = false;
                while(!stop){
                    y++;
                    if(y < getGridWidth()){
                        if(squaresArr.get(x).get(y) != aMine) {
                            xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                            if(squaresArr.get(x).get(y) > 0) {
                                stop = true;
                            }
                        } 
                    } else {
                        stop = true;
                    }
                }
                currentHighestY = y - 1;

                //move left
                y = focalY;
                stop = false;
                while(!stop){
                    y--;
                    if (y >= 0) {
                        if(squaresArr.get(x).get(y) != aMine) {
                            xyArr.add(convertStringToIntegerArray(String.format("%s,%s", x, y)));
                            if(squaresArr.get(x).get(y) > 0) {
                                stop = true;
                            }
                        } 
                    } else {
                        stop = true;
                    }
                }
                currentLowestY =  y + 1;

                x--;
            }else{
                x = -1;
            }
        }
    }

    public void playGame(){
        boolean quit = false;
        boolean exploded = false;
        boolean completed = false;
        System.out.println(getGameTitle());
        
        Scanner sc = new Scanner(System.in);
        while (!quit && !exploded && !completed){
            revealTiles();
            displayTiles();
            System.out.printf("\nPlease enter the coordinates of the tile you wish to open by entering the row number (0-9) and column number (0-9) separated by a comma (eg. 0,2).");
            System.out.println("\nPlease enter 'q' if you wish to quit the game.");
            String xy = sc.nextLine();
            if (xy.equals("q")) {
                quit = true;
            }else{
                if(validateXY(xy)) {
                    Integer[] coordinate = convertStringToIntegerArray(xy);
                    if(hasAMine(coordinate[0], coordinate[1])){
                        exploded = true;
                    } else {
                        xyArr.add(convertStringToIntegerArray(xy));
                        if(isSquareTileEmpty(coordinate[0], coordinate[1])){
                            addEmptySquarestoXYArray(coordinate[0], coordinate[1]);
                        }
                    }
                }else{
                    System.out.println("The coordinates you entered were invalid.");
                }
            }
        }
        sc.close();
        if(quit) System.out.println("See you next time!");
        if(exploded) System.out.println("BOOM!");
    }
}
