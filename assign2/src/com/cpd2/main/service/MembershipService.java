package com.cpd2.main.service;

import java.util.Queue;
import java.util.TreeSet;

import com.cpd2.main.service.communication.MulticastService;
import com.cpd2.main.service.communication.UnicastService;
import com.cpd2.main.service.messages.MembershipMessage;
import com.cpd2.main.service.messages.PipeMessage;
import com.cpd2.main.service.messages.enums.MembershipMessageType;
import com.cpd2.main.service.messages.enums.PipeMessageType;

public class MembershipService extends Thread{

    private MulticastService<MembershipMessage> multicastService;
    private UnicastService<MembershipMessage> unicastService;
    private MembershipView membershipView;
    private MembershipLog membershipLog;
    private TreeSet<String> nodeHashes;
    private boolean stop =false;
    private Queue<PipeMessage> membershipStoragePipe;

    public MembershipService(String multicastAddressString, int multicastPort, MembershipView membershipView, MembershipLog membershipLog, TreeSet<String> nodeHashes, Queue<PipeMessage> membershipStoragePipe){
        // Setting up multicast communication
        multicastService=new MulticastService<MembershipMessage>(multicastAddressString,multicastPort);
        
        this.membershipView = membershipView;

        membershipView.saveMembershipInfo();

        this.membershipLog = membershipLog;

        this.membershipStoragePipe = membershipStoragePipe;

        this.nodeHashes=nodeHashes;
        // Setting up unicast communication
        unicastService = new UnicastService<MembershipMessage>();
    }




    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    public MembershipLog getMembershipLogCopy(){
        return membershipLog.copy();
    }

    public MembershipView getMembershipView(){
        return membershipView;
    }

    private void handleMembershipMessage(MembershipMessage msg){
        if(msg.mView.getNodeIP()==membershipView.getNodeIP()) return;
        if(msg.type==MembershipMessageType.JOIN && !membershipLog.has(msg.mView.getNodeIP())){
            if(!membershipLog.isUpToDate()) return;
            int randomTime=(int) (Math.random() * (1000 - 10 + 1) + 10);  
            try {
                //System.out.println("Node "+ membershipView.getNodeIP() + ": " + "Sleeping "+randomTime+"ms");
                Thread.sleep(randomTime);
                unicastService.sendUnicastMessage(msg.mView.getMembershipPort(), msg.mView.getNodeIP(), new MembershipMessage(membershipView, membershipLog,MembershipMessageType.JOIN_RESPONSE));
                System.out.println("Node "+ membershipView.getNodeIP() + ": " + "Sent unicast response");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            membershipStoragePipe.add(new PipeMessage(PipeMessageType.JOIN,msg.mView));
        }
        membershipLog.checkUpdated(msg.mLog,nodeHashes);
    }

    @Override
    public void run() {
        // Starts listening for membership message
        unicastService.startUnicastReceiver(this.membershipView.getMembershipPort());
        
        System.out.println("Node "+ membershipView.getNodeIP() + ": " +"Sent join message");

        int tries=0;

        // Sends join message (includes MembershipView and MembershipLog)
        while(unicastService.getNumberOfObjectsReceived()!=3&&tries!=3){
            try {
                multicastService.sendMulticastMessage(new MembershipMessage(membershipView, membershipLog,MembershipMessageType.JOIN));
                System.out.println("Node "+ membershipView.getNodeIP() + ": " +"Try " + tries);
                Thread.sleep(2000);
                tries++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
        System.out.println("Node "+ membershipView.getNodeIP() + ": Out of the loop");
        if(unicastService.getNumberOfObjectsReceived()>0){
            while(unicastService.getNumberOfObjectsReceived()!=0){
                
                handleMembershipMessage(unicastService.getLastObjectReceived());
            }
        }
        multicastService.startMulticastReceiver();
        System.out.println("Node "+ membershipView.getNodeIP() + ": " +"Receiving on multicast");
        if(membershipLog.isUpToDate()||membershipLog.getActiveNodeCount()<3){
            multicastService.sendPeriodicMulticastMessage(new MembershipMessage(membershipView, membershipLog,MembershipMessageType.PERIODIC), 1000);
        }
        
        while(keepRunning()){
            while(multicastService.getReceiverMessageSize()!=0){
                //System.out.println("Node "+ membershipView.getNodeIP() + ": " + "Received multicast msg");
                MembershipMessage msg = multicastService.getLastMulticastReceiverMessage();
                handleMembershipMessage(msg);
                if(!membershipLog.isUpToDate()){
                    multicastService.pausePeriodicMulticastSender();
                }
                if(multicastService.serviceIsPaused()){
                    if(membershipLog.isUpToDate()){
                        multicastService.resumePeriodicMulticastSender();
                    }
                }
                
                multicastService.updatePeriodicMessage(new MembershipMessage(membershipView, membershipLog,MembershipMessageType.PERIODIC));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        unicastService.stopUnicastReceiver();

        multicastService.stopMulticastReceiver();

        multicastService.stopPeriodicMulticastSender();

        membershipView.increaseMembershipCount();

        membershipView.saveMembershipInfo();

        membershipLog.updateNodeView(membershipView,nodeHashes);

        multicastService.sendMulticastMessage(new MembershipMessage(membershipView, membershipLog,MembershipMessageType.LEAVE));

    }
    
}
