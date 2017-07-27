package com.example.pankaj.trackpath.storage;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pankaj on 7/26/17.
 */

public class Coordinates implements Serializable{
    int id;
    private String date;
    private String rawdata;
    private LatLng start;
    private LatLng end;
    private ArrayList<LatLng> coordinatelist;

    public Coordinates(){

    }

    public Coordinates(int id, String date, String rawdata, LatLng start, LatLng end, ArrayList<LatLng> ll){
        this.id = id;
        this.date = date;
        this.rawdata = rawdata;
        this.start = start;
        this.end = end;
        this.coordinatelist = ll;
    }

    public Coordinates(int id, String date, String rawdata){
        this.id = id;
        this.date = date;
        this.rawdata = rawdata;;
    }

    public Coordinates( String date, String rawdata){
        this.date = date;
        this.rawdata = rawdata;;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public ArrayList<LatLng> getCoordinatelist() {
        return coordinatelist;
    }

    public void setCoordinatelist(ArrayList<LatLng> coordinatelist) {
        this.coordinatelist = coordinatelist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRawdata() {
        return rawdata;
    }

    public void setRawdata(String rawdata) {
        this.rawdata = rawdata;
    }
}
