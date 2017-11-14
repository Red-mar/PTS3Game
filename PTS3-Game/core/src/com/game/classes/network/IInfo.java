package com.game.classes.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IInfo extends Remote {
    String sendMessage(String message) throws RemoteException;
}
