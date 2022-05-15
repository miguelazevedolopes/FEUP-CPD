package com.cpd2.main.service;

/**
 * This interface represents the persistent key-value Store.
 * @param <K> any class K key.
 * @param <V> any class V value.
 */
public interface KeyValueStore<K, V>{

    /**
     * Adds a key-value pair to the store.
     * @param key class K key determined by the interface implementation.
     * @param value class V value determined by the interface implementation.
     */
    void put(K key, V value);


    /**
     * Retrieves the value bound to a key
     * @param key class K key determined by the interface implementation.
     * @return class V value determined by the interface implementation.
     */
    V get(K key);


    /**
     * Deletes a key-value pair from the store.
     * @param key class K key determined by the interface implementation.
     */
    void delete(K key);

}
