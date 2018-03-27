package com.simonwong.cuhkvirtualcampus;

/**
 * Created by deepansh on 11/1/2018.
 */

public class WaterCooler {
    private String Chi, Eng, Location;

    public WaterCooler(String Location, String Chi, String Eng){
        this.Chi = Chi;
        this.Eng = Eng;
        this.Location = Location;
    }

    public WaterCooler(String Chi, String Eng){
        this.Chi = Chi;
        this.Eng = Eng;
    }

    public WaterCooler(String Location){
        this.Location = Location;
    }

    public String getChi(){
        return this.Chi;
    }

    public String getEng(){
        return this.Eng;
    }

    public String getLocation(){
        return this.Location;
    }
}
