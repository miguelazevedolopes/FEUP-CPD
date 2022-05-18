package com.cpd2.main.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
public class MulticastService<T extends Serializable>{   
    private boolean receiverStarted=false;
    private String multicastAddress;
    private Integer multicastPort;
    private MulticastServiceReceive<T> receiver;
    private MulticastServiceSend sender;

    public MulticastService(String multicastAddress, Integer multicastPort){
        this.multicastAddress=multicastAddress;
        this.multicastPort=multicastPort;
    }

    public void startMulticastReceiver(){
        if(!receiverStarted){
            receiver= new MulticastServiceReceive<T>(multicastAddress, multicastPort);
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

    public LinkedList<T> getMulticastReceiverMessages(){
        return receiver.getMessages();
    }

    public T getLastMulticastReceiverMessage(){
        return receiver.getMessage();
    }

    public Integer getReceiverMessageSize(){
        return receiver.getMessageListSize();
    }

    public void sendMulticastMessage(T messageToSend){
        sender = new MulticastServiceSend(multicastAddress, multicastPort);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(bos);                
            o.writeObject(messageToSend);
            o.flush();
            sender.setMessageToSend(bos.toByteArray());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        sender.start();
    }

}
