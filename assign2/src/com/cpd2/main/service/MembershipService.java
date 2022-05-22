package com.cpd2.main.service;

public class MembershipService extends Thread{

    private MulticastService<MembershipMessage> multicastService;
    private UnicastService<MembershipMessage> unicastService;
    private MembershipView membershipView;
    private MembershipLog membershipLog;
    private boolean stop =false;

    public MembershipService(String multicastAddressString, Integer multicastPort, Integer nodeID){
        // Setting up multicast communication
        multicastService=new MulticastService<MembershipMessage>(multicastAddressString,multicastPort);

        membershipView = new MembershipView(nodeID, 0);

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

    private void sendMulticastMembershipMessage(MessageType type){
        MembershipMessage message = new MembershipMessage(membershipView, membershipLog,type);
        multicastService.sendMulticastMessage(message);
    }

    private void handleMembershipMessage(MembershipMessage msg){
        if(msg.type==MessageType.JOIN){
            /*
            *
            * Verificar aqui se os logs estão up to date. Se nao tiverem, nao envia.
            *
            */
            
            Integer randomTime=(int) (Math.random() * (3000 - 100 + 1) + 100);  
            try {
                Thread.sleep(randomTime);
                unicastService.sendUnicastMessage(msg.mView.port, msg.mView.ipAddress, new MembershipMessage(membershipView, membershipLog,MessageType.JOIN_RESPONSE));
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
        
        System.out.println("Sent join message");

        int tries=0;

        // Sends join message (includes MembershipView and MembershipLog)
        while(unicastService.getNumberOfObjectsReceived()!=3&&tries!=3){
            try {
                sendMulticastMembershipMessage(MessageType.JOIN);
                System.out.println("Try");
                Thread.sleep(2000);
                tries++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
        
        if(unicastService.getNumberOfObjectsReceived()>0){
            for (MembershipMessage msg : unicastService.getObjectReceived()) {
                handleMembershipMessage(msg);
            }
        }
        System.out.println("No response. Must be the first node");
        multicastService.startMulticastReceiver();
        System.out.println("Receiving on multicast");
        multicastService.sendPeriodicMulticastMessage(new MembershipMessage(membershipView, membershipLog,MessageType.PERIODIC), 1000);
        while(keepRunning()){
            if(multicastService.getReceiverMessageSize()!=0){
                MembershipMessage msg = multicastService.getLastMulticastReceiverMessage();
                handleMembershipMessage(msg);
            }
        }
    }
    
}
