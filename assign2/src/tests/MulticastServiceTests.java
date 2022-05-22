package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cpd2.main.service.MulticastService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MulticastServiceTests {

    MulticastService<String> multicastService;

    @BeforeEach
    void setUp(){
        multicastService=new MulticastService<String>("225.0.0.1",7373);
    }

    @Test
    void testSendAndReceive() throws InterruptedException{
        String testMessage="Hello";

        multicastService.startMulticastReceiver();
        
        Thread.sleep(500);

        multicastService.sendMulticastMessage(testMessage);
        
        Thread.sleep(500);

        multicastService.stopMulticastReceiver();

        assertEquals(testMessage,multicastService.getLastMulticastReceiverMessage());
    }

    @Test
    void testStopReceiveService() throws InterruptedException{
        String testMessage="Hello";

        multicastService.startMulticastReceiver();
        
        Thread.sleep(500);

        multicastService.stopMulticastReceiver();

        multicastService.sendMulticastMessage(testMessage);

        assertEquals(0,multicastService.getReceiverMessageSize());
    }

    @Test
    void testSendPeriodicMessages() throws InterruptedException{
        String testMessage="Hello";

        multicastService.startMulticastReceiver();
        
        Thread.sleep(500);

        multicastService.sendPeriodicMulticastMessage(testMessage,1000);
        
        Thread.sleep(2500);

        multicastService.stopMulticastReceiver();

        multicastService.stopPeriodicMulticastSender();

        assertEquals(3,multicastService.getReceiverMessageSize());
    }

    @Test
    void testStopPeriodicSendService() throws InterruptedException{
        String testMessage="Hello";

        multicastService.startMulticastReceiver();
        
        Thread.sleep(500);

        multicastService.sendPeriodicMulticastMessage(testMessage,1000);
        
        Thread.sleep(2500);

        multicastService.stopPeriodicMulticastSender();

        Thread.sleep(2500);

        multicastService.stopMulticastReceiver();

        assertEquals(3,multicastService.getReceiverMessageSize());
    }

    @Test
    void testUpdatePeriodicMessage() throws InterruptedException{
        String testMessage="Hello";

        multicastService.startMulticastReceiver();
        
        Thread.sleep(500);

        multicastService.sendPeriodicMulticastMessage(testMessage,2000);
        
        Thread.sleep(500);

        assertEquals(testMessage,multicastService.getLastMulticastReceiverMessage());

        String newMessage="Goodbye";

        multicastService.updatePeriodicMessage(newMessage);

        Thread.sleep(1500);

        multicastService.stopPeriodicMulticastSender();

        multicastService.stopMulticastReceiver();

        assertEquals(newMessage,multicastService.getLastMulticastReceiverMessage());
    }
}
