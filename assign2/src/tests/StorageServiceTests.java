package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.cpd2.main.service.Node;
import com.cpd2.main.service.Utils;
import com.cpd2.main.service.communication.UnicastService;
import com.cpd2.main.service.messages.StorageMessage;
import com.cpd2.main.service.messages.enums.StorageMessageType;

import org.junit.jupiter.api.Test;


public class StorageServiceTests {

    UnicastService<StorageMessage> unicastService = new UnicastService<StorageMessage>();

    @Test
    public void testTcpPut() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);
        nodeOne.join();
        Thread.sleep(10000);

        unicastService.sendUnicastMessage(7001, "127.0.0.2", new StorageMessage(StorageMessageType.PUT, "Teste teste isto é um teste"));

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(2000);

        assertTrue(file.exists());

        nodeOne.leave();
    }

    @Test
    public void testPut() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);
        nodeOne.join();
        Thread.sleep(10000);

        nodeOne.put("Teste teste isto é um teste");

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(2000);

        assertTrue(file.exists());

        nodeOne.leave();
    }
    
    @Test
    public void testJoinTransfer() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);
        nodeOne.join();
        Thread.sleep(10000);

        unicastService.sendUnicastMessage(7001, "127.0.0.2", new StorageMessage(StorageMessageType.PUT, "Teste teste isto é um teste"));

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        Node nodeTwo= new Node("225.0.0.1",7373,"127.0.0.1",7003);
        nodeTwo.join();
        Thread.sleep(10000);

        assertFalse(file.exists());

        file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));
        
        Thread.sleep(1000);

        assertTrue(file.exists());

        nodeOne.leave();

        nodeTwo.leave();
    }

    @Test
    public void testPutWrongNode() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);
        nodeOne.join();
        Thread.sleep(10000);

        Node nodeTwo= new Node("225.0.0.1",7373,"127.0.0.1",7003);
        nodeTwo.join();
        Thread.sleep(10000);

        nodeOne.put("Teste teste isto é um teste"); 

        Thread.sleep(1000);

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.2") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        assertFalse(file.exists());

        file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));
        
        assertTrue(file.exists());

        nodeOne.leave();

        nodeTwo.leave();
    }


    @Test
    public void testTcpDelete() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);
        nodeOne.join();
        Thread.sleep(10000);

        unicastService.sendUnicastMessage(7001, "127.0.0.1", new StorageMessage(StorageMessageType.PUT, "Teste teste isto é um teste"));

        Thread.sleep(1000);

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        assertTrue(file.exists());
        
        unicastService.sendUnicastMessage(7001, "127.0.0.1", new StorageMessage(StorageMessageType.DELETE, Utils.generateHash("Teste teste isto é um teste")));

        Thread.sleep(1000);

        assertFalse(file.exists());

        nodeOne.leave();
    }

    @Test
    public void testDelete() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);
        nodeOne.join();
        Thread.sleep(10000);

        nodeOne.put("Teste teste isto é um teste");

        Thread.sleep(1000);

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        assertTrue(file.exists());
        
        nodeOne.delete(Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(1000);

        assertFalse(file.exists());

        nodeOne.leave();
    }

    @Test
    public void testDeleteWrongNode() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.2",7001);
        nodeOne.join();
        Thread.sleep(10000);

        Node nodeTwo= new Node("225.0.0.1",7373,"127.0.0.1",7003);
        nodeTwo.join();
        Thread.sleep(10000);

        nodeTwo.put("Teste teste isto é um teste");

        Thread.sleep(1000);

        nodeOne.delete(Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(1000);

        File file = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + Utils.generateHash("127.0.0.1") + "/" + Utils.generateHash("Teste teste isto é um teste"));

        assertFalse(file.exists());
    }

    @Test
    public void testGet() throws InterruptedException, RemoteException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);
        nodeOne.join();
        Thread.sleep(10000);

        nodeOne.put("Teste teste isto é um teste");

        Thread.sleep(1000);
     
        String response = nodeOne.get(Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(1000);

        assertEquals("Teste teste isto é um teste", response);

        nodeOne.leave();
    }

    @Test
    public void testGetWrongNode() throws InterruptedException, RemoteException, MalformedURLException, UnknownHostException{
        Node nodeOne = new Node("225.0.0.1",7373,"127.0.0.1",7001);

        LocateRegistry.createRegistry(1900);
        Naming.rebind("rmi://127.0.0.1:1900/"+nodeOne.getMembershipView().getNodeHash(),nodeOne);

        nodeOne.join();
        Thread.sleep(10000);

        Node nodeTwo= new Node("225.0.0.1",7373,"127.0.0.2",7003);

        Naming.rebind("rmi://127.0.0.2:1900/"+nodeTwo.getMembershipView().getNodeHash(),nodeTwo);

        nodeTwo.join();
        Thread.sleep(10000);

        nodeOne.put("Teste teste isto é um teste");

        Thread.sleep(1000);
        
        String response = nodeTwo.get(Utils.generateHash("Teste teste isto é um teste"));

        Thread.sleep(10000);

        assertEquals("Teste teste isto é um teste", response);

        nodeOne.leave();
    }
}
