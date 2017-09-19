package com.simonwong.cuhkvirtualcampus;

import java.util.ArrayList;

/**
 * Created by simonwong on 13/10/2016.
 */
public class Location {
    private String index,Eng,Chi,Short_Name;
    private double coor_x,coor_y;
    private int region;
    private String photo;
    private ArrayList<Centre> centre = new ArrayList<Centre>();

    public Location(String index, String Eng, String Chi, String Short_Name, double coor_x, double coor_y, int region){
        this.index = index;
        this.Eng = Eng;
        this.Chi = Chi;
        this.Short_Name = Short_Name;
        this.coor_x = coor_x;
        this.coor_y = coor_y;
        this.region = region;
    }

    public Location(String index, String Eng, String Chi, String Short_Name, double coor_x, double coor_y, int region, String photo, ArrayList<Centre> centre){
        this.index = index;
        this.Eng = Eng;
        this.Chi = Chi;
        this.Short_Name = Short_Name;
        this.coor_x = coor_x;
        this.coor_y = coor_y;
        this.region = region;
        this.photo = photo;
        this.centre = centre;
    }

    public String getIndex(){
        return this.index;
    }

    public  String getEng(){
        return this.Eng;
    }

    public  String getChi(){
        return this.Chi;
    }

    public  String getShort_Name(){
        return this.Short_Name;
    }

    public  double getCoor_x(){
        return this.coor_x;
    }

    public  double getCoor_y(){
        return this.coor_y;
    }

    public double[] getCoor(){
        double[] coor = {coor_x,coor_y};
        return coor;
    }

    public int getRegion(){ return this.region;}

    public String getPhoto(){return this.photo;}

    public ArrayList<Centre> getCentre(){
        return this.centre;
    }
}
