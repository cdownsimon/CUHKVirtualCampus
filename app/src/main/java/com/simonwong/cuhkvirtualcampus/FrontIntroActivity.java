package com.simonwong.cuhkvirtualcampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class FrontIntroActivity extends AppIntro2 {

    String LaunchFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle message = getIntent().getExtras();
        LaunchFrom = message.getString("LaunchFrom");

        if(LaunchFrom.equals("front")){
            addSlide(AppIntroFragment.newInstance("Welcome to CU Guides!", "接下來將為你介紹三項主要功能\nWe will introduce the three main features in the following.", getResources().getIdentifier("home_logo", "mipmap", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("Route Planner", "為您尋找最適路線\nPlan the suitable route for you.", getResources().getIdentifier("home_planner", "mipmap", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("Information of Department", "為您提供各學系位置\nLocate the address of each department for you.", getResources().getIdentifier("home_department", "mipmap", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("Call For Help", "小型社交平台幫助帶需要人仕\nMini-social platforms for people in need.", getResources().getIdentifier("home_call", "mipmap", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("main")){
            addSlide(AppIntroFragment.newInstance("Route Planner", "接下來將為你介紹如何使用Route Planner\nWe will introduce the usage of Route Planner.", getResources().getIdentifier("route_planner", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("選擇地點\nChoose Starting Point and Ending Point.", "您可以在文字欄輸入地點名稱(中文／英文)或直接點擊下方地點列表\nYou can type your building's name at the text field(Chinese or English), or click the name list below.", getResources().getIdentifier("route_planner_text", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("步行路線\nWalking Route", "點擊紅圈的Go, CU Guides將為你找尋最適步行路線\nClick the Go, CU Guides will plan the suitable walking route for you.", getResources().getIdentifier("route_planner_go", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("校巴路線\nBus Route\n(Coming soon!)", "點擊紅圈的校巴按鍵, CU Guides將為你找尋最適校巴路線\nClick the Bus button, CU Guides will plan the suitable bus route for you.", getResources().getIdentifier("route_planner_bus", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("傷殘人士路線\nDisabled Route\n(Coming soon!)", "我們考慮到傷殘人士行走樓梯的不便, 點擊紅圈的輪椅按鍵, CU Guides將為你找尋最適傷殘人士路線\nWe consider the diffculties of disabled. Click the wheel chair button, CU Guides will plan the suitable disabled route for you.", getResources().getIdentifier("route_planner_disabled", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("department")){
            addSlide(AppIntroFragment.newInstance("Information of Department", "接下來將為你介紹如何使用Information of Department\nWe will introduce the usage of Information of Department.", getResources().getIdentifier("department", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("建築物資料\nInformation of Building", "點擊地圖上的標記便會顯示建築的名字和圖片\nYou can click on the marker, the name and image of the building will be shown up.", getResources().getIdentifier("department_marker", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("學系資料\nInformation of Department", "再次點擊建築圖片便會顯示該建築內的學系資料\nYou can click on the building's image, the information of the department inside that building will be shown up.", getResources().getIdentifier("department_info", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("學系搜尋\nDepartment Searching", "點擊放大鏡接鍵便可進行學系搜尋\nYou can click on the magnifier to perform department searching.", getResources().getIdentifier("department_search", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("返回首頁\nBack to Home Page", "點擊房屋按鍵便可返回首頁\nYou can click on the home button to back to home page.", getResources().getIdentifier("department_home_button", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("RouteInfo")){
            addSlide(AppIntroFragment.newInstance("路線概覽\nOverview of Route", "這裡顯示路線概覽,包括起點、終點及途經地點\nThe overview of the route will be shown here including starting point, ending point and intermediate points.", getResources().getIdentifier("route_info", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("路線圖\nMap", "點擊地圖按鍵便可在地圖上預覽路線\nYou can click on the map button to view the route on map.", getResources().getIdentifier("route_info_map_button", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("街景預覽\nStreetview Guidance", "點擊相機按鍵便可預覽路線街景圖\nYou can click on the camera button to preview the street view images.", getResources().getIdentifier("route_info_streetview_button", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("返回地點選擇頁\nBack to Route Planner", "點擊列表按鍵便可返回地點選擇頁\nYou can click on the list button to back to location selection page.", getResources().getIdentifier("route_info_sel_button", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("返回首頁\nBack to Home Page", "點擊房屋按鍵便可返回首頁\nYou can click on the home button to back to home page.", getResources().getIdentifier("route_info_home_button", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("Map")){
            addSlide(AppIntroFragment.newInstance("路線圖\nMap", "這裡在地圖上顯示路線\nThe route is shown on the map here.", getResources().getIdentifier("map", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("路線概覽\nOverview of Route", "點擊感嘆號按鍵便可回到路線概覽\nYou can click on the exclamation mark button to back to overview of the route.", getResources().getIdentifier("map_pathinfo", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("衛星圖\nSatellite Image", "點擊衛星按鍵便可檢視衛星圖像\nYou can click on the satellite button to back to view the satellite image.", getResources().getIdentifier("map_satellite", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("導航模式\nReal Time Navigation\n(Coming soon!)", "點擊箭咀按鍵便可進入導航模式\nYou can click on the arrow button to enter real time navigation.", getResources().getIdentifier("map_navigation", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("Streetview")){
            addSlide(AppIntroFragment.newInstance("街景預覽\nStreetview Guidance", "這裡可以預覽路線街景圖\nYou can preview the street view images here.", getResources().getIdentifier("streetview", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("路線概覽\nOverview of Route", "點擊感嘆號按鍵便可回到路線概覽\nYou can click on the exclamation mark button to back to overview of the route.", getResources().getIdentifier("streetview_pathinfo", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("拖動按鈕\nDrag the Button", "拖動按鈕下方接鈕檢視整條路線的街景圖\nYou can drag the button below to preview the streew view images of the whole route.", getResources().getIdentifier("streetview_bar", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("CallHelp")){
            addSlide(AppIntroFragment.newInstance("Call For Help", "有需要人仕可以在這裡向其他人求助\nPeople in need can post a request to acquire others' help.", getResources().getIdentifier("call_help", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("請求\nRequest", "在這裡可以檢視其他人的請求, 點擊將會顯示詳細資料並選擇是否接受他人請求\nYou can view the other's request here, the details will be shown up when you click on it.You can also accept other's request.", getResources().getIdentifier("call_help_request", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("新增請求\nAdd Request", "您可以點擊加號新增請求並需要填寫基本資料, 如名稱、起點、終點等\nYou can click on the plus button to add a request, you may need to fill in some basic information such as name, starting point, ending point etc.", getResources().getIdentifier("call_help_add", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("功能表\nFunctions List", "您可以點擊這裡打開功能表\nYou can click here to open the functions list.", getResources().getIdentifier("call_help_func", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("我的請求\nMy Request", "您可以點擊這裡檢視您的請求\nYou can click here to view your request.", getResources().getIdentifier("call_help_my_re", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("接受請求\nMy Accept", "您可以點擊這裡檢視您接受了的請求\nYou can click here to view your accepted request.", getResources().getIdentifier("call_help_my_ac", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("返回請求列表\nBack to Request List", "您可以點擊這裡返回請求列表\nYou can click here to back to request list.", getResources().getIdentifier("call_help_re_list", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("返回首頁\nBack to Home Page", "您可以點擊這裡返回首頁\nYou can click here to back to home page.", getResources().getIdentifier("call_help_home", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
            addSlide(AppIntroFragment.newInstance("刪除請求\nDelete Request", "所有請求將會在十分鐘後自動刪除\nAll the request will be deleted after 10 minutes.", getResources().getIdentifier("call_help_del", "drawable", getPackageName()), Color.parseColor("#12a8a8")));
        }else if(LaunchFrom.equals("SeemInfoDay")){
            addSlide(AppIntroFragment.newInstance("本科入學資訊日\nOrientation Day", "CU Guides is developed by the CUHK Department of Systems Engineering and Engineering Management", getResources().getIdentifier("seem_logo", "drawable", getPackageName()), Color.parseColor("#0f228e")));
            addSlide(AppIntroFragment.newInstance("Project Demonstrations", "我們將會示範不同的研究項目, 歡迎來到蒙民偉工程學大樓6樓612室查詢!\nWe will have several project demonstrations, please come to William M.W. Mong Engineering Building Room 612!", getResources().getIdentifier("erb_google_map", "drawable", getPackageName()), Color.parseColor("#0f228e")));
        }


        // OPTIONAL METHODS
        // Override bar/separator color.
        if(LaunchFrom.equals("SeemInfoDay")){
            setBarColor(Color.parseColor("#0f228e"));
        }else {
            setBarColor(Color.parseColor("#12a8a8"));
        }

        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        if(LaunchFrom.equals("SeemInfoDay")) {
            Intent i = new Intent(FrontIntroActivity.this, FrontIntroActivity.class);

            i.putExtra("LaunchFrom", "front");
            startActivity(i);
        }

        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        if(LaunchFrom.equals("SeemInfoDay")) {
            Intent i = new Intent(FrontIntroActivity.this, FrontIntroActivity.class);

            i.putExtra("LaunchFrom", "front");
            startActivity(i);
        }

        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
