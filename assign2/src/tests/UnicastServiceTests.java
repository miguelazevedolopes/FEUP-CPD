package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cpd2.main.communication.UnicastService;


public class UnicastServiceTests {
    UnicastService unicastService;

    @BeforeEach
    void setUp(){
        unicastService = new UnicastService();
    }

    @Test
    void testSendAndReceive() throws InterruptedException{
        String testMessage="Hello\nMyfriends";

        unicastService.startUnicastReceiver(7001);

        Thread.sleep(500);
        
        unicastService.sendUnicastMessage(7001, "127.0.0.1", testMessage);

        Thread.sleep(1000);

        assertEquals(testMessage,unicastService.getLastObjectReceived());

        unicastService.stopUnicastReceiver();
    }

    @Test
    void testStopReceiveService() throws InterruptedException{

        String testMessage="Hello";

        unicastService.startUnicastReceiver(7003);

        Thread.sleep(1000);

        unicastService.stopUnicastReceiver();

        Thread.sleep(2000);
        
        unicastService.sendUnicastMessage(7003, "127.0.0.1", testMessage);

        Thread.sleep(1000);

        assertNull(unicastService.getLastObjectReceived());
    }
}
