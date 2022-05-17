package com.cpd2.main.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.LinkedList;


public class MulticastServiceReceive extends Thread{
    private DatagramSocket receiver=null;
    InetSocketAddress group;
    private boolean doStop = false;
    LinkedList<MembershipMessage> messagesReceived=new LinkedList<>();

    public MulticastServiceReceive(String multicastAddress, Integer multicastPort){
        try {

            // Setting up multicast receive
            receiver = new DatagramSocket(null); // unbound
            receiver.setReuseAddress(true); // set reuse address before binding
            receiver.bind(new InetSocketAddress(multicastPort)); // bind

            InetAddress mcastaddr = InetAddress.getByName(multicastAddress);
            group = new InetSocketAddress(mcastaddr, 0);
            NetworkInterface netIf = NetworkInterface.getByName("lo");
            receiver.joinGroup(group, netIf);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Receives multicast message (this is probably going to change)
     */
    private void receiveMulticastMessage(){
        byte[] msgBytes = new byte[1024]; // up to 1024 bytes
        DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length);
        try {
            receiver.receive(packet);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized void stopService() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    private boolean isMessageListEmpty(){
        return messagesReceived.isEmpty();
    }

    private MembershipMessage getMessage(){
        return messagesReceived.removeFirst();
    }

    @Override
    public void run() {
        while(keepRunning()){
            receiveMulticastMessage();
        }
    }

    
}
