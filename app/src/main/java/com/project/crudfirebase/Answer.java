package com.project.crudfirebase;

public class Answer {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private String description;
    private String author;

    public Answer(){

    }

    public Answer(String key, String description, String author) {
        this.key = key;
        this.description = description;
        this.author = author;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
