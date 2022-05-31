package com.cpd2.main.service.messages;

import java.io.Serializable;

import com.cpd2.main.service.messages.enums.StorageMessageType;

public class StorageMessage implements Serializable {

    public StorageMessageType type;
    public String valueToStore;

    public StorageMessage(StorageMessageType type,String valueToStore) {
        this.type = type;
        this.valueToStore=valueToStore;
    }
}
