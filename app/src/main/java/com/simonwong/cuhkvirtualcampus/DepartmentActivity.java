/*Disabled "Go Here" button */

package com.simonwong.cuhkvirtualcampus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;

    SearchView DeptSearchBar;
    ListView DeptNameList;
    ImageButton DeptHomeButton;
    Fragment map;

    ArrayAdapter<String> DeptNameAdapter, DeptNameAdapterChi;

    LocationList location_list = new LocationList(0);

    ArrayList<String> DeptName = new ArrayList<>();
    ArrayList<String> DeptNameChi = new ArrayList<>();

    private ArrayList<Marker> Markers = new ArrayList<>();
    private Map<Marker, Location> MarkLocation = new HashMap<>();
    private Map<Marker, String> MarkersChi = new HashMap<Marker, String>();
    private Map<Marker, String> MarkersEng = new HashMap<Marker, String>();
    private Map<Marker, Integer> MarkersRe = new HashMap<>();
    private Map<Marker, String> MarkersPhoto = new HashMap<Marker, String>();
    private Map<Marker, Boolean> MarkersPhotoLoaded = new HashMap<>();
    final ArrayList<String> PhotoIdForMarker = new ArrayList<>();

    boolean language = true;
    boolean waterCooler = false;

    private Spinner spinner;
    private static final String[]paths = {"All Departments", "Toilets", "Water Coolers", "Canteen", "Sports Arena", "Super Market"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("DeptfirstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(DepartmentActivity.this, FrontIntroActivity.class);

                    i.putExtra("LaunchFrom", "department");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("DeptfirstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DeptSearchBar = (SearchView) findViewById(R.id.DeptSearch);
        DeptNameList = (ListView) findViewById(R.id.DeptName);
        DeptHomeButton = (ImageButton) findViewById(R.id.DepartmentHomeButton);
        map = getSupportFragmentManager().findFragmentById(R.id.map);
        DeptSearchBar.setSubmitButtonEnabled(true);
        //DeptSearch.onActionViewExpanded();

        for(Location location:location_list.getLocationlist()){
            for(Centre centre:location.getCentre()){
                DeptName.add(centre.getEng());
                DeptNameChi.add(centre.getChi());
            }
        }

        Collections.sort(DeptName);

        DeptNameAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.location_name, DeptName);
        DeptNameAdapterChi = new ArrayAdapter<String>(this, R.layout.list_item, R.id.location_name, DeptNameChi);
        DeptNameList.setAdapter(DeptNameAdapter);

        DeptSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepartmentActivity.this);

                // set title
                alertDialogBuilder.setTitle("Warning");

                // set dialog message
                alertDialogBuilder
                        .setMessage("請輸入正確的部門/中心名稱.\nPlease enter correct department/center's name.")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                if (DeptName.contains(query) || DeptNameChi.contains(query)) {
                    Location TargetLocation = DeptBelongsTo(query);
                    Marker TargetMarker = null;
                    for(Marker key:MarkLocation.keySet()) {
                        if (MarkLocation.get(key).getIndex().equals(TargetLocation.getIndex())) {
                            TargetMarker = key;
                            break;
                        }
                    }

                    DeptNameList.setVisibility(View.GONE);
                    map.getView().setVisibility(View.VISIBLE);

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(TargetLocation.getCoor_x(), TargetLocation.getCoor_y())), 1000, null);
                    TargetMarker.showInfoWindow();
                    DisplayInfoWindows(TargetMarker);

                } else {
                    alertDialog.show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (IsEng(newText)) {
                    DeptNameAdapter.getFilter().filter(newText);
                    DeptNameList.setAdapter(DeptNameAdapter);
                    language = true;
                } else if (newText.equals("")) {
                    if (language) {
                        DeptNameAdapter.getFilter().filter(newText);
                        DeptNameList.setAdapter(DeptNameAdapter);
                    } else {
                       DeptNameAdapterChi.getFilter().filter(newText);
                        DeptNameList.setAdapter(DeptNameAdapterChi);
                    }
                } else {
                   DeptNameAdapterChi.getFilter().filter(newText);
                    DeptNameList.setAdapter(DeptNameAdapterChi);
                    language = false;
                }

                DeptNameList.setVisibility(View.VISIBLE);
                map.getView().setVisibility(View.GONE);

                //DeptNameAdapter.getFilter().filter(newText);
                return false;
            }
        });

        DeptSearchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                DeptNameList.setVisibility(View.GONE);
                map.getView().setVisibility(View.VISIBLE);
                return false;
            }
        });

        DeptSearchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeptNameList.setVisibility(View.VISIBLE);
                map.getView().setVisibility(View.GONE);
            }
        });

        DeptNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
                DeptSearchBar.setQuery(value,false);
            }
        });

        DeptHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), FrontActivity.class);

                startActivityForResult(intent, 0);
                finish();
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(DepartmentActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                //Mark the marker for all buildings
                waterCooler = false;
                mMap.clear();
                for(Location tmp : location_list.getLocationlist()){
                    Marker marker;
                    //Mark the marker if size of center > 0
                    if(tmp.getCentre().size()>0) {

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(tmp.getCoor_x(), tmp.getCoor_y())));

                        Markers.add(marker);
                        MarkLocation.put(marker, tmp);
                        MarkersChi.put(marker, tmp.getChi());
                        MarkersEng.put(marker, tmp.getEng());
                        MarkersRe.put(marker, tmp.getRegion());
                        MarkersPhoto.put(marker, tmp.getPhoto());
                        MarkersPhotoLoaded.put(marker,false);
                        PhotoIdForMarker.add(tmp.getPhoto());
                    }
                }

                break;
            case 1:

                break;
            case 2:
                waterCooler = true;
                mMap.clear();
                //Mark the marker for all Watercoolers
                for(Location tmp : location_list.getLocationlist()){
                    Marker marker;
                    //Mark the marker if size of Watercoolers > 0
                    if(tmp.getWaterCooler().size()>0) {
                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                        Bitmap bmp = Bitmap.createBitmap(160, 160, conf);
                        Canvas canvas1 = new Canvas(bmp);

                        Paint color = new Paint();
                        color.setColor(Color.BLACK);

                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inMutable = true;

                        Bitmap imageBitmap=BitmapFactory.decodeResource(getResources(),
                                R.drawable.watercooler,opt);
                        Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 160, 160, true);
                        canvas1.drawBitmap(resized, 40, 40, color);

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(tmp.getCoor_x(), tmp.getCoor_y())).icon(BitmapDescriptorFactory.fromBitmap(bmp)));

                        Markers.add(marker);
                        MarkLocation.put(marker, tmp);
                        MarkersChi.put(marker, tmp.getChi());
                        MarkersEng.put(marker, tmp.getEng());
                        MarkersRe.put(marker, tmp.getRegion());
                        MarkersPhoto.put(marker, tmp.getPhoto());
                        MarkersPhotoLoaded.put(marker,false);
                        PhotoIdForMarker.add(tmp.getPhoto());
                    }
                }
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                waterCooler = false;
                mMap.clear();
                //Mark the marker for all Watercoolers
                for(Location tmp : location_list.getLocationlist()){
                    Marker marker;
                    //Mark the marker for index number "46"
                    if(tmp.getIndex() == "46") {
                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                        Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
                        Canvas canvas1 = new Canvas(bmp);

                        Paint color = new Paint();
                        color.setColor(Color.BLACK);

                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inMutable = true;

                        Bitmap imageBitmap=BitmapFactory.decodeResource(getResources(),
                                R.drawable.supermarket,opt);
                        Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 100, 100, true);
                        canvas1.drawBitmap(resized, 12, 10, color);

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(tmp.getCoor_x(), tmp.getCoor_y())).icon(BitmapDescriptorFactory.fromBitmap(bmp)));

                        Markers.add(marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                        MarkLocation.put(marker, tmp);
                        MarkersChi.put(marker, tmp.getChi());
                        MarkersEng.put(marker, tmp.getEng());
                        MarkersRe.put(marker, tmp.getRegion());
                        MarkersPhoto.put(marker, tmp.getPhoto());
                        MarkersPhotoLoaded.put(marker,false);
                        PhotoIdForMarker.add(tmp.getPhoto());
                    }
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Display the details informaiton of each building, e.g. Departments information
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepartmentActivity.this);

                // set title
                alertDialogBuilder.setTitle(MarkersChi.get(marker) + " " + MarkersEng.get(marker));

                //set image
                View ContentsView = getLayoutInflater().inflate(R.layout.info_windows_image, null);
                ImageView InfoImage = ((ImageView)ContentsView.findViewById(R.id.location_image));
                ListView InfoMessage = ((ListView)ContentsView.findViewById(R.id.dept));

                //int id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());
                //InfoImage.setImageResource(id);

//                String url = "https://s3-ap-southeast-1.amazonaws.com/cuhk-images/" + MarkersPhoto.get(marker) + ".png";
                String url = MarkersPhoto.get(marker);

                Picasso.with(getApplicationContext()).load(url).placeholder(R.layout.animation).into(InfoImage);

                final Location CurrentLocation = MarkLocation.get(marker);


                ArrayAdapter adapter;

                if(waterCooler == true){
                    adapter = new WaterCoolerAdapter(CurrentLocation.getWaterCooler());
                }else{
                    adapter = new MyListAdapter(CurrentLocation.getCentre());
                }

                InfoMessage.setAdapter(adapter);

                alertDialogBuilder.setView(ContentsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Go here", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(DepartmentActivity.this, PathInfoActivity.class);
                                String start = "Current Location";
                                String end = CurrentLocation.getEng();
                                String lang;

                                /*if(IsEng(start) && IsEng(end)){
                                    lang = "eng";
                                }else{
                                    lang = "chi";
                                }
                                */

                                if(language){
                                    lang = "eng";
                                }else{
                                    lang = "chi";
                                }

                                intent.putExtra("from", start);
                                intent.putExtra("to", end);
                                intent.putExtra("lang", lang);
                                intent.putExtra("bus","false");
                                intent.putExtra("activity", "dept");

                                /*Disabled, will enable later.*/
                                Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                                toast.show();
                                //startActivityForResult(intent, 0);
                                //finish();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        LatLng MiddlePoint = new LatLng(22.419491, 114.206975);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MiddlePoint));
        /*
        //Mark the marker for all buildings
        mMap.clear();
        for(Location tmp : location_list.getLocationlist()){
            Marker marker;
            //Mark the marker if size of center > 0
            if(tmp.getCentre().size()>0) {

                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmp.getCoor_x(), tmp.getCoor_y())));

                Markers.add(marker);
                MarkLocation.put(marker, tmp);
                MarkersChi.put(marker, tmp.getChi());
                MarkersEng.put(marker, tmp.getEng());
                MarkersRe.put(marker, tmp.getRegion());
                MarkersPhoto.put(marker, tmp.getPhoto());
                MarkersPhotoLoaded.put(marker,false);
                PhotoIdForMarker.add(tmp.getPhoto());
            }
        }
        */

    }

    private class MyListAdapter extends ArrayAdapter<Centre> {

        //ArrayList<Centre> centres = new ArrayList<>();

        public MyListAdapter(ArrayList<Centre> centres) {
            super(DepartmentActivity.this, R.layout.info_windows_list_item, centres);
            //this.centres = centres;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.info_windows_list_item, parent, false);

            }

            // Fill the view
            TextView Chi = (TextView) itemView.findViewById(R.id.chi);
            TextView Eng = (TextView) itemView.findViewById(R.id.eng);
            TextView Room = (TextView) itemView.findViewById(R.id.room);
            TextView Phone = (TextView) itemView.findViewById(R.id.phone);

            Chi.setText(super.getItem(position).getChi());
            Eng.setText(super.getItem(position).getEng());
            Room.setText(super.getItem(position).getRoom());
            Phone.setText(super.getItem(position).getPhone());

            return itemView;
        }

    }

    private class WaterCoolerAdapter extends ArrayAdapter<WaterCooler> {

        //ArrayList<Centre> centres = new ArrayList<>();

        public WaterCoolerAdapter(ArrayList<WaterCooler> centres) {
            super(DepartmentActivity.this, R.layout.info_windows_watercooler_list_item, centres);
            //this.centres = centres;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.info_windows_watercooler_list_item, parent, false);

            }

            // Fill the view
            TextView Chi = (TextView) itemView.findViewById(R.id.chi);
            TextView Eng = (TextView) itemView.findViewById(R.id.eng);
            TextView location = (TextView) itemView.findViewById(R.id.location);

            Chi.setText(super.getItem(position).getChi());
            if (Chi.getText().toString().matches("")){
                Chi.setText("-");
            }
            Eng.setText(super.getItem(position).getEng());
            location.setText(super.getItem(position).getLocation());
            if (location.getText().toString().matches("")){
                location.setText("-");
            }

                return itemView;
        }

    }

    class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        public InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {}
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
                //id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());

//                String url = "https://s3-ap-southeast-1.amazonaws.com/cuhk-images/" + MarkersPhoto.get(marker) + ".png";
                String url = MarkersPhoto.get(marker);

                if(MarkersPhotoLoaded.get(marker)) {
                    Picasso.with(getApplicationContext()).load(url).into(markerImage);
                }else{
                    MarkersPhotoLoaded.put(marker,true);
                    Picasso.with(getApplicationContext()).load(url).placeholder(R.layout.animation).into(markerImage, new InfoWindowRefresher(marker));
                }

                //markerImage.setImageResource(id);
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

    private boolean IsEng(String str){
        return str.matches("^[\u0000-\u0080]+$");
    }

    private Location DeptBelongsTo(String Dept){
        Location location = null;

        for(Location tmp:location_list.getLocationlist()){
            for(Centre centre:tmp.getCentre()){
                if(centre.getEng().equals(Dept) || centre.getChi().equals(Dept)){
                    return tmp;
                }
            }
        }

        return location;

    }

    private void DisplayInfoWindows(Marker marker){
        //Display the details informaiton of each building, e.g. Departments information
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepartmentActivity.this);

        // set title
        alertDialogBuilder.setTitle(MarkersChi.get(marker) + " " + MarkersEng.get(marker));

        //set image
        View ContentsView = getLayoutInflater().inflate(R.layout.info_windows_image, null);
        ImageView InfoImage = ((ImageView)ContentsView.findViewById(R.id.location_image));
        ListView InfoMessage = ((ListView)ContentsView.findViewById(R.id.dept));

        //int id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());
        //InfoImage.setImageResource(id);

//        String url = "https://s3-ap-southeast-1.amazonaws.com/cuhk-images/" + MarkersPhoto.get(marker) + ".png";
        String url = MarkersPhoto.get(marker);

        Picasso.with(getApplicationContext()).load(url).placeholder(R.layout.animation).into(InfoImage);

        final Location CurrentLocation = MarkLocation.get(marker);

        ArrayAdapter<Centre> adapter = new MyListAdapter(CurrentLocation.getCentre());

        InfoMessage.setAdapter(adapter);

        alertDialogBuilder.setView(ContentsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Go here", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(DepartmentActivity.this, PathInfoActivity.class);
                        String start = "Current Location";
                        String end = CurrentLocation.getEng();
                        String lang;

                                /*if(IsEng(start) && IsEng(end)){
                                    lang = "eng";
                                }else{
                                    lang = "chi";
                                }
                                */

                        if(language){
                            lang = "eng";
                        }else{
                            lang = "chi";
                        }

                        intent.putExtra("from", start);
                        intent.putExtra("to", end);
                        intent.putExtra("lang", lang);
                        intent.putExtra("bus","false");
                        intent.putExtra("activity", "dept");

                        startActivityForResult(intent, 0);
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
