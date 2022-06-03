package com.cpd2.main.service.messages;
import com.cpd2.main.service.MembershipLog;
import com.cpd2.main.service.MembershipView;
import com.cpd2.main.service.messages.enums.MembershipMessageType;

public class MembershipMessage {
    public MembershipView mView;
    public MembershipLog mLog;
    public MembershipMessageType type;
    
    public MembershipMessage(MembershipView mv,MembershipLog ml, MembershipMessageType type){
        this.mView=mv;
        this.mLog=ml.copy();
        this.type=type;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type.name());
        stringBuilder.append("\n-\n");
        stringBuilder.append(mView.toString());
        stringBuilder.append("\n-\n");
        stringBuilder.append(mLog.toString());
        return stringBuilder.toString();
    }

    public MembershipMessage(String lastObjectReceived) {
        String[] components =lastObjectReceived.split("\\n-\\n");
        this.type=MembershipMessageType.valueOf(components[0]);
        this.mView=new MembershipView(components[1]);
        this.mLog=new MembershipLog(components[2]);
    }
    
}
