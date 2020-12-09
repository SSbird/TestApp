package com.example.testapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.adapter.LinearAdapter_RequestList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// 求购请求列表
public class RequestListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        TitleBar titleBar = findViewById(R.id.titleBar_requestList);
        titleBar.setLeftClickListener(v -> this.finish());
        String shopper_id = String.valueOf(MyApp.getApp_map().get("phone"));
        String data = getIntent().getStringExtra("mapList");
        List<HashMap<String, Object>> mapList = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        RecyclerView requestList = findViewById(R.id.buyer_request_table);
        requestList.setLayoutManager(new LinearLayoutManager(this));
        requestList.setAdapter(new LinearAdapter_RequestList(this, mapList, shopper_id));
    }
}