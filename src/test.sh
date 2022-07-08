#!/bin/sh

# Compiles the source files
javac @sources.txt

# Starts RMI regestry
rmiregistry &

sleep 2

# Creates 3 nodes
java com.cpd2.main.KeyValueStoreServer 1 &
java com.cpd2.main.KeyValueStoreServer 3 &
java com.cpd2.main.KeyValueStoreServer 5 &
java com.cpd2.main.KeyValueStoreServer 7 &


sleep 2

# Join
java com.cpd2.main.TestClient join 127.0.0.1 &

sleep 4

java com.cpd2.main.TestClient join 127.0.0.3 &

sleep 4

java com.cpd2.main.TestClient join 127.0.0.5 &

sleep 4

java com.cpd2.main.TestClient join 127.0.0.7 &

sleep 4

printf "\n"
printf "\n"
printf "\nNode 1: Membership Log\n"
cat storage/12ca17b49af2289436f303e0166030a21e525d266e209267433801a8fd4071a0/membership.txt

printf "\n"
printf "\n"
printf "\nNode 3: Membership Log\n"
cat storage/18dd41c9f2e8e4879a1575fb780514ef33cf6e1f66578c4ae7cca31f49b9f2ed/membership.txt

printf "\n"
printf "\n"
printf "\nNode 5: Membership Log\n"
cat storage/2228ad75817cc1e7a33cc883e66f9cc0a09c6b52df2bb570561cd3fad2b8b3a0/membership.txt

printf "\n"
printf "\n"
printf "\nNode 7: Membership Log\n"
cat storage/a8cd5c83a9c2334c18ed02c82abc57f4c765cc9b1fd599e5586358aab26ff45a/membership.txt
printf "\n"
printf "\n"



# Put resquest sent to Node 1, to store testfile3, that belongs to Node 3 
java com.cpd2.main.TestClient put 127.0.0.1 /home/miguel/Documents/Faculdade/g01/assign2/src/testfile3.txt &

printf "\nPUT request sent\n"



sleep 10

printf "\nNode 1's Files:\n"
ls storage/12ca17b49af2289436f303e0166030a21e525d266e209267433801a8fd4071a0
printf "\n"

printf "\nNode 3's Files:\n"
ls storage/18dd41c9f2e8e4879a1575fb780514ef33cf6e1f66578c4ae7cca31f49b9f2ed
printf "\n"

printf "\nNode 5's Files:\n"
ls storage/2228ad75817cc1e7a33cc883e66f9cc0a09c6b52df2bb570561cd3fad2b8b3a0
printf "\n"

printf "\nNode 7's Files:\n"
ls storage/a8cd5c83a9c2334c18ed02c82abc57f4c765cc9b1fd599e5586358aab26ff45a
printf "\n"


# Leave
java com.cpd2.main.TestClient leave 127.0.0.1

sleep 2

printf "\n"
printf "\n"
printf "\nNode 1: Membership Log\n"
cat storage/12ca17b49af2289436f303e0166030a21e525d266e209267433801a8fd4071a0/membership.txt

printf "\n"
printf "\n"
printf "\nNode 3: Membership Log\n"
cat storage/18dd41c9f2e8e4879a1575fb780514ef33cf6e1f66578c4ae7cca31f49b9f2ed/membership.txt

printf "\n"
printf "\n"
printf "\nNode 5: Membership Log\n"
cat storage/2228ad75817cc1e7a33cc883e66f9cc0a09c6b52df2bb570561cd3fad2b8b3a0/membership.txt
printf "\n"
printf "\n"

sleep 2

java com.cpd2.main.TestClient leave 127.0.0.3

sleep 2

printf "\n"
printf "\n"
printf "\nNode 3: Membership Log\n"
cat storage/18dd41c9f2e8e4879a1575fb780514ef33cf6e1f66578c4ae7cca31f49b9f2ed/membership.txt

printf "\n"
printf "\n"
printf "\nNode 5: Membership Log\n"
cat storage/2228ad75817cc1e7a33cc883e66f9cc0a09c6b52df2bb570561cd3fad2b8b3a0/membership.txt
printf "\n"
printf "\n"

sleep 2

java com.cpd2.main.TestClient leave 127.0.0.5

sleep 2

printf "\n"
printf "\n"
printf "\nNode 5: Membership Log\n"
cat storage/2228ad75817cc1e7a33cc883e66f9cc0a09c6b52df2bb570561cd3fad2b8b3a0/membership.txt
printf "\n"
printf "\n"

sleep 6

java com.cpd2.main.TestClient leave 127.0.0.7

sleep 6





