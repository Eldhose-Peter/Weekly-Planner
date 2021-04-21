package com.eldhose.weeklyplanner.ModelClass;

public class SubNote {

    String title;
    boolean isCompleted;
    String docRefid;

    public SubNote() {
    }

    public SubNote(String title, boolean isCompleted, String docRefid) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.docRefid = docRefid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDocRefid() {
        return docRefid;
    }

    public void setDocRefid(String docRefid) {
        this.docRefid = docRefid;
    }
}
