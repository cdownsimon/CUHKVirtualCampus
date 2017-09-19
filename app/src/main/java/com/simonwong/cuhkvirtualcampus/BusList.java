package com.simonwong.cuhkvirtualcampus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonwong on 21/2/2017.
 */
public class BusList {
    private List<BusInfo> Buslist = new ArrayList<BusInfo>();

    public BusList(){
        this.addBus();
    }

    private void addBus(){
        this.Buslist.add(new BusInfo("1A",new String[]{"b0","b1","b2","b3","b4","b0"}));
        this.Buslist.add(new BusInfo("1B",new String[]{"b0","b5","b1","b2","b3","b4","b0"}));
        this.Buslist.add(new BusInfo("2", new String[]{"b24", "b1", "b2", "b6", "b7", "b8", "b9", "b3", "b4", "b0"}));
        this.Buslist.add(new BusInfo("3", new String[]{"b10", "b1", "b11", "b6", "b12", "b13", "b14", "b15", "b16", "b17", "b13", "b18", "b3", "b4", "b0"}));
        this.Buslist.add(new BusInfo("4", new String[]{"b10", "b19", "b20", "b14", "b15", "b16", "b17", "b13", "b18", "b8", "b9", "b3", "b4", "b0"}));
        this.Buslist.add(new BusInfo("5", new String[]{"b21", "b1", "b2" ,"b6", "b7", "b8", "b12", "b13", "b14"}));
        this.Buslist.add(new BusInfo("6A", new String[]{"b8", "b9", "b3", "b4", "b24", "b21"}));
        this.Buslist.add(new BusInfo("6B", new String[]{"b14", "b16", "b17", "b8", "b9" ,"b3", "b4", "b24", "b21"}));
        this.Buslist.add(new BusInfo("8", new String[]{"b14", "b16", "b17", "b13", "b18", "b3", "b11", "b22", "b9", "b12", "b13", "b14"}));
    }

    public ArrayList<String> findBus(ArrayList<String> stops){
        ArrayList<String> BusLine = new ArrayList<>();
        for(BusInfo tmp : Buslist) {
            String[] reachable = tmp.getRoute();
            int i = 0;
            boolean start = false;
            for (String stop : reachable) {
                if (!start && stop.equals(stops.get(i))) {
                    start = true;
                }
                if(start){
                    if(i < stops.size() && stop.equals(stops.get(i))){
                        i = i + 1;
                    }else{
                        break;
                    }
                }
            }
            if (i == stops.size()) {
                BusLine.add(tmp.getName());
            }
        }
        return BusLine;
    }

}
