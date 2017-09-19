package com.simonwong.cuhkvirtualcampus;

/**
 * Created by simonwong on 21/2/2017.
 */
public class BusInfo {
    private String name, description;
    private String[] route;

    public BusInfo(String name, String[] route){
        this.name = name;
        this.route = route;
    }

    public String getName(){
        return this.name;
    }

    public String[] getRoute(){
        return this.route;
    }
}
