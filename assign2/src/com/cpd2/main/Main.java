package com.cpd2.main;

import com.cpd2.main.service.MembershipView;
import com.cpd2.main.service.Node;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        MembershipView mv1=new MembershipView(11,3);
        MembershipView mv2=new MembershipView("/home/miguel/Documents/Faculdade/g01/assign2/storage/11/membership");
        System.out.println(mv2);
    }
}
