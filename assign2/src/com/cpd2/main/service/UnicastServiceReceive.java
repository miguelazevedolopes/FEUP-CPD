package com.cpd2.main.service;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UnicastServiceReceive<T extends Serializable> extends Thread {

    private Integer port;
    private List<T> objectsReceived=new ArrayList<T>();
    private boolean doStop=false;

    public UnicastServiceReceive(int port){
        this.port=port;
    }

    public List<T> getObjectReceived() {
        return objectsReceived;
    }

    public synchronized void stopService() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {  
            while (keepRunning()) {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                ObjectInputStream receiver = new ObjectInputStream(input);
                objectsReceived.add((T) receiver.readObject());
            }
 
        } catch (Exception ex) {
            System.out.println("TCP Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Integer numberOfObjectsReceived(){
        return objectsReceived.size();
    }
    
}
