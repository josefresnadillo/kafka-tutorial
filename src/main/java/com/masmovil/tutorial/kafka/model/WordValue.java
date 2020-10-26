package com.masmovil.tutorial.kafka.model;

import java.util.Objects;

public class WordValue {
    private String id;
    private String word;
    private String date;


    public WordValue() {
    }

    public WordValue(final String id,
                     final String word,
                     final String date) {
        this.id = id;
        this.word = word;
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordValue that = (WordValue) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(word, that.word) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, date);
    }

    @Override
    public String toString() {
        return "WordValue{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
