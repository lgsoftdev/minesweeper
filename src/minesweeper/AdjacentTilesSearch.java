package minesweeper;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AdjacentTilesSearch {
    private ArrayList<Integer[]> coordinatesArray;
    private ArrayList<ArrayList<Integer>> tilesArray;
    private int initialX;
    private int initialY;
    private int toFind;
    private boolean includeBorderTiles;

    //Constructor
    public AdjacentTilesSearch(ArrayList<ArrayList<Integer>> tilesArray, int initialX, int initialY, int toFind, boolean includeBorderTiles){
        this.coordinatesArray = new ArrayList<Integer[]>();
        setTilesArray(tilesArray);
        setInitialX(initialX);
        setInitialY(initialY);
        setToFind(toFind);
        setIncludeBorderTiles(includeBorderTiles);
    }
    
    private void setTilesArray(ArrayList<ArrayList<Integer>> tilesArray) {
        this.tilesArray = tilesArray;
    }

    private int getInitialX() {
        return initialX;
    }

    private void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    private int getInitialY() {
        return initialY;
    }

    private void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    private int getToFind() {
        return toFind;
    }

    private void setToFind(int toFind) {
        this.toFind = toFind;
    }

    private boolean isIncludeBorderTiles() {
        return includeBorderTiles;
    }

    private void setIncludeBorderTiles(boolean includeBorderTiles) {
        this.includeBorderTiles = includeBorderTiles;
    }

    private boolean isAMatch(int x, int y){
        if(tilesArray.get(x).get(y) == getToFind()) return true;
        return false;
    }

    private void addCoordinates(int x, int y){
        coordinatesArray.add(new Integer[]{x,y});
    }

    public ArrayList<Integer[]> getMatchedTilesCoordinates(){
        //1. Check tiles on initialX row.
        findMatchedTilesOnRow(getInitialX(), getInitialY());
        //2. Traverse up rows from initialX
        //3. Traverse down rows from highest traversed row.
        traverseUpRowsFromCoordinateX(getInitialX(), true);
        //4. Traverse down rows from initialX
        //5. Traverse up rows from lowest traversed row.
        traverseDownRowsFromCoordinateX(getInitialX(), true);
        if(isIncludeBorderTiles()) addBorderTilesCoordinates();

        return coordinatesArray;
    }

    private void findMatchedTilesOnRow(int currentX, int currentY){
        //move right
        int x = currentX;
        int y = currentY;
        boolean stop = false;
        while(!stop){ 
            if(y < tilesArray.get(x).size()){
                if(isAMatch(x, y)) addCoordinates(x, y);
                else stop = true;
                y++;
            } else {
                stop = true;
            }
        }

        //move left
        y = currentY - 1;
        stop = false;
        while(!stop){               
            if (y >= 0) {
                if(isAMatch(x, y)) addCoordinates(x, y);
                else stop = true;
                y--;
            } else {
                stop = true;
            }
        }
    }

    private void traverseUpRowsFromCoordinateX(int fromCoordinateX, boolean traverseDown){
        //Check from row after initialX until last row with matched cells.
        int highestX = fromCoordinateX;
        int x = fromCoordinateX + 1; 
        while(x < tilesArray.size()){
            int previousX = x - 1;
            ArrayList<Integer[]> coordsArr = coordinatesArray.stream().filter(item -> item[0] == previousX).collect(Collectors.toCollection(ArrayList::new));   
            ArrayList<Integer> yIndicesArr = new ArrayList<>();    
            for(int i = 0; i < coordsArr.size(); i++){
                //store y coordinate of current matched cell which aligns with previous row's matched cells.
                if(isAMatch(x, coordsArr.get(i)[1])) {
                    yIndicesArr.add(coordsArr.get(i)[1]);
                    highestX = x;
                }
            }

            //move right and left 
            //Duplicates may occur unless empty cells have non-empty cells in between. Duplicates won't cause any error.
            int size = yIndicesArr.size(); 
            if(size > 0) {
                for(int i = 0; i < size; i++){
                    findMatchedTilesOnRow(x, yIndicesArr.get(i));
                }
                x++;
            } else{
                x = tilesArray.size(); //so as to fail the while condition;
            }
        }

        if (traverseDown) traverseDownRowsFromCoordinateX(highestX, false);
    }

    private void traverseDownRowsFromCoordinateX(int fromCoordinateX, boolean traverseUp){
        //Check from row after initialX until last row with matched cells.
        int lowestX = fromCoordinateX;
        int x = fromCoordinateX - 1; 
        while(x >= 0){
            int previousX = x + 1;
            ArrayList<Integer[]> coordsArr = coordinatesArray.stream().filter(item -> item[0] == previousX).collect(Collectors.toCollection(ArrayList::new));   
            ArrayList<Integer> yIndicesArr = new ArrayList<>();    
            for(int i = 0; i < coordsArr.size(); i++){
                //store y coordinate of current matched cell which aligns with previous row's matched cells.
                if(isAMatch(x, coordsArr.get(i)[1])) {
                    yIndicesArr.add(coordsArr.get(i)[1]);
                    lowestX = x;
                }
            }

            //move right and left 
            //Duplicates may occur unless empty cells have non-empty cells in between. Duplicates won't cause any error.
            int size = yIndicesArr.size(); 
            if(size > 0) {
                for(int i = 0; i < size; i++){
                    findMatchedTilesOnRow(x, yIndicesArr.get(i));
                }
                x--;
            } else{
                x = -1; //so as to fail the while condition;
            }
        }

        if (traverseUp) traverseUpRowsFromCoordinateX(lowestX, false);
    }

    private void addBorderTilesCoordinates(){
        int size = coordinatesArray.size();
        if(size > 0){
            for(int i = 0; i < size; i++){
                int x = coordinatesArray.get(i)[0];
                int y = coordinatesArray.get(i)[1];
                //check tile on top
                int newX = x - 1;
                int newY = y;
                addCoordinatesIfNotMatched(newX, newY);
                //check tile at bottom
                newX = x + 1;
                addCoordinatesIfNotMatched(newX, newY);
                //check tile on right
                newX = x;
                newY = y + 1;
                addCoordinatesIfNotMatched(newX, newY);
                //check tile on left
                newY = y - 1;
                addCoordinatesIfNotMatched(newX, newY);
            }
        }
    }

    private void addCoordinatesIfNotMatched(int x, int y){
        if(!Helper.isOutOfBounds(x, y, tilesArray.size(), tilesArray.get(0).size())){
            if(!isAMatch(x, y)) addCoordinates(x, y);
        }
    }


}
