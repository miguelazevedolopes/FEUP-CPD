package com.cpd2.main.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


public class Node implements KeyValueStore<Object,Object>,ClusterMembership{

    MembershipService membershipService;
    private final Map<Object, Object> storage;
    private TreeSet<Node> clusterNodes;


    public Node(String multicastAddressString, int multicastPort, String nodeIpAddress, int nodePort){

        this.storage = new HashMap<>();
        this.membershipService=new MembershipService(multicastAddressString, multicastPort, nodeIpAddress,nodePort);
        join();
     
    }

    public MembershipLog getMembershipLog(){
        return membershipService.getMembershipLogCopy();
    }

    public MembershipView getMembershipView() {
        return membershipService.getMembershipView();
    }

    public Map<Object, Object> getStorage() {
        return storage;
    }

    public void addNode(Node node) {
        clusterNodes.add(node);
        transferOnJoin(node);
    }

    public void removeNode(Node node) {
        transferOnLeave(node);
        clusterNodes.remove(node);
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

    public void transferOnLeave(Node node) {

    }

    public void transferOnJoin(Node node) {

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
        membershipService.start();
    }

    @Override
    public void leave() {
        membershipService.stopService();
    }

}
