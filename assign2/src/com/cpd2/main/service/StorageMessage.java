package com.cpd2.main.service;

import java.io.Serializable;

enum StorageMessageType {
    GET,
    PUT,
    DELETE
}

public class StorageMessage implements Serializable {

    StorageMessageType type;

    public StorageMessage(StorageMessageType type) {
        this.type = type;
    }
}
