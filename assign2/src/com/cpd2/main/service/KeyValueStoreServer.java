package com.cpd2.main.service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.cpd2.main.service.rmi.KeyValueStore;


public class KeyValueStoreServer {
    public static void main(String[] args) throws RemoteException, UnknownHostException, MalformedURLException {
        
        KeyValueStore node = new Node("224.0.0.1",7373, InetAddress.getLocalHost().toString(), 7001);
        LocateRegistry.createRegistry(1900);
        Naming.rebind("rmi://"+InetAddress.getLocalHost().toString()+":1900",node);
    }
}
