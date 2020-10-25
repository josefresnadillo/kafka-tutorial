package com.masmovil.tutorial.kafka;

public enum Topic {
    RAGNAROK("ragnarokTopic");

    private final String name;

    Topic(final String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
