package com.cpd2.main.service.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
public class MulticastService{   
    private boolean receiverStarted=false;
    private String multicastAddress;
    private int multicastPort;
    private MulticastServiceReceive receiver;
    private MulticastServiceSend sender;
    private MulticastServiceSend periodicSender;

    public MulticastService(String multicastAddress, int multicastPort){
        this.multicastAddress=multicastAddress;
        this.multicastPort=multicastPort;
    }

    public void startMulticastReceiver(){
        if(!receiverStarted){
            receiver= new MulticastServiceReceive(multicastAddress, multicastPort);
            this.receiverStarted=true;
            receiver.start();
        }
        else{
            System.out.println("Multicast error: Trying to start an already running multicast receiver server");
        }
        
    }

    public void stopMulticastReceiver(){
        if(receiverStarted){
            receiver.stopService();
        }
    }

    public LinkedList<String> getMulticastReceiverMessages(){
        return receiver.getMessages();
    }

    public String getLastMulticastReceiverMessage(){
        return receiver.getMessage();
    }

    public int getReceiverMessageSize(){
        return receiver.getMessageListSize();
    }

    public void sendMulticastMessage(String messageToSend){
        sender = new MulticastServiceSend(multicastAddress, multicastPort);
        sender.setMessageToSend(messageToSend.getBytes(StandardCharsets.UTF_8));     
        sender.start();
    }

    public void sendPeriodicMulticastMessage(String messageToSend,int period){
        periodicSender = new MulticastServiceSend(multicastAddress, multicastPort, period);
        periodicSender.setMessageToSend(messageToSend.getBytes(StandardCharsets.UTF_8));  
        periodicSender.start();
    }

    public void stopPeriodicMulticastSender(){
        periodicSender.stopService();
    }

    public void pausePeriodicMulticastSender(){
        periodicSender.pauseService();
    }

    public void resumePeriodicMulticastSender(){
        periodicSender.resumeService();
    }

    public boolean serviceIsPaused(){
        return periodicSender.isPaused();
    }

    public void updatePeriodicMessage(String messageToSend){
        periodicSender.setMessageToSend(messageToSend.getBytes(StandardCharsets.UTF_8));  
    }

}
