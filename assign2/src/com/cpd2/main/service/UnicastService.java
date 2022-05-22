package com.cpd2.main.service;

import java.io.Serializable;
import java.util.List;

public class UnicastService<T extends Serializable> {
    UnicastServiceReceive<T> receiver = null;
    UnicastServiceSend sender =null;

    public void startUnicastReceiver(Integer port){
        receiver= new UnicastServiceReceive<T>(port);
        receiver.start();
    }

    public void stopUnicastReceiver(){
        receiver.stopService();
    }

    public Integer getNumberOfObjectsReceived(){
        return receiver.numberOfObjectsReceived();
    }

    public List<T> getObjectReceived(){
        return receiver.getObjectReceived();
    }

    public T getLastObjectReceived(){
        return receiver.getObjectReceived().size()>0 ? receiver.getObjectReceived().get(0) : null; 
    }

    public void sendUnicastMessage(Integer port, String ipAddress, T messageToSend){
        sender = new UnicastServiceSend(port, ipAddress);
        sender.setObjectToSend(messageToSend);
        sender.start();
    }
}
