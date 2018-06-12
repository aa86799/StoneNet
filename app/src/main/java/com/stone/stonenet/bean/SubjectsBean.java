package com.stone.stonenet.bean;

import java.io.Serializable;

public class SubjectsBean implements Serializable {
    private String title, year, id;

    public SubjectsBean(String title, String year, String id) {
        this.title = title;
        this.year = year;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setId(String id) {
        this.id = id;
    }
}