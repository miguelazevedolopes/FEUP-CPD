package com.cpd2.main.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;


public class MulticastServiceSend extends Thread{
    private DatagramSocket sender=null;
    private byte[] messageToSend;
    InetSocketAddress dest;
    public MulticastServiceSend(String multicastAddressString, Integer multicastPort){
        try {

            // Setting up multicast send
            sender = new DatagramSocket(new InetSocketAddress(0));
            NetworkInterface outgoingIf = NetworkInterface.getByName("lo");
            sender.setOption(StandardSocketOptions.IP_MULTICAST_IF, outgoingIf);
            InetAddress mcastaddr = InetAddress.getByName(multicastAddressString);
            dest = new InetSocketAddress(mcastaddr, multicastPort);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setMessageToSend(byte[] messageToSend){
        this.messageToSend=messageToSend;
    }

    /**
     * Sends a multicast message (msgBytes)
     * @param messageToSend
     */
    private void sendMulticastMessage(){
        try{
            DatagramPacket packet = new DatagramPacket(messageToSend, messageToSend.length, dest);
            sender.send(packet);
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendMulticastMessage();
    }
}
