package com.xiaohongxiedaima.demo.kafka.stream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.StateRestoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.acl.WorldGroupImpl;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by liusheng on 18-4-27.
 */
public class WordCountApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WordCountApplication.class);

    /**
     * kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic input1
     * kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic output1
     * kafka-topics.sh --describe --zookeeper localhost:2181
     *
     * kafka-console-producer.sh --broker-list localhost:9092 --topic input1
     * kafka-console-consumer.sh -bootstrap-server localhost:9092 --topic output1
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "WordCountApplication");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("input1");


        KTable<String, Long> wordCounts = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word)
                .count();

        wordCounts.toStream().foreach((key, value) -> System.out.println("key: " + key + " value: " + value));
        wordCounts.toStream().to("output2", Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        LOG.info(topology.describe().toString());

        KafkaStreams streams = new KafkaStreams(topology, config);
        streams.setUncaughtExceptionHandler((t, e) -> LOG.error(t.toString(), e));
        streams.setGlobalStateRestoreListener(new StateRestoreListener() {
            @Override
            public void onRestoreStart(TopicPartition topicPartition, String storeName, long startingOffset, long endingOffset) {
                LOG.info("onRestoreStart,TopicPartition: {}, StoreName: {}, StartingOffset: {}, EndingOffset: {}.",
                        topicPartition,
                        storeName,
                        startingOffset,
                        endingOffset);
            }

            @Override
            public void onBatchRestored(TopicPartition topicPartition, String storeName, long batchEndOffset, long numRestored) {
                LOG.info("onBatchRestored,TopicPartition: {}, StoreName: {}, BatchEndOffset: {}, NumRestored: {}.",
                        topicPartition,
                        storeName,
                        batchEndOffset,
                        numRestored);
            }

            @Override
            public void onRestoreEnd(TopicPartition topicPartition, String storeName, long totalRestored) {
                LOG.info("onBatchRestored,TopicPartition: {}, StoreName: {}, TotalRestored: {}.",
                        topicPartition,
                        storeName,
                        totalRestored);
            }
        });
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> streams.close(), "kafka-stream-shutdown-hook"));
    }

}
