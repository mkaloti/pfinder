package com.example.jbt.placesfinder;

public class places {//places class
    String title, details;
    float distance;
    double lat, lng;


    String image;
    int id;


    public places(int id, String title, String details, String image) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.image = image;
    }


    public places(String image, String title, String details, float distance) {
        this.image = image;
        this.distance = distance;
        this.details = details;
        this.title = title;
    }

    public places(String title, String details, float distance, double lat, double lng, String image) {
        this.title = title;
        this.details = details;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
    }

    public double getLat() {
        return lat;
    }


    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }


    public float getDistance() {
        return distance;
    }


    public String getImage() {
        return image;
    }


}
