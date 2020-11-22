package com.example.testapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapter.LinearAdapter_MyBuy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

// 购买记录页面
public class MyBuyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);
        String temp = getIntent().getStringExtra("mapList");
        ArrayList<HashMap<String, Object>> mapList = new Gson().fromJson(temp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        TitleBar titleBar = findViewById(R.id.titleBar_myBuy);
        titleBar.setLeftClickListener(v -> this.finish());
        RecyclerView rView = findViewById(R.id.buyed_item_list);
        rView.setLayoutManager(new LinearLayoutManager(MyBuyActivity.this));
        rView.setAdapter(new LinearAdapter_MyBuy(MyBuyActivity.this, mapList));

    }

}