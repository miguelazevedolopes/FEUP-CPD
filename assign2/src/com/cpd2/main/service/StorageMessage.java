package com.cpd2.main.service;

import java.io.Serializable;

enum StorageMessageType {
    GET,
    PUT,
    DELETE
}

public class StorageMessage implements Serializable {

    StorageMessageType type;
    String valueToStore;

    public StorageMessage(StorageMessageType type,String valueToStore) {
        this.type = type;
        this.valueToStore=valueToStore;
    }
}
