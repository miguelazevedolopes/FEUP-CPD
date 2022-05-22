package com.cpd2.main.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.LinkedList;


public class MulticastServiceReceive<T extends Serializable> extends Thread{
    private DatagramSocket receiver=null;
    private InetSocketAddress group;
    private boolean stop = false;
    private LinkedList<T> messagesReceived=new LinkedList<>();

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
            ByteArrayInputStream bos = new ByteArrayInputStream(msgBytes);
            ObjectInputStream o = new ObjectInputStream(bos);
            T object = (T) o.readObject();

            messagesReceived.add(object);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    public Integer getMessageListSize(){
        return messagesReceived.size();
    }

    public T getMessage(){
        if(!messagesReceived.isEmpty())
            return messagesReceived.removeFirst();
        return null;
    }

    public LinkedList<T> getMessages(){
        return messagesReceived;
    }

    @Override
    public void run() {
        while(keepRunning()){
            receiveMulticastMessage();
        }
        receiver.close();
    }

    
}
