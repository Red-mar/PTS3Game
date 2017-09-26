package com.example.network.Server;

public class ClientInfo {
    private String name;
    private int x;
    private int y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ClientInfo(String name){
        this.name = name;
    }
}
