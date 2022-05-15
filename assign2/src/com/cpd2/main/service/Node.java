package com.cpd2.main.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardSocketOptions;

public class Node implements ClusterMembership, KeyValueStore{

    DatagramSocket sender=null;
    NetworkInterface outgoingIf;
    

    Node(){
        
            
        

        
    }
    
    private void sendMessage(byte[] msgBytes,String multicastAddressString){
        try{
            sender = new DatagramSocket(new InetSocketAddress(0));
            NetworkInterface outgoingIf = NetworkInterface.getByName("lo");
            sender.setOption(StandardSocketOptions.IP_MULTICAST_IF, outgoingIf);
            InetAddress mcastaddr = InetAddress.getByName(multicastAddressString);
            int port = 7373;
            InetSocketAddress dest = new InetSocketAddress(mcastaddr, port);
            DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length, dest);
            sender.send(packet);
        }
        catch (Exception e){

        }
    }

    @Override
    public void put(Object key, Object value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object get(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Object key) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void join() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub
        
    }

}
