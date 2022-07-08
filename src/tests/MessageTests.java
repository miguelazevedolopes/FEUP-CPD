package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.cpd2.main.messages.MembershipMessage;
import com.cpd2.main.service.MembershipLog;
import com.cpd2.main.service.MembershipView;
import com.cpd2.main.service.Utils;

public class MessageTests {


    @Test
    void testMembershipViewString() throws InterruptedException{
        MembershipView mView= new MembershipView("127.0.0.1", 0, 7001);

        assertEquals(mView, new MembershipView(mView.toString()));
    }

    @Test
    void testMembershipLogString() throws InterruptedException{
        MembershipView mView= new MembershipView("127.0.0.1", 0, 7001);

        MembershipLog mLog = new MembershipLog(mView);

        TreeSet<String> nodeHashes= new TreeSet<>();

        for (int i = 2; i < 10; i++) {
            nodeHashes.add(Utils.generateHash(("127.0.0."+Integer.toString(i))));
        }

        for (int i = 2; i < 10; i++) {
            mLog.updateNodeView(new MembershipView(("127.0.0."+Integer.toString(i)), 0, 7000+i), nodeHashes);
        }

        assertEquals(mLog, new MembershipLog(mLog.toString()));
    }
}
