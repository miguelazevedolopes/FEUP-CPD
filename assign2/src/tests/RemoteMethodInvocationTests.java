package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.junit.jupiter.api.Test;

import com.cpd2.main.rmi.KeyValueStore;
import com.cpd2.main.service.Node;
import com.cpd2.main.service.Utils;

public class RemoteMethodInvocationTests {



    RemoteMethodInvocationTests() throws RemoteException, UnknownHostException, MalformedURLException{

        KeyValueStore nodeServer = new Node("225.0.0.1",7373,"127.0.0.1",7023);
        LocateRegistry.createRegistry(1900);
        Naming.rebind("rmi://127.0.0.1:1900/"+Utils.generateHash("127.0.0.1"),nodeServer);
    }
    
    @Test
    public void testJoinAndLeave() throws MalformedURLException, RemoteException, NotBoundException, InterruptedException{
        KeyValueStore client = (KeyValueStore)Naming.lookup("rmi://127.0.0.1:1900/"+Utils.generateHash("127.0.0.1"));
        client.join();
        Thread.sleep(10000);
        File f= new File(Utils.getRelativePath()+Utils.generateHash("127.0.0.1"));
        assertTrue(f.exists());
        client.leave();
    }

    @Test
    public void testPut() throws MalformedURLException, RemoteException, NotBoundException, InterruptedException{
        KeyValueStore client = (KeyValueStore)Naming.lookup("rmi://127.0.0.1:1900/"+Utils.generateHash("127.0.0.1"));
        client.join();
        Thread.sleep(10000);

        client.put("Hello hello");

        File f= new File(Utils.getRelativePath()+Utils.generateHash("127.0.0.1")+"/"+Utils.generateHash("Hello hello"));

        Thread.sleep(1000);

        assertTrue(f.exists());

        client.leave();
    }

    @Test
    public void testGet() throws MalformedURLException, RemoteException, NotBoundException, InterruptedException{
        KeyValueStore client = (KeyValueStore)Naming.lookup("rmi://127.0.0.1:1900/"+Utils.generateHash("127.0.0.1"));
        client.join();
        Thread.sleep(10000);

        client.put("Hello hello");

        Thread.sleep(1000);

        String response = client.get(Utils.generateHash("Hello hello"));
        
        assertEquals("Hello hello", response);

        client.leave();
    }

    @Test
    public void testDelete() throws MalformedURLException, RemoteException, NotBoundException, InterruptedException{
        KeyValueStore client = (KeyValueStore)Naming.lookup("rmi://127.0.0.1:1900/"+Utils.generateHash("127.0.0.1"));
        client.join();
        Thread.sleep(10000);

        client.put("Hello hello");

        Thread.sleep(1000);

        client.delete(Utils.generateHash("Hello hello"));

        Thread.sleep(1000);

        File f= new File(Utils.getRelativePath()+Utils.generateHash("127.0.0.1")+"/"+Utils.generateHash("Hello hello"));

        assertFalse(f.exists());

        client.leave();
    }





}
