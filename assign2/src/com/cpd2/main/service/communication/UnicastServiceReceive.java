package com.cpd2.main.service.communication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UnicastServiceReceive extends Thread {

    private int port;
    private List<String> objectsReceived=new ArrayList<String>();
    private boolean doStop=false;

    public UnicastServiceReceive(int port){
        this.port=port;
    }

    public List<String> getObjectReceived() {
        return objectsReceived;
    }

    public String getLastUnicastObjectReceived(){
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line="";
                String message="";
                while ((line = reader.readLine()) != null) {
                    message+=line+"\n";
                }
                objectsReceived.add(message.substring(0, message.length()-1));
            }
            serverSocket.close();
            socket.close();
            input.close();
            objectsReceived.clear();
        } catch (Exception ex) {
            System.out.println("TCP Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int numberOfObjectsReceived(){
        return objectsReceived.size();
    }
    
}
