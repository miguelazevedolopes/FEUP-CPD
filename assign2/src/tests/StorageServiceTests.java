package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import java.io.File;

import com.cpd2.main.service.Node;
import com.cpd2.main.service.Utils;
import com.cpd2.main.service.communication.UnicastService;
import com.cpd2.main.service.messages.StorageMessage;
import com.cpd2.main.service.messages.enums.StorageMessageType;

import org.junit.jupiter.api.Test;


public class StorageServiceTests {

    UnicastService<StorageMessage> unicastService = new UnicastService<StorageMessage>();

    @Test
    public void testPut() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);

        Thread.sleep(10000);

        unicastService.sendUnicastMessage(7001, "127.0.0.2", new StorageMessage(StorageMessageType.PUT, "Teste teste isto é um teste"));

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(2000);

        assertTrue(file.exists());

        Thread.sleep(1500);

        nodeOne.leave();
    }
    
    @Test
    public void testJoinTransfer() throws InterruptedException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);

        Thread.sleep(10000);

        unicastService.sendUnicastMessage(7001, "127.0.0.2", new StorageMessage(StorageMessageType.PUT, "Teste teste isto é um teste"));

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        Node nodeTwo= new Node("225.0.0.1",7373,"127.0.0.1",7003);

        Thread.sleep(10000);

        assertFalse(file.exists());

        file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));
        
        Thread.sleep(1000);

        assertTrue(file.exists());

        Thread.sleep(2000);

        nodeOne.leave();

        nodeTwo.leave();
    }


}
