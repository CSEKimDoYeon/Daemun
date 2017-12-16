package com.example.kimdoyeon.daemun.Daemun_DB;

import android.graphics.Bitmap;

/**
 * Created by KimDoYeon on 2017-12-05.
 */

public class Events {
    public String event_key;
    public String event_name;
    public String event_date;
    public String event_place;
    public String event_img;
    public String event_imgPath;
    public Bitmap event_bm;

    // 위치값
    public double event_lat;
    public double event_lon;

    public Events() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Events(String event_key, String event_name, String event_date, String event_palce) {
        this.event_key=event_key;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_place = event_palce;
    }
    public Events(String event_key, String event_name, String event_date, String event_palce, String event_img) {
        this.event_key=event_key;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_place = event_palce;
        this.event_img = event_img;
    }

    public Events(String event_key, String event_name, String event_date, String event_palce, Bitmap event_bm) {
        this.event_bm = event_bm;
        this.event_key=event_key;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_place = event_palce;
    }

    public Events(double event_lat, double event_lon, String event_name, String event_place) {
        this.event_place = event_place;
        this.event_name = event_name;
        this.event_lat = event_lat;
        this.event_lon = event_lon;
    }

    public String getEvent_img(){
        return event_img;
    }

    public String getEvent_key() {
        return event_key;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_place() {
        return event_place;
    }


    public void setLat(double lat){
        this.event_lat = lat;
    }
    public void setlon(double lon){
        this.event_lon = lon;
    }

    public double getLat() {
    return event_lat;
    }

    public double getLon() {
        return event_lon;
    }
}