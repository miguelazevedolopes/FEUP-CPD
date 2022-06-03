package com.cpd2.main.service.communication;

import java.util.List;

public class UnicastService {
    UnicastServiceReceive receiver = null;
    UnicastServiceSend sender =null;

    public UnicastService(){
        receiver = null;
        sender =null;
    }

    public void startUnicastReceiver(int port){
        receiver= new UnicastServiceReceive(port);
        receiver.start();
    }

    public void stopUnicastReceiver(){
        receiver.stopService();
    }

    public int getNumberOfObjectsReceived(){
        return receiver.numberOfObjectsReceived();
    }

    public List<String> getObjectReceived(){
        return receiver.getObjectReceived();
    }

    public String getLastObjectReceived(){
        return receiver.getLastUnicastObjectReceived(); 
    }

    public void sendUnicastMessage(int port, String ipAddress, String messageToSend){
        sender = new UnicastServiceSend(port, ipAddress);
        sender.setObjectToSend(messageToSend);
        sender.start();
    }
}
