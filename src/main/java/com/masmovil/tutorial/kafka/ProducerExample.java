package com.masmovil.tutorial.kafka;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class ProducerExample {

    public static void main(final String[] args) {

        final Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        // without key
        ProducerRecord<String, String> record = new ProducerRecord<>("ragnarok", "record2");

        producer.send(record, (m, e) -> {
            if (e != null) {
                e.printStackTrace();
            } else {
                System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
            }
        });

        producer.flush();
        producer.close();
    }
}
