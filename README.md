# Distributed Key-Value Persistent Store for a Large Cluster

This was the second project developed for the Parallel and Distributed Computation class at FEUP. The projects specification can be accessed [here](docs/Project_Specification.pdf).

Although the project's aim was to create a distributed storage service, it was developed to be run locally, since it was the only way to test it efficiently.

## Compile Instructions

In order to compile the program simply access the src directory and run the following command in your terminal:

```javac @sources.txt```

or, alternatively, compile each file individually.

## Node Invocation

To invoke a node, first start the Java RMI registry: 

```
\\ on Ubuntu
rmiregistry &

\\ on Windows
start rmiregistry 
```

java com.cpd2.main.KeyValueStoreServer <number of the node>

example:

java com.cpd2.main.KeyValueStoreServer 1, will invoke a node running at local address 127.0.0.1 and with a storage port 7001

## Usage / Test Client

To use the test client simply call the TestClient as described in the project guide.

For example:

java com.cpd2.main.TestClient join 127.0.0.1, will make the node at 127.0.0.1 join the cluster.


## Testing

In order for all the tests to pass, first start the RMI registry (see instructions in the Node Invocation section).