package com.project.crudfirebase;

public class Question {
    private String id;
    private String category;
    private String description;
    private String views;
    private String title;
    private String author;
    private String key;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public Question() {
    }

    public Question(String category, String description, String id, String author, String views) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.author = author;
        this.views = views;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
