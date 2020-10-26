package com.masmovil.tutorial.kafka;

import com.masmovil.tutorial.kafka.model.Topic;
import com.masmovil.tutorial.kafka.model.WordValue;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerExample {

    public static void main(final String[] args) {

        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, WordValue.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ragnarok-consumer-1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Consumer<String, WordValue> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(Topic.RAGNAROK_WORDS_STREAM.getTopicName()));

        try {
            while (true) {
                ConsumerRecords<String, WordValue> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, WordValue> record : records) {
                    String key = record.key();
                    WordValue value = record.value();
                    System.out.printf("Consumed record with key %s and value %s", key, value);
                }
            }
        } finally {
            consumer.close();
        }
    }
}
