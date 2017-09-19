package com.simonwong.cuhkvirtualcampus;

import java.util.Map;

/**
 * Created by simonwong on 13/4/2017.
 */

public class ChatRoomMessage {

    private String message, date;
    private boolean accepter;

    public ChatRoomMessage(){

    }

    public ChatRoomMessage(String message, String date, boolean accepter){
        this.message = message;
        this.date = date;
        this.accepter = accepter;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public boolean getAccepter(){
        return this.accepter;
    }

    public void setAccepter(boolean accepter){
        this.accepter = accepter;
    }
}
