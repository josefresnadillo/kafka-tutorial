package com.masmovil.tutorial.kafka.model;

public enum Topic {
    RAGNAROK("ragnarokTopic"),
    RAGNAROK_STREAM_RESULT("ragnarokWordCount");

    private final String topicName;

    Topic(final String name){
        this.topicName = name;
    }

    public String getTopicName() {
        return topicName;
    }
}
