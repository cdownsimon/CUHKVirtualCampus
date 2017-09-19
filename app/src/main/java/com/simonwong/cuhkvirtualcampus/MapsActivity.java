package com.simonwong.cuhkvirtualcampus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.*;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Dijkstra find_path;
    String shortest_path;
    String[] location_order;
    boolean maptype;

    //TextView qeury;
    ImageButton StreetViewButton,BackListButton,PathButton,HomeButton,NavigationButton,MapTypeButton;

    String start,end,lang,startIdx,endIdx,bus,activity;
    LocationList location_list = new LocationList();
    PathList path_list = new PathList();

    ArrayList<PathInfo> RequiredPath = new ArrayList<PathInfo>();
    final ArrayList<String> LocationChi = new ArrayList<String>();
    final ArrayList<String> LocationEng = new ArrayList<String>();
    final ArrayList<Double> LocationX = new ArrayList<>();
    final ArrayList<Double> LocationY = new ArrayList<>();
    final ArrayList<Integer> LocationRe = new ArrayList<>();
    final ArrayList<String> NumOfPhotoOfEachPath = new ArrayList<String>();
    final ArrayList<String> PhotoSerisId = new ArrayList<String>();
    int Progress;

    private ArrayList<Marker> Markers = new ArrayList<>();
    private Map<Marker, String> MarkersChi = new HashMap<Marker, String>();
    private Map<Marker, String> MarkersEng = new HashMap<Marker, String>();
    private Map<Marker, Integer> MarkersRe = new HashMap<>();
    private Map<Marker, String> MarkersPhoto = new HashMap<Marker, String>();
    final ArrayList<String> PhotoIdForMarker = new ArrayList<>();

    double[] CurrentLocation = new double[2];
    ArrayList<ArrayList<double[]>> UnreachCoor = new ArrayList<ArrayList<double[]>>();
    ArrayList<double[]> CurrentLocationPath = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        putUnreachCoor();

        //handling the location request
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            CurrentLocation[0] = location.getLatitude();
            CurrentLocation[1] = location.getLongitude();
        }

        Bundle message = getIntent().getExtras();
        start = message.getString("from");
        end = message.getString("to");
        lang = message.getString("lang");
        activity = message.getString("activity");

        try {
            bus = message.getString("bus");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            Progress = getIntent().getIntExtra("Progress", 0);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        maptype = true;

        StreetViewButton = (ImageButton) findViewById(R.id.StreetViewButton);
        HomeButton = (ImageButton) findViewById(R.id.HomeButton);
        BackListButton = (ImageButton) findViewById(R.id.BackListButton);
        PathButton = (ImageButton) findViewById(R.id.PathInfoButton);
        MapTypeButton = (ImageButton) findViewById(R.id.MapTypeButton);
        NavigationButton = (ImageButton) findViewById(R.id.NavigationButton);

        if (bus.equals("true")) {
            location_list = new LocationList("true");
            path_list = new PathList("true");
        } else if (bus.equals("disabled")) {
            path_list = new PathList("disabled");
        }

        System.out.println(start);
        System.out.println(end);

        //stuff of finding shortest path
        if (start.equals("Current Location") || start.equals("目前位置")) {
            startIdx = findNearestLocationIndex(CurrentLocation);
            CurrentLocationPath = findOverlapRegion(CurrentLocation, location_list.getCoor(Integer.parseInt(startIdx)));
        } else {
            startIdx = findLocationIndex(start);
        }

        endIdx = findLocationIndex(end);
        find_path = new Dijkstra();
        System.out.println("Start point: " + startIdx);
        System.out.println("End point: " + endIdx);

        //if bus is true, load the information including bus stops
        if (bus.equals("true")) {
            shortest_path = find_path.shortestPathBus(startIdx, endIdx);
        }else if(bus.equals("disabled")){
            shortest_path = find_path.shortestPathDisabled(startIdx, endIdx);
        }else{
            shortest_path = find_path.shortestPath(startIdx, endIdx);
        }
        find_path = null;

        location_order = shortest_path.split(",");

        System.out.println("shortest path pass!");

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

        for(int i=0;i<location_order.length-1;i++){
            RequiredPath.add(path_list.getPath(location_order[i], location_order[i + 1]));
        }
        path_list = null;
        System.out.println("requried path pass!");

        //prepare the name of the lcations along the path
        for(int i=0;i<location_order.length;i++){
            LocationChi.add(location_list.getLocation(findLoctionNumber(location_order[i])).getChi());
            LocationEng.add(location_list.getLocation(findLoctionNumber(location_order[i])).getEng());

            LocationX.add(location_list.getLocation(findLoctionNumber(location_order[i])).getCoor_x());
            LocationY.add(location_list.getLocation(findLoctionNumber(location_order[i])).getCoor_y());

            LocationRe.add(location_list.getLocation(findLoctionNumber(location_order[i])).getRegion());
        }
        System.out.println("Location name pass!");

        //prepare the PhotoId array to pass
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
        System.out.println("Photo seris pass!");

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

        PathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathInfoActivity.class);

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

        NavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NavigationActivity.class);

                ArrayList<double []> pathCoorInfo = new ArrayList<double[]>();
                pathCoorInfo = getPathCoorInfo();

                intent.putExtra("from", start);
                intent.putExtra("to", end);
                intent.putExtra("PathCoorInfo", pathCoorInfo);
                intent.putExtra("LocationX",LocationX);
                intent.putExtra("LocationY",LocationY);
                intent.putExtra("LocationChi",LocationChi);
                intent.putExtra("LocationEng",LocationEng);
                intent.putExtra("LocationRe",LocationRe);
                intent.putExtra("PhotoIdForMarker",PhotoIdForMarker);
                intent.putExtra("lang", lang);

                startActivityForResult(intent, 0);
            }
        });

        MapTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maptype){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    maptype = false;
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    maptype = true;
                }
            }
        });
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.marker_info_layout, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView markerChi = ((TextView)myContentsView.findViewById(R.id.MarkerChi));
            markerChi.setText(MarkersChi.get(marker));
            TextView markerEng = ((TextView)myContentsView.findViewById(R.id.MarkerEng));
            markerEng.setText(MarkersEng.get(marker));
            TextView markerRe = ((TextView)myContentsView.findViewById(R.id.MarkerRe));

            switch (MarkersRe.get(marker)) {
                case 1:
                    markerRe.setText("(山腳 Base)");
                    break;
                case 2:
                    markerRe.setText("(本部 Main)");
                    break;
                case 3:
                    markerRe.setText("(山項 Peak)");
                    break;
                default:
                    
            }

            ImageView markerImage = ((ImageView)myContentsView.findViewById(R.id.MarkerImage));

            int id;

            try {
                id = MapsActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", MapsActivity.this.getPackageName());
                markerImage.setImageResource(id);
            }catch (Exception e){
                System.err.println(e);
            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(Markers.contains(marker)){
                    return false;
                }else {
                    return true;
                }
            }
        });

        LatLng StartPoint = new LatLng(location_list.getLocation(Integer.parseInt(startIdx)).getCoor_x(), location_list.getLocation(Integer.parseInt(startIdx)).getCoor_y());
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(StartPoint));

        //Draw the polyline
        double[][] CoorInfo;
        int NumOfCoor;
        ArrayList<LatLng> coorList = new ArrayList<>();

        //for current location
        if(CurrentLocationPath.size() > 0){
            for(int i=0;i<CurrentLocationPath.size();i++){
                System.out.println("current path: " +CurrentLocationPath.get(i)[0]+","+CurrentLocationPath.get(i)[1]);
                coorList.add(new LatLng(CurrentLocationPath.get(i)[0], CurrentLocationPath.get(i)[1]));
            }
        }

        //normal path(location to location)
        for(int i=0;i<RequiredPath.size()-1;i++){
            CoorInfo = RequiredPath.get(i).getCoorInfo();
            NumOfCoor = RequiredPath.get(i).getNumOfCoor()-1;

            System.out.println("Num of Coor: " + NumOfCoor);

            for(double[] tmp:CoorInfo){
                System.out.println(tmp[0]+", "+tmp[1]);
            }

            for(int j=0;j<NumOfCoor; j++) {
                coorList.add(new LatLng(CoorInfo[j][0],CoorInfo[j][1]));
                //mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(CoorInfo[j][0], CoorInfo[j][1])).add(new LatLng(CoorInfo[j + 1][0], CoorInfo[j+1][1])).width(10).color(Color.YELLOW));
            }
        }
        CoorInfo = RequiredPath.get(RequiredPath.size()-1).getCoorInfo();
        NumOfCoor = RequiredPath.get(RequiredPath.size()-1).getNumOfCoor();

        for(int j=0;j<NumOfCoor; j++) {
            coorList.add(new LatLng(CoorInfo[j][0],CoorInfo[j][1]));
            //mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(CoorInfo[j][0], CoorInfo[j][1])).add(new LatLng(CoorInfo[j + 1][0], CoorInfo[j+1][1])).width(10).color(Color.YELLOW));
        }

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coorList).width(10).color(Color.YELLOW);
        mMap.addPolyline(polylineOptions);

        //Mark the markers
        for(int i=0;i<location_order.length-1;i++){
            Marker marker;
            if(i==0){
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LocationX.get(i),LocationY.get(i)))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }else {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LocationX.get(i), LocationY.get(i))));
            }
            Markers.add(marker);
            MarkersChi.put(marker, LocationChi.get(i));
            MarkersEng.put(marker,LocationEng.get(i));
            MarkersRe.put(marker,LocationRe.get(i));
            MarkersPhoto.put(marker,RequiredPath.get(i).getFirstPhotoId());
            PhotoIdForMarker.add(RequiredPath.get(i).getFirstPhotoId());
        }
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LocationX.get(LocationX.size()-1), LocationY.get(LocationY.size()-1)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Markers.add(marker);
        MarkersChi.put(marker, LocationChi.get(LocationChi.size() - 1));
        MarkersEng.put(marker,LocationEng.get(LocationEng.size() - 1));
        MarkersRe.put(marker,LocationRe.get(LocationRe.size() - 1));
        MarkersPhoto.put(marker, RequiredPath.get(RequiredPath.size() - 1).getLastPhotoId());
        PhotoIdForMarker.add(RequiredPath.get(RequiredPath.size()-1).getLastPhotoId());

        //Mark the arrows
        ArrayList<double[]> CoorList = getPathCoorInfo();

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.arrow);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallArrow = Bitmap.createScaledBitmap(b, 50, 50, false);

        for(int i=0;i<CoorList.size()-1;i=i+2){
            if(CoorList.get(i)[0] != CoorList.get(i + 1)[0] && CoorList.get(i)[1] != CoorList.get(i + 1)[1]) {
                double heading = Math.toDegrees(Bearing(CoorList.get(i), CoorList.get(i + 1)));
                double[] midpt = MidPoint(CoorList.get(i), CoorList.get(i + 1));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(midpt[0], midpt[1]))
                        .flat(true)
                        .rotation((float) heading)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallArrow)));
            }
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
            if(input.equals(location_list.getLocation(i).getEng()) || input.equals(location_list.getLocation(i).getChi()) || input.equals(location_list.getLocation(i).getShort_Name())){
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

    private ArrayList<double[]> getPathCoorInfo(){
        double[][] CoorInfo;
        int NumOfCoor;
        ArrayList<double[]> coorList = new ArrayList<>();

        if(CurrentLocationPath.size() > 0){
            for(int i=0;i<CurrentLocationPath.size();i++){
                coorList.add(new double[]{CurrentLocationPath.get(i)[0],CurrentLocationPath.get(i)[1]});
            }
        }

        for(int i=0;i<RequiredPath.size()-1;i++){
            CoorInfo = RequiredPath.get(i).getCoorInfo();
            NumOfCoor = RequiredPath.get(i).getNumOfCoor()-1;

            for(int j=0;j<NumOfCoor; j++) {
                coorList.add(new double[]{CoorInfo[j][0],CoorInfo[j][1]});
            }
        }
        CoorInfo = RequiredPath.get(RequiredPath.size()-1).getCoorInfo();
        NumOfCoor = RequiredPath.get(RequiredPath.size()-1).getNumOfCoor();

        for(int j=0;j<NumOfCoor; j++) {
            coorList.add(new double[]{CoorInfo[j][0],CoorInfo[j][1]});
        }
        return coorList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 0) {
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

    private double Bearing(double[] A, double[] B){
        double phi1 = Math.toRadians(A[0]);
        double phi2 = Math.toRadians(B[0]);
        double lambda1 = Math.toRadians(A[1]);
        double lambda2 = Math.toRadians(B[1]);

        double y = Math.sin(lambda2-lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2-lambda1);

        return Math.atan2(y,x);
    }

    private double[] Intersection(double[] A_1, double[] A_2, double[] B_1, double[] B_2){
        double phi1 = Math.toRadians(A_1[0]);
        double phi2 = Math.toRadians(B_1[0]);
        double lambda1 = Math.toRadians(A_1[1]);
        double lambda2 = Math.toRadians(B_1[1]);

        double theta13 = Bearing(A_1,A_2);
        double theta23 = Bearing(B_1,B_2);

        double d_phi = phi2 - phi1;
        double d_lambda = lambda2 - lambda1;

        double delta12 = 2*Math.asin(Math.sqrt(Math.sin(d_phi/2)*Math.sin(d_phi/2)+Math.cos(phi1)*Math.cos(phi2)*Math.sin(d_lambda/2)*Math.sin(d_lambda/2)));

        if(delta12 == 0){
            return null;
        }

        double thetaa = Math.acos((Math.sin(phi2) - Math.sin(phi1)*Math.cos(delta12))/( Math.sin(delta12)*Math.cos(phi1)));

        if(Double.isNaN(thetaa)){
            thetaa = 0;
        }

        double thetab = Math.acos((Math.sin(phi1) - Math.sin(phi2)*Math.cos(delta12))/(Math.sin(delta12)*Math.cos(phi2)));

        double theta12;
        double theta21;

        if(Math.sin(lambda2-lambda1)>0){
            theta12 = thetaa;
        }else{
            theta12 = 2*Math.PI - thetaa;
        }

        if(Math.sin(lambda2-lambda1)>0){
            theta21 = 2*Math.PI - thetab;
        }else{
            theta21 = thetab;
        }

        double alpha1 = (theta13 - theta12 + Math.PI) % (2*Math.PI) - Math.PI;
        double alpha2 = (theta21 - theta23 + Math.PI) % (2*Math.PI) - Math.PI;

        if(Math.sin(alpha1)==0 && Math.sin(alpha2)==0){
            //return null;
        }

        if(Math.sin(alpha1)*Math.sin(alpha2) < 0){
            //return null;
        }

        double alpha3 = Math.acos(-Math.cos(alpha1)*Math.cos(alpha2) + Math.sin(alpha1)*Math.sin(alpha2)*Math.cos(delta12));
        double delta13 = Math.atan2(Math.sin(delta12)*Math.sin(alpha1)*Math.sin(alpha2), Math.cos(alpha2)+Math.cos(alpha1)*Math.cos(alpha3));
        double phi3 = Math.asin(Math.sin(phi1)*Math.cos(delta13) + Math.cos(phi1)*Math.sin(delta13)*Math.cos(theta13));
        double d_lambda13 = Math.atan2(Math.sin(theta13)*Math.sin(delta13)*Math.cos(phi1), Math.cos(delta13)-Math.sin(phi1)*Math.sin(phi3));
        double lambda3 = lambda1 + d_lambda13;

        return new double[]{Math.toDegrees(phi3), (Math.toDegrees(lambda3)+540)%360-180};

    }

    private double CrossProduct(double[] A, double[] B, double[] P){
        double[] a_to_b = {B[0]-A[0], B[1]-A[1]};
        double[] a_to_p = {P[0]-A[0], P[1]-A[1]};

        double z = a_to_b[0]*a_to_p[1]-a_to_b[1]*a_to_p[0];

        return z;
    }

    private boolean isBetween(double[] A, double[] B, double[] C){
        double crossproduct = (C[1] - A[1]) * (B[0] - A[0]) - (C[0] - A[0]) * (B[1] - A[1]);
        if (Math.abs(crossproduct) > 0.00001){
            return false;
        }

        double dotproduct = (C[0] - A[0]) * (B[0] - A[0]) + (C[1] - A[1])*(B[1] - A[1]);
        if (dotproduct < 0){
            return false;
        }

        double squaredlengthba = (B[0] - A[0])*(B[0] - A[0]) + (B[1] - A[1])*(B[1] - A[1]);
        if (dotproduct > squaredlengthba){
            return false;
        }

        return true;
    }

    private double[] MidPoint(double[] A, double[] B){
        double phi1 = Math.toRadians(A[0]);
        double phi2 = Math.toRadians(B[0]);
        double lambda1 = Math.toRadians(A[1]);
        double lambda2 = Math.toRadians(B[1]);

        double Bx = Math.cos(phi2) * Math.cos(lambda2-lambda1);
        double By = Math.cos(phi2) * Math.sin(lambda2 - lambda1);
        double phi3 = Math.atan2(Math.sin(phi1) + Math.sin(phi2), Math.sqrt((Math.cos(phi1) + Bx) * (Math.cos(phi1) + Bx) + By * By));
        double lambda3 = lambda1 + Math.atan2(By, Math.cos(phi1) + Bx);

        return new double[]{Math.toDegrees(phi3),Math.toDegrees(lambda3)};
    }

    private ArrayList<double[]> findOverlapRegion(double[] A,double[] B){
        double[] start = A;
        double[] end = B;

        ArrayList<ArrayList<double[]>> Overlap = new ArrayList<ArrayList<double[]>>();
        ArrayList<ArrayList<double[]>> AllOverlap = new ArrayList<ArrayList<double[]>>();
        ArrayList<Integer> AllOverlapIndex = new ArrayList<Integer>();

        for(int i = 0;i < UnreachCoor.size();i++){
            int count = 0;
            for(int j = 0;j < UnreachCoor.get(i).size()-1;j++){
                ArrayList<double[]> CoorList = UnreachCoor.get(i);
                ArrayList<double[]> LineWithIntersection = new ArrayList<double[]>();
                if(isBetween(start,end,Intersection(start,end,CoorList.get(j),CoorList.get(j+1))) && isBetween(CoorList.get(j),CoorList.get(j+1),Intersection(start,end,CoorList.get(j),CoorList.get(j+1)))){
                    LineWithIntersection.add(CoorList.get(j));
                    LineWithIntersection.add(CoorList.get(j+1));
                    LineWithIntersection.add(Intersection(start,end,CoorList.get(j),CoorList.get(j+1)));
                    Overlap.add(LineWithIntersection);
                    AllOverlap.add(LineWithIntersection);
                    AllOverlapIndex.add(AllOverlap.size()-1);
                    count = count + 1;
                }else if(count > 0){
                    LineWithIntersection.add(CoorList.get(j));
                    LineWithIntersection.add(CoorList.get(j+1));
                    AllOverlap.add(LineWithIntersection);
                }
            }

            ArrayList<double[]> CoorList = UnreachCoor.get(i);
            ArrayList<double[]> LineWithIntersection = new ArrayList<double[]>();
            if(isBetween(start,end,Intersection(start,end,CoorList.get(UnreachCoor.get(i).size()-1),CoorList.get(0))) && isBetween(CoorList.get(UnreachCoor.get(i).size()-1),CoorList.get(0),Intersection(start,end,CoorList.get(UnreachCoor.get(i).size()-1),CoorList.get(0)))){
                LineWithIntersection.add(CoorList.get(UnreachCoor.get(i).size()-1));
                LineWithIntersection.add(CoorList.get(0));
                LineWithIntersection.add(Intersection(start,end,CoorList.get(UnreachCoor.get(i).size()-1),CoorList.get(0)));
                Overlap.add(LineWithIntersection);
                AllOverlap.add(LineWithIntersection);
                AllOverlapIndex.add(AllOverlap.size()-1);
                count = count + 1;
            }else if(count > 0){
                LineWithIntersection.add(CoorList.get(UnreachCoor.get(i).size()-1));
                LineWithIntersection.add(CoorList.get(0));
                AllOverlap.add(LineWithIntersection);
            }

            System.out.println("All over lap size: " + AllOverlap.size());

            try {
                //remove non-intersection line segment untill meet the last one
                while (AllOverlap.get(AllOverlap.size() - 1).size() < 3) {
                    AllOverlap.remove(AllOverlap.size() - 1);
                }
            }catch (Exception e){

            }

        }

        if(AllOverlap.size() == 0 ){
            ArrayList<double[]> directConnect = new ArrayList<double[]>();
            directConnect.add(start);
            return directConnect;
        }

        ArrayList<Double> distances = new ArrayList<Double>();

        for(int i = 0;i < Overlap.size();i++){
            distances.add(HaverSine(start,Overlap.get(i).get(2)));
        }

        //find the order of line segments
        ArrayList<Integer> order = new ArrayList<Integer>();

        for(int i = 0;i < distances.size();i++){
            double min = 999999;
            Integer idx = 99999;
            for(int j = 0; j < distances.size();j++){
                if(order.contains(j)){
                    continue;
                }else{
                    if(distances.get(j) < min){
                        min = distances.get(j);
                        idx = j;
                    }
                }
            }
            order.add(idx);
        }

        ArrayList<ArrayList<double[]>> Ordered = new ArrayList<ArrayList<double[]>>();
        ArrayList<ArrayList<double[]>> AllOverlapOrdered = new ArrayList<ArrayList<double[]>>();

        for(int i = 0;i < order.size()-1;i++){
            if(AllOverlapIndex.get(order.get(i)) < AllOverlapIndex.get(order.get(i+1))){
                for(int j = AllOverlapIndex.get(order.get(i));j < AllOverlapIndex.get(order.get(i+1));j++){
                    AllOverlapOrdered.add(AllOverlap.get(j));
                }
            }else{
                for(int j = AllOverlapIndex.get(order.get(i));j > AllOverlapIndex.get(order.get(i+1));j--){
                    AllOverlapOrdered.add(AllOverlap.get(j));
                }
            }
        }

            AllOverlapOrdered.add(AllOverlap.get(AllOverlapIndex.get(order.get(order.size() - 1))));


        //double[][] Path = new double[AllOverlapOrdered.size()][2];
        //int pathIdx = 0;
        ArrayList<double[]> Path = new ArrayList<double[]>();

        double[] P1 = new double[]{AllOverlapOrdered.get(0).get(0)[0],AllOverlapOrdered.get(0).get(0)[1]};
        double[] P2 = new double[]{AllOverlapOrdered.get(0).get(1)[0],AllOverlapOrdered.get(0).get(1)[1]};

        System.out.println("All Overlap Ordered: ");
        for(int i=0;i<AllOverlapOrdered.size();i++){
            System.out.println("i:" + i + ":");
            for(int j=0;j<AllOverlapOrdered.get(i).size();j++) {
                System.out.println(j + ":");
                System.out.println(AllOverlapOrdered.get(i).get(j)[0] + "," + AllOverlapOrdered.get(i).get(j)[1]);
            }
        }

        //Path[pathIdx][0] = AllOverlapOrdered.get(0).get(2)[0];
        //Path[pathIdx][1] = AllOverlapOrdered.get(0).get(2)[1];
        //pathIdx = pathIdx + 1;
        Path.add(start);
        Path.add(new double[]{AllOverlapOrdered.get(0).get(2)[0],AllOverlapOrdered.get(0).get(2)[1]});

        for(int i = 1;i < AllOverlapOrdered.size();i++){
            double [] sharedPoint = new double[2];
            if(AllOverlapOrdered.get(i).size() == 3){
                sharedPoint = findSharedPoint(P1,P2,AllOverlapOrdered.get(i).get(0),AllOverlapOrdered.get(i).get(1));

                //Path[pathIdx][0] = sharedPoint[0];
                //Path[pathIdx][1] = sharedPoint[1];
                //pathIdx = pathIdx + 1
                if(sharedPoint != null){
                    Path.add(sharedPoint);
                }

                //Path[pathIdx][0] = AllOverlapOrdered.get(i).get(2)[0];
                //Path[pathIdx][1] = AllOverlapOrdered.get(i).get(2)[1];
                Path.add(new double[]{AllOverlapOrdered.get(i).get(2)[0],AllOverlapOrdered.get(i).get(2)[1]});

                P1 = new double[]{AllOverlapOrdered.get(i).get(0)[0],AllOverlapOrdered.get(i).get(0)[1]};
                P2 = new double[]{AllOverlapOrdered.get(i).get(1)[0],AllOverlapOrdered.get(i).get(1)[1]};
            }else{
                sharedPoint = findSharedPoint(P1,P2,AllOverlapOrdered.get(i).get(0),AllOverlapOrdered.get(i).get(1));

                //Path[pathIdx][0] = sharedPoint[0];
                //Path[pathIdx][1] = sharedPoint[1];
                Path.add(sharedPoint);

                P1 = new double[]{AllOverlapOrdered.get(i).get(0)[0],AllOverlapOrdered.get(i).get(0)[1]};
                P2 = new double[]{AllOverlapOrdered.get(i).get(1)[0],AllOverlapOrdered.get(i).get(1)[1]};
            }
            //pathIdx = pathIdx + 1;
        }

        System.out.println("Final path: ");
        for(int i=0;i<Path.size();i++){
            System.out.println(i + ":");
            System.out.println(Path.get(i)[0] + "," + Path.get(i)[1]);
        }

        return Path;
    }

    private double[] findSharedPoint(double[] A1,double[] A2, double[] B1, double[] B2){

        if(Math.abs(A1[0] - B1[0] + A1[1] - B1[1]) < 0.000001){
            return A1;
        }else if(Math.abs(A1[0] - B2[0] + A1[1] - B2[1]) < 0.000001){
            return A1;
        }else if(Math.abs(A2[0] - B1[0] + A2[1] - B1[1]) < 0.000001){
            return A2;
        }else if(Math.abs(A2[0] - B2[0] + A2[1] - B2[1]) < 0.000001){
            return A2;
        }else{
            return null;
        }
    }

    private void putUnreachCoor(){

        ArrayList<double[]> CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419891, 114.204394});
        CoorList.add(new double[]{22.419871, 114.205129});
        CoorList.add(new double[]{22.419312, 114.205102});
        CoorList.add(new double[]{22.419349, 114.204371});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419851,114.203276});
        CoorList.add(new double[]{22.419835,114.204061});
        CoorList.add(new double[]{22.419142,114.204057});
        CoorList.add(new double[]{22.419136,114.203898});
        CoorList.add(new double[]{22.419308,114.203511});
        CoorList.add(new double[]{22.419428,114.203516});
        CoorList.add(new double[]{22.419432,114.203807});
        CoorList.add(new double[]{22.419667,114.203275});
        CoorList.add(new double[]{22.419678,114.203281});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419589,114.202891});
        CoorList.add(new double[]{22.419585,114.203705});
        CoorList.add(new double[]{22.419461,114.203703});
        CoorList.add(new double[]{22.419458,114.202888});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419136,114.204887});
        CoorList.add(new double[]{22.419098,114.205527});
        CoorList.add(new double[]{22.418823,114.205503});
        CoorList.add(new double[]{22.418824,114.204888});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.420396,114.202868});
        CoorList.add(new double[]{22.420348,114.202961});
        CoorList.add(new double[]{22.420001,114.202808});
        CoorList.add(new double[]{22.419691,114.202832});
        CoorList.add(new double[]{22.419683,114.202719});
        CoorList.add(new double[]{22.420067,114.202708});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.418741,114.204786});
        CoorList.add(new double[]{22.418702,114.205556});
        CoorList.add(new double[]{22.418451,114.205549});
        CoorList.add(new double[]{22.418459,114.204762});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.418376,114.204466});
        CoorList.add(new double[]{22.418381,114.205202});
        CoorList.add(new double[]{22.418334,114.205203});
        CoorList.add(new double[]{22.418321,114.205731});
        CoorList.add(new double[]{22.418233,114.205724});
        CoorList.add(new double[]{22.418227,114.205445});
        CoorList.add(new double[]{22.418192,114.205445});
        CoorList.add(new double[]{22.418189,114.204694});
        CoorList.add(new double[]{22.418049,114.204693});
        CoorList.add(new double[]{22.418049,114.204481});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419825,114.205576});
        CoorList.add(new double[]{22.419826,114.206981});
        CoorList.add(new double[]{22.419535,114.206976});
        CoorList.add(new double[]{22.419535,114.205601});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419343,114.205615});
        CoorList.add(new double[]{22.419343,114.206476});
        CoorList.add(new double[]{22.419031,114.206428});
        CoorList.add(new double[]{22.418875,114.205614});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.418881,114.205732});
        CoorList.add(new double[]{22.418928,114.206471});
        CoorList.add(new double[]{22.418625,114.206453});
        CoorList.add(new double[]{22.418761,114.205675});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419129,114.207281});
        CoorList.add(new double[]{22.419213,114.207174});
        CoorList.add(new double[]{22.419173,114.206568});
        CoorList.add(new double[]{22.418609,114.206571});
        CoorList.add(new double[]{22.418546,114.207173});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.420217,114.206359});
        CoorList.add(new double[]{22.420512,114.206888});
        CoorList.add(new double[]{22.420805,114.207425});
        CoorList.add(new double[]{22.420571,114.207736});
        CoorList.add(new double[]{22.420435,114.208001});
        CoorList.add(new double[]{22.420458,114.208102});
        CoorList.add(new double[]{22.420259,114.207933});
        CoorList.add(new double[]{22.420281,114.207591});
        CoorList.add(new double[]{22.419923,114.207525});
        CoorList.add(new double[]{22.419927,114.206872});
        CoorList.add(new double[]{22.420201,114.206885});
        CoorList.add(new double[]{22.420145,114.206687});
        CoorList.add(new double[]{22.419908,114.206677});
        CoorList.add(new double[]{22.419902,114.206364});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.420621,114.208264});
        CoorList.add(new double[]{22.420439,114.208621});
        CoorList.add(new double[]{22.420445,114.209072});
        CoorList.add(new double[]{22.419693,114.209201});
        CoorList.add(new double[]{22.419705,114.208841});
        CoorList.add(new double[]{22.419868,114.208531});
        CoorList.add(new double[]{22.419897,114.207576});
        CoorList.add(new double[]{22.420261,114.207631});
        CoorList.add(new double[]{22.420221,114.207968});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.420925,114.210138});
        CoorList.add(new double[]{22.420616,114.209638});
        CoorList.add(new double[]{22.420318,114.209471});
        CoorList.add(new double[]{22.420465,114.209254});
        CoorList.add(new double[]{22.420449,114.209135});
        CoorList.add(new double[]{22.419703,114.209293});
        CoorList.add(new double[]{22.419977,114.209857});
        CoorList.add(new double[]{22.420294,114.210142});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419779,114.207494});
        CoorList.add(new double[]{22.419755,114.208604});
        CoorList.add(new double[]{22.419589,114.208481});
        CoorList.add(new double[]{22.419605,114.207489});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419329,114.207496});
        CoorList.add(new double[]{22.419315,114.208601});
        CoorList.add(new double[]{22.419131,114.208603});
        CoorList.add(new double[]{22.419152,114.207491});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419319,114.208692});
        CoorList.add(new double[]{22.419316,114.208871});
        CoorList.add(new double[]{22.418967,114.208871});
        CoorList.add(new double[]{22.418968,114.208687});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.418731,114.207429});
        CoorList.add(new double[]{22.419071,114.207909});
        CoorList.add(new double[]{22.419096,114.208053});
        CoorList.add(new double[]{22.419074,114.208536});
        CoorList.add(new double[]{22.418893,114.208515});
        CoorList.add(new double[]{22.418558,114.207598});
        UnreachCoor.add(CoorList);
        CoorList = new ArrayList<double[]>();
        CoorList.add(new double[]{22.419551,114.208591});
        CoorList.add(new double[]{22.419551,114.209143});
        CoorList.add(new double[]{22.419365,114.209147});
        CoorList.add(new double[]{22.419365,114.208591});
        UnreachCoor.add(CoorList);
    }
}
