package com.cpd2.main.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.Map;

public class Node implements ClusterMembership, KeyValueStore{

    DatagramSocket sender=null;
    NetworkInterface outgoingIf;
    private final Map<Object, Object> storage;
    

    Node(){
        this.storage = new HashMap<>();
        

        
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
        storage.put(key, value);
    }

    @Override
    public Object get(Object key) {
        if (storage.containsKey(key)) {
            return storage.get(key);
        }
        return -1;
    }

    @Override
    public void delete(Object key) {
        if (storage.containsKey(key)) {
            storage.remove(key);
        }
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
