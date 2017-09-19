package com.simonwong.cuhkvirtualcampus;

/**
 * Created by simonwong on 23/3/2017.
 */
public class Centre {
    private String Chi, Eng, Room, Phone, Abbr;

    public Centre(String Room, String Chi, String Eng, String Phone,  String Abbr){
        this.Chi = Chi;
        this.Eng = Eng;
        this.Room = Room;
        this.Phone = Phone;
        this.Abbr = Abbr;
    }

    public Centre(String Room, String Chi, String Eng,  String Phone){
        this.Chi = Chi;
        this.Eng = Eng;
        this.Room = Room;
        this.Phone = Phone;
    }

    public String getChi(){
        return this.Chi;
    }

    public String getEng(){
        return this.Eng;
    }

    public String getRoom(){
        return this.Room;
    }

    public String getPhone(){
        return this.Phone;
    }

    public String getAbbr(){
        return this.Abbr;
    }

}
