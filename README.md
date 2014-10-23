# jredis

A Java based clone of Redis.

Only a subset commands are implemented right now,

1.  `SET`
2.  `GET`
3.  `SETBIT`
4.  `GETBIT`
6.  `ZCOUNT`
7.  `ZCARD`
8.  `ZRANGE`
9.  `SAVE`
10. `QUIT`

Apart from the above, we cover loading an rdf file during startup as well.

## Usage

### Build

To build from source,

```
$ git clone https://github.com/anoopelias/jredis.git
$ cd jredis
$ mvn clean install
```
This should run all test cases as well as generate a package in `target` folder.

### Run

To run the package, follow the instructions below
```
$ cd target
$ tar -xvf jredis-0.5.4-SNAPSHOT-bin.tar.gz
$ ./start.sh

```

##Why?

* To find out what it takes to write a bit of system level programs.
* To see how different is a Java implementation compared to its C counterpart.
* And most important - to have some fun.

## To Do
* Performance benchmarks against the original
