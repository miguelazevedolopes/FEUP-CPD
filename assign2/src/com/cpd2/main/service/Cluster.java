package com.cpd2.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cluster {

    private final List<Node> nodes;

    public Cluster() {
        this.nodes = new ArrayList<>();
    }

    public Node getNode(String nodeHash) {
        for (Node node : nodes) {
            if (node.getMembershipView().getNodeHash().equals(nodeHash)) {
                return node;
            }
        }
        // TODO: probably change this
        return new Node("error", 0, 0);
    }

    public Node getSuccessor(String nodeHash) {
        for (Node node : nodes) {
            if (node.getMembershipView().getNodeHash().equals(nodeHash)) {
                int index = nodes.indexOf(node);
                if (index == nodes.size() - 1) {
                    return nodes.get(0);
                }
                return nodes.get(index + 1);
            }
        }
        // TODO: probably change this
        return new Node("error", 0, 0);
    }

    public void transferLeave(String nodeHash) {
        Node successor = getSuccessor(nodeHash);
        Map<Object, Object> storage = getNode(nodeHash).getStorage();
        for (Object key : storage.keySet()) {
            successor.put(key, storage.get(key));
        }
    }

    public void receiveJoin(String nodeHash) {
        Node successor = getSuccessor(nodeHash);
        Node newNode = getNode(nodeHash);
        Map<Object, Object> successorStorage = getNode(successor.getMembershipView().getNodeHash()).getStorage();
        for (Object key : successorStorage.keySet()) {
            // TODO: idk about this
            if (Integer.parseInt((String) key) <= Integer.parseInt(nodeHash)) {
                successor.delete(key);
                newNode.put(key, successorStorage.get(key));
            }
        }
    }
}
