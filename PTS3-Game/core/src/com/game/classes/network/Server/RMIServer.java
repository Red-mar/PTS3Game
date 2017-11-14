package com.game.classes.network.Server;

import com.game.classes.network.Informatie;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    private static final int portNumberPush = 1337;

    private static final String bindingNamePush = "Info";

    private Registry registryPush = null;
    private Informatie informatie = null;

    public RMIServer() {
        try {
            informatie = new Informatie();
            System.out.println("Server: informatie created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create informatie");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            informatie = null;
        }

        try {
            registryPush = LocateRegistry.createRegistry(portNumberPush);
            System.out.println("Server: Registry created on port number ");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registryPush = null;
        }

        try {
            registryPush.rebind(bindingNamePush, informatie);
            System.out.println("Binding chat");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind chat");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }
}
