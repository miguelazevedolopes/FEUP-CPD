package com.cpd2.main.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.TreeSet;

import com.cpd2.main.messages.PipeMessage;
import com.cpd2.main.messages.StorageMessage;
import com.cpd2.main.messages.enums.StorageMessageType;
import com.cpd2.main.rmi.KeyValueStore;


public class Node extends UnicastRemoteObject implements KeyValueStore{


    MembershipService membershipService;
    StorageService storageService;

    public Node(String multicastAddressString, int multicastPort, String nodeIpAddress, int nodePort) throws RemoteException{
        super();
        MembershipView membershipView = new MembershipView(nodeIpAddress, 0, nodePort);
        MembershipLog membershipLog = new MembershipLog(membershipView);
        LinkedList<PipeMessage> membershipStoragePipe = new LinkedList<>();
        TreeSet<String> nodeHashes = new TreeSet<>();


        this.membershipService=new MembershipService(multicastAddressString, multicastPort,membershipView,membershipLog,nodeHashes,membershipStoragePipe);
        this.storageService= new StorageService(membershipLog, membershipView, nodeHashes,membershipStoragePipe);

    }

    public MembershipLog getMembershipLog(){
        return membershipService.getMembershipLogCopy();
    }

    public MembershipView getMembershipView() {
        return membershipService.getMembershipView();
    }

    @Override
    public void join() {
        storageService.start();
        membershipService.start();
    }

    @Override
    public void leave() {
        storageService.stopService();
        membershipService.stopService();
    }

    @Override
    public void put(String file) throws RemoteException {
        storageService.put(new StorageMessage(StorageMessageType.PUT, file));
    }

    @Override
    public void delete(String fileKey) throws RemoteException {
        storageService.delete(new StorageMessage(StorageMessageType.DELETE, fileKey));
        
    }

    @Override
    public String get(String fileKey) throws RemoteException {
        return storageService.get(new StorageMessage(StorageMessageType.GET, fileKey));
    }


}
