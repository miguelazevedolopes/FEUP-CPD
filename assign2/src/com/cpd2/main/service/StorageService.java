package com.cpd2.main.service;

public class StorageService extends Thread{

    private UnicastService<MembershipMessage> unicastService;

    public StorageService(){
        // Setting up unicast communication
        unicastService = new UnicastService<MembershipMessage>();

        
    }
    
    
}

    
