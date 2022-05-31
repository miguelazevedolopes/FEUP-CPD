package com.cpd2.main.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedList;
import java.util.TreeSet;

import com.cpd2.main.service.messages.PipeMessage;


public class Node {

    MembershipService membershipService;
    StorageService storageService;

    public Node(String multicastAddressString, int multicastPort, String nodeIpAddress, int nodePort){

        MembershipView membershipView = new MembershipView(nodeIpAddress, 0, nodePort);
        MembershipLog membershipLog = new MembershipLog(membershipView);
        LinkedList<PipeMessage> membershipStoragePipe = new LinkedList<>();
        TreeSet<String> nodeHashes = new TreeSet<>();


        this.membershipService=new MembershipService(multicastAddressString, multicastPort,membershipView,membershipLog,nodeHashes,membershipStoragePipe);
        this.storageService= new StorageService(membershipLog, membershipView, nodeHashes,membershipStoragePipe);
        join();
     
    }

    public MembershipLog getMembershipLog(){
        return membershipService.getMembershipLogCopy();
    }

    public MembershipView getMembershipView() {
        return membershipService.getMembershipView();
    }


    public void join() {
        storageService.start();
        membershipService.start();
    }

    public void leave() {

        membershipService.stopService();
        storageService.stopService();
    }


}
