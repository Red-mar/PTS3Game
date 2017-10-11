package com.game.classes.pathing;

import com.game.classes.Map;
import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class aStarPathing {
    private Map map;

    private List<Terrain> openList; /** Set of tiles that has not been evaluated **/
    private HashSet<Terrain> closedList; /** Set of nodes that has been evaluated **/

    public aStarPathing(Map map) {
        openList = new ArrayList<Terrain>();
        closedList = new HashSet<Terrain>();
        this.map = map;
    }

    public void findPath(Terrain start, Terrain target){
        openList.add(start);

        while (openList.size() > 0){
            Terrain current = openList.get(0);
            for (Terrain terrain : openList) {
                if (terrain.getfCost() < current.getfCost() ||
                        terrain.getfCost() == current.getfCost() && terrain.gethCost() < current.gethCost()){
                    current = terrain;
                }
            }

            openList.remove(current);
            closedList.add(current);

            if (current == target){
                retracePath(start, target);
                return;
            }

            for (Terrain neighbour : map.getNeighbours(current)) {
                if (neighbour.getProperty() == TerrainProperties.Impassable || closedList.contains(neighbour)) continue;

                int newMovementCostToNeighbour = current.getgCost() + getDistance(current, neighbour);

                if (newMovementCostToNeighbour < neighbour.getgCost() || !openList.contains(neighbour)){
                    neighbour.setgCost(newMovementCostToNeighbour);
                    neighbour.sethCost(getDistance(neighbour, target));
                    neighbour.setParent(current);
                }

                if (!openList.contains(neighbour)){
                    openList.add(neighbour);
                }

            }
        }
    }

    private void retracePath(Terrain start, Terrain target){
        ArrayList<Terrain> path = new ArrayList<Terrain>();
        Terrain current = target;

        while (current != start){
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
    }

    private int getDistance(Terrain tileA, Terrain tileB){
        int distanceX = Math.abs(tileA.getX() - tileB.getX());
        int distanceY = Math.abs(tileA.getY() - tileB.getY());

        if (distanceX > distanceY){
            return 14 * distanceY + 10 * (distanceX-distanceY);
        }
        return 14 * distanceX + 10 * (distanceY - distanceX);
    }
}
