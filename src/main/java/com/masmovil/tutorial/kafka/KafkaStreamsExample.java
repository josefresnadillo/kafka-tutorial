package com.masmovil.tutorial.kafka;

import com.masmovil.tutorial.kafka.model.Topic;
import com.masmovil.tutorial.kafka.model.WordValue;
import com.masmovil.tutorial.kafka.util.JsonPOJODeserializer;
import com.masmovil.tutorial.kafka.util.JsonPOJOSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaStreamsExample {

    public static void main(final String[] args) {

        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "sum-words-lambda-example");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "sum-words-lambda-example-client");
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000); // 10 seconds

        // Key Serializer and Deserializer
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Value Serializer and Deserializer
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("JsonPOJOClass", WordValue.class);
        final Serializer<WordValue> worldValueSerializer = new JsonPOJOSerializer<>();
        worldValueSerializer.configure(serdeProps, false);
        final Deserializer<WordValue> wordValueDeserializer = new JsonPOJODeserializer<>();
        wordValueDeserializer.configure(serdeProps, false);
        final Serde<WordValue> wordValueSerde = Serdes.serdeFrom(worldValueSerializer, wordValueDeserializer);


        final StreamsBuilder builder = new StreamsBuilder();

        // We assume the input topic contains records where the values are WordValue. We don't care about the key
        final KStream<String, WordValue> input = builder.stream(Topic.RAGNAROK_WORDS_STREAM.getTopicName(),
                Consumed.with(Serdes.String(), wordValueSerde));

        final KTable<String, Long> wordCount = input
                .selectKey((k, v) -> v.getWord())
                .groupByKey()
                .count();

        wordCount.toStream().to(Topic.RAGNAROK_WORDS_COUNT_STREAM_RESULT.getTopicName());

        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);
        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
