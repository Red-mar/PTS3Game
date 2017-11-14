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
    private List<Terrain> path;

    public aStarPathing(Map map) {
        this.map = map;
        path = new ArrayList<Terrain>();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<Terrain> getPath() {
        return path;
    }

    public void findPath(Terrain start, Terrain target){
        List<Terrain> openList = new ArrayList<Terrain>();
        HashSet<Terrain> closedList = new HashSet<Terrain>();

        openList.add(start);

        while (openList.size() > 0){
            Terrain current = openList.get(0);
            for (int i = 1;i < openList.size(); i++){
                if (openList.get(i).getfCost() < current.getfCost() ||
                        openList.get(i).getfCost() == current.getfCost() && openList.get(i).gethCost() < current.gethCost()){
                    current = openList.get(i);
                }
            }

            openList.remove(current);
            closedList.add(current);

            if (current.getX() == target.getX() && current.getY() == target.getY()){
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

        this.path = path;
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
