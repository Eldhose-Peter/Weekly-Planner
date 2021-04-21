package com.eldhose.weeklyplanner.ModelClass;


public class Note {

    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;
    private boolean isReminded;
    private boolean isExpanded;
    private String uid;

    public int getColor() {
        return color;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;

    public Note() {
    }

    public Note(String title, String description, String dueDate, boolean isCompleted, boolean isReminded, String uid,int color) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.isReminded = isReminded;
        this.uid = uid;
        this.isExpanded=false;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String  getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isReminded() {
        return isReminded;
    }

    public void setReminded(boolean reminded) {
        isReminded = reminded;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
