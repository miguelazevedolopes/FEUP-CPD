package com.cpd2.main.communication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.LinkedList;


public class MulticastServiceReceive extends Thread{
    private DatagramSocket receiver=null;
    private InetSocketAddress group;
    private boolean stop = false; 
    private LinkedList<String> messagesReceived=new LinkedList<>();

    public MulticastServiceReceive(String multicastAddress, int multicastPort){
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
        byte[] msgBytes = new byte[1000]; // up to 2048 bytes
        DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length);
        try {
            receiver.receive(packet);
            ByteArrayInputStream bos = new ByteArrayInputStream(msgBytes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bos));
            String line="";
            String message="";
            while ((line = reader.readLine()) != null) {
                message+=line+"\n";
            }
            messagesReceived.add(message.substring(0, message.length()-1).trim());
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

    public int getMessageListSize(){
        return messagesReceived.size();
    }

    public String getMessage(){
        if(!messagesReceived.isEmpty())
            return messagesReceived.removeFirst();
        return null;
    }

    public LinkedList<String> getMessages(){
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
