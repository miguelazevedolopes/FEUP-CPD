package com.cpd2.main.service;

import java.io.Serializable;

public class UnicastService<T extends Serializable> {
    UnicastServiceReceive<T> receiver = null;

    public void startUnicastReceiver(Integer port){
        receiver= new UnicastServiceReceive<T>(port);
        receiver.start();
    }

    public Integer getNumberOfObjectsReceived(){
        return receiver.numberOfObjectsReceived();
    }
}
