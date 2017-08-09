# Firehose ?
`The firehose API is a steady stream of all available data from a source in realtime –  a giant spigot that delivers data to any number of subscribers at a time. The stream is constant, delivering new, updated data as it happens. The amount of data in the firehose can vary with spikes and lows, but nonetheless, the data continues to flow through the firehose until it is crunched. Once crunched, that data can be visualized, published, graphed; really anything you want to do with it, all in realtime.`

## How we can build firehose ?

```[Data] + [Queue] + [Streaming] = Firehose```

1. Data
    * Weather and temperature data
    * Stock quote prices
    * Public transportation time and location data
    * RSS and blog feeds
    * Multiplayer game player position and state
    * Internet of Things sensor network data

2. Queue server support
   * ActiveMQ
   * Amazon SQS
   * Apache Kafka
   * RabbitMQ

3. Streaming Server support
    * Amazon Kinesis
    * Apache Spark
    * Apache Storm
    * Google DataFlow

### Our use case - Process 1 million stock market data.
* [Bombay stock exchange historical stock price data.](http://www.bseindia.com/markets/equity/EQReports/StockPrcHistori.aspx?scripcode=512289&flag=sp&Submit=G)
* Apache kafka
* Apache Spark

## Kafka Setup

#### Download kafka at your machine [download kafka](https://kafka.apache.org/quickstart)


### Kafka setting up multiple broker server

* First we make a config file for each of the brokers

```
$ cp config/server.properties config/server-1.properties
$ cp config/server.properties config/server-2.properties
$ cp config/server.properties config/server-3.properties
```

* Now edit these new files and set the following properties:

```
config/server-1.properties:
    broker.id=1
    listeners=PLAINTEXT://:9093
    log.dir=/tmp/kafka-logs-1

config/server-2.properties:
    broker.id=2
    listeners=PLAINTEXT://:9094
    log.dir=/tmp/kafka-logs-2

config/server-3.properties:
    broker.id=3
    listeners=PLAINTEXT://:9095
    log.dir=/tmp/kafka-logs-3
```

### Start the zookeeper

```
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```

### Start the server

```
$ bin/kafka-server-start.sh config/server-1.properties

$ bin/kafka-server-start.sh config/server-2.properties

$ bin/kafka-server-start.sh config/server-3.properties
```

### create a new topic with a replication factor of three:

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic test-firehose
```


### Kafka Producer Properties

* Update Kafka producer properties in [application.yml](firehose/src/main/resources/application.yml)

```
bootstrap servers: : localhost:9093,localhost:9094,localhost:9095

```

### Spark consumer Properties

* Update Spark consumer properties in [application.yml](firehose/src/main/resources/application.yml)

```
bootstrap servers: : localhost:9093,localhost:9094,localhost:9095
zookeeper connect: localhost:2181

```

### Stock market data location
* Update data location in [application.yml](firehose/src/main/resources/application.yml)

```
data location: /Users/rohitkumar/Work/code-brusher/firehose/src/main/resources/data
```


### How to run the firehose


```
$  mvn spring-boot:run

```

### See Spark UI

* [spark-web-ui](http://localhost:4040) for the firehose job stats.


## firehose Statistics on my machine - Processed 1 million stock price records.

#### 1 Million Stock Price Record Processed in `4 min and 16` seconds.

* Machine mac-book pro Processor - 2.7 GHz Intel dual Core i5 and Ram - 8 GB 1867 MHz DDR3 and 128 GB SSD.

![Spark one million](firehose/src/main/resources/spark_stats/Spark-1.png "Spark UI")
![Spark scheduling delay](firehose/src/main/resources/spark_stats/spark-2.png "Spark UI")
![Spark Bath status](firehose/src/main/resources/spark_stats/spark-3.png "Spark UI")
![Complete Data Processed](firehose/src/main/resources/spark_stats/spark-4.png "Spark UI")

### Performance Tuning spark streaming.

#### Batch Interval Parameter :

Start with some intuitive batch interval say 5 or 10 seconds.
If your overall processing time < batch interval time, then application is stable.
In my case 15 seconds suited my processing and I got the performance.

```
JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, new Duration(15000));
```

#### ConcurrentJobs Parameter :

By default the number of concurrent jobs is 1 which means at a time only 1 job will be active and till its not finished,
other jobs will be queued up even if the resources are available and idle. However this parameter is intentionally not
documented in Spark docs as sometimes it may cause weird behaviour as Spark Streaming creator Tathagata discussed in
this useful[thread](http://stackoverflow.com/questions/23528006/how-jobs-are-assigned-to-executors-in-spark-streaming).
Tune it accordingly keeping side-effects in mind.

Running concurrent jobs brings down the overall processing time and
scheduling delay even if a batch takes processing time slightly more than batch interval.

In my case :

```
"spark.streaming.concurrentJobs","1" - Scheduling Delay around - 3.43 second and Processing Time - 9.8 Seconds
"spark.streaming.concurrentJobs","2 - Improved - Scheduling Delay - 1 milli-second - 8.8 seconds
```

#### Backpressure Parameter :
Spark gives a very powerful feature called backpressure .
Having this property enabled means spark streaming will tell kafka to slow down rate of sending messages if the processing
time is coming more than batch interval and scheduling delay is increasing. Its helpful in cases like when there is sudden
surge in data flow and is a must have property to have in production to avoid application being over burdened. However this
property should be disabled during development and staging phase otherwise we cannot test the limit of the maximum load our
application can and should handle.

```
set(spark.streaming.backpressure.enabled","true")
```












