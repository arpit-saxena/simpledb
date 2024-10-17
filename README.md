# SimpleDB

This is code from following the Database Design and Implementation book by Edward Sciore.
The original code is taken from the zip file available ata https://cs.bc.edu/~sciore/simpledb/
See [SimpleDB's README](./README-simpledb.txt) for details

## Building SimpleDB

These instructions are taken from [this gist](https://gist.github.com/ankithooda/b0d624aec9b3ed2882713d59feba4b11)
and are being copied here

```sh
# Create bin folder to store the compiled .class files
> mkdir -p bin/server
> mkdir -p bin/client

# Compile Server
> javac -d bin/server simpledb/server/*

# Compile the various Client programs

> javac -d bin/client simpleclient/SimpleIJ.java
> javac -d bin/client simpleclient/network/*
> javac -d bin/client simpleclient/embedded/*

# Now the bin/server contains all the class files for server
# and bin/client contains all the class files for running various sample clients


################ Run Server ##################

> cd bin/server/
> java simpledb.server.StartServer


################ Run Clients #################

> cd bin/client

# Create some sample data by running the CreateStudentDB and StudentMajor clients
> java network.CreateStudentDB
> java network.StudentMajor


# Run interactive client
> java SimpleIJ
```

## Running Derby

Download Derby from [Derby website](https://db.apache.org/derby/derby_downloads.html).
I have downloaded Derby 10.17.1.0 which is for Java 21 and later, specifically downloaded
db-derby-10.17.1.0-bin.tar.gz.

Then follow these instructions

```sh
# Extract the tar file and set the folder as DERBY_HOME.
export DERBY_HOME="/home/arpit/codes/simple-db/db-derby-10.17.1.0-bin"

# Set CLASSPATH required for running ij
source db-derby-10.17.1.0-bin/bin/setEmbeddedCP

# Run IJ
java org.apache.derby.tools.ij
```
