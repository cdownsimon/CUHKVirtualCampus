package com.simonwong.cuhkvirtualcampus;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by simonwong on 11/10/2016.
 */
public class StreetViewActivity extends AppCompatActivity {

    String start,end,lang,bus,activity;
    //TextView qeury;
    TextView ChiName, EngName;
    ImageView CurrentPhoto;
    SeekBar DragPhoto;
    ImageButton PathInfoButton,BackListButton,StreetViewHomeButton,BackMap;

    //PathList path_list = new PathList();
    ArrayList<String> PhotoList = new ArrayList<String>();
    ArrayList<String> ChiList = new ArrayList<String>();
    ArrayList<String> EngList = new ArrayList<String>();
    ArrayList<String> NumList = new ArrayList<String>();
    int Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);

        Bundle message = getIntent().getExtras();
        start = message.getString("from");
        end = message.getString("to");
        lang = message.getString("lang");
        bus = message.getString("bus");
        activity = message.getString("activity");

        PhotoList = (ArrayList<String>) getIntent().getSerializableExtra("PhotoId");
        ChiList = (ArrayList<String>) getIntent().getSerializableExtra("LocationChi");
        EngList = (ArrayList<String>) getIntent().getSerializableExtra("LocationEng");
        NumList = (ArrayList<String>) getIntent().getSerializableExtra("NumOfPhotoOfEachPath");

        //qeury = (TextView) findViewById(R.id.textView);
        ChiName = (TextView) findViewById(R.id.ChiName);
        EngName = (TextView) findViewById(R.id.EngName);
        CurrentPhoto = (ImageView) findViewById(R.id.CurrentPhoto);
        DragPhoto = (SeekBar) findViewById(R.id.seekBar);
        BackMap = (ImageButton) findViewById(R.id.backMap);
        BackListButton = (ImageButton) findViewById(R.id.BackListButton);
        StreetViewHomeButton = (ImageButton) findViewById(R.id.StreetViewHomeButton);
        PathInfoButton = (ImageButton) findViewById(R.id.PathInfoButton);

        DragPhoto.setMax(PhotoList.size() - 1);
        DragPhoto.setProgress(getIntent().getIntExtra("Progress",0));
        Progress = DragPhoto.getProgress();

        //initialize Seek bar
        initSeekbar();

        DragPhoto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Context context = StreetViewActivity.this;
                int id;
                id = context.getResources().getIdentifier(PhotoList.get(progress), "drawable", context.getPackageName());
                /*
                InputStream inputStream = getResources().openRawResource(id);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inSampleSize = 9;
                CurrentPhoto.setImageBitmap(BitmapFactory.decodeStream(inputStream, null, options));
                */

                String url = "https://s3-ap-southeast-1.amazonaws.com/cuhk-images/" + PhotoList.get(progress) + ".png";
                //CurrentPhoto.setImageResource(id);

                Picasso.with(getApplicationContext()).load(url).placeholder(R.layout.animation).into(CurrentPhoto);

                //save progress
                Progress = progress;

                //handle the text
                int LocationIndex = 0;
                int CheckLocationUpdate = LocationIndex;
                if (progress == 0) {
                    EngName.setText("You are at the " + EngList.get(0) + " now. (Starting Point)");
                    ChiName.setText("你正在" + ChiList.get(0) + ". (起點)");
                } else if (progress == DragPhoto.getMax()) {
                    EngName.setText("You are at the " + EngList.get(EngList.size() - 1) + " now. (Ending Point)");
                    ChiName.setText("你正在" + ChiList.get(ChiList.size() - 1) + ". (終點)");
                } else {
                    CheckLocationUpdate = LocationIndex;
                    for (int i = 0; i < NumList.size(); i++) {
                        if (progress == Integer.parseInt(NumList.get(i))) {
                            LocationIndex = i + 1;
                            break;
                        }
                    }
                    if (CheckLocationUpdate == LocationIndex) {
                        EngName.setText("Moving...");
                        ChiName.setText("移動中...");
                    } else {
                        EngName.setText("You are at the " + EngList.get(LocationIndex) + " now.");
                        ChiName.setText("你正在" + ChiList.get(LocationIndex) + ".");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BackMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MapsActivity.class);
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

        StreetViewHomeButton.setOnClickListener(new View.OnClickListener()  {
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

        PathInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PathInfoActivity.class);
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
    public void onBackPressed(){
        Intent intent = new Intent(StreetViewActivity.this,MapsActivity.class);
        intent.putExtra("Progress", Progress);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }


    private void initSeekbar(){
        Context ctx = StreetViewActivity.this;
        int InitialPhoto = ctx.getResources().getIdentifier(PhotoList.get(Progress), "drawable", ctx.getPackageName());

        String url = "https://s3-ap-southeast-1.amazonaws.com/cuhk-images/" + PhotoList.get(Progress) + ".png";

        Picasso.with(getApplicationContext()).load(url).into(CurrentPhoto);

        //CurrentPhoto.setImageResource(InitialPhoto);
        //qeury.setText(PhotoList.get(Progress));

        //handle the text
        int LocationIndex = 0;
        int CheckLocationUpdate = LocationIndex;
        if (Progress == 0) {
            EngName.setText("You are at the " + EngList.get(0) + " now. (Starting Point)" );
            ChiName.setText("你正在" + ChiList.get(0) + ". (起點)");
        } else if (Progress == DragPhoto.getMax()) {
            EngName.setText("You are at the " + EngList.get(EngList.size() - 1) + " now. (Ending Point)");
            ChiName.setText("你正在" + ChiList.get(ChiList.size() - 1) + ". (終點)");
        } else {
            CheckLocationUpdate = LocationIndex;
            for (int i = 0; i < NumList.size(); i++) {
                if (Progress == Integer.parseInt(NumList.get(i))) {
                    LocationIndex = i + 1;
                    break;
                }
            }
            if (CheckLocationUpdate == LocationIndex) {
                EngName.setText("Moving...");
                ChiName.setText("移動中...");
            } else {
                EngName.setText("You are at the " + EngList.get(LocationIndex) + " now.");
                ChiName.setText("你正在" + ChiList.get(LocationIndex) + ".");
            }
        }
    }
}

