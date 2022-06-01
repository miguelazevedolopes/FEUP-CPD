package com.cpd2.main.service.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KeyValueStore extends Remote{
    public void join() throws RemoteException;

    public void leave() throws RemoteException;

    public void put(String file) throws RemoteException;

    public void delete(String fileKey) throws RemoteException;

    public String get(String fileKey) throws RemoteException;
}
