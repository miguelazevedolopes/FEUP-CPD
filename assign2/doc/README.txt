In order to compile the program simply access the src directory and run the following command in your terminal:

javac @sources.txt

or, alternatively compile each file individually.

After that to invoke a node run 

java com.cpd2.main.KeyValueStoreServer <number of the node>

example:

java com.cpd2.main.KeyValueStoreServer 1, will invoke a node running at local address 127.0.0.1 and with a storage port 7001


To use the test client simply call the TestClient as described in the project guide.

For example:

java com.cpd2.main.TestClient join 127.0.0.1, will make the node at 127.0.0.1 join the cluster