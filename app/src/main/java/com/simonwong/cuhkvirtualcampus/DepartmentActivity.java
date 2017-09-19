package com.simonwong.cuhkvirtualcampus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActivity extends FragmentActivity implements OnMapReadyCallback {

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
    final ArrayList<String> PhotoIdForMarker = new ArrayList<>();

    boolean language = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

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

        /*
        DeptName.add("January");
        DeptName.add("February");
        DeptName.add("March");
        DeptName.add("April");
        DeptName.add("May");
        DeptName.add("June");
        DeptName.add("July");
        DeptName.add("August");
        DeptName.add("September");
        DeptName.add("October");
        DeptName.add("November");
        DeptName.add("December");
        */

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

                int id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());
                InfoImage.setImageResource(id);

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
        });

        LatLng MiddlePoint = new LatLng(22.419491, 114.206975);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MiddlePoint));

        //Mark the marker for all buildings
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
                PhotoIdForMarker.add(tmp.getPhoto());
            }
        }

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
                id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());
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

        int id = DepartmentActivity.this.getResources().getIdentifier(MarkersPhoto.get(marker), "drawable", DepartmentActivity.this.getPackageName());
        InfoImage.setImageResource(id);

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
