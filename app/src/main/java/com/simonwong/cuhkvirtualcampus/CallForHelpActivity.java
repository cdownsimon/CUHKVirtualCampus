package com.simonwong.cuhkvirtualcampus;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CallForHelpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static CallHelpMessage MyCallHelpMessage;
    private static CallHelpMessage MyAcceptedCallHelp;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageNameView;
        public TextView messageStartView;
        public TextView messageEndView;
        public TextView messageStatusView;

        public MessageViewHolder(View v) {
            super(v);
            messageNameView = (TextView) itemView.findViewById(R.id.messageNameView);
            messageStartView = (TextView) itemView.findViewById(R.id.messageStartView);
            messageEndView = (TextView) itemView.findViewById(R.id.messageEndView);
            messageStatusView = (TextView) itemView.findViewById(R.id.messageStatusView);

            //listener set on ENTIRE ROW, you may set on individual components within a row.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
        }

        private MessageViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        public void setOnClickListener(MessageViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }

    private static final String TAG = "CallForHelpActivity";
    public static final String MESSAGES_CHILD = "messages";

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<CallHelpMessage, MessageViewHolder>
            mFirebaseAdapter;

    static double[] CurrentLocation = new double[2];
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

    //Navigation Drawer
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_for_help);

        //Temp!!!!!!!!!!!!!!!!!!!!!!
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Initialize Navigation Drawer
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestListFragment()).commit();

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DrawerItemModel[] drawerItem = new DrawerItemModel[4];

        drawerItem[0] = new DrawerItemModel(R.mipmap.request_list, "Request List");
        drawerItem[1] = new DrawerItemModel(R.mipmap.my_request, "My Request");
        drawerItem[2] = new DrawerItemModel(R.mipmap.my_accept, "My Accept");
        drawerItem[3] = new DrawerItemModel(R.mipmap.home_button,"Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        setTitle(R.string.toolbar_title);

        //handling the location request
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            CurrentLocation[0] = location.getLatitude();
            CurrentLocation[1] = location.getLongitude();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        long TimeCutOff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(10 , TimeUnit.MINUTES);

        System.out.println("Time Cut Off: " +TimeCutOff);

        Query OldRecords = mFirebaseDatabaseReference.child(MESSAGES_CHILD).orderByChild("timestamp").endAt(TimeCutOff);

        OldRecords.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot record: dataSnapshot.getChildren()){
                    record.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabaseReference.child(MESSAGES_CHILD).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                CallHelpMessage temp = dataSnapshot.getValue(CallHelpMessage.class);
                System.out.println("This message is removed: " + temp.getName());

                try {
                    if (temp.getKey().equals(MyCallHelpMessage.getKey())) {
                        MyCallHelpMessage = null;
                    }
                }catch (Exception e){

                }

                try {
                    if (temp.getKey().equals(MyAcceptedCallHelp.getKey())) {
                        MyAcceptedCallHelp = null;
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                System.out.println("Resulted key: " + data.getExtras().getString("AcceptedRequestKey"));
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(data.getExtras().getString("AcceptedRequestKey")).child("accepted").setValue(true);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(data.getExtras().getString("AcceptedRequestKey")).child("accepterName").setValue(data.getExtras().getString("AccepterName"));
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(data.getExtras().getString("AcceptedRequestKey")).child("accepterPhone").setValue(data.getExtras().getString("AccepterPhone"));
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(data.getExtras().getString("AcceptedRequestKey")).child("accepterMessage").setValue(data.getExtras().getString("AccepterMessage"));

                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(data.getExtras().getString("AcceptedRequestKey")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MyAcceptedCallHelp = dataSnapshot.getValue(CallHelpMessage.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    //Required things for Navigation Drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new RequestListFragment();
                break;
            case 1:
                fragment = new MyRequestFragment();
                break;
            case 2:
                fragment = new MyAcceptFragment();
                break;
            case 3:
                Intent intent = new Intent(CallForHelpActivity.this, FrontActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("CallForHelpActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.toolbar_title, R.string.toolbar_title);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    public static class RequestListFragment extends Fragment {

        private ImageButton addMessageButton;
        private TextView noRequestText;
        private ProgressBar mProgressBar;
        private LinearLayoutManager mLinearLayoutManager;
        private RecyclerView mMessageRecyclerView;

        // Firebase instance variables
        private DatabaseReference mFirebaseDatabaseReference;
        private FirebaseRecyclerAdapter<CallHelpMessage, MessageViewHolder>
                mFirebaseAdapter;

        public RequestListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.request_list, container, false);

            // Initialize ProgressBar and RecyclerView.
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mMessageRecyclerView = (RecyclerView) rootView.findViewById(R.id.messageRecyclerView);
            mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            mLinearLayoutManager.setStackFromEnd(false);
            mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

            // Initialize all Button and Text
            addMessageButton = (ImageButton) rootView.findViewById(R.id.addMessageButton);
            noRequestText = (TextView) rootView.findViewById(R.id.noRequestText);

            // New child entries
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

            //Check Child Count
            mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        noRequestText.setVisibility(View.INVISIBLE);
                    }else{
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        noRequestText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Populate the receyler view
            mFirebaseAdapter = new FirebaseRecyclerAdapter<CallHelpMessage,
                    MessageViewHolder>(
                    CallHelpMessage.class,
                    R.layout.item_message,
                    MessageViewHolder.class,
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {

                @Override
                protected void populateViewHolder(final MessageViewHolder viewHolder,
                                                  CallHelpMessage callHelpMessage, int position) {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    noRequestText.setVisibility(View.INVISIBLE);
                    if (callHelpMessage.getName() != null) {
                        viewHolder.messageNameView.setText(callHelpMessage.getName());
                        viewHolder.messageStartView.setText(callHelpMessage.getStart());
                        viewHolder.messageEndView.setText(callHelpMessage.getEnd());

                        if(callHelpMessage.getAccepted()){
                            viewHolder.messageStatusView.setText("ACCEPTED");
                            viewHolder.messageStatusView.setTextColor(Color.GREEN);
                        }else {
                            viewHolder.messageStatusView.setText("PENDING");
                            viewHolder.messageStatusView.setTextColor(Color.RED);
                        }
                    }
                }

                @Override
                public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    MessageViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                    viewHolder.setOnClickListener(new MessageViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String MyKey = null;

                            try {
                                MyKey = MyCallHelpMessage.getKey();
                            }catch (Exception e){

                            }

                            if(!mFirebaseAdapter.getItem(position).getKey().equals(MyKey)) {
                                if (!mFirebaseAdapter.getItem(position).getAccepted()) {
                                    Toast.makeText(rootView.getContext(), "Message: " + mFirebaseAdapter.getItem(position).getMessage(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(rootView.getContext(), CallForHelpDetailsActivity.class);

                                    intent.putExtra("name", mFirebaseAdapter.getItem(position).getName());
                                    intent.putExtra("from", mFirebaseAdapter.getItem(position).getStart());
                                    intent.putExtra("to", mFirebaseAdapter.getItem(position).getEnd());
                                    intent.putExtra("phone", mFirebaseAdapter.getItem(position).getPhone());
                                    intent.putExtra("message", mFirebaseAdapter.getItem(position).getMessage());
                                    intent.putExtra("coor_x", mFirebaseAdapter.getItem(position).getCoor_x());
                                    intent.putExtra("coor_y", mFirebaseAdapter.getItem(position).getCoor_y());
                                    intent.putExtra("key", mFirebaseAdapter.getItem(position).getKey());

                                    getActivity().startActivityForResult(intent, 0);
                                } else {
                                    Toast.makeText(rootView.getContext(), "This request is accepted by someone.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(rootView.getContext(), "This is your request, please view the details in My Request.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return viewHolder;
                }
            };

            mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                    int lastVisiblePosition =
                            mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.
                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        mMessageRecyclerView.scrollToPosition(positionStart);
                    }
                }
            });

            mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);

            addMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(MyCallHelpMessage == null) {

                        //Pop up form to input the information of request
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());

                        // set title
                        alertDialogBuilder.setTitle("Request");

                        //set all the text view
                        View ContentsView = getActivity().getLayoutInflater().inflate(R.layout.call_for_help_request_form, null);
                        final EditText RequestName = (EditText) ContentsView.findViewById(R.id.request_edit_name);

                        final AutoCompleteTextView RequestStart = (AutoCompleteTextView) ContentsView.findViewById(R.id.request_edit_start);
                        RequestStart.setThreshold(1);
                        final AutoCompleteTextView RequestEnd = (AutoCompleteTextView) ContentsView.findViewById(R.id.request_edit_end);
                        RequestEnd.setThreshold(1);

                        final EditText RequestPhone = (EditText) ContentsView.findViewById(R.id.request_edit_phone);
                        final EditText RequestMessage = (EditText) ContentsView.findViewById(R.id.request_edit_message);

                        final Button sendButton = (Button) ContentsView.findViewById(R.id.sendButton);
                        final Button cancelButton = (Button) ContentsView.findViewById(R.id.cancelButton);

                        final Button NameButton = (Button) ContentsView.findViewById(R.id.NameButton);
                        final Button StartButton = (Button) ContentsView.findViewById(R.id.StartButton);
                        final Button EndButton = (Button) ContentsView.findViewById(R.id.EndButton);
                        final Button PhoneButton = (Button) ContentsView.findViewById(R.id.PhoneButton);
                        final Button MessageButton = (Button) ContentsView.findViewById(R.id.MessageButton);

                        final TextView NameWarningText = (TextView) ContentsView.findViewById(R.id.request_warning_name);
                        final TextView StartWarningText = (TextView) ContentsView.findViewById(R.id.request_warning_start);
                        final TextView EndWarningText = (TextView) ContentsView.findViewById(R.id.request_warning_end);

                        final String locations[] = {
                                "Current Location", "An Integrated Teaching Building",
                                "Art Museum", "Benjamin Franklin Centre", "Benjamin Franklin Centre Coffee Corner", "Benjamin Franklin Centre Student Canteen", "Ch'ien Mu Library",
                                "Chen Kou Bun Building", "Cheng Ming Building", "Cheng Yu Tung Building", "Cheung Chuk Shan Amenities Building", "Choh-Ming Li Basic Medical Science Building",
                                "Chung Chi College Chapel", "Chung Chi College Staff Club Clubhouse", "Chung Chi Garden", "Chung Chi Tang", "Cultural Square",
                                "Elisabeth Luce Moore Library", "Esther Lee Building", "Fung King Hey Building", "Ho Sin-Hang Engineering Building 5/F", "Ho Sin-Hang Engineering Building G/F",
                                "Ho Tim Building", "Huen Wing Ming Building", "Hui Yeung Shing Building", "Humanities Building", "Institute of Chinese Studies",
                                "John Fulton Centre", "Lady Shaw Building", "Lake Ad Excellentiam", "Lee Shau Kee Building", "Lee Woo Sing College",
                                "Leung Kau Kui Building", "Li Dak Sum Building", "Li Wai Chun Building", "Mong Man Wai Building", "Morningside College",
                                "MTR Station", "Orchid Lodge", "Park'n Shop Supermarket", "Pavilion of Harmony", "Pentecostal Mission Hall Complex(Low Block)",
                                "Pi Ch'iu Building", "Pommerenke Student Centre", "S.H. Ho College", "Science Centre East Block", "Science Centre North Block",
                                "Science Centre South Block", "Shaw College Gymnasium", "Shaw College Lecture Theatre", "Sino Building", "Sir Run Run Shaw Hall",
                                "Staff Student Centre Leung Hung Kee Building", "Sui Loong Pao Building", "Swimming Pool", "T.C. Cheng Building", "The University Mall",
                                "Tsang Shiu Tim Building", "University Administration Building", "University Health Centre", "University Library", "Wen Lan Tang",
                                "William M.W. Mong Engineering Building 4/F", "William M.W. Mong Engineering Building 9/F", "William M.W. Mong Engineering Building G/F", "Women Workers' Cooperation", "Wong Foo Yuan Building",
                                "Wu Chung Multimedia Library", "Wu Ho Man Yuen Building", "Wu Yee Sun College", "Y.C. Liang Hall", "Yasumoto International Academic Park",

                                "目前位置", "綜合教學大樓",
                                "文物館", "范克廉樓", "范克廉樓咖啡閣", "范克廉樓學生膳堂", "錢穆圖書館",
                                "陳國本樓", "誠明館", "鄭裕彤樓", "張祝珊師生康樂大樓", "李卓敏基本醫學大樓",
                                "崇基教堂", "聯誼會", "何草", "眾志堂", "文化廣場",
                                "牟路思怡圖書館", "利黃瑤璧樓", "馮景禧樓", "何善衡工程學大樓5樓", "何善衡工程學大樓地下",
                                "何添樓", "禤永明樓多功能學習中心", "許讓成樓", "人文館", "中國文化研究所",
                                "富爾敦樓", "邵逸夫夫人樓", "未圓湖", "李兆基樓", "和聲書院",
                                "梁銶琚樓", "李達三樓", "李慧珍樓", "蒙民偉樓", "晨興書院",
                                "港鐵站", "蘭苑", "百佳超級市場", "合一亭", "五旬節會樓低座",
                                "碧秋樓", "龐萬倫學生中心禮堂", "善衡書院", "科學館東座", "科學館北座高錕樓",
                                "科學館南座馬臨樓", "逸夫書院體育館", "逸夫書院大講堂", "信和樓", "邵逸夫堂",
                                "樂群館梁雄姬樓", "兆龍樓", "游泳池", "鄭棟材樓", "林蔭大道",
                                "曾肇添樓", "大學行政樓", "大學保健醫療中心", "大學圖書館", "文瀾堂",
                                "蒙民偉工程學大樓4樓", "蒙民偉工程學大樓9樓", "蒙民偉工程學大樓地下", "女工合作社", "王福元樓",
                                "胡忠多媒體圖書館", "伍何曼原樓", "伍宜孫書院", "潤昌堂", "康本國際學術園"
                        };

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.list_item, R.id.location_name, locations);

                        RequestStart.setAdapter(adapter);
                        RequestEnd.setAdapter(adapter);

                        alertDialogBuilder.setView(ContentsView);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false);

                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        RequestName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(!RequestName.getText().toString().equals("")){
                                    NameWarningText.setVisibility(View.INVISIBLE);
                                }else{
                                    NameWarningText.setVisibility(View.VISIBLE);
                                }

                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }

                                if (RequestName.getText().toString().equals("")) {
                                    NameButton.setVisibility(View.GONE);
                                } else {
                                    NameButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }
                            }
                        });

                        NameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestName.setText("");
                            }
                        });

                        RequestStart.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(!RequestStart.getText().toString().equals("")){
                                    StartWarningText.setVisibility(View.INVISIBLE);
                                }else{
                                    StartWarningText.setVisibility(View.VISIBLE);
                                }

                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }

                                if (RequestStart.getText().toString().equals("")) {
                                    StartButton.setVisibility(View.GONE);
                                } else {
                                    StartButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }
                            }
                        });

                        StartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestStart.setText("");
                            }
                        });

                        RequestEnd.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(!RequestEnd.getText().toString().equals("")){
                                    EndWarningText.setVisibility(View.INVISIBLE);
                                }else{
                                    EndWarningText.setVisibility(View.VISIBLE);
                                }

                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }

                                if (RequestEnd.getText().toString().equals("")) {
                                    EndButton.setVisibility(View.GONE);
                                } else {
                                    EndButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }
                            }
                        });

                        EndButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestEnd.setText("");
                            }
                        });

                        RequestPhone.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }

                                if (RequestPhone.getText().toString().equals("")) {
                                    PhoneButton.setVisibility(View.GONE);
                                } else {
                                    PhoneButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }
                            }
                        });

                        PhoneButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestPhone.setText("");
                            }
                        });

                        RequestMessage.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }

                                if (RequestMessage.getText().toString().equals("")) {
                                    MessageButton.setVisibility(View.GONE);
                                } else {
                                    MessageButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if ((Arrays.asList(locations).contains(RequestStart.getText().toString()) && Arrays.asList(locations).contains(RequestEnd.getText().toString())) && !RequestStart.getText().toString().equals(RequestEnd.getText().toString()) && !RequestName.getText().toString().equals("")) {
                                    sendButton.setEnabled(true);
                                }else{
                                    sendButton.setEnabled(false);
                                }
                            }
                        });

                        MessageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestMessage.setText("");
                            }
                        });

                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String message = RequestMessage.getText().toString();

                                if (message.equals("")) {
                                    message = "I want to go to " + RequestEnd.getText().toString() + " from " + RequestStart.getText().toString() + ".";
                                }

                                String key = mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                        .push().getKey();

                                String phone = RequestPhone.getText().toString();
                                if(phone.length() <8 ){
                                    phone = null;
                                }

                                MyCallHelpMessage = new
                                        CallHelpMessage(RequestName.getText().toString(),
                                        RequestStart.getText().toString(),
                                        RequestEnd.getText().toString(),
                                        phone,
                                        message,
                                        String.valueOf(CurrentLocation[0]),
                                        String.valueOf(CurrentLocation[1]),
                                        ServerValue.TIMESTAMP,
                                        false,
                                        key,
                                        null, //Accepter Name set to Null
                                        null, //Accepter Phone set to Null
                                        null); //Accetper Message set to Null

                                // Send messages on click.
                                CallHelpMessage callHelpMessage = MyCallHelpMessage;

                                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                        .child(key).setValue(callHelpMessage);

                                System.out.println("Key: " + key);

                                mFirebaseDatabaseReference.child(MESSAGES_CHILD).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        try {
                                            if (dataSnapshot.getKey().equals(MyCallHelpMessage.getKey())) {
                                                MyCallHelpMessage = dataSnapshot.getValue(CallHelpMessage.class);
                                                System.out.println("Status: " + MyCallHelpMessage.getAccepted());
                                            }
                                        }catch (Exception e){

                                        }
                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                alertDialog.dismiss();
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }else{
                        //Pop up warning message if the user has a request
                        Toast.makeText(rootView.getContext(), "You currently have a request!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return rootView;
        }
    }

    public static class MyRequestFragment extends Fragment {

        TextView MyRequestName, MyRequestStart, MyRequestEnd, MyRequestPhone, MyRequestMessage, MyRequestStatus;
        TextView DetailsName, DetailsStart, DetailsEnd, DetailsPhone, DetailsMessage, DetailsStatus;
        TextView NoRequest;
        TextView AccepterNameText, AccepterPhoneText, AccepterMessageText;
        TextView AccepterName, AccepterPhone, AccepterMessage;
        TextView AccepterInfo;

        Button DeleteRequest;

        Button ViewRouteButton,ChatButton, PhoneButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.my_request, container, false);

            MyRequestName = (TextView)rootView.findViewById(R.id.my_request_name);
            MyRequestStart = (TextView)rootView.findViewById(R.id.my_request_start);
            MyRequestEnd = (TextView)rootView.findViewById(R.id.my_request_end);
            MyRequestPhone = (TextView)rootView.findViewById(R.id.my_request_phone);
            MyRequestMessage = (TextView)rootView.findViewById(R.id.my_request_message);
            MyRequestStatus = (TextView)rootView.findViewById(R.id.my_request_status);

            DetailsName = (TextView)rootView.findViewById(R.id.request_destails_text_name);
            DetailsStart = (TextView)rootView.findViewById(R.id.request_destails_text_start);
            DetailsEnd = (TextView)rootView.findViewById(R.id.request_destails_text_end);
            DetailsPhone = (TextView)rootView.findViewById(R.id.request_destails_text_phone);
            DetailsMessage = (TextView)rootView.findViewById(R.id.request_destails_text_message);
            DetailsStatus = (TextView)rootView.findViewById(R.id.request_destails_text_status);

            NoRequest = (TextView)rootView.findViewById(R.id.request_destails_no_request);

            AccepterNameText = (TextView)rootView.findViewById(R.id.my_request_accepter_name);
            AccepterPhoneText = (TextView)rootView.findViewById(R.id.my_request_accepter_phone);
            AccepterMessageText = (TextView)rootView.findViewById(R.id.my_request_accepter_message);

            AccepterName = (TextView)rootView.findViewById(R.id.request_destails_text_accepter_name);
            AccepterPhone = (TextView)rootView.findViewById(R.id.request_destails_text_accepter_phone);
            AccepterMessage = (TextView)rootView.findViewById(R.id.request_destails_text_accepter_message);

            AccepterInfo = (TextView)rootView.findViewById(R.id.request_destails_accetped_text);

            DeleteRequest = (Button)rootView.findViewById(R.id.DeleteRequestButton);

            ViewRouteButton = (Button)rootView.findViewById(R.id.my_request_view_route_button);
            ChatButton = (Button)rootView.findViewById(R.id.my_request_chat_button);
            PhoneButton = (Button)rootView.findViewById(R.id.my_request_phone_button);

            DeleteRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query CurrentRecord = FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).orderByChild("phone").equalTo(MyCallHelpMessage.getPhone());

                    CurrentRecord.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot record: dataSnapshot.getChildren()){
                                record.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    MyCallHelpMessage = null;

                    MyRequestName.setVisibility(View.INVISIBLE);
                    MyRequestStart.setVisibility(View.INVISIBLE);
                    MyRequestEnd.setVisibility(View.INVISIBLE);
                    MyRequestPhone.setVisibility(View.INVISIBLE);
                    MyRequestMessage.setVisibility(View.INVISIBLE);
                    MyRequestStatus.setVisibility(View.INVISIBLE);

                    DetailsName.setVisibility(View.INVISIBLE);
                    DetailsStart.setVisibility(View.INVISIBLE);
                    DetailsEnd.setVisibility(View.INVISIBLE);
                    DetailsPhone.setVisibility(View.INVISIBLE);
                    DetailsMessage.setVisibility(View.INVISIBLE);
                    DetailsStatus.setVisibility(View.INVISIBLE);

                    DeleteRequest.setVisibility(View.INVISIBLE);

                    AccepterNameText.setVisibility(View.INVISIBLE);
                    AccepterPhoneText.setVisibility(View.INVISIBLE);
                    AccepterMessageText.setVisibility(View.INVISIBLE);

                    AccepterName.setVisibility(View.INVISIBLE);
                    AccepterPhone.setVisibility(View.INVISIBLE);
                    AccepterMessage.setVisibility(View.INVISIBLE);

                    AccepterInfo.setVisibility(View.INVISIBLE);

                    ViewRouteButton.setVisibility(View.INVISIBLE);
                    PhoneButton.setVisibility(View.INVISIBLE);
                    ChatButton.setVisibility(View.INVISIBLE);

                    NoRequest.setVisibility(View.VISIBLE);

                }
            });

            ViewRouteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PathInfoActivity.class);

                    intent.putExtra("from", MyCallHelpMessage.getStart());
                    intent.putExtra("to", MyCallHelpMessage.getEnd());
                    intent.putExtra("lang", "eng");
                    intent.putExtra("bus","false");
                    intent.putExtra("activity","main");

                    startActivityForResult(intent, 1);
                }
            });

            ChatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ChatRoomFragment()).commit();
                }
            });

            PhoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + MyCallHelpMessage.getAccepterPhone()));
                    startActivity(callIntent);
                }
            });

            if(MyCallHelpMessage != null){

                MyRequestName.setVisibility(View.VISIBLE);
                MyRequestStart.setVisibility(View.VISIBLE);
                MyRequestEnd.setVisibility(View.VISIBLE);
                MyRequestPhone.setVisibility(View.VISIBLE);
                MyRequestMessage.setVisibility(View.VISIBLE);
                MyRequestStatus.setVisibility(View.VISIBLE);

                DetailsName.setVisibility(View.VISIBLE);
                DetailsStart.setVisibility(View.VISIBLE);
                DetailsEnd.setVisibility(View.VISIBLE);
                DetailsPhone.setVisibility(View.VISIBLE);
                DetailsMessage.setVisibility(View.VISIBLE);
                DetailsStatus.setVisibility(View.VISIBLE);

                DetailsName.setText(MyCallHelpMessage.getName());
                DetailsStart.setText(MyCallHelpMessage.getStart());
                DetailsEnd.setText(MyCallHelpMessage.getEnd());
                DetailsPhone.setText(MyCallHelpMessage.getPhone());
                DetailsMessage.setText(MyCallHelpMessage.getMessage());

                if(MyCallHelpMessage.getAccepted()){
                    DetailsStatus.setText("Your request is accetped");

                    AccepterNameText.setVisibility(View.VISIBLE);
                    AccepterPhoneText.setVisibility(View.VISIBLE);
                    AccepterMessageText.setVisibility(View.VISIBLE);

                    AccepterName.setVisibility(View.VISIBLE);
                    AccepterPhone.setVisibility(View.VISIBLE);
                    AccepterMessage.setVisibility(View.VISIBLE);

                    AccepterInfo.setVisibility(View.VISIBLE);

                    AccepterName.setText(MyCallHelpMessage.getAccepterName());
                    AccepterPhone.setText(MyCallHelpMessage.getAccepterPhone());
                    AccepterMessage.setText(MyCallHelpMessage.getAccepterMessage());

                    if(MyCallHelpMessage.getAccepterPhone() != null) {
                        PhoneButton.setEnabled(true);
                    }else{
                        PhoneButton.setEnabled(false);
                    }
                    ChatButton.setEnabled(true);

                }else{
                    DetailsStatus.setText("Pending");

                    AccepterNameText.setVisibility(View.INVISIBLE);
                    AccepterPhoneText.setVisibility(View.INVISIBLE);
                    AccepterMessageText.setVisibility(View.INVISIBLE);

                    AccepterName.setVisibility(View.INVISIBLE);
                    AccepterPhone.setVisibility(View.INVISIBLE);
                    AccepterMessage.setVisibility(View.INVISIBLE);

                    AccepterInfo.setVisibility(View.INVISIBLE);
                    PhoneButton.setEnabled(false);
                    ChatButton.setEnabled(false);
                }

                DeleteRequest.setVisibility(View.VISIBLE);

                ViewRouteButton.setVisibility(View.VISIBLE);

                NoRequest.setVisibility(View.INVISIBLE);

            }else{

                MyRequestName.setVisibility(View.INVISIBLE);
                MyRequestStart.setVisibility(View.INVISIBLE);
                MyRequestEnd.setVisibility(View.INVISIBLE);
                MyRequestPhone.setVisibility(View.INVISIBLE);
                MyRequestMessage.setVisibility(View.INVISIBLE);
                MyRequestStatus.setVisibility(View.INVISIBLE);

                DetailsName.setVisibility(View.INVISIBLE);
                DetailsStart.setVisibility(View.INVISIBLE);
                DetailsEnd.setVisibility(View.INVISIBLE);
                DetailsPhone.setVisibility(View.INVISIBLE);
                DetailsMessage.setVisibility(View.INVISIBLE);
                DetailsStatus.setVisibility(View.INVISIBLE);

                DeleteRequest.setVisibility(View.INVISIBLE);

                AccepterNameText.setVisibility(View.INVISIBLE);
                AccepterPhoneText.setVisibility(View.INVISIBLE);
                AccepterMessageText.setVisibility(View.INVISIBLE);

                AccepterName.setVisibility(View.INVISIBLE);
                AccepterPhone.setVisibility(View.INVISIBLE);
                AccepterMessage.setVisibility(View.INVISIBLE);

                AccepterInfo.setVisibility(View.INVISIBLE);

                ViewRouteButton.setVisibility(View.INVISIBLE);
                PhoneButton.setVisibility(View.INVISIBLE);
                ChatButton.setVisibility(View.INVISIBLE);

                NoRequest.setVisibility(View.VISIBLE);

            }

            return rootView;
        }
    }

    public static class MyAcceptFragment extends Fragment {

        TextView MyAcceptName, MyAcceptStart, MyAcceptEnd, MyAcceptPhone, MyAcceptMessage;
        TextView DetailsName, DetailsStart, DetailsEnd, DetailsPhone, DetailsMessage;
        TextView NoAccept;
        TextView AccepterNameText, AccepterPhoneText, AccepterMessageText;
        TextView AccepterName, AccepterPhone, AccepterMessage;
        TextView AccepterInfo;

        Button DeleteAccept;

        Button ViewRouteButton, ChatButton, PhoneButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.my_accept, container, false);

            AccepterNameText = (TextView)rootView.findViewById(R.id.my_accept_accepter_name);
            AccepterPhoneText = (TextView)rootView.findViewById(R.id.my_accept_accepter_phone);
            AccepterMessageText = (TextView)rootView.findViewById(R.id.my_accept_accepter_message);

            AccepterName = (TextView)rootView.findViewById(R.id.accept_destails_text_accepter_name);
            AccepterPhone = (TextView)rootView.findViewById(R.id.accept_destails_text_accepter_phone);
            AccepterMessage = (TextView)rootView.findViewById(R.id.accept_destails_text_accepter_message);

            NoAccept = (TextView)rootView.findViewById(R.id.accept_destails_no_accept);

            MyAcceptName = (TextView)rootView.findViewById(R.id.my_accept_name);
            MyAcceptStart = (TextView)rootView.findViewById(R.id.my_accept_start);
            MyAcceptEnd = (TextView)rootView.findViewById(R.id.my_accept_end);
            MyAcceptPhone = (TextView)rootView.findViewById(R.id.my_accept_phone);
            MyAcceptMessage = (TextView)rootView.findViewById(R.id.my_accept_message);

            DetailsName = (TextView)rootView.findViewById(R.id.accept_destails_text_name);
            DetailsStart = (TextView)rootView.findViewById(R.id.accept_destails_text_start);
            DetailsEnd = (TextView)rootView.findViewById(R.id.accept_destails_text_end);
            DetailsPhone = (TextView)rootView.findViewById(R.id.accept_destails_text_phone);
            DetailsMessage = (TextView)rootView.findViewById(R.id.accept_destails_text_message);

            AccepterInfo = (TextView)rootView.findViewById(R.id.accept_destails_accetped_text);

            DeleteAccept = (Button)rootView.findViewById(R.id.DeleteAcceptButton);

            ViewRouteButton = (Button)rootView.findViewById(R.id.my_accept_view_route_button);
            ChatButton = (Button)rootView.findViewById(R.id.my_accept_chat_button);
            PhoneButton = (Button)rootView.findViewById(R.id.my_accept_phone_button);

            DeleteAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("accepted").setValue(false);
                    FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("accepterName").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("accepterPhone").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("accepterMessage").setValue(null);

                    FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("chat").removeValue();

                    MyAcceptedCallHelp = null;

                    MyAcceptName.setVisibility(View.INVISIBLE);
                    MyAcceptStart.setVisibility(View.INVISIBLE);
                    MyAcceptEnd.setVisibility(View.INVISIBLE);
                    MyAcceptPhone.setVisibility(View.INVISIBLE);
                    MyAcceptMessage.setVisibility(View.INVISIBLE);

                    DetailsName.setVisibility(View.INVISIBLE);
                    DetailsStart.setVisibility(View.INVISIBLE);
                    DetailsEnd.setVisibility(View.INVISIBLE);
                    DetailsPhone.setVisibility(View.INVISIBLE);
                    DetailsMessage.setVisibility(View.INVISIBLE);

                    DeleteAccept.setVisibility(View.INVISIBLE);

                    AccepterNameText.setVisibility(View.INVISIBLE);
                    AccepterPhoneText.setVisibility(View.INVISIBLE);
                    AccepterMessageText.setVisibility(View.INVISIBLE);

                    AccepterName.setVisibility(View.INVISIBLE);
                    AccepterPhone.setVisibility(View.INVISIBLE);
                    AccepterMessage.setVisibility(View.INVISIBLE);

                    AccepterInfo.setVisibility(View.INVISIBLE);

                    ViewRouteButton.setVisibility(View.INVISIBLE);
                    ChatButton.setVisibility(View.INVISIBLE);
                    PhoneButton.setVisibility(View.INVISIBLE);

                    NoAccept.setVisibility(View.VISIBLE);

                }
            });

            ViewRouteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PathInfoActivity.class);

                    intent.putExtra("from", MyAcceptedCallHelp.getStart());
                    intent.putExtra("to", MyAcceptedCallHelp.getEnd());
                    intent.putExtra("lang", "eng");
                    intent.putExtra("bus","false");
                    intent.putExtra("activity","main");

                    startActivityForResult(intent, 1);
                }
            });

            ChatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ChatRoomFragment()).commit();
                }
            });

            PhoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + MyAcceptedCallHelp.getPhone()));
                    startActivity(callIntent);
                }
            });

            if(MyAcceptedCallHelp != null){

                MyAcceptName.setVisibility(View.VISIBLE);
                MyAcceptStart.setVisibility(View.VISIBLE);
                MyAcceptEnd.setVisibility(View.VISIBLE);
                MyAcceptPhone.setVisibility(View.VISIBLE);
                MyAcceptMessage.setVisibility(View.VISIBLE);

                DetailsName.setVisibility(View.VISIBLE);
                DetailsStart.setVisibility(View.VISIBLE);
                DetailsEnd.setVisibility(View.VISIBLE);
                DetailsPhone.setVisibility(View.VISIBLE);
                DetailsMessage.setVisibility(View.VISIBLE);

                DetailsName.setText(MyAcceptedCallHelp.getName());
                DetailsStart.setText(MyAcceptedCallHelp.getStart());
                DetailsEnd.setText(MyAcceptedCallHelp.getEnd());
                DetailsPhone.setText(MyAcceptedCallHelp.getPhone());
                DetailsMessage.setText(MyAcceptedCallHelp.getMessage());

                if(MyAcceptedCallHelp.getAccepted()){
                    AccepterNameText.setVisibility(View.VISIBLE);
                    AccepterPhoneText.setVisibility(View.VISIBLE);
                    AccepterMessageText.setVisibility(View.VISIBLE);

                    AccepterName.setVisibility(View.VISIBLE);
                    AccepterPhone.setVisibility(View.VISIBLE);
                    AccepterMessage.setVisibility(View.VISIBLE);

                    AccepterInfo.setVisibility(View.VISIBLE);

                    AccepterName.setText(MyAcceptedCallHelp.getAccepterName());
                    AccepterPhone.setText(MyAcceptedCallHelp.getAccepterPhone());
                    AccepterMessage.setText(MyAcceptedCallHelp.getAccepterMessage());

                    if(MyAcceptedCallHelp.getPhone() != null) {
                        PhoneButton.setEnabled(true);
                    }else{
                        PhoneButton.setEnabled(false);
                    }
                    ChatButton.setEnabled(true);

                }else{
                    AccepterNameText.setVisibility(View.INVISIBLE);
                    AccepterPhoneText.setVisibility(View.INVISIBLE);
                    AccepterMessageText.setVisibility(View.INVISIBLE);

                    AccepterName.setVisibility(View.INVISIBLE);
                    AccepterPhone.setVisibility(View.INVISIBLE);
                    AccepterMessage.setVisibility(View.INVISIBLE);

                    AccepterInfo.setVisibility(View.INVISIBLE);
                    PhoneButton.setEnabled(false);
                    ChatButton.setEnabled(false);
                }

                DeleteAccept.setVisibility(View.VISIBLE);

                ViewRouteButton.setVisibility(View.VISIBLE);

                NoAccept.setVisibility(View.INVISIBLE);

            }else{

                MyAcceptName.setVisibility(View.INVISIBLE);
                MyAcceptStart.setVisibility(View.INVISIBLE);
                MyAcceptEnd.setVisibility(View.INVISIBLE);
                MyAcceptPhone.setVisibility(View.INVISIBLE);
                MyAcceptMessage.setVisibility(View.INVISIBLE);

                DetailsName.setVisibility(View.INVISIBLE);
                DetailsStart.setVisibility(View.INVISIBLE);
                DetailsEnd.setVisibility(View.INVISIBLE);
                DetailsPhone.setVisibility(View.INVISIBLE);
                DetailsMessage.setVisibility(View.INVISIBLE);

                DeleteAccept.setVisibility(View.INVISIBLE);

                AccepterNameText.setVisibility(View.INVISIBLE);
                AccepterPhoneText.setVisibility(View.INVISIBLE);
                AccepterMessageText.setVisibility(View.INVISIBLE);

                AccepterName.setVisibility(View.INVISIBLE);
                AccepterPhone.setVisibility(View.INVISIBLE);
                AccepterMessage.setVisibility(View.INVISIBLE);

                AccepterInfo.setVisibility(View.INVISIBLE);

                ViewRouteButton.setVisibility(View.INVISIBLE);
                ChatButton.setVisibility(View.INVISIBLE);
                PhoneButton.setVisibility(View.INVISIBLE);

                NoAccept.setVisibility(View.VISIBLE);

            }

            return rootView;
        }
    }

    public static class ChatRoomFragment extends Fragment {

        private SharedPreferences mSharedPreferences;

        private Button SendMessageButton;
        private ProgressBar mProgressBar;
        private EditText mMessageEditText;
        private LinearLayoutManager mLinearLayoutManager;
        private RecyclerView mMessageRecyclerView;

        // Firebase instance variables
        private DatabaseReference mFirebaseDatabaseReference;
        private FirebaseRecyclerAdapter<ChatRoomMessage, ChatMessageViewHolder>
                mFirebaseAdapter;

        DatabaseReference ChatRoomRef;

        //Determine accepter of not
        boolean IsAccepter;

        public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
            public TextView messageTextView;
            public TextView messageDateView;
            public TextView recevieTextView;
            public TextView recevieDateView;
            public LinearLayout left,right;

            public ChatMessageViewHolder(View v) {
                super(v);
                left = (LinearLayout) itemView.findViewById(R.id.left);
                right = (LinearLayout) itemView.findViewById(R.id.right);
                messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
                messageDateView = (TextView) itemView.findViewById(R.id.messageDateView);
                recevieTextView = (TextView) itemView.findViewById(R.id.receiveTextView);
                recevieDateView = (TextView) itemView.findViewById(R.id.receiveDateView);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.chat_room, container, false);

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());

            // Initialize ProgressBar and RecyclerView.
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mMessageRecyclerView = (RecyclerView) rootView.findViewById(R.id.messageRecyclerView);

            mProgressBar.setVisibility(ProgressBar.INVISIBLE);

            mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            mLinearLayoutManager.setStackFromEnd(true);
            mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

            // Initialize all Button
            SendMessageButton = (Button) rootView.findViewById(R.id.sendButton);

            // New child entries
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

            if(MyCallHelpMessage != null){
                ChatRoomRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(MyCallHelpMessage.getKey()).child("chat");
                IsAccepter = false;
            }else{
                ChatRoomRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(MyAcceptedCallHelp.getKey()).child("chat");
                IsAccepter = true;
            }

            mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatRoomMessage,
                    ChatMessageViewHolder>(
                    ChatRoomMessage.class,
                    R.layout.item_chat_message,
                    ChatMessageViewHolder.class,
                    ChatRoomRef.orderByChild("date")) {

                @Override
                public int getItemViewType(int position) {
                    return position;
                }

                @Override
                protected void populateViewHolder(final ChatMessageViewHolder viewHolder,
                                                  ChatRoomMessage chatRoomMessage, int position) {
                    if (!IsAccepter) {
                        if(chatRoomMessage.getAccepter()) {
                            viewHolder.left.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.2f));
                            viewHolder.right.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.5f));
                            viewHolder.messageTextView.setVisibility(View.VISIBLE);
                            viewHolder.messageDateView.setVisibility(View.VISIBLE);
                            viewHolder.messageTextView.setText(chatRoomMessage.getMessage());
                            viewHolder.messageDateView.setText(chatRoomMessage.getDate());
                            viewHolder.recevieTextView.setVisibility(View.INVISIBLE);
                            viewHolder.recevieDateView.setVisibility(View.INVISIBLE);
                        }
                        else{
                            viewHolder.right.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.2f));
                            viewHolder.left.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.5f));
                            viewHolder.recevieTextView.setVisibility(View.VISIBLE);
                            viewHolder.recevieDateView.setVisibility(View.VISIBLE);
                            viewHolder.recevieTextView.setText(chatRoomMessage.getMessage());
                            viewHolder.recevieDateView.setText(chatRoomMessage.getDate());
                            viewHolder.messageTextView.setVisibility(View.INVISIBLE);
                            viewHolder.messageDateView.setVisibility(View.INVISIBLE);

                        }
                    }else{
                        if(chatRoomMessage.getAccepter()) {
                            viewHolder.right.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.2f));
                            viewHolder.left.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.5f));
                            viewHolder.recevieTextView.setVisibility(View.VISIBLE);
                            viewHolder.recevieDateView.setVisibility(View.VISIBLE);
                            viewHolder.recevieTextView.setText(chatRoomMessage.getMessage());
                            viewHolder.recevieDateView.setText(chatRoomMessage.getDate());
                            viewHolder.messageTextView.setVisibility(View.INVISIBLE);
                            viewHolder.messageDateView.setVisibility(View.INVISIBLE);
                        }
                        else{
                            viewHolder.left.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.2f));
                            viewHolder.right.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.5f));
                            viewHolder.messageTextView.setVisibility(View.VISIBLE);
                            viewHolder.messageDateView.setVisibility(View.VISIBLE);
                            viewHolder.messageTextView.setText(chatRoomMessage.getMessage());
                            viewHolder.messageDateView.setText(chatRoomMessage.getDate());
                            viewHolder.recevieTextView.setVisibility(View.INVISIBLE);
                            viewHolder.recevieDateView.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            };

            mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                    int lastVisiblePosition =
                            mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.
                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        mMessageRecyclerView.scrollToPosition(positionStart);
                    }
                }
            });

            mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);

            mMessageEditText = (EditText) rootView.findViewById(R.id.messageEditText);
            mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                    .getInt("friendly_msg_length", 100))});
            mMessageEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().trim().length() > 0) {
                        SendMessageButton.setEnabled(true);
                    } else {
                        SendMessageButton.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            SendMessageButton = (Button) rootView.findViewById(R.id.sendButton);
            SendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date Now = Calendar.getInstance().getTime();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // Send messages on click.
                    ChatRoomMessage chatRoomMessage = new
                            ChatRoomMessage(mMessageEditText.getText().toString(),
                            simpleDateFormat.format(Now),
                            IsAccepter);

                    ChatRoomRef.push().setValue(chatRoomMessage);
                    mMessageEditText.setText("");
                }
            });

            return rootView;
        }
    }

}
