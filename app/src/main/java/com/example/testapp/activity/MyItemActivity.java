package com.example.testapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.adapter.LinearAdapter_MyItem;
import com.xuexiang.xui.widget.actionbar.TitleBar;

// 我发布的
public class MyItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);
        MyApp app = (MyApp) getApplication();
        RecyclerView rView = findViewById(R.id.my_item_list);
        TitleBar titleBar = findViewById(R.id.titleBar_myItem);
        titleBar.setLeftClickListener(v -> this.finish());
        rView.setLayoutManager(new LinearLayoutManager(MyItemActivity.this));
        rView.setAdapter(new LinearAdapter_MyItem(MyItemActivity.this, app.getApp_map(), MyApp.getUser_item_list()));
    }
}