package com.cpd2.main;

import com.cpd2.main.service.Node;

public class Main {

    public static void main(String[] args) {
        Node node=new Node("225.0.0.1",7373,10);

        node.join();
    }
    
}
