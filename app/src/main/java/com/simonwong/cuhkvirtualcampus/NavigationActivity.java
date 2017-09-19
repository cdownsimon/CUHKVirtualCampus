package com.simonwong.cuhkvirtualcampus;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    TextView ChiMessage, EngMessage;

    ArrayList<double[]> PathCoorInfo;
    String start,end,lang;

    ArrayList<String> LocationChi = new ArrayList<String>();
    ArrayList<String> LocationEng = new ArrayList<String>();
    ArrayList<Double> LocationX = new ArrayList<>();
    ArrayList<Double> LocationY = new ArrayList<>();
    ArrayList<Integer> LocationRe = new ArrayList<>();
    HashMap<Double,HashMap<Double,Integer>> CoorSound = new  HashMap<Double,HashMap<Double,Integer>>();

    private ArrayList<Marker> Markers = new ArrayList<>();
    private Map<Marker, String> MarkersChi = new HashMap<Marker, String>();
    private Map<Marker, String> MarkersEng = new HashMap<Marker, String>();
    private Map<Marker, Integer> MarkersRe = new HashMap<>();
    private Map<Marker, String> MarkersPhoto = new HashMap<Marker, String>();
    ArrayList<String> PhotoIdForMarker = new ArrayList<>();

    //State indicators
    boolean IsMapInited = false;
    boolean IsStarted = false;
    boolean IsEnd = false;
    boolean IsAnnounced = false;
    boolean IsOutOfBlueLine = false;
    boolean IsArrivedBuilding = false;
    boolean IsRemovedArrow = false;
    int NextLocation = 0;
    int NextBuilding = 0;
    Polyline PathOnMap;
    ArrayList<double[]> CurrentPathCoorInfo = new ArrayList<double[]>();
    ArrayList<Marker> CurrentMarkers = new ArrayList<Marker>();
    ArrayList<Marker> CurrentArrows = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle message = getIntent().getExtras();
        start = message.getString("from");
        end = message.getString("to");
        lang = message.getString("lang");
        PathCoorInfo = (ArrayList<double[]>) getIntent().getSerializableExtra("PathCoorInfo");
        LocationX = (ArrayList<Double>)getIntent().getSerializableExtra("LocationX");
        LocationY = (ArrayList<Double>)getIntent().getSerializableExtra("LocationY");
        LocationChi = getIntent().getStringArrayListExtra("LocationChi");
        LocationEng = getIntent().getStringArrayListExtra("LocationEng");
        LocationRe = (ArrayList<Integer>)getIntent().getSerializableExtra("LocationRe");
        PhotoIdForMarker = getIntent().getStringArrayListExtra("PhotoIdForMarker");

        for(double[] tmp : PathCoorInfo){
            CurrentPathCoorInfo.add(tmp);
        }

        ChiMessage = (TextView) findViewById(R.id.ChiMessage);
        EngMessage = (TextView) findViewById(R.id.EngMessage);

        putCoorSound();

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
                id = NavigationActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", NavigationActivity.this.getPackageName());
                markerImage.setImageResource(id);
            }catch (Exception e){

            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
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
                if (Markers.contains(marker)) {
                    return false;
                } else {
                    return true;
                }
            }
        });

        if(!IsMapInited) {
            LatLng StartPoint = new LatLng(PathCoorInfo.get(0)[0], PathCoorInfo.get(0)[1]);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(StartPoint));

            ArrayList<LatLng> coorList = new ArrayList<>();
            for (int i = 0; i < PathCoorInfo.size(); i++) {
                coorList.add(new LatLng(PathCoorInfo.get(i)[0], PathCoorInfo.get(i)[1]));
            }

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(coorList).width(10).color(Color.BLUE);
            PathOnMap = mMap.addPolyline(polylineOptions);

            System.out.println("Path: " + PathOnMap);

            //Mark the markers
            for (int i = 0; i < PhotoIdForMarker.size() - 1; i++) {
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
                CurrentMarkers.add(marker);
                MarkersChi.put(marker, LocationChi.get(i));
                MarkersEng.put(marker, LocationEng.get(i));
                MarkersRe.put(marker, LocationRe.get(i));
                MarkersPhoto.put(marker, PhotoIdForMarker.get(i));
            }
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(LocationX.get(LocationX.size() - 1), LocationY.get(LocationY.size() - 1)))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            Markers.add(marker);
            CurrentMarkers.add(marker);
            MarkersChi.put(marker, LocationChi.get(LocationChi.size() - 1));
            MarkersEng.put(marker, LocationEng.get(LocationEng.size() - 1));
            MarkersRe.put(marker, LocationRe.get(LocationRe.size() - 1));
            MarkersPhoto.put(marker, PhotoIdForMarker.get(PhotoIdForMarker.size() - 1));

            //Mark the arrows
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.arrow);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallArrow = Bitmap.createScaledBitmap(b, 50, 50, false);

            Marker arrow;
            for(int i=0;i<PathCoorInfo.size()-1;i=i+2){
                if(PathCoorInfo.get(i)[0] != PathCoorInfo.get(i + 1)[0] && PathCoorInfo.get(i)[1] != PathCoorInfo.get(i + 1)[1]) {
                    double heading = Math.toDegrees(Bearing(PathCoorInfo.get(i), PathCoorInfo.get(i + 1)));
                    double[] midpt = MidPoint(PathCoorInfo.get(i), PathCoorInfo.get(i + 1));

                    arrow = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(midpt[0], midpt[1]))
                            .flat(true)
                            .rotation((float) heading)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallArrow)));

                    CurrentArrows.add(arrow);
                }
            }
            IsMapInited = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }

        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mGoogleApiClient != null) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }catch (Exception e){

            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        double[] CurrentLocation = {mLastLocation.getLatitude(), mLastLocation.getLongitude()};

        System.out.println("Coor: " + CurrentLocation[0] + " " + CurrentLocation[1]);

        try {
            System.out.println("Cross Track Distance: " + CrossTrack(PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation - 1), CurrentLocation));
            double[] tmp = findClosestPoint(PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation - 1), CurrentLocation);
            System.out.println("A: " + PathCoorInfo.get(NextLocation-1)[0] + ", " + PathCoorInfo.get(NextLocation-1)[1] + "\nB: " + PathCoorInfo.get(NextLocation)[0] + ", " + PathCoorInfo.get(NextLocation)[1] + "\nClosest Point: " + tmp[0] + ", " + tmp[1]);

            int nearest = findNearestPath(CurrentLocation);
            System.out.println("NextLocation: "+NextLocation+", Nearest Point: "+nearest+", "+PathCoorInfo.get(nearest)[0]+", "+PathCoorInfo.get(nearest)[1]);

        }catch (Exception e){

        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CurrentLocation[0],CurrentLocation[1])));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(CurrentLocation[0],CurrentLocation[1])),1000,null);

        if(!IsEnd) {
            try {
                int nearestP = findNearestPath(CurrentLocation);
                if (!IsStarted) {
                    if (HaverSine(PathCoorInfo.get(0), CurrentLocation) >= 10.0 && HaverSine(PathCoorInfo.get(nearestP), CurrentLocation) >= 10.0) {
                        ChiMessage.setText("請先到達" + LocationChi.get(0) + ".");
                        EngMessage.setText("Please go to " + LocationEng.get(0) + " first.");
                        IsStarted = false;
                    } else {
                        ChiMessage.setText("開始導航...");
                        EngMessage.setText("Navigation is started...");

                        MediaPlayer mMediaPlayer;
                        //Language
                        if(lang.equals("eng")){
                            mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.start_eng);
                        }else{
                            mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.start_can);
                        }


                        IsStarted = true;
                        if (nearestP != NextLocation) {

                            for (int i = NextLocation; i < nearestP; i++) {
                                updatePathOnMap();
                            }
                            for (int i = NextLocation; i < nearestP; i=i+2){
                                CurrentArrows.get(0).remove();
                                CurrentArrows.remove(0);
                            }

                            NextLocation = nearestP + 1;

                            //Update the NextBuiling and remove the markers, arrows
                            boolean selected = false;
                            for (int i = nearestP; i < PathCoorInfo.size(); i++) {
                                if (!selected) {
                                    for (int j = 0; j < LocationX.size(); j++) {
                                        if (IsArrivedBuilding(PathCoorInfo.get(i), new double[]{LocationX.get(j), LocationY.get(j)})) {
                                            if (i == nearestP) {
                                                for (int k = 0; k < j + 1; k++) {
                                                    CurrentMarkers.get(k).remove();
                                                }
                                                NextBuilding = j + 1;
                                            } else {
                                                for (int k = 0; k < j; k++) {
                                                    CurrentMarkers.get(k).remove();
                                                }
                                                NextBuilding = j;
                                            }
                                            selected = true;
                                            break;
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }

                            if(NextLocation == PathCoorInfo.size()){
                                Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                                mVibrator.vibrate(500);

                                ChiMessage.setText("你已到達終點:" + LocationChi.get(LocationChi.size() - 1));
                                EngMessage.setText("You have arrived the end point, " + LocationEng.get(LocationEng.size() - 1));

                                //Language
                                if(lang.equals("eng")){
                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.end_eng);
                                }else {
                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.end_can);
                                }

                                IsEnd = true;
                                PathOnMap.remove();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Delays is running.");
                                        onBackPressed();
                                    }
                                }, 2000);
                            }
                        } else {
                            NextLocation++;
                            CurrentMarkers.get(NextBuilding).remove();
                            NextBuilding++;
                        }

                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });

                        mMediaPlayer.start();
                    }
                } else if (Math.abs(CrossTrack(PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation - 1), CurrentLocation)) >= 15) {

                    int nearest = findNearestPath(CurrentLocation);

                    System.out.println("Nearest: " + nearest + ", NextLocation: " + NextLocation);

                    if (nearest != NextLocation) {
                        for (int i = NextLocation; i < nearest + 1; i++) {
                            updatePathOnMap();
                        }
                        for (int i = NextLocation; i < nearestP; i=i+2){
                            CurrentArrows.get(0).remove();
                            CurrentArrows.remove(0);
                        }
                        NextLocation = nearest + 1;

                        boolean selected = false;
                        for (int i = NextLocation; i < PathCoorInfo.size(); i++) {
                            if (!selected) {
                                for (int j = 0; j < LocationX.size(); j++) {
                                    if (IsArrivedBuilding(PathCoorInfo.get(i), new double[]{LocationX.get(j), LocationY.get(j)})) {
                                        for (int k = NextBuilding; k < j; k++) {
                                            CurrentMarkers.get(k).remove();
                                        }
                                        NextBuilding = j;
                                        selected = true;
                                        break;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                        IsOutOfBlueLine = false;
                    } else {
                        ChiMessage.setText("你已離開藍線, 請返回藍線繼續");
                        EngMessage.setText("You are out of the blue line. Please go back.");
                        if (!IsOutOfBlueLine) {

                            MediaPlayer mMediaPlayer;
                            //Language
                            if(lang.equals("eng")){
                                mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.back_blue_line_eng);
                            }else {
                                mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.back_blue_line_can);
                            }

                            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();
                                }
                            });

                            mMediaPlayer.start();
                            IsOutOfBlueLine = true;
                        }
                    }
                } else if (HaverSine(PathCoorInfo.get(NextLocation), CurrentLocation) >= 10.0) {
                    ChiMessage.setText("請沿藍線前進.");
                    EngMessage.setText("Please follow the blue line.");
                    IsOutOfBlueLine = false;
                    double[] closestP = findClosestPoint(PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation - 1), CurrentLocation);
                    shortenPathOnMap(closestP);


                        System.out.println("closeest to NextLocation: " + HaverSine(closestP, PathCoorInfo.get(NextLocation)) + ", Mid to NextLocation: " + HaverSine(MidPoint(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation)), PathCoorInfo.get(NextLocation)));
                        System.out.println("case: " + (HaverSine(closestP, PathCoorInfo.get(NextLocation)) < HaverSine(MidPoint(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation)),PathCoorInfo.get(NextLocation))));
                        System.out.println("IsRemovedArrow: " + IsRemovedArrow);

                        if(!IsRemovedArrow) {

                            System.out.println("Ready to remove...");

                            double cloest_to_next = HaverSine(closestP, PathCoorInfo.get(NextLocation));
                            double mid_to_next = HaverSine(MidPoint(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation)),PathCoorInfo.get(NextLocation));

                            if (cloest_to_next < mid_to_next || Math.abs(cloest_to_next-mid_to_next) <= 0.000000001) {
                                if(NextLocation%2 == 1) {
                                    CurrentArrows.get(0).remove();
                                    CurrentArrows.remove(0);
                                    IsRemovedArrow = true;
                                    System.out.println("Removing...");
                                }
                            }
                        }

                    //prevent index out of range
                    try {
                        if (HaverSine(findClosestPoint(PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation - 1), CurrentLocation), PathCoorInfo.get(NextLocation)) < 20.0) {
                            if (!IsAnnounced) {
                            /*
                            if (getDirection(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation + 1))) {
                                MediaPlayer mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_right_after20m_can);
                                mMediaPlayer.start();
                            } else {
                                MediaPlayer mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_left_after20m_can);
                                mMediaPlayer.start();
                            }
                            */
                                Integer mp3 = CoorSound.get(PathCoorInfo.get(NextLocation)[0]).get(PathCoorInfo.get(NextLocation)[1]);
                                System.out.println("Ready to play mp3..." + mp3);

                                if (mp3 != null) {
                                    final MediaPlayer mMediaPlayer;
                                    if (mp3.equals(4)) {
                                        System.out.println("switch running, " + LocationEng.get(NextBuilding + 1) + ", " + LocationEng.get(NextBuilding));
                                        switch (LocationEng.get(NextBuilding + 1)) {
                                            case "William M.W. Mong Engineering Building G/F":

                                                if(lang.equals("eng")) {
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_g_eng);
                                                }else {
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_g_can);
                                                }

                                                break;
                                            case "William M.W. Mong Engineering Building 9/F":

                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_9_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_9_can);
                                                }

                                                break;
                                            case "William M.W. Mong Engineering Building 4/F":

                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_4_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.lift_press_4_can);
                                                }

                                                break;
                                            default:

                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.in_lift_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.in_lift_can);
                                                }

                                        }
                                    } else {
                                        if (getDirection(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation + 1))) {
                                            if (mp3.equals(1)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.road_right_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.road_right_can);
                                                }
                                            } else if (mp3.equals(2)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.zebra_right_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.zebra_right_can);
                                                }
                                            } else if (mp3.equals(3)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_right_after20m_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_right_after20m_can);
                                                }
                                            } else {
                                                mMediaPlayer = MediaPlayer.create(NavigationActivity.this, mp3);
                                            }
                                        } else {
                                            if (mp3.equals(1)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.road_left_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.road_left_can);
                                                }
                                            } else if (mp3.equals(2)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.zebra_left_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.zebra_left_can);
                                                }
                                            } else if (mp3.equals(3)) {
                                                if(lang.equals("eng")){
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_left_after20m_eng);
                                                }else{
                                                    mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.turn_left_after20m_can);
                                                }
                                            } else {
                                                mMediaPlayer = MediaPlayer.create(NavigationActivity.this, mp3);
                                            }
                                        }
                                    }

                                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            mp.release();
                                        }
                                    });

                                    mMediaPlayer.start();
                                }
                                IsAnnounced = true;
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }catch (Exception e){

            }

            try {
                if (HaverSine(PathCoorInfo.get(NextLocation), CurrentLocation) < 10.0) {
                    double[] BuildingCoor = {LocationX.get(NextBuilding), LocationY.get(NextBuilding)};

                    System.out.println("Building Coor: " + BuildingCoor[0] + " " + BuildingCoor[1]);
                    System.out.println("NextLocation Coor: " + PathCoorInfo.get(NextLocation)[0] + " " + PathCoorInfo.get(NextLocation)[1]);

                    if (IsArrivedBuilding(PathCoorInfo.get(NextLocation), BuildingCoor)) {
                        Toast.makeText(this, "你已到達" + LocationChi.get(NextBuilding) + "\nYou arrived " + LocationEng.get(NextBuilding), Toast.LENGTH_SHORT).show();
                        CurrentMarkers.get(NextBuilding).remove();
                        NextBuilding++;
                    }
                    if (NextBuilding < LocationChi.size()) {
                        //check if certain coordinate needed to play mp3
                        if (CoorSound.get(PathCoorInfo.get(NextLocation)[0]) != null) {
                            if (CoorSound.get(PathCoorInfo.get(NextLocation)[0]).get(PathCoorInfo.get(NextLocation)[1]) != null) {
                                Integer mp3 = CoorSound.get(PathCoorInfo.get(NextLocation)[0]).get(PathCoorInfo.get(NextLocation)[1]);
                                if (mp3.equals(4)) {
                                    switch (LocationEng.get(NextBuilding)) {
                                        case "William M.W. Mong Engineering Building G/F":
                                            ChiMessage.setText("入升降機後按G字.");
                                            EngMessage.setText("Please press G/F.");
                                            break;
                                        case "William M.W. Mong Engineering Building 9/F":
                                            ChiMessage.setText("入升降機後按9字.");
                                            EngMessage.setText("Please press 9/F.");
                                            break;
                                        case "William M.W. Mong Engineering Building 4/F":
                                            ChiMessage.setText("入升降機後按4字.");
                                            EngMessage.setText("Please press 4/F.");
                                            break;
                                        default:
                                            ChiMessage.setText("請入升降機.");
                                            EngMessage.setText("Please take the lift.");
                                    }
                                } else {
                                    if (getDirection(PathCoorInfo.get(NextLocation - 1), PathCoorInfo.get(NextLocation), PathCoorInfo.get(NextLocation + 1))) {
                                        ChiMessage.setText("請轉右.");
                                        EngMessage.setText("Please turn right.");
                                    } else {
                                        ChiMessage.setText("請轉左.");
                                        EngMessage.setText("Please turn left.");
                                    }
                                }
                            }
                        }
                        NextLocation++;
                        updatePathOnMap();
                        IsAnnounced = false;
                        IsRemovedArrow = false;
                    } else {
                        Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                        mVibrator.vibrate(500);

                        ChiMessage.setText("你已到達終點:" + LocationChi.get(LocationChi.size() - 1));
                        EngMessage.setText("You arrived the end point, " + LocationEng.get(LocationEng.size() - 1));

                        //Language
                        MediaPlayer mMediaPlayer;
                        if(lang.equals("eng")){
                            mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.end_eng);
                        }else{
                            mMediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.end_can);
                        }

                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });

                        mMediaPlayer.start();

                        IsEnd = true;
                        PathOnMap.remove();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Delays is running.");
                                onBackPressed();
                            }
                        }, 2000);
                    }
                }
            }catch (Exception e){

            }
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CurrentLocation[0], CurrentLocation[1])));
    }

    private void updatePathOnMap(){
        CurrentPathCoorInfo.remove(0);

        ArrayList<LatLng> coorList = new ArrayList<>();
        for(int i=0;i<CurrentPathCoorInfo.size();i++){
            coorList.add(new LatLng(CurrentPathCoorInfo.get(i)[0],CurrentPathCoorInfo.get(i)[1]));
        }

        /*
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coorList).width(10).color(Color.BLUE);
        PathOnMap = mMap.addPolyline(polylineOptions);
        */

        PathOnMap.setPoints(coorList);
    }

    private void shortenPathOnMap(double[] closestP){
        CurrentPathCoorInfo.set(0, closestP);

        ArrayList<LatLng> coorList = new ArrayList<>();
        for(int i=0;i<CurrentPathCoorInfo.size();i++){
            coorList.add(new LatLng(CurrentPathCoorInfo.get(i)[0],CurrentPathCoorInfo.get(i)[1]));
        }

        PathOnMap.setPoints(coorList);
    }

    private void updateMarker(){

    }

    private boolean IsArrivedBuilding(double[] A, double[] B){
        double err = Math.abs(A[0]-B[0]) + Math.abs(A[1]-B[1]);

        if(err < 0.000001){
            return true;
        }else{
            return false;
        }
    }

    /*
    private double[] findClosestPoint(double[] A, double[] B, double[] P){
        double[] point = MidPoint(A,B);

        double[] direction;
        int count = 0;

        if(HaverSine(A,P) < HaverSine(B,P)){
            direction = A;
        }else{
            direction = B;
        }

        while(Math.abs(Math.abs(HaverSine(P,point)) - Math.abs(CrossTrack(A,B,P))) > 2.0 && count < 20){
            point = MidPoint(direction,point);
            if(HaverSine(direction,P) > HaverSine(point,P)){
                direction = point;
            }
            //System.out.println("while loop running");
            count++;
        }

        return point;
    }
    */

    private double[] findClosestPoint(double[] A, double[] B , double[] P){
        double b = Math.abs(CrossTrack(A,B,P));
        double c = HaverSine(A,P);
        double a = Math.sqrt(Math.pow(c,2)+Math.pow(b,2));

        double f = a/HaverSine(A,B);

        return IntermediatePoint(A,B,f);
    }

    private int findNearestPath(double[] P){
        double distance;
        double min = 99999;
        int idxA=0, idxB=0;

        for(int i=NextLocation;i<PathCoorInfo.size();i++){
            distance = Math.abs(HaverSine(PathCoorInfo.get(i),P));
            if(distance < min){
                min = distance;
                idxA = i;
                idxB = i;
            }
        }
        /*
        if(HaverSine(PathCoorInfo.get(idxA),P)<HaverSine(PathCoorInfo.get(idxB),P)){
            return idxA;
        }else {
            return idxB;
        }*/
        return idxB;
    }

    private double getDistance(double[] A, double[] B) {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(B[0] - A[0]);
        Double lonDistance = Math.toRadians(B[1] - A[1]);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(A[0])) * Math.cos(Math.toRadians(B[0]))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    private boolean getDirection(double[] A, double[] B, double[] P){
        //true = right, false = left
        boolean dir;
        double[] a_to_b = {B[0]-A[0], B[1]-A[1]};
        double[] a_to_p = {P[0]-A[0], P[1]-A[1]};

        double z = a_to_b[0]*a_to_p[1]-a_to_b[1]*a_to_p[0];

        if(z > 0.0f){
            dir = true;
        }else{
            dir = false;
        }

        return dir;
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

    private double CrossTrack(double[] A, double[] B, double[] P){
        double r = 6371000.0;
        double delta13 = HaverSine(A,P)/r;
        double brng12 = Bearing(A,B);
        double brng13 = Bearing(A,P);

        return Math.asin(Math.sin(delta13) * Math.sin(brng13-brng12)) * r;
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

    private double[] IntermediatePoint(double[] A, double[] B, double frac){
        double phi1 = Math.toRadians(A[0]);
        double phi2 = Math.toRadians(B[0]);
        double lambda1 = Math.toRadians(A[1]);
        double lambda2 = Math.toRadians(B[1]);

        double r = 6371000.0;

        double a = Math.sin((1 - frac) * HaverSine(A,B)/r)/Math.sin(HaverSine(A,B)/r);
        double b = Math.sin((frac) * HaverSine(A,B)/r)/Math.sin(HaverSine(A,B)/r);
        double i = a * Math.cos(phi1) * Math.cos(lambda1) + b * Math.cos(phi2) * Math.cos(lambda2);
        double j = a * Math.cos(phi1) * Math.sin(lambda1) + b * Math.cos(phi2) * Math.sin(lambda2);
        double k = a * Math.sin(phi1) + b * Math.sin(phi2);
        double phi3 = Math.atan2(k,Math.sqrt(Math.pow(i,2)+Math.pow(j,2)));
        double lambda3 = Math.atan2(j,i);

        return new double[]{Math.toDegrees(phi3),Math.toDegrees(lambda3)};
    }

    private void putCoorSound(){

        //1 is road, 2 is zebra, 3 is normal turn

        CoorSound.put(22.415853 ,  new HashMap<Double,Integer>(){{put(114.211126,2);}});
        CoorSound.put(22.416051 ,  new HashMap<Double,Integer>(){{put(114.210907,3);}});
        CoorSound.put(22.417296 ,  new HashMap<Double,Integer>(){{put(114.211591,3);}});
        CoorSound.put(22.417453 ,  new HashMap<Double,Integer>(){{put(114.210988,1);}});
        CoorSound.put(22.417603 ,  new HashMap<Double,Integer>(){{put(114.210987,1);}});
        CoorSound.put(22.418181 ,  new HashMap<Double,Integer>(){{put(114.207974,4);}});

    }
}
