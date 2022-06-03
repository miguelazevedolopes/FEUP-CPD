package com.cpd2.main.communication;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;


public class MulticastServiceSend extends Thread{
    private DatagramSocket sender=null;
    private byte[] messageToSend;
    InetSocketAddress dest;
    int period=-1;
    private boolean stop=false, updatingMessage=false, pause=false;

    public synchronized void stopService() {
        this.stop = true;
    }

    public synchronized void pauseService() {
        this.pause = true;
    }

    public synchronized void resumeService() {
        this.pause = false;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    public synchronized boolean isPaused(){
        return this.pause;
    }

    public MulticastServiceSend(String multicastAddressString, int multicastPort){
        try {

            // Setting up multicast send
            sender = new DatagramSocket(new InetSocketAddress(0));
            NetworkInterface outgoingIf = NetworkInterface.getByName("lo");
            sender.setOption(StandardSocketOptions.IP_MULTICAST_IF, outgoingIf);
            InetAddress mcastaddr = InetAddress.getByName(multicastAddressString);
            dest = new InetSocketAddress(mcastaddr, multicastPort);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public MulticastServiceSend(String multicastAddressString, int multicastPort, int period){
        try {
            // Setting up multicast send
            sender = new DatagramSocket(new InetSocketAddress(0));
            NetworkInterface outgoingIf = NetworkInterface.getByName("lo");
            sender.setOption(StandardSocketOptions.IP_MULTICAST_IF, outgoingIf);
            InetAddress mcastaddr = InetAddress.getByName(multicastAddressString);
            dest = new InetSocketAddress(mcastaddr, multicastPort);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.period=period;
    }

    public void setMessageToSend(byte[] messageToSend){
        this.updatingMessage=true;
        this.messageToSend=messageToSend;
        this.updatingMessage=false;
    }

    /**
     * Sends a multicast message (msgBytes)
     * @param messageToSend
     */
    private void sendMulticastMessage(){
        
        if(period==-1){
            try{
                DatagramPacket packet = new DatagramPacket(messageToSend, messageToSend.length, dest);
                sender.send(packet);
            }
            catch (Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            while(keepRunning()){
                if(isPaused()) continue;
                try{
                    if(!updatingMessage){
                        DatagramPacket packet = new DatagramPacket(messageToSend, messageToSend.length, dest);
                        sender.send(packet);
                        Thread.sleep(period);
                    }
                }
                catch (Exception e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
    }


    @Override
    public void run() {
        sendMulticastMessage();
    }
}
