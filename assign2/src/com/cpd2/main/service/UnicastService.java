package com.cpd2.main.service;

import java.io.Serializable;
import java.util.List;

public class UnicastService<T extends Serializable> {
    UnicastServiceReceive<T> receiver = null;
    UnicastServiceSend sender =null;

    public void startUnicastReceiver(int port){
        receiver= new UnicastServiceReceive<T>(port);
        receiver.start();
    }

    public void stopUnicastReceiver(){
        receiver.stopService();
    }

    public int getNumberOfObjectsReceived(){
        return receiver.numberOfObjectsReceived();
    }

    public List<T> getObjectReceived(){
        return receiver.getObjectReceived();
    }

    public T getLastObjectReceived(){
        return receiver.getLastUnicastObjectReceived(); 
    }

    public void sendUnicastMessage(int port, String ipAddress, T messageToSend){
        sender = new UnicastServiceSend(port, ipAddress);
        sender.setObjectToSend(messageToSend);
        sender.start();
    }
}
