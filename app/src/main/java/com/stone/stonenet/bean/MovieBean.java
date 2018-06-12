package com.stone.stonenet.bean;

import java.io.Serializable;
import java.util.List;

public class MovieBean implements Serializable {
    private String title;
    private List<SubjectsBean> subjects;


    public String getTitle() {
        return title;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }
}
