package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.cpd2.main.service.communication.UnicastService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UnicastServiceTests {
    UnicastService<String> unicastService;

    @BeforeEach
    void setUp(){
        unicastService = new UnicastService<String>();
    }

    @Test
    void testSendAndReceive() throws InterruptedException{
        String testMessage="Hello";

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

        Thread.sleep(10000);
        
        unicastService.sendUnicastMessage(7003, "127.0.0.1", testMessage);

        Thread.sleep(1000);

        assertNull(unicastService.getLastObjectReceived());
    }
}
