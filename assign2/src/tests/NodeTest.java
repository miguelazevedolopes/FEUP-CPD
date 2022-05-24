package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cpd2.main.service.Node;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

public class NodeTest {
    
    @Test
    public void testMembershipService() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,1,"127.0.0.1");

        Thread.sleep(10000);

        assertEquals(1, nodeOne.getMembershipLog().getLogSize());
        
        Node nodeTwo = new Node("225.0.0.1",7373,2,"127.0.0.1");

        Thread.sleep(10000);

        assertEquals(2, nodeOne.getMembershipLog().getLogSize());
        assertEquals(2, nodeTwo.getMembershipLog().getLogSize());

        Node nodeThree = new Node("225.0.0.1",7373,3,"127.0.0.1");

        Thread.sleep(10000);

        assertEquals(3, nodeOne.getMembershipLog().getLogSize());
        assertEquals(3, nodeTwo.getMembershipLog().getLogSize());
        assertEquals(3, nodeThree.getMembershipLog().getLogSize());

        Node nodeFour = new Node("225.0.0.1",7373,4,"127.0.0.1");

        Thread.sleep(5000);

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
    public void testLeaveService() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,5,"127.0.0.1");
        Thread.sleep(10000);  
          
        Node nodeTwo = new Node("225.0.0.1",7373,6,"127.0.0.1");
        Thread.sleep(10000);

        nodeTwo.leave();
        
        Thread.sleep(2000);
        System.out.println(nodeOne.getMembershipLog());
        assertEquals(2, nodeOne.getMembershipLog().getLogSize());
        assertEquals(1, nodeOne.getMembershipLog().getMembershipCount(6));

        nodeOne.leave();

    } 

}
