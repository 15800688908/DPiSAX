# DPiSAX
Massively Distributed Indexing of Time Series for Apache Spark


## Abstract 
Distributed Partitioned iSAX (DPiSAX)  is a parallel solution to construct the state of the art iSAX-based index over large sets of time series by making the most of the parallel environment by carefully distributing the work load ([Dpisax: Massively distributed partitioned isax](https://hal-lirmm.ccsd.cnrs.fr/lirmm-01620125/document)).


DPiSAX is based on a sampling phase that allows anticipating the distribution of time series  among the computing nodes. Such anticipation is mandatory for  efficient query processing, since it will allow, later on, to decide which partition contains the time series that actually correspond to the query. DPiSAX  splits the full dataset for distribution into partitions using the partition table constructed at the sampling stage. Then each worker builds an independent iSAX index on its partition, with the iSAX representations having the highest possible cardinalities. Alongside an efficient node splitting policy, it allows to preserve  index tree's load balance and to improve query performance, hence taking full advantage of time series indexing in distributed environments. 

## Architecture

DPiSAX application is implemented with Scala and Apache Spark on top of HDFS.  

DPiSAX stores index sub-trees to HDFS using JSON format. Terminal nodes data are stored to HDFS as text files, identified by the iSAX word of the node. DPiSAX supports both distributed  and centralized storages for the input data.        





## Getting Started
 
### Prerequisites

Resource Name | Resource Description | Supported Version  | Remarks
------------ | ------------- | ------------- | -------------
Oracle Java | The Java Runtime Environment (JRE) is a software package to run Java and Scala applications | 8.0
Apache Hadoop | Hadoop Distributed File System (HDFS™): A distributed file system that provides high-throughput access to application data | v. 2.7.x 
Apache Spark | Large-scale data processing framework | v. 2.1.0 or later 



### Installing 

The code is presented as a Maven-built project. An executable jar with all dependencies can be built with the following command:

`mvn clean package
`

## Running

### Configuration from command line
You can run the application providing settings to driver and executors through command line parameters :

`-Dpath.to.config.value=<value>`

Example:

<pre>

 $SPARK_HOME/bin/spark-submit --class fr.inria.zenith.DPiSAX \
--conf spark.executor.extraJavaOptions="-DtsFilePath=input_path -DqueryFilePath=query_path  -DsampleSize=dec_val -Dpls=boolean -Dthreshold=dec_val -DnumPart=int_val -DwordLength=int_val -DbasicCardSymb=int_val -Dtopk=int_val" \
--conf spark.driver.extraJavaOptions="-DtsFilePath=input_path -DqueryFilePath=query_path  -DsampleSize=dec_val -Dpls=boolean -Dthreshold=dec_val -DnumPart=int_val -DwordLength=int_val -DbasicCardSymb=int_val -Dtopk=int_val" \
/tmp/spark-DPiSAX-1.0-SNAPSHOT-jar-with-dependencies.jar

</pre>


### Configuration from a file

The default configuration is loaded from application.conf file. 
To use your own configuration run the application with: 

`-Dconfig.file=<path_to_file>`


Example:

<pre>

 $SPARK_HOME/bin/spark-submit --class fr.inria.zenith.DPiSAX \
--conf spark.executor.extraJavaOptions="-Dconfig.file=my_config.conf" \
--conf spark.driver.extraJavaOptions="-Dconfig.file=my_config.conf" \
/tmp/spark-DPiSAX-1.0-SNAPSHOT-jar-with-dependencies.jar

</pre>

### Options
 <pre>

    Options:
    
    --maxCardSymb           The maximum cardinality of the SAX representation [Default: 8]
    --wordLength            The length of SAX word [Default: 8]
    --basicCardSymb         The basic cardinality of the SAX representation [Default: 1]
    --threshold             Terminal (leaf) node items threshold [Default: 100]
    --numPart               Number of partitions for parallel data processing [Default: 0]
    --sampleSize            Size of sample dataset [Default: 0.2]
    --topk                  Number of top candidates to return  (nearest neighbours)[Default: 10]
    --tsFilePath            Path to the Time Series input file
    --queryFilePath         Path to a given collection of queries
    --saveDir               Path to the result of index construction in a format, ex.: "/tmp/dpisax_res/", "dpisax_res/"
    --pls                   Optional parallel linear search on a given dataset and queries [Default: true]
    
</pre>


## Datasets 
As an input DPiSAX accepts data in txt, csv and object files. The tool supports distributed or centralized file systems. 

For experimental verification synthetic and real datasets were used.
 
Random Walk dataset generated by [Random Walk Time Series Generator](https://github.com/lev-a/RandomWalk-tsGenerator).  At each time point the generator draws a random number from a Gaussian distribution N(0,1), then adds the value of the last number to the new number.


## License
Apache License Version 2.0
