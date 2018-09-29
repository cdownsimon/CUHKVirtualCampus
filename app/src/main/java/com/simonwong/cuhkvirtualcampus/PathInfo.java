package com.simonwong.cuhkvirtualcampus;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by simonwong on 13/10/2016.
 */

public class PathInfo {
    private String Start,End;
    private int NumOfPhoto;
    private int NumOfCoor;
    private int IsDisabled;
    private ArrayList<String> PhotoId = new ArrayList<String>();
    private double[][] CoorInfo;

    public PathInfo(String Start, String End, int NumOfPhoto,int NumOfCoor, double[][] CoorInfo){
        this.Start = Start;
        this.End = End;
        this.NumOfPhoto = NumOfPhoto;
        this.NumOfCoor = NumOfCoor;
        this.CoorInfo = CoorInfo;

        for(int i=0;i<this.NumOfPhoto;i++) {
            if(i<10){
                PhotoId.add("p"+Start+"_"+End+"_0"+String.valueOf(i)+".png");
            }else{
                PhotoId.add("p"+Start+"_"+End+"_"+String.valueOf(i)+".png");
            }
        }
    }

    public PathInfo(String Start, String End, int NumOfPhoto,int NumOfCoor,int IsDisabled , double[][] CoorInfo){
        this.Start = Start;
        this.End = End;
        this.NumOfPhoto = NumOfPhoto;
        this.NumOfCoor = NumOfCoor;
        this.IsDisabled = IsDisabled;
        this.CoorInfo = CoorInfo;

        for(int i=0;i<this.NumOfPhoto;i++) {
            if(IsDisabled == 1) {
                if (i < 10) {
                    PhotoId.add("pd" + Start + "_d" + End + "_0" + String.valueOf(i));
                } else {
                    PhotoId.add("pd" + Start + "_d" + End + "_" + String.valueOf(i));
                }
            }else{
                if (i < 10) {
                    PhotoId.add("p" + Start + "_" + End + "_0" + String.valueOf(i));
                } else {
                    PhotoId.add("p" + Start + "_" + End + "_" + String.valueOf(i));
                }
            }
        }
    }

    public String getFirstPhotoId(){
        try {
            return this.PhotoId.get(0);
        }catch (Exception e){

        }
        return "";
    }

    public String getLastPhotoId(){
        return this.PhotoId.get(this.PhotoId.size()-1);
    }

    public ArrayList<String> getPhotoId(){
        return this.PhotoId;
    }

    public ArrayList<String> getPhotoIdExceptLast(){
        ArrayList<String> tmp = this.PhotoId;
        tmp.remove(tmp.size()-1);
        return tmp;
    }

    public String getStart(){
        return this.Start;
    }

    public String getEnd(){
        return this.End;
    }

    public int getNumOfPhoto(){return this.NumOfPhoto;}

    public double[][] getCoorInfo(){return this.CoorInfo;}

    public int getNumOfCoor(){return this.NumOfCoor;}

    public void setPhotoId(ArrayList<String> PhotoId){
        this.PhotoId = PhotoId;
    }

}
