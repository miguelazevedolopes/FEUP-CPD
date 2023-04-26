# CPD Projects

## [Assignment 1](./assign1)

Consisted of comparing several matrix multiplication algorithms and analyzing their performance. 

## [Assignment 2](./assign2)

This project involved developing a distributed key-value persistent store for a large cluster. The key-value store partitions the data items among different cluster nodes using consistent-hashing. The service was designed to handle concurrent requests and to tolerate node crashes and message loss.

To implement the data-store nodes, we developed a key-value store interface that provides operations such as put, get, and delete, and a cluster membership interface that allows adding or removing nodes from the cluster.

The service node is invoked by running a Java Store with the address of the IP multicast group used by the membership service, the port number of the IP multicast group, a unique node ID, and the port number used by the storage service.

To keep track of the cluster membership, the storage system implements a distributed membership service and protocol. The membership protocol was designed to keep the membership information up-to-date, even in the presence of node crashes. It requires every node to know every other node in the cluster.

The project also involved implementing a test client to verify the functionality of the key-value store and the membership service. Overall, the project was challenging, but it provided an excellent opportunity to learn about distributed systems and the challenges of implementing them in practice.

Our design is loosely based on Amazon's Dynamo, in that it uses consistent-hashing to partition the key-value pairs among the different nodes.

## Group Members

| Name                               | Student Number | Email             |
| ---------------------------------- | -------------- | ----------------- |
| Afonso Duarte de Carvalho Monteiro | up201907284    | up201907284@up.pt |
| Miguel Lopes                       | up201704590    | up201704590@up.pt |
| Vasco Alves                        | up201808031    | up201808031@up.pt |

## Disclaimer

This repository, and every other repository (I own) named in the format FEUP-COURSENAME on GitHub correspond to the projects I developed during my time as a student at Faculty of Engineering of University of Porto.
