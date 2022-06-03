package com.cpd2.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.cpd2.main.rmi.KeyValueStore;
import com.cpd2.main.service.Utils;

public class TestClient {
    private static String ipAddress;
    private static String port;

    public static void main(String[] args) {
        if(args.length==1){
            if(args[0].equals("help")){
                System.out.println("\nThe test client should be invoked as follows,\n");
                System.out.println("\t $ java TestClient <node_ap> <operation> [<opnd>]\n");
                System.out.println("where:\n");
                System.out.println("<node_ap> is the node's access point. This depends on the implementation.If the service uses UDP or TCP, the format of the access point must be <IP address>:<port number>, where <IP address> and <port number> are respectively the IP address and the port number being used by the node. If the service uses RMI, this must be the IP address and the name of the remote object providing the service.\n");
                System.out.println("<operation> is the string specifying the operation the node must execute. It can be either a key-value operation, i.e. \"put\", \"get\" or \"delete\", or a membership operation, i.e. \"join\" or \"leave\"\n");
                System.out.println("<opnd> is the argument of the operation. It is used only for key-value operations. In the case of:\n");
                System.out.println("\t- put, is the file pathname of the file with the value to add");
                System.out.println("\t- otherwise (get or delete) is the string of hexadecimal symbols encoding the sha-256 key returned by put, as described in the next section.");
            }else System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");
        }
        if(args.length==0 || args.length>3){
            System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");
        }
        else if (args.length==2){
            if(parseAccessPoint(args[1])==false){
                System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");
                return;
            }
            if(args[0].equals("join")){
                KeyValueStore client;
                try {
                    client = (KeyValueStore)Naming.lookup("rmi://"+ipAddress+":1900"+"/"+ Utils.generateHash(ipAddress));
                    client.join();
                } catch (MalformedURLException | RemoteException | NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            else if(args[0].equals("leave")){
                KeyValueStore client;
                try {
                    client = (KeyValueStore)Naming.lookup("rmi://"+ipAddress+":1900"+"/"+ Utils.generateHash(ipAddress));
                    client.leave();
                } catch (MalformedURLException | RemoteException | NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");
        }
        else if (args.length==3){
            if(parseAccessPoint(args[1])==false){
                System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");
                return;
            }
            if(args[0].equals("put")){
                KeyValueStore client;
                try {
                    client = (KeyValueStore)Naming.lookup("rmi://"+ipAddress+":1900"+"/"+ Utils.generateHash(ipAddress));
                    client.put(getFileFromPath(args[2]));
                    System.out.println(Utils.generateHash(ipAddress)); 
                } catch (MalformedURLException | RemoteException | NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            else if(args[0].equals("get")){
                KeyValueStore client;
                try {
                    client = (KeyValueStore)Naming.lookup("rmi://"+ipAddress+":1900"+"/"+ Utils.generateHash(ipAddress));
                    String fileContents= client.get(args[2]);
                    reconstructFile(fileContents);
                } catch (MalformedURLException | RemoteException | NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(args[0].equals("delete")){
                KeyValueStore client;
                try {
                    client = (KeyValueStore)Naming.lookup("rmi://"+ipAddress+":1900"+"/"+ Utils.generateHash(ipAddress));
                    client.delete(args[2]);
                } catch (MalformedURLException | RemoteException | NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else System.out.println("Invalid operation. Use 'java TestClient help' for a guide on how to use this program.");

        }
    }

    private static void reconstructFile(String fileContents) {
        var stringArray= fileContents.split("\\n",2);
        File f = new File(new File("").getAbsolutePath(), stringArray[0]);

        FileWriter myWriter=null;
        try {
            f.createNewFile();
            myWriter = new FileWriter(f.getAbsolutePath());
            myWriter.write(stringArray[1]);
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String getFileFromPath(String string) {
        File f = new File(string);
        StringBuilder str= new StringBuilder();
        str.append(f.getName());
        str.append("\n");
        Path filePath = Path.of(f.getAbsolutePath());
        try {
            str.append(Files.readString(filePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str.toString();
    }

    private static boolean parseAccessPoint(String string) {
        ipAddress=string;
        return true;
    }
}
