/*Disabled bus/disabled button and C.W.C college area 39*/

package com.simonwong.cuhkvirtualcampus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter0,adapter1;

    // Search EditText
    EditText StartSearch, EndSearch;

    // ArrayList for Listview
    //ArrayList<HashMap<String, String>> locationlist;

    // Cross Button
    Button StartButton, EndButton;
    ImageButton DisabledButton, SearchButton, BusButton, UndoButton, SwapButton;

    // Start and End
    String start,end;

    //Current field
    boolean field = true;
    boolean language = true;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI(findViewById(R.id.main_layout));

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("MainfirstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, FrontIntroActivity.class);

                    i.putExtra("LaunchFrom", "main");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("MainfirstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        // Listview Data
        //final String locations[] = {"MTR","ERB","SHB","CC can","UC can","CC lib","U lib","med can","sir run run"
        //,"YIA","MMW","ATI","WMY","Ho suk","science centre","healt","SHHO","morningside"};
        final String locations[] = {
                /*"Current Location",*/"An Integrated Teaching Building",
                "Art Museum","Benjamin Franklin Centre","Benjamin Franklin Centre Coffee Corner","Benjamin Franklin Centre Student Canteen","Ch'ien Mu Library",
                "Chen Kou Bun Building","Cheng Ming Building","Cheng Yu Tung Building","Cheung Chuk Shan Amenities Building","Choh-Ming Li Basic Medical Science Building",
                "Chung Chi College Chapel","Chung Chi College Staff Club Clubhouse","Chung Chi Garden","Chung Chi Tang","Cultural Square",/*"C.W.Chu College",*/
                "Elisabeth Luce Moore Library","Esther Lee Building","Fung King Hey Building","Ho Sin-Hang Engineering Building 5/F","Ho Sin-Hang Engineering Building G/F",
                "Ho Tim Building","Huen Wing Ming Building","Hui Yeung Shing Building","Humanities Building","Institute of Chinese Studies",
                "John Fulton Centre","Lady Shaw Building","Lake Ad Excellentiam","Lee Shau Kee Building","Lee Woo Sing College",
                "Leung Kau Kui Building","Li Dak Sum Building","Li Wai Chun Building",/*"Lo Kwee-Seong Integrated Biomedical Sciences Building",*/"Mong Man Wai Building","Morningside College",
                "MTR Station","Orchid Lodge","Park'n Shop Supermarket","Pavilion of Harmony","Pentecostal Mission Hall Complex(Low Block)",
                "Pi Ch'iu Building","Pommerenke Student Centre","S.H. Ho College","Science Centre East Block","Science Centre North Block",
                "Science Centre South Block","Shaw College Gymnasium","Shaw College Lecture Theatre","Sino Building","Sir Run Run Shaw Hall",
                "Staff Student Centre Leung Hung Kee Building","Sui Loong Pao Building","Swimming Pool","T.C. Cheng Building","The University Mall",
                "Tsang Shiu Tim Building","University Administration Building","University Health Centre","University Library","Wen Lan Tang",
                "William M.W. Mong Engineering Building 4/F","William M.W. Mong Engineering Building 9/F","William M.W. Mong Engineering Building G/F","Women Workers' Cooperation","Wong Foo Yuan Building",
                "Wu Chung Multimedia Library","Wu Ho Man Yuen Building","Wu Yee Sun College","Y.C. Liang Hall","Yasumoto International Academic Park"
        };

        final String locations_chi[] = {
                /*"目前位置",*/"綜合教學大樓",
                "文物館","范克廉樓","范克廉樓咖啡閣","范克廉樓學生膳堂","錢穆圖書館",
                "陳國本樓","誠明館","鄭裕彤樓","張祝珊師生康樂大樓","李卓敏基本醫學大樓",
                "崇基教堂","聯誼會","何草","眾志堂","文化廣場",
                "牟路思怡圖書館","利黃瑤璧樓","馮景禧樓","何善衡工程學大樓5樓","何善衡工程學大樓地下",
                "何添樓","禤永明樓多功能學習中心","許讓成樓","人文館","中國文化研究所",
                "富爾敦樓","邵逸夫夫人樓","未圓湖","李兆基樓","和聲書院",
                "梁銶琚樓","李達三樓","李慧珍樓","蒙民偉樓","晨興書院",
                "港鐵站","蘭苑","百佳超級市場","合一亭","五旬節會樓低座",
                "碧秋樓","龐萬倫學生中心禮堂","善衡書院","科學館東座","科學館北座高錕樓",
                "科學館南座馬臨樓","逸夫書院體育館","逸夫書院大講堂","信和樓","邵逸夫堂",
                "樂群館梁雄姬樓","兆龍樓","游泳池","鄭棟材樓","林蔭大道",
                "曾肇添樓","大學行政樓","大學保健醫療中心","大學圖書館","文瀾堂",
                "蒙民偉工程學大樓4樓","蒙民偉工程學大樓9樓","蒙民偉工程學大樓地下","女工合作社","王福元樓",
                "胡忠多媒體圖書館","伍何曼原樓","伍宜孫書院","潤昌堂","康本國際學術園"/*,"敬文書院","羅桂祥綜合生物醫學大樓"*/
        };

        final String abbr[] = {
                "ATI",
                "AM","BFC","Coffee Con","Franklin","NA Lib",
                "CKB","NAA","CYT","CCSAB","BMSB",
                "CCCC","CCCS","CCG","CC Can","CS",
                "CC Lib","ELB","FKH","SHB(5/F)","SHB(G/F)",
                "HTB","HWM","HYS","NAH","ICS",
                "JFC","LSB","Lake","LSK","WS",
                "KKB","LDS","LWCB","MMW","MC",
                "MTR","OL Can","Market","POH","PMHCL",
                "PCB","PSC","SHHO","SCE","SCN",
                "SCS","Shaw gym","SWC","SB","RRS",
                "LHKB","SLP","Pool","UCC","Mall",
                "UCA","UAB","UHC","U Lib","WLS",
                "ERB(4/F)","ERB(9/F)","ERB(G/F)","WWC","FYB",
                "UC Lib","WMY","WYS","LHC","YIA","LKS",
        };

        lv = (ListView) findViewById(R.id.list_view);

        StartSearch = (EditText) findViewById(R.id.StartSearch);
        EndSearch = (EditText) findViewById(R.id.EndSearch);

        StartButton = (Button) findViewById(R.id.StartButton);
        EndButton = (Button) findViewById(R.id.EndButton);
        DisabledButton = (ImageButton) findViewById(R.id.DiabledButton);
        SearchButton = (ImageButton) findViewById(R.id.SearchButton);
        BusButton = (ImageButton) findViewById(R.id.BusButton);
        UndoButton = (ImageButton) findViewById(R.id.UndoButton);
        SwapButton = (ImageButton) findViewById(R.id.SwapButton);

        // Adding items to listview
        adapter0 = new ArrayAdapter<String>(this, R.layout.list_item, R.id.location_name, locations);
        adapter1 = new ArrayAdapter<String>(this, R.layout.list_item, R.id.location_name, locations_chi);

        lv.setAdapter(adapter0);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
                if (field) {
                    StartSearch.setText(value);
                    StartSearch.setSelection(StartSearch.length());
                } else {
                    EndSearch.setText(value);
                    EndSearch.setSelection(EndSearch.length());
                }
            }
        });

        StartSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                field = true;
                boolean IsEng = StartSearch.getText().toString().matches("^[\u0000-\u0080]+$");

                if (StartSearch.getText().toString().equals("")) {
                    MainActivity.this.adapter0.getFilter().filter("");
                    MainActivity.this.adapter1.getFilter().filter("");
                    /*
                    if (IsEng) {
                        MainActivity.this.adapter0.getFilter().filter("");
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter("");
                        lv.setAdapter(adapter1);
                    }
                    */
                } else {
                    if (IsEng) {
                        MainActivity.this.adapter0.getFilter().filter(StartSearch.getText().toString());
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter(StartSearch.getText().toString());
                        lv.setAdapter(adapter1);
                    }
                }

                return false;
            }
        });

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSearch.setText("");
            }
        });

        StartSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String filterString = cs.toString();
                boolean IsEng = filterString.matches("^[\u0000-\u0080]+$");

                if (IsEng) {
                    MainActivity.this.adapter0.getFilter().filter(cs);
                    lv.setAdapter(adapter0);
                    language = true;
                } else if (filterString.equals("")) {
                    if (language) {
                        MainActivity.this.adapter0.getFilter().filter(cs);
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter(cs);
                        lv.setAdapter(adapter1);
                    }
                } else {
                    MainActivity.this.adapter1.getFilter().filter(cs);
                    lv.setAdapter(adapter1);
                    language = false;
                }

                if (StartSearch.getText().toString().equals("")) {
                    StartButton.setVisibility(View.GONE);
                } else {
                    StartButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        EndSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                field = false;
                boolean IsEng = EndSearch.getText().toString().matches("^[\u0000-\u0080]+$");

                if (EndSearch.getText().toString().equals("")) {
                    MainActivity.this.adapter0.getFilter().filter("");
                    MainActivity.this.adapter1.getFilter().filter("");
                    /*
                    if (IsEng) {
                        MainActivity.this.adapter0.getFilter().filter("");
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter("");
                        lv.setAdapter(adapter1);
                    }
                    */
                } else {
                    if (IsEng) {
                        MainActivity.this.adapter0.getFilter().filter(EndSearch.getText().toString());
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter(EndSearch.getText().toString());
                        lv.setAdapter(adapter1);
                    }
                }

                return false;
            }
        });

        EndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndSearch.setText("");
            }
        });

        EndSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String filterString = cs.toString();
                boolean IsEng = filterString.matches("^[\u0000-\u0080]+$");

                if (IsEng) {
                    MainActivity.this.adapter0.getFilter().filter(cs);
                    lv.setAdapter(adapter0);
                    language = true;
                } else if (filterString.equals("")) {
                    if (language) {
                        MainActivity.this.adapter0.getFilter().filter(cs);
                        lv.setAdapter(adapter0);
                    } else {
                        MainActivity.this.adapter1.getFilter().filter(cs);
                        lv.setAdapter(adapter1);
                    }
                } else {
                    MainActivity.this.adapter1.getFilter().filter(cs);
                    lv.setAdapter(adapter1);
                    language = false;
                }

                if (EndSearch.getText().toString().equals("")) {
                    EndButton.setVisibility(View.GONE);
                } else {
                    EndButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        SwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = StartSearch.getText().toString();
                StartSearch.setText(EndSearch.getText().toString());
                EndSearch.setText(tmp);

                if (field) {
                    StartSearch.setSelection(StartSearch.length());
                } else {
                    EndSearch.setSelection(EndSearch.length());
                }
            }
        });

        UndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSearch.setText("");
                EndSearch.setText("");

                if (field) {
                    StartSearch.setSelection(StartSearch.length());
                } else {
                    EndSearch.setSelection(EndSearch.length());
                }
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathInfoActivity.class);
                start = StartSearch.getText().toString();
                end = EndSearch.getText().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // set title
                alertDialogBuilder.setTitle("Warning");
                // set dialog message
                alertDialogBuilder
                        .setMessage("請輸入正確(不同)的位置名稱.\nPlease enter correct(different) location's name.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                if (((Arrays.asList(locations).contains(start) || Arrays.asList(locations_chi).contains(start) || Arrays.asList(abbr).contains(start)) && (Arrays.asList(locations).contains(end) || Arrays.asList(locations_chi).contains(end) || Arrays.asList(abbr).contains(end)) && !start.equals(end))) {
                    String lang;

                    if(IsEng(start) && IsEng(end)){
                        lang = "eng";
                    }else{
                        lang = "chi";
                    }

                    if(start.equals("敬文書院") || start.equals("羅桂祥綜合生物醫學大樓") || end.equals("敬文書院") || end.equals("羅桂祥綜合生物醫學大樓") || start.equals("C.W.Chu College") || start.equals("Lo Kwee-Seong Integrated Biomedical Sciences Building") || end.equals("C.W.Chu College") || end.equals("Lo Kwee-Seong Integrated Biomedical Sciences Building")){
                        intent.putExtra("from", start);
                        intent.putExtra("to", end);
                        intent.putExtra("lang", lang);
                        intent.putExtra("bus","true");
                        intent.putExtra("activity","main");
                    }else {
                        intent.putExtra("from", start);
                        intent.putExtra("to", end);
                        intent.putExtra("lang", lang);
                        intent.putExtra("bus","false");
                        intent.putExtra("activity","main");
                    }

                    startActivityForResult(intent, 0);
                    finish();
                } else {

                    alertDialog.show();
                }
            }
        });

        BusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathInfoActivity.class);
                start = StartSearch.getText().toString();
                end = EndSearch.getText().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                /*
                // set title
                alertDialogBuilder.setTitle("Warning");

                // set dialog message
                alertDialogBuilder
                        .setMessage("請輸入正確(不同)的位置名稱.\nPlease enter correct(different) location's name.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                if (((Arrays.asList(locations).contains(start) || Arrays.asList(locations_chi).contains(start) || Arrays.asList(abbr).contains(start)) && (Arrays.asList(locations).contains(end) || Arrays.asList(locations_chi).contains(end) || Arrays.asList(abbr).contains(end)) && !start.equals(end))) {
                    String lang;

                    if(IsEng(start) && IsEng(end)){
                        lang = "eng";
                    }else{
                        lang = "chi";
                    }

                    intent.putExtra("from", start);
                    intent.putExtra("to", end);
                    intent.putExtra("lang", lang);
                    intent.putExtra("bus","true");
                    intent.putExtra("activity","main");

                    Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                    toast.show();

                    startActivityForResult(intent, 0);
                    finish();
                } else {
                    alertDialog.show();
                }
                */

                Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        DisabledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathInfoActivity.class);
                start = StartSearch.getText().toString();
                end = EndSearch.getText().toString();

                /*
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // set title
                alertDialogBuilder.setTitle("Warning");

                // set dialog message
                alertDialogBuilder
                        .setMessage("請輸入正確(不同)的位置名稱.\nPlease enter correct(different) location's name.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                if (((Arrays.asList(locations).contains(start) || Arrays.asList(locations_chi).contains(start) || Arrays.asList(abbr).contains(start)) && (Arrays.asList(locations).contains(end) || Arrays.asList(locations_chi).contains(end) || Arrays.asList(abbr).contains(end)) && !start.equals(end))) {
                    String lang;

                    if(IsEng(start) && IsEng(end)){
                        lang = "eng";
                    }else{
                        lang = "chi";
                    }

                    if(start.equals("敬文書院") || start.equals("羅桂祥綜合生物醫學大樓") || end.equals("敬文書院") || end.equals("羅桂祥綜合生物醫學大樓") || start.equals("C.W.Chu College") || start.equals("Lo Kwee-Seong Integrated Biomedical Sciences Building") || end.equals("C.W.Chu College") || end.equals("Lo Kwee-Seong Integrated Biomedical Sciences Building")){
                        intent.putExtra("from", start);
                        intent.putExtra("to", end);
                        intent.putExtra("lang", lang);
                        intent.putExtra("bus","true");
                        intent.putExtra("activity","main");
                    }else {
                        intent.putExtra("from", start);
                        intent.putExtra("to", end);
                        intent.putExtra("lang", lang);
                        intent.putExtra("bus", "disabled");
                        intent.putExtra("activity", "main");
                    }

                    startActivityForResult(intent, 0);
                    finish();
                } else {
                    alertDialog.show();
                }
                */

                Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private boolean IsEng(String str){
        return str.matches("^[\u0000-\u0080]+$");
    }

}
