package com.cpd2.main.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;

public class MulticastService<T>{   
    boolean receiverStarted=false;
    String multicastAddress;
    Integer multicastPort;
    MulticastServiceReceive receiver;
    MulticastServiceSend sender;

    public MulticastService(String multicastAddress, Integer multicastPort){
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        sender.start();
    }

}
