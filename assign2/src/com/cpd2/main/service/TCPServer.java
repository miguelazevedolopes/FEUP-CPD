package com.cpd2.main.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer<T> extends Thread {

    private Integer port;
    private T objectReceived=null;

    public TCPServer(int port){
        this.port=port;
    }

    public T getObjectReceived() {
        return objectReceived;
    }

    public void setObjectReceived(T object) {
        this.objectReceived = object;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
  
            while (true) {
                Socket socket = serverSocket.accept();
 
                InputStream input = socket.getInputStream();
                ObjectInputStream receiver = new ObjectInputStream(input);
                this.setObjectReceived((T) receiver.readObject());
            }
 
        } catch (Exception ex) {
            System.out.println("TCP Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
