/*Disabled request of current location*/

package com.simonwong.cuhkvirtualcampus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;


import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathInfoActivity extends AppCompatActivity{

    //Dijkstra find_path = new Dijkstra();

    ShortestPathList find_path = new ShortestPathList();

    String shortest_path;
    String[] location_order;
    List<Location> Locations = new ArrayList<Location>();

    String start,end,lang,startIdx,endIdx;
    String bus;
    String activity;
    LocationList location_list = new LocationList();
    PathList path_list = new PathList();
    BusList bus_list = new BusList();

    ArrayList<PathInfo> RequiredPath = new ArrayList<PathInfo>();
    final ArrayList<String> LocationChi = new ArrayList<String>();
    final ArrayList<String> LocationEng = new ArrayList<String>();
    final ArrayList<String> NumOfPhotoOfEachPath = new ArrayList<String>();
    int Progress;

    ListView PathInfoList;
    ImageButton StreetViewButton,HomeButton,BackListButton,MapButton;

    double[] CurrentLocation = new double[2];
    LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged (android.location.Location location){
            CurrentLocation[0] = location.getLatitude();
            CurrentLocation[1] = location.getLongitude();
        }

        @Override
        public void onProviderDisabled (String provider){
            Log.d("Latitude", "disable");
        }

        @Override
        public void onProviderEnabled (String provider){
            Log.d("Latitude", "enable");
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){
            Log.d("Latitude", "status");
        }
    };
    android.location.Location location = null;

    ArrayList<String> BusStops = new ArrayList<>();
    ArrayList<String> ReachableBus = new ArrayList<>();
    String ReachableChi="你可乘搭",ReachableEng="You can take ";
    int FirstBusStop = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_info);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("PathInfofirstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(PathInfoActivity.this, FrontIntroActivity.class);

                    i.putExtra("LaunchFrom", "RouteInfo");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("PathInfofirstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        //handling the location request
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            //CurrentLocation[0] = location.getLatitude();
            //CurrentLocation[1] = location.getLongitude();
        }

        Bundle message = getIntent().getExtras();
        start = message.getString("from");
        end = message.getString("to");
        lang = message.getString("lang");
        activity = message.getString("activity");

        try{
            bus = message.getString("bus");
        }catch (Exception ex){
            System.out.println(ex);
        }

        try{
            Progress = getIntent().getIntExtra("Progress",0);
        }catch (Exception ex){
            System.out.println(ex);
        };

        StreetViewButton = (ImageButton) findViewById(R.id.StreetViewButton);
        HomeButton = (ImageButton) findViewById(R.id.HomeButton);
        BackListButton = (ImageButton) findViewById(R.id.BackListButton);
        MapButton = (ImageButton) findViewById(R.id.MapButton);
        PathInfoList = (ListView) findViewById(R.id.PathInfoList);

        if(bus.equals("true")){
            location_list = null;
            location_list = new LocationList("true");
            path_list = new PathList("true");
        }else if(bus.equals("disabled")){
            path_list = new PathList("disabled");
        }

        System.out.println("Start: "+start);
        if(start.equals("Current Location") || start.equals("目前位置")){
            startIdx = findNearestLocationIndex(CurrentLocation);
            System.out.println("Start Index: "+startIdx);
            Locations.add(new Location("999","Current Location","目前位置","cur",CurrentLocation[0],CurrentLocation[1],99));
        }else {
            startIdx = findLocationIndex(start);
        }
        endIdx = findLocationIndex(end);

        //if bus is true, load the information including bus stops
        if(bus.equals("true")) {
            shortest_path = find_path.shortestPathBus(startIdx, endIdx);
        }else if(bus.equals("disabled")){
            shortest_path = find_path.shortestPathDisabled(startIdx, endIdx);
        }else{
            shortest_path = find_path.shortestPath(startIdx, endIdx);
        }
        find_path = null;

        location_order = shortest_path.split(",");

        //Print the shortest parth result
        for(String tmp : location_order){System.out.println(tmp);}

        //Remove the header of each bus stop, i.e. 1A.b0/1B.b0...
        ArrayList<String> location_order_tmp = new ArrayList<String>();

        String[] point;

        for(String tmp: location_order){
            point = tmp.split("\\.");
            if(Arrays.asList(point).size() > 1){
                location_order_tmp.add(point[1]);
            }else{
                location_order_tmp.add(point[0]);
            }
        }

        location_order = location_order_tmp.toArray(new String[location_order_tmp.size()]);

        //Find the path between two points
        for(int i=0;i<location_order.length-1;i++){
            Log.i("finding path index",location_order[i]+", "+location_order[i + 1]);
            RequiredPath.add(path_list.getPath(location_order[i], location_order[i + 1]));
        }

        //find the suitable bus
        if(bus.equals("true")) {
            for (int i = 0; i < Arrays.asList(location_order).size(); i++) {
                if (Arrays.asList(location_order).get(i).matches("b" + "(.*)")) {
                    BusStops.add(Arrays.asList(location_order).get(i));
                    if (FirstBusStop < 0) {
                        FirstBusStop = i;
                        if(start.equals("Current Location")){
                            FirstBusStop += 1;
                        }
                    }
                }
            }
            if(BusStops.size() > 0) {
                ReachableBus = bus_list.findBus(BusStops);
            }
            if (ReachableBus.size() > 0) {
                for (int i = 0; i < ReachableBus.size() - 1; i++) {
                    ReachableChi = ReachableChi + ReachableBus.get(i) + ", ";
                    ReachableEng = ReachableEng + ReachableBus.get(i) + ", ";
                }
                ReachableChi = ReachableChi + ReachableBus.get(ReachableBus.size() - 1);
                ReachableEng = ReachableEng + ReachableBus.get(ReachableBus.size() - 1);
            }
        }

        //prepare the name of the locations along the path
        for(int i=0;i<location_order.length;i++){
            LocationChi.add(location_list.getLocation(findLoctionNumber(location_order[i])).getChi());
            LocationEng.add(location_list.getLocation(findLoctionNumber(location_order[i])).getEng());
            Locations.add(location_list.getLocation(findLoctionNumber(location_order[i])));
        }

        //prepare the PhotoId array to pass
        final ArrayList<String> PhotoSerisId = new ArrayList<String>();

        PathInfo tmp;
        for(int i=0;i<RequiredPath.size()-1;i++){
            tmp = RequiredPath.get(i);
            if(!tmp.getStart().matches("b" + "(.*)") && tmp.getEnd().matches("b" + "(.*)")){
                PhotoSerisId.addAll(tmp.getPhotoId());
                NumOfPhotoOfEachPath.add(String.valueOf(PhotoSerisId.size()-1));
            }else {
                PhotoSerisId.addAll(tmp.getPhotoIdExceptLast());
                NumOfPhotoOfEachPath.add(String.valueOf(PhotoSerisId.size()));
            }
        }
        tmp = RequiredPath.get(RequiredPath.size() - 1);
        PhotoSerisId.addAll(tmp.getPhotoId());

        ArrayAdapter<Location> adapter = new MyListAdapter();
        PathInfoList.setAdapter(adapter);

        StreetViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StreetViewActivity.class);

                intent.putExtra("from", start);
                intent.putExtra("to", end);
                intent.putExtra("PhotoId", PhotoSerisId);
                intent.putExtra("LocationChi", LocationChi);
                intent.putExtra("LocationEng", LocationEng);
                intent.putExtra("NumOfPhotoOfEachPath", NumOfPhotoOfEachPath);
                intent.putExtra("Progress", Progress);
                intent.putExtra("lang", lang);
                intent.putExtra("bus",bus);
                intent.putExtra("activity",activity);

                /*Disabled, will be enable later.
                Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                toast.show();*/

                startActivityForResult(intent, 0);
                finish();
            }
        });

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), FrontActivity.class);

                startActivityForResult(intent, 0);
                finish();
            }
        });

        BackListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(activity.equals("main")) {
                    intent = new Intent(v.getContext(), MainActivity.class);
                }else{
                    intent = new Intent(v.getContext(), DepartmentActivity.class);
                }

                startActivityForResult(intent, 0);
                finish();
            }
        });

        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);

                intent.putExtra("from", start);
                intent.putExtra("to", end);
                intent.putExtra("Progress", Progress);
                intent.putExtra("lang", lang);
                intent.putExtra("bus",bus);
                intent.putExtra("activity",activity);

                startActivityForResult(intent, 0);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private int findLoctionNumber(String input){
        int index=-1;
        for(int i=0;i<location_list.getLength();i++){
            if(input.equals(location_list.getLocation(i).getIndex())){
                index = i;
                break;
            }
        }
        return index;
    }

    private String findLocationIndex(String input){
        String index="";
        for(int i=0;i<location_list.getLength();i++){
            if(input.equals(location_list.getLocation(i).getIndex()) || input.equals(location_list.getLocation(i).getEng()) || input.equals(location_list.getLocation(i).getChi()) || input.equals(location_list.getLocation(i).getShort_Name())){
                index = location_list.getLocation(i).getIndex();
                break;
            }
        }
        return index;
    }

    private String findNearestLocationIndex(double[] location){
        String index="";
        double min = 99999999;
        for(int i=0;i<location_list.getLength();i++){
            double distance = HaverSine(location,location_list.getLocation(i).getCoor());
            if(distance < min){
                min = distance;
                index = String.valueOf(i);
            }
        }
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1) {
                Progress = data.getIntExtra("Progress", 0);
            }
        }
    }

    private double HaverSine(double[] A, double[] B){
        double r = 6371000.0;
        double phi1 = Math.toRadians(A[0]);
        double phi2 = Math.toRadians(B[0]);
        double Dphi = Math.toRadians(B[0]-A[0]);
        double Dlambda = Math.toRadians(B[1]-A[1]);

        double a = Math.sin(Dphi/2) * Math.sin(Dphi/2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(Dlambda/2) * Math.sin(Dlambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return r * c;
    }

    private class MyListAdapter extends ArrayAdapter<Location> {
        public MyListAdapter() {
            super(PathInfoActivity.this, R.layout.list_path_info, Locations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_path_info, parent, false);

            }
            // Find the location to work with
            Location currentLocation = Locations.get(position);

            // Fill the view
            TextView Chi = (TextView) itemView.findViewById(R.id.Chi);
            if(position == FirstBusStop) {
                Chi.setText(currentLocation.getChi() /*+ "(" + currentLocation.getIndex() + ")"*/ +"\n("+ReachableChi+")");
            }else{
                Chi.setText(currentLocation.getChi() /*+ "(" + currentLocation.getIndex() + ")"*/ );
            }

            TextView Eng = (TextView) itemView.findViewById(R.id.Eng);
            if(position == FirstBusStop){
                Eng.setText(currentLocation.getEng() /*+ "(" + currentLocation.getIndex() + ")"*/  +"\n("+ReachableEng+")");
            }else {
                Eng.setText(currentLocation.getEng() /*+ "(" + currentLocation.getIndex() + ")"*/ );
            }

            ImageView Circle = (ImageView) itemView.findViewById(R.id.imageView2);

            if(position==0 || position==Locations.size()-1){
                Chi.setTypeface(null, Typeface.BOLD);
                Eng.setTypeface(null, Typeface.BOLD);
                Circle.setImageResource(R.mipmap.circle_s);
            } else {
                Chi.setTypeface(null, Typeface.NORMAL);
                Eng.setTypeface(null, Typeface.NORMAL);
                if(currentLocation.getIndex().matches("b" + "(.*)")){
                    Circle.setImageResource(R.mipmap.bus_c);
                }else {
                    Circle.setImageResource(R.mipmap.walk_c);
                }
            }

            return itemView;
        }

    }
}


