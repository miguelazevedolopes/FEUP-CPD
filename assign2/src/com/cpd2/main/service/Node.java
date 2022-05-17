package com.cpd2.main.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.Map;

public class Node implements KeyValueStore{

    MulticastService multicastService;    
    MembershipView membershipView;
    MembershipLog membershipLog;
    private final Map<Object, Object> storage;


    Node(String multicastAddressString, Integer multicastPort, Integer nodeID){

        this.storage = new HashMap<>();
        
        // Setting up multicast communication
        multicastService=new MulticastService(multicastAddressString,multicastPort);

        membershipView = new MembershipView(nodeID, 0);

        membershipLog = new MembershipLog(nodeID,0);

        TCPServer tcpServer = new TCPServer<MembershipMessage>(7000+nodeID);
        
        sendJoinMessage();

        // Aguardar por resposta TCP, senão voltar a enviar join message
    }


    private void sendJoinMessage(){
        MembershipMessage message = new MembershipMessage(membershipView, membershipLog);
        multicastService.sendMulticastMessage(message);
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

    // @Override
    // public void join() {
    //     // TODO Auto-generated method stub
        
    // }

    // @Override
    // public void leave() {
    //     // TODO Auto-generated method stub
        
    // }

}
