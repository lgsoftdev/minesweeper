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

    //Coordinates xy has content/value equal to the search value.
    private boolean isAMatch(int x, int y){
        if(tilesArray.get(x).get(y) == getToFind()) return true;
        return false;
    }

    private void addCoordinates(int x, int y){
        if(!areCoordinatesInArray(x, y)) coordinatesArray.add(new Integer[]{x,y});
    }

    public ArrayList<Integer[]> getMatchedTilesCoordinates(){
        //1. Check tiles on initialX row.
        findMatchedTilesOnRow(getInitialX(), getInitialY());
        //2. Traverse up rows from initialX
        //3. Traverse down rows from highest traversed row.
        traverseRows(true, getInitialX(), true);
        //4. Traverse down rows from initialX
        //5. Traverse up rows from lowest traversed row.
        traverseRows(false, getInitialX(), true);

        if(isIncludeBorderTiles()) addBorderTilesCoordinates();

        return coordinatesArray;
    }

    private void findMatchedTilesOnRow(int currentX, int currentY){
        //move right
        int x = currentX;
        int y = currentY;
        findMatchOnRow(x, y, true);

        //move left
        y = currentY - 1;
        findMatchOnRow(x, y, false);
    }

    //Helper method for findMatchedTilesOnRow.
    private void findMatchOnRow(int x, int y, boolean increment){
        boolean stop = false;
        while(!stop){ 
            if((increment && y < tilesArray.get(x).size()) || (!increment && y >= 0)){
                if(isAMatch(x, y)) addCoordinates(x, y);
                else stop = true;
                
                if(increment) y++;
                else y--;
            
            } else {
                stop = true;
            }
        }
    }

    //If traverseUp == false, then traverse rows down.
    private void traverseRows(boolean traverseUp, int fromCoordinateX, boolean traverseOppositeDirection){
        //Check from row after initialX until last row with matched cells.
        int focalX = fromCoordinateX;
        int x = traverseUp ? fromCoordinateX + 1 : fromCoordinateX - 1; 
        while((traverseUp && x < tilesArray.size()) || (!traverseUp && x >= 0)){
            int previousX = traverseUp ? x - 1 : x + 1;
            ArrayList<Integer[]> coordsArr = coordinatesArray.stream().filter(item -> item[0] == previousX).collect(Collectors.toCollection(ArrayList::new));   
            ArrayList<Integer> yIndicesArr = new ArrayList<>();   
            for(int i = 0; i < coordsArr.size(); i++){
                //store y coordinate of current matched cell which aligns with previous row's matched cells.
                if(isAMatch(x, coordsArr.get(i)[1])) {
                    yIndicesArr.add(coordsArr.get(i)[1]);
                    focalX = x;
                }
            }

            //move right and left 
            int size = yIndicesArr.size(); 
            if(size > 0) {
                for(int i = 0; i < size; i++){
                    findMatchedTilesOnRow(x, yIndicesArr.get(i));
                }
                if(traverseUp) x++;
                else x--;
            } else{
                if(traverseUp) x = tilesArray.size(); //so as to fail the while condition;
                else x = -1;
            }
        }

        if(traverseOppositeDirection) traverseRows(!traverseUp, focalX, false);
    }

    private void addBorderTilesCoordinates(){
        int size = coordinatesArray.size();
        if(size > 0){
            for(int i = 0; i < size; i++){
                int x = coordinatesArray.get(i)[0];
                int y = coordinatesArray.get(i)[1];
                ArrayList<Integer[]> coordsArr = new ArrayList<>();
                //check tile on top
                coordsArr.add(new Integer[]{ x - 1, y});
                //check tile at bottom
                coordsArr.add(new Integer[]{ x + 1, y});
                //check tile on right
                coordsArr.add(new Integer[]{ x, y + 1});
                //check tile on left
                coordsArr.add(new Integer[]{ x, y - 1});
                coordsArr.stream().forEach(xy -> addCoordinatesIfNotMatched(xy[0], xy[1]));
            }
        }
    }

    private void addCoordinatesIfNotMatched(int x, int y){
        if(!Helper.isOutOfBounds(x, y, tilesArray.size(), tilesArray.get(0).size())){
            if(!isAMatch(x, y)) addCoordinates(x, y);
        }
    }

    private boolean areCoordinatesInArray(int x, int y){
        if(coordinatesArray.size() > 0){
            ArrayList<Integer[]> coords = coordinatesArray.stream().filter(item -> item[0] == x && item[1] == y).collect(Collectors.toCollection(ArrayList::new));
            if (coords.size() > 0) return true;
        }
        return false;
    }
}
