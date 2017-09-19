package com.simonwong.cuhkvirtualcampus;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;

public class CallForHelpDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String name, start, end, phone, message, key;
    double coor_x, coor_y;

    TextView DetailsName, DetailsStart, DetailsEnd, DetailsPhone, DetailsMessage;
    Button AcceptButton, CancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_for_help_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DetailsName = (TextView)findViewById(R.id.request_destails_text_name);
        DetailsStart = (TextView)findViewById(R.id.request_destails_text_start);
        DetailsEnd = (TextView)findViewById(R.id.request_destails_text_end);
        DetailsPhone = (TextView)findViewById(R.id.request_destails_text_phone);
        DetailsMessage = (TextView)findViewById(R.id.request_destails_text_message);

        AcceptButton = (Button)findViewById(R.id.acceptButton);
        CancelButton = (Button)findViewById(R.id.cancelButton);

        Bundle messages = getIntent().getExtras();
        name = messages.getString("name");
        start = messages.getString("from");
        end = messages.getString("to");
        phone = messages.getString("phone");
        message = messages.getString("message");
        coor_x = Double.parseDouble(messages.getString("coor_x"));
        coor_y = Double.parseDouble(messages.getString("coor_y"));
        key = messages.getString("key");

        DetailsName.setText(name);
        DetailsStart.setText(start);
        DetailsEnd.setText(end);
        DetailsPhone.setText(phone);
        DetailsMessage.setText(message);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pop up form to input the information of accepter
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                // set title
                alertDialogBuilder.setTitle("Your Information");

                //set all the text view
                View ContentsView = CallForHelpDetailsActivity.this.getLayoutInflater().inflate(R.layout.call_for_help_accept_form, null);

                final EditText AcceptName = (EditText) ContentsView.findViewById(R.id.accept_edit_name);
                final EditText AcceptPhone = (EditText) ContentsView.findViewById(R.id.accept_edit_phone);
                final EditText AcceptMessage = (EditText) ContentsView.findViewById(R.id.accept_edit_message);

                final Button AcceptCancelButton = (Button) ContentsView.findViewById(R.id.cancelButton);
                final Button AcceptSendButton = (Button) ContentsView.findViewById(R.id.sendButton);

                final Button AcceptNameButton = (Button) ContentsView.findViewById(R.id.AcceptNameButton);
                final Button AcceptPhoneButton = (Button) ContentsView.findViewById(R.id.AcceptPhoneButton);
                final Button AcceptMessageButton = (Button) ContentsView.findViewById(R.id.AcceptMessageButton);

                final TextView NameWarningText = (TextView) ContentsView.findViewById(R.id.accept_warning_name);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false);

                alertDialogBuilder.setView(ContentsView);

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                AcceptName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                            NameWarningText.setVisibility(View.INVISIBLE);
                        }else{
                            AcceptSendButton.setEnabled(false);
                            NameWarningText.setVisibility(View.VISIBLE);
                        }

                        if (AcceptName.getText().toString().equals("")) {
                            AcceptNameButton.setVisibility(View.GONE);
                        } else {
                            AcceptNameButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                        }else{
                            AcceptSendButton.setEnabled(false);
                        }
                    }
                });

                AcceptNameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AcceptName.setText("");
                    }
                });

                AcceptPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                        }else{
                            AcceptSendButton.setEnabled(false);
                        }

                        if (AcceptPhone.getText().toString().equals("")) {
                            AcceptPhoneButton.setVisibility(View.GONE);
                        } else {
                            AcceptPhoneButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                        }else{
                            AcceptSendButton.setEnabled(false);
                        }
                    }
                });

                AcceptPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AcceptPhone.setText("");
                    }
                });

                AcceptMessage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                        }else{
                            AcceptSendButton.setEnabled(false);
                        }

                        if (AcceptMessage.getText().toString().equals("")) {
                            AcceptMessageButton.setVisibility(View.GONE);
                        } else {
                            AcceptMessageButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!AcceptName.getText().toString().equals("")) {
                            AcceptSendButton.setEnabled(true);
                        }else{
                            AcceptSendButton.setEnabled(false);
                        }
                    }
                });

                AcceptMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AcceptMessage.setText("");
                    }
                });

                AcceptSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        Intent intent = new Intent();
                        Bundle b = new Bundle();

                        String phone = AcceptPhone.getText().toString();
                        if(phone.length() <8 ){
                            phone = null;
                        }

                        b.putString("AcceptedRequestKey", key);
                        b.putString("AccepterName",AcceptName.getText().toString());
                        b.putString("AccepterPhone",phone);
                        b.putString("AccepterMessage",AcceptMessage.getText().toString());

                        intent.putExtras(b);

                        CallForHelpDetailsActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                AcceptCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
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
        mMap.getUiSettings().setMapToolbarEnabled(true);

        LatLng CurrentPoint = new LatLng(coor_x, coor_y);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentPoint));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(coor_x,coor_y)));
    }
}
