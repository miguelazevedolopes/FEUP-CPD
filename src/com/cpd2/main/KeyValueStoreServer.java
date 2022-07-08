package com.cpd2.main;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.cpd2.main.rmi.KeyValueStore;
import com.cpd2.main.service.Node;
import com.cpd2.main.service.Utils;


public class KeyValueStoreServer {
    public static void main(String[] args) throws RemoteException, UnknownHostException, MalformedURLException {
        KeyValueStore node = new Node("224.0.0.1",7373,"127.0.0."+args[0], 7000+Integer.parseInt(args[0]));
        Naming.rebind("rmi://"+"127.0.0."+args[0]+"/"+Utils.generateHash("127.0.0."+args[0]),node);
    }
}