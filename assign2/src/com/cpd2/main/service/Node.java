package com.cpd2.main.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.StandardSocketOptions;

public class Node{

    MulticastService multicastService;    

    MembershipView membershipView;
    MembershipLog membershipLog;
    

    Node(String multicastAddressString, Integer multicastPort, Integer nodeID){
        
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
   
}
