package com.cpd2.main.service.communication;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UnicastServiceReceive<T extends Serializable> extends Thread {

    private int port;
    private List<T> objectsReceived=new ArrayList<T>();
    private boolean doStop=false;

    public UnicastServiceReceive(int port){
        this.port=port;
    }

    public List<T> getObjectReceived() {
        return objectsReceived;
    }

    public T getLastUnicastObjectReceived(){
        if(!objectsReceived.isEmpty()){
            return objectsReceived.remove(0);
        }
        return null;
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
            Socket socket=null;
            InputStream input=null;
            while (keepRunning()) {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                ObjectInputStream receiver = new ObjectInputStream(input);
                objectsReceived.add((T) receiver.readObject());
            }
            socket.close();
            input.close();
        } catch (Exception ex) {
            System.out.println("TCP Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int numberOfObjectsReceived(){
        return objectsReceived.size();
    }
    
}
