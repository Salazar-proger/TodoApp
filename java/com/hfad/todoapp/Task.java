package com.hfad.todoapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable {
    private String title;
    private String description;
    private String id;
    private boolean isChecked;
    private Date date;
    private Uri imageUri;

    public Task(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Task(String title, String description, String id, boolean isChecked, Date date) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.isChecked = isChecked;
        this.date = date;
    }

    public Task(String title, String description, String id, boolean isChecked) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.isChecked = isChecked;
    }

    public Task(String title, String description, boolean isChecked, Date date) {
        this.title = title;
        this.description = description;
        this.isChecked = isChecked;
    }

    public Task() {}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
