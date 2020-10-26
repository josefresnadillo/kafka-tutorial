package com.masmovil.tutorial.kafka;

import com.masmovil.tutorial.kafka.model.Topic;
import com.masmovil.tutorial.kafka.model.WordValue;
import org.apache.kafka.clients.producer.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class ProducerExample {

    private static final List<String> words = List.of("Plandemia", "Villgates", "Killgates", "Negacionist", "5G",
            "Trump", "China", "Covid", "MiguelBose");

    public static void main(final String[] args) {

        final Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");


        final WordValue wordValue = new WordValue("id",
                words.get(new Random().nextInt(words.size())),
                LocalDateTime.now().toString());

        // ProducerRecord without key
        final Producer<String, WordValue> producer = new KafkaProducer<>(props);
        final ProducerRecord<String, WordValue> record = new ProducerRecord<>(Topic.RAGNAROK_WORDS_STREAM.getTopicName(), wordValue);
        producer.send(record, (m, e) -> {
            if (e != null) {
                e.printStackTrace();
            } else {
                System.out.printf("Produced record with word %s to topic %s partition [%d] @ offset %d%n", wordValue.toString(), m.topic(), m.partition(), m.offset());
            }
        });

        producer.flush();
        producer.close();
    }
}
