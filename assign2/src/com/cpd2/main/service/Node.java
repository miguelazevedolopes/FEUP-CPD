package com.cpd2.main.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


public class Node implements KeyValueStore<Object,Object>,ClusterMembership{

    MulticastService<MembershipMessage> multicastService;
    UnicastService<MembershipMessage> unicastService;
    MembershipView membershipView;
    MembershipLog membershipLog;
    boolean stop =false;
    private final Map<Object, Object> storage;
    private TreeSet<Node> clusterNodes;


    public Node(String multicastAddressString, Integer multicastPort, Integer nodeID){

        this.storage = new HashMap<>();
        this.clusterNodes = new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                // TODO: does this work?
                return o1.getMembershipView().getNodeHash().compareTo(o2.getMembershipView().getNodeHash());
            }
        });
        
        // Setting up multicast communication
        multicastService=new MulticastService<MembershipMessage>(multicastAddressString,multicastPort);

        membershipView = new MembershipView(nodeID, 0);

        membershipLog = new MembershipLog(nodeID,0);

        // Setting up unicast communication
        unicastService = new UnicastService<MembershipMessage>();
        
    }

    public MembershipView getMembershipView() {
        return membershipView;
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

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    private void sendMulticastMembershipMessage(){
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

    @Override
    public void join() {
        // Starts listening for membership message
        unicastService.startUnicastReceiver(7000+this.membershipView.nodeID);
        


        System.out.println("Sent join message");

        int tries=0;

        // Sends join message (includes MembershipView and MembershipLog)
        while(unicastService.getNumberOfObjectsReceived()!=3&&tries!=3){
            try {
                
                sendMulticastMembershipMessage();
                System.out.println("Try");
                Thread.sleep(2000);
                tries++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
        
        System.out.println("Aqui");
        if(tries==3 && unicastService.getNumberOfObjectsReceived()==0){
            System.out.println("No response. Must be the first node");
            multicastService.startMulticastReceiver();
            System.out.println("Receiving on multicast");
            
            Runnable periodicMessage = new Runnable() {
                public void run() {
                    while(true){
                        System.out.println("Periodic membership message sent.");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            while(keepRunning()){
                
                if(multicastService.getReceiverMessageSize()!=0){
                    // insert update log + membership function here
                }

                periodicMessage.run();
            }
        }        
    }

    @Override
    public void leave() {

    }

}
