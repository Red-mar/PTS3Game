package com.game.classes;

import java.util.Arrays;

public class Pathfinder {

    private static Map map;

    private static Terrain previousTile;
    private static Terrain newCharacterTile;

    public Pathfinder(){


    }

    public static boolean canFindPath(Terrain terrain, Character character){
        Terrain[] terrains = new Terrain[4];
        int[] fValues;
        // 0 = up
        // 1 = down
        // 2 = right
        // 3 = left

        try {
            terrains[0] = map.getTerrains()[character.getCurrentTerrain().getX()][character.getCurrentTerrain().getY() + 1];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[0] = null;
        }
        try {
            terrains[1] = map.getTerrains()[character.getCurrentTerrain().getX()][character.getCurrentTerrain().getY() - 1];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[1] = null;
        }
        try {
            terrains[2] = map.getTerrains()[character.getCurrentTerrain().getX() + 1][character.getCurrentTerrain().getY()];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[2] = null;
        }
        try {
            terrains[3] = map.getTerrains()[character.getCurrentTerrain().getX() - 1][character.getCurrentTerrain().getY()];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[3] = null;
        }

        // Set the F-Value. If the terrain is impassable, set the F-Value to a very high number. Else set it to the right number.
        fValues = getFvalues(terrains, character, terrain, new Terrain(TerrainProperties.Normal, -400,-400));

        // Get the index of the lowest F-Value.
        int lowestIndexFValue = getMinValue(fValues, terrain, character.getCurrentTerrain(), terrains);

        if (fValues[lowestIndexFValue] > character.getMovementPoints()){
            return false;
        }
        else {
            // Continue finding A* Path Values until end is reached
            previousTile = character.getCurrentTerrain();
            newCharacterTile = terrains[lowestIndexFValue];
            for (int i = 1; i < character.getMovementPoints(); i++){
                if (isOnEnd(terrain)){
                    return true;
                }
                if (!continuePath(terrain,character) ) {
                    // Path can't be found
                    return false;

                }
            }
        }
//        if (!isOnEnd(terrain)){
//            return false;
//        }



        //return true if path can be found
        return true;
    }

    private static boolean isOnEnd(Terrain terrain){
        if (terrain.getX() == newCharacterTile.getX() && terrain.getY() == newCharacterTile.getY()){
            return true;
        }
        return false;
    }

    //TODO: Make sure it doesn't fuck shit up.
    private static boolean continuePath(Terrain terrain, Character character){
        Terrain[] terrains = new Terrain[4];
        int[] fValues;
        // 0 = up
        // 1 = down
        // 2 = right
        // 3 = left
        try {
            terrains[0] = map.getTerrains()[newCharacterTile.getX()][newCharacterTile.getY() + 1];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[0] = null;
        }
        try {
            terrains[1] = map.getTerrains()[newCharacterTile.getX()][newCharacterTile.getY() - 1];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[1] = null;
        }
        try {
            terrains[2] = map.getTerrains()[newCharacterTile.getX() + 1][newCharacterTile.getY()];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[2] = null;
        }
        try {
            terrains[3] = map.getTerrains()[newCharacterTile.getX() - 1][newCharacterTile.getY()];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            terrains[3] = null;
        }


        // Set the F-Value. If the terrain is impassable, set the F-Value to a very high number. Else set it to the right number.
        fValues = getFvalues(terrains, character, terrain, previousTile);

        int lowestIndexFValue = getMinValue(fValues, terrain, newCharacterTile, terrains);

        previousTile = newCharacterTile;
        newCharacterTile = terrains[lowestIndexFValue];

        if (fValues[lowestIndexFValue] > character.getMovementPoints()){
            return false;
        }
        else {
            return true;
        }
    }

    private static int[] getFvalues(Terrain[] terrains, Character character, Terrain terrain, Terrain previousTile){
        int[] fValues = new int[4];
        for (int i = 0; i < 4; i ++){
            if (terrains[i] == null){
                fValues[i] = 900;
            }
            else if (terrains[i].getX() == previousTile.getX() && terrains[i].getY() == previousTile.getY()){
                fValues[i] = 900;
            }
            else {
                if (terrains[i].getProperty() == TerrainProperties.Impassable){
                    fValues[i] = 900;
                }
                //F-Value = G-Value + H-Value
                else {
                    int gValue = Math.abs(terrains[i].getX() - character.getCurrentTerrain().getX()) + Math.abs(terrains[i].getY() - character.getCurrentTerrain().getY());
                    int hValue = Math.abs(terrains[i].getX() - terrain.getX()) + Math.abs(terrains[i].getY() - terrain.getY());
                    fValues[i] = gValue + hValue;
                }
            }
        }
        return fValues;
    }

    private static int getMinValue(int[] array, Terrain terrain, Terrain currentlyOn, Terrain[] terrains){

        int minValue = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++){
            if (array[i] < minValue) {
                minValue = array[i];
                index = i;
            }
        }
        return index;
    }


    public void setMap(Map map){
        this.map = map;
    }

}

