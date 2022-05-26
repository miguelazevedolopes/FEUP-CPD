package com.cpd2.main.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


public class Node implements ClusterMembership{

    MembershipService membershipService;
    private TreeSet<Node> clusterNodes;


    public Node(String multicastAddressString, int multicastPort, String nodeIpAddress, int nodePort){

        MembershipView membershipView = new MembershipView(nodeIpAddress, 0, nodePort);
        MembershipLog membershipLog = new MembershipLog(membershipView);

        this.membershipService=new MembershipService(multicastAddressString, multicastPort,membershipView,membershipLog);
        join();
     
    }

    public MembershipLog getMembershipLog(){
        return membershipService.getMembershipLogCopy();
    }

    public MembershipView getMembershipView() {
        return membershipService.getMembershipView();
    }

    public Node getSuccessor(Node node) {
        if (clusterNodes.last().equals(node)) {
            return clusterNodes.first();
        }
        else {
            // TODO: does this work?
            return clusterNodes.higher(node);
        }
    }

    @Override
    public void join() {
        membershipService.start();
    }

    @Override
    public void leave() {
        membershipService.stopService();
    }

}
