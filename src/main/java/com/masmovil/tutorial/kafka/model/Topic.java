package com.masmovil.tutorial.kafka.model;

public enum Topic {
    RAGNAROK_WORDS_STREAM("ragnarokTopic"),
    RAGNAROK_WORDS_COUNT_STREAM_RESULT("ragnarokWordCount");

    private final String topicName;

    Topic(final String name){
        this.topicName = name;
    }

    public String getTopicName() {
        return topicName;
    }
}
