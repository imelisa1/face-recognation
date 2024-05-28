package com.atharvakale.facerecognition;

import android.graphics.Bitmap;

public class records {
    private String id;
    private Bitmap image;
    private String name;
    private String date;

    public records(String id, Bitmap image, String name, String date) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}