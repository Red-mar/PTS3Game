package com.game.classes.network.Client;


import com.game.classes.network.IInfo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private static final String bindingName = "Info";

    private Registry registry = null;
    private IInfo info = null;

    // Constructor
    public RMIClient(String ipAddress, int portNumber) {

        // Locate registry at IP address and port number
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
            System.out.println("Binding registry.");
        } catch (RemoteException ex) {
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }

        if (registry != null) {
            try {
                info = (IInfo) registry.lookup(bindingName);
                System.out.println("Binding info class.");
            } catch (RemoteException ex) {
                System.out.println("Client: RemoteException: " + ex.getMessage());
            } catch (NotBoundException ex) {
                System.out.println("Client: NotBoundException: " + ex.getMessage());
            }
        }
    }

    public IInfo getInfo() {
        return info;
    }
}