package com.example.project;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("유통기한 관리 어플");

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, MyCalendar.class);
        spec = tabHost.newTabSpec("CALENDAR"); // 객체를 생성
        spec.setIndicator("달력보기"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, Ingredient.class);
        spec = tabHost.newTabSpec("INGREDIENT"); // 객체를 생성
        spec.setIndicator("재료보기"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, Recipe.class);
        spec = tabHost.newTabSpec("RECIPE"); // 객체를 생성
        spec.setIndicator("레시피보기"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}