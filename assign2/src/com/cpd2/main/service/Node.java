package com.cpd2.main.service;

import java.util.HashMap;
import java.util.Map;


public class Node implements KeyValueStore<Object,Object>,ClusterMembership{

    MembershipService membershipService;
    private final Map<Object, Object> storage;


    public Node(String multicastAddressString, Integer multicastPort, Integer nodeID){

        this.storage = new HashMap<>();
        this.membershipService=new MembershipService(multicastAddressString, multicastPort, nodeID);
        
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
