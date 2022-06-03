package com.cpd2.main.service.messages;

import com.cpd2.main.service.messages.enums.StorageMessageType;

public class StorageMessage {

    public StorageMessageType type;
    public String contents;

    public StorageMessage(StorageMessageType type,String contents) {
        this.type = type;
        this.contents=contents;
    }

    public StorageMessage(String lastObjectReceived) {
        String[] components =lastObjectReceived.split("\\n-\\n");
        this.type=StorageMessageType.valueOf(components[0]);
        this.contents=components[1];
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type.name());
        stringBuilder.append("\n-\n");
        stringBuilder.append(contents);
        return stringBuilder.toString();
    }

}
