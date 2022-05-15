package com.cpd2.main.service;


/**
 * This class allows the user to add/remove nodes to the cluster.
 */
public interface ClusterMembership {

    /**
     * Adds a node to the cluster
     */
    void join();


    /**
     * Removes a node from the cluster
     */
    void leave();
}
