package minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Tiles {
    final int emptyTile = 0;
    final int aMine = -1;
    final int beginnerGridHeight = 10;
    final int beginnerGridWidth = 10;
    final int beginnerTotalMines = 10;
    final String boom = "BOOM";
    
    private int gridHeight = beginnerGridHeight;
    private int gridWidth = beginnerGridWidth;
    private int totalMines = beginnerTotalMines;
    private boolean gameCompleted = false;

    private ArrayList<ArrayList<Integer>> squaresArr = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer[]> xyArr = new ArrayList<Integer[]>();
    private ArrayList<Integer[]> minesArr;

    public Tiles(){
        //Default is 10x10 tile with 10 mines.
        buildTiles();
    }

    public Tiles(int height, int width, int mines){
        //Default is 10x10 tile.
        setGridHeight(height);
        setGridWidth(width);
        setTotalMines(mines);

        buildTiles();
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

    public ArrayList<ArrayList<Integer>> getSquaresArr() {
        return squaresArr;
    }

    private void buildTiles(){
        // 1. Initialise squares.
        initialiseSquareTiles();
        // 2. Plant mines.
        plantMines();
        // 3. Assign mine count on squares adjacent to the mines.
        countAdjacentMines();
    }

    private void initialiseSquareTiles(){
        for(int i = 0; i < getGridHeight(); i++)  {
            squaresArr.add(new ArrayList<Integer>(Collections.nCopies(getGridWidth(), emptyTile)));
        }
    }

    private void plantMines(){
        RandomCoordinates mines = new RandomCoordinates(getGridHeight(), getGridWidth(), getTotalMines());
        minesArr = mines.getCoordinatesArray();
        minesArr.stream().forEach(mine -> squaresArr.get(mine[0]).set(mine[1], aMine));
    }

    //A square tile adjacent to a mine must have the count of the number of mines adjacent to it.
    private void countAdjacentMines() {
        minesArr.stream().forEach(mine -> {
            ArrayList<Integer[]> coordsArr = new ArrayList<>();
            int i = mine[0];
            int j = mine[1];
            //Check rows above the current square mine.
            coordsArr.add(new Integer[]{i - 1, j - 1});
            coordsArr.add(new Integer[]{i - 1, j});
            coordsArr.add(new Integer[]{i - 1, j + 1});
            //Check rows adjacent to the current square mine.
            coordsArr.add(new Integer[]{i, j - 1});
            coordsArr.add(new Integer[]{i, j + 1});
            //Check rows below the current square mine.
            coordsArr.add(new Integer[]{i + 1, j - 1});
            coordsArr.add(new Integer[]{i + 1, j});
            coordsArr.add(new Integer[]{i + 1, j + 1});

            coordsArr.stream().forEach(xy -> {
                if(!hasAMine(xy[0], xy[1])) incrementMineCountInSquare(xy[0], xy[1]);
            });
        });
    }

    private boolean hasAMine(int heightIndex, int widthIndex){
        if(!Helper.isOutOfBounds(heightIndex, widthIndex, getGridHeight(), getGridWidth())){
            if(squaresArr.get(heightIndex).get(widthIndex) == aMine) return true;
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

    public void revealTiles(Integer[] explosionCoordinates){
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            //System.out.println("");
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

    private void showTiles(){
        boolean completed = true;
        for(int i = 0; i < squaresArr.size(); i++)  {
            printRowTopBorder();
            //System.out.println("");
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
        String value = Helper.doCoordinatesExist(xyArr, x, y) ? (squaresArr.get(x).get(y) == 0) ? " " : Integer.toString(squaresArr.get(x).get(y)) : "*";
        return value;
    }

    private void printRowTopBorder(){
        for(int i = 0; i < getGridWidth(); i++)  {
            System.out.print("------");
            if(i == getGridWidth() - 1) System.out.print("-");
        }
        System.out.println("");
    }

    private boolean validateXY(String xy) {
        //1. Size when string is split must be 2.
        String[] strArr = xy.split(",");
        if(strArr.length != 2) return false;
        //2. x and y must be numbers.
        if(!Helper.isAnInteger(strArr[0])) return false;
        if(!Helper.isAnInteger(strArr[1])) return false;
        //3. Check that coordinates are not out of bounds.
        if(Helper.isOutOfBounds(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]), getGridHeight(), getGridWidth())) return false;
        return true;
    }
   
    public void playGame(Scanner sc){
        boolean quit = false;
        boolean exploded = false;
        String header = String.format("\nMinesweeper %dx%d Square Tiles with %d Mines", getGridHeight(), getGridWidth(), getTotalMines());
        System.out.println(header);
        
        while (!quit && !exploded && !isGameCompleted()){
            showTiles();
            if(!isGameCompleted()) {
                System.out.printf("\nPlease enter the coordinates of the tile you wish to open by entering the row number (0-%d) and column number (0-%d) separated by a comma (eg. 0,2).", getGridHeight() - 1, getGridWidth() - 1);
                System.out.println("\nPlease enter 'q' if you wish to quit the game.");
                String xy = sc.nextLine();
                if (xy.equalsIgnoreCase("q")) {
                    quit = true;
                }else{
                    if(validateXY(xy)) {
                        Integer[] coordinate = Helper.convertStringToIntegerArray(xy);
                        if(hasAMine(coordinate[0], coordinate[1])){
                            exploded = true;
                            revealTiles(coordinate);
                            System.out.println(boom + "! You LOSE!");
                        } else {
                            if(isSquareTileEmpty(coordinate[0], coordinate[1])){
                                AdjacentTilesSearch search = new AdjacentTilesSearch(squaresArr, coordinate[0], coordinate[1], emptyTile, true);
                                ArrayList<Integer[]> coordinatesArr =  search.getMatchedTilesCoordinates();
                                if(coordinatesArr.size() > 0){
                                    coordinatesArr.stream().forEach(element -> {
                                        xyArr.add(Helper.convertStringToIntegerArray(String.format("%s,%s", element[0], element[1])));
                                    });
                                }
                            } else{
                                xyArr.add(coordinate);
                            }
                        }
                    }else{
                        System.out.println("The coordinates you entered are invalid.");
                    }
                }
            } else{
                revealTiles(new Integer[0]);
                System.out.println("Congratulations! You WIN!");
            }
        }

        if(quit) System.out.println("See you next time!");
    }
}
