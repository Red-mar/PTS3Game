package com.game.classes.network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Informatie extends UnicastRemoteObject implements IInfo {

    public Informatie() throws RemoteException {
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        return message;
    }
}
