package com.cpd2.main.service.communication;

import java.io.IOException;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class UnicastServiceSend extends Thread{
    int port;
    String ipAddress;
    Object msgToSend;

    public UnicastServiceSend(int port, String ipAddress){
        this.port=port;
        this.ipAddress = ipAddress;
    }
    
    public void setObjectToSend(Object msgToSend){
        this.msgToSend=msgToSend;
    }

    @Override
    public void run() {

        // TODO Auto-generated method stub
        try (Socket socket = new Socket(ipAddress, port)) {
 
            OutputStream output = socket.getOutputStream();
            PrintWriter sender =  new PrintWriter(output, true);
            msgToSend.toString().lines().forEach(s-> sender.println(s));
            socket.close();

        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
