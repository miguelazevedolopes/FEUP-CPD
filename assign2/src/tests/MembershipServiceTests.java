package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cpd2.main.service.Node;

import org.junit.Test;

public class MembershipServiceTests {
    
    
    @Test
    public void testJoinServiceLogCount() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);

        Thread.sleep(10000);

        assertEquals(1, nodeOne.getMembershipLog().getLogSize());
        
        Node nodeTwo = new Node("225.0.0.1",7373,"127.0.0.2",7003);

        Thread.sleep(10000);

        assertEquals(2, nodeOne.getMembershipLog().getLogSize());
        assertEquals(2, nodeTwo.getMembershipLog().getLogSize());

        Node nodeThree = new Node("225.0.0.1",7373,"127.0.0.3",7005);

        Thread.sleep(10000);

        assertEquals(3, nodeOne.getMembershipLog().getLogSize());
        assertEquals(3, nodeTwo.getMembershipLog().getLogSize());
        assertEquals(3, nodeThree.getMembershipLog().getLogSize());

        Node nodeFour = new Node("225.0.0.1",7373,"127.0.0.4",7001);

        Thread.sleep(7000);

        assertEquals(4, nodeOne.getMembershipLog().getLogSize());
        assertEquals(4, nodeTwo.getMembershipLog().getLogSize());
        assertEquals(4, nodeThree.getMembershipLog().getLogSize());
        assertEquals(4, nodeFour.getMembershipLog().getLogSize());

        nodeOne.leave();
        nodeTwo.leave();
        nodeThree.leave();
        nodeFour.leave();
    }

    @Test
    public void testLeaveServiceLogCount() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);
        Thread.sleep(10000);  
          
        Node nodeTwo = new Node("225.0.0.1",7373,"127.0.0.2",7003);
        Thread.sleep(10000);

        nodeTwo.leave();
        
        Thread.sleep(2000);
        System.out.println(nodeOne.getMembershipLog());
        assertEquals(2, nodeOne.getMembershipLog().getLogSize());
        assertEquals(1, nodeOne.getMembershipLog().getMembershipCount("127.0.0.2"));

        nodeOne.leave();

    } 


}
