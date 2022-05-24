package com.cpd2.main.service;

public class MembershipService extends Thread{

    private MulticastService<MembershipMessage> multicastService;
    private UnicastService<MembershipMessage> unicastService;
    private MembershipView membershipView;
    private MembershipLog membershipLog;
    private boolean stop =false;

    public MembershipService(String multicastAddressString, int multicastPort, int nodeID,String nodeIpAddress){
        // Setting up multicast communication
        multicastService=new MulticastService<MembershipMessage>(multicastAddressString,multicastPort);

        membershipView = new MembershipView(nodeID, 0,nodeIpAddress);

        membershipLog = new MembershipLog(nodeID,0);

        // Setting up unicast communication
        unicastService = new UnicastService<MembershipMessage>();
    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    public MembershipLog getMembershipLog(){
        return membershipLog;
    }

    public MembershipView getMembershipView(){
        return membershipView;
    }

    private void handleMembershipMessage(MembershipMessage msg){
        if(msg.mView.nodeID==membershipView.nodeID) return;
        if(msg.type==MessageType.JOIN){
            /*
            *
            * Verificar aqui se os logs estão up to date. Se nao tiverem, nao envia.
            *
            */
            
            int randomTime=(int) (Math.random() * (1000 - 10 + 1) + 10);  
            try {
                System.out.println("Node "+ membershipView.nodeID + ": " + "Sleeping "+randomTime+"ms");
                Thread.sleep(randomTime);
                unicastService.sendUnicastMessage(msg.mView.port, msg.mView.ipAddress, new MembershipMessage(membershipView, membershipLog,MessageType.JOIN_RESPONSE));
                System.out.println("Node "+ membershipView.nodeID + ": " + "Sent unicast response");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        membershipLog.checkUpdated(msg.mLog);
    }

    @Override
    public void run() {
        // Starts listening for membership message
        unicastService.startUnicastReceiver(7000+this.membershipView.nodeID);
        
        System.out.println("Node "+ membershipView.nodeID + ": " +"Sent join message");

        int tries=0;

        // Sends join message (includes MembershipView and MembershipLog)
        while(unicastService.getNumberOfObjectsReceived()!=3&&tries!=3){
            try {
                multicastService.sendMulticastMessage(new MembershipMessage(membershipView, membershipLog,MessageType.JOIN));
                System.out.println("Node "+ membershipView.nodeID + ": " +"Try " + tries);
                Thread.sleep(2000);
                tries++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
        System.out.println("Node "+ membershipView.nodeID + ": Out of the loop");
        if(unicastService.getNumberOfObjectsReceived()>0){
            while(unicastService.getNumberOfObjectsReceived()!=0){
                
                handleMembershipMessage(unicastService.getLastObjectReceived());
            }
        }
        multicastService.startMulticastReceiver();
        System.out.println("Node "+ membershipView.nodeID + ": " +"Receiving on multicast");
        multicastService.sendPeriodicMulticastMessage(new MembershipMessage(membershipView, membershipLog,MessageType.PERIODIC), 1000);
        while(keepRunning()){
            while(multicastService.getReceiverMessageSize()!=0){
                System.out.println("Node "+ membershipView.nodeID + ": " + "Received multicast msg");
                MembershipMessage msg = multicastService.getLastMulticastReceiverMessage();
                handleMembershipMessage(msg);
                multicastService.updatePeriodicMessage(new MembershipMessage(membershipView, membershipLog,MessageType.PERIODIC));
            }
            // try {
            //     Thread.sleep(500);
            // } catch (InterruptedException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
        }

        unicastService.stopUnicastReceiver();

        multicastService.stopMulticastReceiver();

        multicastService.stopPeriodicMulticastSender();

        membershipView.increaseMembershipCount();

        membershipLog.updateNodeView(membershipView.nodeID, membershipView.membershipCount);

        multicastService.sendMulticastMessage(new MembershipMessage(membershipView, membershipLog,MessageType.LEAVE));

    }
    
}
