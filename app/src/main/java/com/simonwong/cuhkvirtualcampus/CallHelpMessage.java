package com.simonwong.cuhkvirtualcampus;

import java.util.Map;

/**
 * Created by simonwong on 10/4/2017.
 */

public class CallHelpMessage {

    private String id;
    private String name;
    private String start;
    private String end;
    private String phone;
    private String message;
    private String coor_x,coor_y;
    private Map<String,String> timestamp;
    private long times;
    private boolean accepted;
    private String key;

    private String accepterPhone, accepterName,  accepterMessage;

    public CallHelpMessage(){

    }

    public CallHelpMessage(String name, String start, String end, String phone, String message, String coor_x, String coor_y, Map<String,String> timestamp, boolean accepted, String key, String accepterName, String accepterPhone, String accepterMessage){
        this.name = name;
        this.start = start;
        this.end = end;
        this.phone = phone;
        this.message = message;
        this.coor_x = coor_x;
        this.coor_y = coor_y;
        this.timestamp = timestamp;
        this.accepted = accepted;
        this.key = key;
        this.accepterName = accepterName;
        this.accepterPhone = accepterPhone;
        this.accepterMessage = accepterMessage;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getStart(){
        return this.start;
    }

    public void setStart(String start){
        this.start = start;
    }

    public String getEnd(){
        return this.end;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getCoor_x(){
        return this.coor_x;
    }

    public void setCoor_x(String coor_x){
        this.coor_x = coor_x;
    }

    public String getCoor_y(){
        return this.coor_y;
    }

    public void setCoor_y(String coor_y){
        this.coor_y = coor_y;
    }

    public Map<String,String> getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(long timestamp){
        this.times = timestamp;
    }

    public boolean getAccepted(){
        return this.accepted;
    }

    public void setAccepted(boolean accepted){
        this.accepted = accepted;
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getAccepterName(){
        return this.accepterName;
    }

    public void setAccepterName(String accepterName){
        this.accepterName = accepterName;
    }

    public String getAccepterPhone(){
        return this.accepterPhone;
    }

    public void setAccepterPhone(String accepterPhone){
        this.accepterPhone = accepterPhone;
    }

    public String getAccepterMessage(){
        return this.accepterMessage;
    }

    public void setAccepterMessage(String accepterMessage){
        this.accepterMessage = accepterMessage;
    }
}
