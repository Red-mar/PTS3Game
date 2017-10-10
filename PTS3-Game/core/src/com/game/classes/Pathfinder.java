package com.game.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Pathfinder implements Serializable {

    private Character character;
    private ArrayList<Terrain> impassableTiles = new ArrayList<Terrain>();
    private Map map;

    public Pathfinder(Character character){
        this.character = character;
    }

    public boolean canFindPath(Terrain terrain){

        if (terrain.getX() == character.getCurrentTerrain().getX()) {
            //On same X as char, either up or down.
            if (terrain.getY() > character.getCurrentTerrain().getY()){
                //up
                for (int i = terrain.getY() - 1; i > character.getCurrentTerrain().getY(); i--){
                    if (map.getTerrains()[terrain.getX()][i].getProperty() == TerrainProperties.Impassable){
                        //impassable terrain found underneath terrain.
                        if (character.getMovementPoints() - 2 >= terrain.getY() - character.getCurrentTerrain().getY()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
            else {
                //down
                for (int i = terrain.getY() + 1; i < character.getCurrentTerrain().getY(); i++){
                    if (map.getTerrains()[terrain.getX()][i].getProperty() == TerrainProperties.Impassable){
                        //impassable terrain found above terrain.
                        if (character.getMovementPoints() - 2 >= character.getCurrentTerrain().getY() - terrain.getY()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        else if (terrain.getY() == character.getCurrentTerrain().getY()){
            //On same Y as char, either left or right.
            if (terrain.getX() > character.getCurrentTerrain().getX()){
                //left
                for (int i = terrain.getX() - 1; i > character.getCurrentTerrain().getX(); i--){
                    if (map.getTerrains()[i][terrain.getY()].getProperty() == TerrainProperties.Impassable){
                        //impassable terrain found left of terrain.
                        if (character.getMovementPoints() - 2 >= terrain.getX() - character.getCurrentTerrain().getX()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
            else{
                //right

                for (int i = terrain.getX() + 1; i < character.getCurrentTerrain().getX(); i++){
                    if (map.getTerrains()[i][terrain.getY()].getProperty() == TerrainProperties.Impassable){
                        //impassable terrain found right of terrain.
                        if (character.getMovementPoints() - 2 >= character.getCurrentTerrain().getX() - terrain.getX()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        else{
            //not in the direct LoS of char.
            if (terrain.getY() > character.getCurrentTerrain().getY()){
                //up
            }
            else {
                //down
            }
            if (terrain.getX() > character.getCurrentTerrain().getX()){
                //left
            }
            else{
                //right
            }
        }
        return true;
    }

    public void setMap(Map map){
        this.map = map;
    }

}

enum Location {
    LEFT,
    DOWN,
    RIGHT,
    UP
}
