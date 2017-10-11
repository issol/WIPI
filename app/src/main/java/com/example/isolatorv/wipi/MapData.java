package com.example.isolatorv.wipi;

/**
 * Created by tlsdm on 2017-09-21.
 */

public class MapData {
    private String name = null;
    private double Latitude = 0.0;
    private double Longtitude = 0.0;
    private String sinpat = null;
    private int post = 0;

    public MapData(String name, double Latitude, double Longtitude, String sinpat){
        this.name = name;
        this.Latitude=Latitude  ;
        this.Longtitude=Longtitude  ;
        this.sinpat=sinpat  ;
    }
    public MapData(String name, double Latitude, double Longtitude, String sinpat,int post){
        this.name = name;
        this.Latitude=Latitude  ;
        this.Longtitude=Longtitude  ;
        this.sinpat=sinpat  ;
        this.post = post;
    }
    public String getName(){
        return name;
    }
    public double getLatitude(){return Latitude;}
    public double getLongtitude(){
        return Longtitude;
    }
    public String getsinpat(){
        return sinpat;
    }
    public int getPost(){return post;}
}
